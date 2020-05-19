package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.LeaveGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.MakeMoveException;
import ch.uzh.ifi.seal.soprafs20.exceptions.NotFoundException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PieceRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserStatsRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

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

    @Autowired
    private PieceRepository pieceRepository;

    @Qualifier("userStatsRepository")
    @Autowired
    private UserStatsRepository userStatsRepository;


    private User playerA;

    private User playerB;

    private User playerC;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
        pieceRepository.deleteAll();
        userStatsRepository.deleteAll();

        // register and login two users
        assertNull(userRepository.findByUsername("playerA"));
        assertNull(userRepository.findByUsername("playerB"));
        assertNull(userRepository.findByUsername("playerC"));

        UserPostDTO dtoA = new UserPostDTO();
        dtoA.setUsername("pA");
        dtoA.setPassword("123456");

        User userA = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(dtoA);

        playerA = userService.createUser(userA);
        userService.loginUser(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        UserPostDTO dtoB = new UserPostDTO();
        dtoB.setUsername("pB");
        dtoB.setPassword("123456");

        User userB = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(dtoB);

        playerB = userService.createUser(userB);
        userService.loginUser(playerB);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.ONLINE);

        UserPostDTO dtoC = new UserPostDTO();
        dtoC.setUsername("pC");
        dtoC.setPassword("123456");

        User userC = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(dtoC);

        playerC = userService.createUser(userC);
        userService.loginUser(playerC);
        assertEquals(userService.findUserByUsername("pC").getStatus(), UserStatus.ONLINE);
    }

    @Test
    public void createGame_validInput_success() {
        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);
    }

    @Test
    public void joinGame_validInput_success() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.joinGame(playerB, game);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.PLAYING);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.PLAYING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.FULL);
    }

    @Test
    public void joinGame_thirdInvalidPlayer_exception() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.joinGame(playerB, game);
        assertThrows(
                JoinGameException.class,
                () -> {
                    gameService.joinGame(playerC, game);
                }
        );
    }

    @Test
    public void joinGame_samePlayerTwoGames_exception() {
        Game game = gameService.createNewGame(playerA);
        assertThrows(
                JoinGameException.class,
                () -> {
                    gameService.createNewGame(playerA);
                }
        );
    }

    @Test
    public void makeMove_GameStatusIsWaiting_exception() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        Long pieceId = 1L;
        int x = 1;
        int y = 3;

        assertThrows(
                MakeMoveException.class,
                () -> {
                    gameService.makeMove(game.getGameId(), pieceId, x,y);
                }
        );
    }

    @Test
    public void leaveGame_validInput_success() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.joinGame(playerB, game);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.PLAYING);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.PLAYING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.FULL);

        gameService.leaveGame(game.getGameId(), playerB);

        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.ONLINE);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WON);
    }

    @Test
    public void leaveGame_invalidInput_exception() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.joinGame(playerB, game);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.PLAYING);
        assertEquals(userService.findUserByUsername("pB").getStatus(), UserStatus.PLAYING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.FULL);

        gameService.leaveGame(game.getGameId(), playerB);

        assertThrows(
                LeaveGameException.class,
                () -> {
                    gameService.leaveGame(game.getGameId(), playerA);
                }
        );
    }

    @Test
    public void deleteGame_validInput_throwsException() {
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.ONLINE);

        Game game = gameService.createNewGame(playerA);
        assertEquals(userService.findUserByUsername("pA").getStatus(), UserStatus.SEARCHING);
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);

        gameService.deleteGame(game.getGameId());
        assertThrows(
                NotFoundException.class,
                () -> {
                    gameService.deleteGame(game.getGameId());
                }
        );
    }

    @Test
    public void offerDraw_validInput_success() {
        Game game = gameService.createNewGame(playerA);
        gameService.joinGame(playerB, game);

        assertEquals(false, gameService.findGameByGameId(game.getGameId()).getWhiteOffersDraw());
        assertEquals(false, gameService.findGameByGameId(game.getGameId()).getBlackOffersDraw());

        gameService.draw(game.getGameId(),playerA.getUserId());
        assertTrue(gameService.findGameByGameId(game.getGameId()).getWhiteOffersDraw() ||
                gameService.findGameByGameId(game.getGameId()).getBlackOffersDraw());

        assertFalse(gameService.findGameByGameId(game.getGameId()).getWhiteOffersDraw() &&
                gameService.findGameByGameId(game.getGameId()).getBlackOffersDraw());

        assertEquals(GameStatus.FULL, gameService.findGameByGameId(game.getGameId()).getGameStatus());

        gameService.draw(game.getGameId(),playerB.getUserId());
        assertTrue(gameService.findGameByGameId(game.getGameId()).getWhiteOffersDraw() &&
                gameService.findGameByGameId(game.getGameId()).getBlackOffersDraw());
        assertEquals(GameStatus.DRAW, gameService.findGameByGameId(game.getGameId()).getGameStatus());
    }
}
