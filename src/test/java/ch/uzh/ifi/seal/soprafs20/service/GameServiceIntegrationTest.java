package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
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

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        userRepository.deleteAll();

        // register and login two users
        assertNull(userRepository.findByUsername("playerA"));
        assertNull(userRepository.findByUsername("playerB"));

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
    }


    @Test
    public void createGame_registeredUser_success() {
        Game game = gameService.createNewGame(playerA);
        System.out.println(game.getGameId());
        assertEquals(gameRepository.findByGameId(game.getGameId()).getGameStatus(), GameStatus.WAITING);
    }
/*
    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password1");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername");
        testUser2.setPassword("password2");

        // check that an error is thrown
        String exceptionMessage = "The username provided is not unique. Therefore, the user could not be created!";
        SopraServiceException exception = assertThrows(SopraServiceException.class, () -> userService.createUser(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());
    }*/
}
