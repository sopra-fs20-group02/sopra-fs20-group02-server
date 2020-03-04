package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.LoginException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserLoginDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserProfileDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // Handles the get request to get all users
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    // Handles the get request to get a specific users
    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserProfileDTO getSpecificUser(@PathVariable("id") Long id) {
        return DTOMapper.INSTANCE.convertEntityToUserProfileDTO(userService.findUserById(id));
    }

    // Handles the post request to create a user
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User user = userService.createUser(userInput);

        // convert internal representation of user back to API
        return "http://localhost:8080/users/"+user.getId();
    }

    // Handles the put request to login the user
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserLoginDTO findUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        return DTOMapper.INSTANCE.convertEntityToUserLoginDTO(userService.loginUser(userInput));
    }

    // Handles the put request to update a specific user
    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateSpecificUser(@PathVariable("id") Long id, @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userService.updateUser(id, userInput);
    }

    // Handles the put request to logout the user
    @PutMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logoutUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userService.logoutUser(userInput.getToken());
    }

}
