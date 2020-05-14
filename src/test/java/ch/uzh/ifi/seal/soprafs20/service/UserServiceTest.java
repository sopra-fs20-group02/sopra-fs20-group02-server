package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatsRepository userStatsRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserStats userStats;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        userStats = new UserStats();
        userStats.setTotalTimePlayed(0);
        userStats.setNumberOfLosses(0);
        userStats.setNumberOfWinnings(0);
        userStats.setNumberOfDraws(0);

        testUser.setUserStats(userStats);


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userStatsRepository.save(Mockito.any())).thenReturn(userStats);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userStatsRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        String exceptionMessage = "The username provided is not unique. Therefore, the user could not be created!";
        SopraServiceException exception = assertThrows(SopraServiceException.class, () -> userService.createUser(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());
    }

}
