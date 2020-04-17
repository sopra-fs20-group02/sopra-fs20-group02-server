package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Move;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public GameGetDTO joinOrCreateGame(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        List<Game> games = gameService.getGames();

        for (Game game : games){
            if (game.getGameStatus() == GameStatus.WAITING){
                // Found game that can be joined
                gameService.joinGame(userInput, game);
                return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
            }
        }

        // precondition: no game can be joined
        Game newGame = gameService.createNewGame(userInput);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(newGame);
        // TODO: add tests
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
        // TODO: add tests
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

    @PostMapping(value = "/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void makeMove(@PathVariable("id") Long id, @RequestBody MovePostDTO movePostDTO) {
        Move move = DTOMapper.INSTANCE.convertMovePostDTOtoEntity(movePostDTO);
        Game game = gameService.makeMove(id, move.getPieceId(),
                move.getX(), move.getY());
        return;
    }

    @GetMapping(value = "/games/{gameId}/{pieceId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Vector> getMoves(@PathVariable("gameId") Long gameId, @PathVariable("pieceId") Long pieceId) {
        List<Vector> moves = gameService.getPossibleMoves(gameId,pieceId);
        return moves;
    }

    // TODO
    @GetMapping(value = "/games/history/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getHistory() {
        //List<Vector> moves = gameService.getPossibleMoves(gameId,pieceId);
        return;
    }

    // TODO: etc...

}
