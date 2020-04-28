package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
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

    private User playerA;

    private User playerB;

    private User playerC;

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
}
