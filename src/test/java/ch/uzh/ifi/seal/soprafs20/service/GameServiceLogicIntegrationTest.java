package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceLogicIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    private User playerA;

    private User playerB;

    private Game game;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        userRepository.deleteAll();

        // register and login two users
        assertNull(userRepository.findByUsername("playerA"));
        assertNull(userRepository.findByUsername("playerB"));
        assertNull(userRepository.findByUsername("playerC"));

        UserPostDTO dtoA = new UserPostDTO();
        dtoA.setUsername("pA");
        dtoA.setName("playerA");
        dtoA.setPassword("123456");

        User userA = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(dtoA);

        playerA = userService.createUser(userA);
        userService.loginUser(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        UserPostDTO dtoB = new UserPostDTO();
        dtoB.setUsername("pB");
        dtoB.setName("playerB");
        dtoB.setPassword("123456");

        User userB = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(dtoB);

        playerB = userService.createUser(userB);
        userService.loginUser(playerB);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.ONLINE);

        UserPostDTO dtoC = new UserPostDTO();
        dtoC.setUsername("pC");
        dtoC.setName("playerC");
        dtoC.setPassword("123456");

        this.game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(this.game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.joinGame(playerB, this.game);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.PLAYING);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.PLAYING);
        assertEquals(gameRepository.findByGameId(this.game.getGameId()).getGameStatus(), GameStatus.FULL);
    }

    /**
     * fast way to achieve checkmate
     * https://www.thesprucecrafts.com/fools-mate-the-fastest-checkmate-611599
     */

    /*
    @Test
    public void foolsMate() {
        User playerWhite = this.game.getPlayerWhite();
        User playerBlack = this.game.getPlayerBlack();

        Long id = this.game.getGameId();

        Boolean found = false;
        // first move
        for (PieceDB pieceDB : game.getPieces()){
            if (pieceDB.getYCord() == 2 && pieceDB.getXCord() == 6){
                List<Vector> moves = gameService.getPossibleMoves(id, pieceDB.getPieceId());
                assertEquals(2, moves.size());
                gameService.makeMove(id, pieceDB.getPieceId(), 6,3);
                found = true;
            }
        }
        assertEquals(true, found);
        found = false;
        // second move
        for (PieceDB pieceDB : game.getPieces()){
            if (pieceDB.getYCord() == 7 && pieceDB.getXCord() == 5){
                List<Vector> moves = gameService.getPossibleMoves(id, pieceDB.getPieceId());
                assertEquals(2, moves.size());
                gameService.makeMove(id, pieceDB.getPieceId(), 5,5);
                found = true;
            }
        }
        assertEquals(true, found);
        found = false;
        // third move
        for (PieceDB pieceDB : game.getPieces()){
            if (pieceDB.getYCord() == 2 && pieceDB.getXCord() == 7){
                List<Vector> moves = gameService.getPossibleMoves(id, pieceDB.getPieceId());
                assertEquals(2, moves.size());
                gameService.makeMove(id, pieceDB.getPieceId(), 7,4);
                found = true;
            }
        }
        assertEquals(true, found);
        found = false;
        // fourth move
        for (PieceDB pieceDB : game.getPieces()){
            if (pieceDB.getYCord() == 8 && pieceDB.getXCord() == 4){
                List<Vector> moves = gameService.getPossibleMoves(id, pieceDB.getPieceId());
                assertEquals(4, moves.size());
                gameService.makeMove(id, pieceDB.getPieceId(), 8,4);
                found = true;
            }
        }
        assertEquals(true, found);

        assertEquals(GameStatus.WON,gameService.findGameByGameId(id).getGameStatus());
    }*/


}
