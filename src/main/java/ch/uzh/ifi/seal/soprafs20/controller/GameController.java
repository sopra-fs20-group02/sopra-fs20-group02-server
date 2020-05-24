package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Move;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.JoinPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class handles all the REST requests related to the game creation and join.
 * It delegates the execution to the GameService.
 */
@RestController
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    // Handles the request to create a new game, or join a waiting game
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createNewGame(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        Game newGame;
        if(userPostDTO.getGameMode()==null){
            newGame = gameService.createNewGame(userInput);
        }
        else {
            newGame = gameService.createNewGame(userInput, userPostDTO.getGameMode());
        }
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(newGame);
    }

    @PutMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO joinGame(@RequestBody JoinPutDTO joinPutDTO) {
        User user = userService.findUserByUserId(joinPutDTO.getUserId());
        Game game = null;
        if(joinPutDTO.getGameId()!=null) {
            game = gameService.findGameByGameId(joinPutDTO.getGameId());
        }
        else {
            List<Game> games = gameService.getGames();
            List<Game> gamesToJoin = new ArrayList<>();
            for (Game g : games){
                if (g.getGameStatus() == GameStatus.WAITING){
                    // Found game that can be joined
                    gamesToJoin.add(g);
                }
            }
            if (gamesToJoin.isEmpty()) {
                throw new JoinGameException("No free game could be found");
            }
            Random random = new Random();
            int n = random.nextInt(gamesToJoin.size());
            game = gamesToJoin.get(n);
        }

        gameService.joinGame(user, game);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    // Handles the request to get all games
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getGames() {
        List<Game> games = gameService.getGames();
        List<GameGetDTO> gameGetDTOs = new ArrayList<>();

        for (Game game : games) {
            gameGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOs;
    }

    @GetMapping(value = "/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable("id") Long gameId) {
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameService.findGameByGameId(gameId));
    }

    @PutMapping(value = "/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void leaveGame(@PathVariable("id") Long id, @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        gameService.leaveGame(id, userInput);
    }

    @DeleteMapping(value = "/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable("id") Long id) {
        gameService.deleteGame(id);
    }

    @PutMapping(value = "/games/{gameId}/{pieceId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO makeMove(@PathVariable("gameId") Long gameId, @PathVariable("pieceId") Long pieceId, @RequestBody MovePostDTO movePostDTO) {
        Move move = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(movePostDTO);
        Game game = gameService.makeMove(gameId, pieceId, move.getX(), move.getY());
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping(value = "/games/{gameId}/{pieceId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Vector> getMoves(@PathVariable("gameId") Long gameId, @PathVariable("pieceId") Long pieceId) {
        return gameService.getPossibleMoves(gameId,pieceId);
    }

    @GetMapping(value = "/users/{userId}/gameHistory")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getGameHistory(@PathVariable("userId") Long userId) {
        return gameService.getGameHistory(userId);
    }

    @PostMapping(value = "/games/{gameId}/draw")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO offerOrAcceptDraw(@PathVariable("gameId") Long gameId, @RequestBody UserPostDTO userPostDTO) {
        Game game = gameService.offerOrAcceptDraw(gameId, userPostDTO.getUserId());
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping(value = "/games/{gameId}/draw")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO declineDraw(@PathVariable("gameId") Long gameId, @RequestBody UserPostDTO userPostDTO) {
        Game game = gameService.declineDraw(gameId, userPostDTO.getUserId());
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping(value = "/games/{gameId}/movable")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PieceDB> getMovablePieces(@PathVariable("gameId") Long gameId, @RequestBody UserPostDTO userPostDTO) {
        List<PieceDB> pieces = gameService.getMovablePieces(gameId, userPostDTO.getUserId());
        return pieces;
    }

}
