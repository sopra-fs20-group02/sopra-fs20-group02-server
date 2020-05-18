package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PieceRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserStatsRepository;
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
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("userStatsRepository")
    @Autowired
    private UserStatsRepository userStatsRepository;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PieceRepository pieceRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        userRepository.deleteAll();
        pieceRepository.deleteAll();
        userStatsRepository.deleteAll();
    }

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setUserStats(new UserStats());


        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password1");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setUsername("testUsername");
        testUser2.setPassword("password2");

        // check that an error is thrown
        String exceptionMessage = "The username provided is not unique. Therefore, the user could not be created!";
        SopraServiceException exception = assertThrows(SopraServiceException.class, () -> userService.createUser(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());
    }
}
