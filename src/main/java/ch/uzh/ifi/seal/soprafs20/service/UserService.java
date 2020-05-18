package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.AlreadyLoggedInException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.LoginException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("userStatsRepository") UserStatsRepository userStatsRepository) {
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User findUserByToken(String token) {
        return this.userRepository.findByToken(token);
    }


    public User findUserByUserId(Long id){
        User user = userRepository.findByUserId(id);
        if(user != null) {
            return user;
        }
        else {
            throw new UserException("User with id: " + id + " was not found.");
        }
    }

    // Returns the user which matches the given username and password
    // Otherwise it throws an exception
    public User loginUser(User userInput){
        User foundUser = findUserByUsername(userInput.getUsername());
        if(foundUser == null){
            throw new LoginException("Login failed because credentials are incorrect.");
        }
        else if(userInput.getPassword().equals(foundUser.getPassword())){
            if(foundUser.getStatus().equals(UserStatus.OFFLINE)) {
                foundUser.setStatus(UserStatus.ONLINE);
            }
            userRepository.save(foundUser);
            userRepository.flush();
            return foundUser;
        }
        else {
            throw new LoginException("Login failed because credentials are incorrect.");
        }
    }

    // Updates the username and birthdate of a specific user
    public User updateUser(Long id, User userInput){
        User foundUser = findUserByUserId(id);
        if(userInput.getUsername() != null) {
            foundUser.setUsername(userInput.getUsername());
        }
        if(userInput.getBirthDate() != null) {
            foundUser.setBirthDate(userInput.getBirthDate());
        }

        userRepository.save(foundUser);
        userRepository.flush();
        return foundUser;
    }

    // creates a new user with the given input
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        newUser.setCreationDate(dateFormat.format(date));

        UserStats userstats = new UserStats();
        userstats.setTotalTimePlayed(0);
        userstats.setNumberOfLosses(0);
        userstats.setNumberOfWinnings(0);
        userstats.setNumberOfDraws(0);
        newUser.setUserStats(userstats);
        userStatsRepository.save(userstats);
        userStatsRepository.flush();

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    // sets the status of the user which matches the given id to OFFLINE
    public User logoutUser(User userInput){
        User user = findUserByUserId(userInput.getUserId());
        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
        userRepository.flush();
        return user;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws SopraServiceException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        /*if (userByUsername != null && userByName != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "username and the name", "are"));
        }*/
        if (userByUsername != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "username", "is"));
        }
        /*else if (userByName != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "name", "is"));
        }*/
    }
}
