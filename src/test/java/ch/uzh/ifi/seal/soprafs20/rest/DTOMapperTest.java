package ch.uzh.ifi.seal.soprafs20.rest;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {

    @Test
    @Order(0)
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("tespassword");
        userPostDTO.setBirthDate("00/00/00");
        userPostDTO.setToken("1");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getBirthDate(), user.getBirthDate());
        assertEquals(userPostDTO.getToken(), user.getToken());
    }

    @Test
    @Order(1)
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setBirthDate("00/00/0000");
        user.setUserId(1L);
        user.setCreationDate("11/11/1111");
        user.setPassword("testpassword");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getUserId(), userGetDTO.getUserId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getToken(), userGetDTO.getToken());
        assertEquals(user.getBirthDate(), userGetDTO.getBirthDate());
        assertEquals(user.getCreationDate(), userGetDTO.getCreationDate());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
    }

    @Test
    @Order(2)
    public void testGetUser_fromUser_toUserLoginDTO_success() {
        // create User
        User user = new User();
        user.setToken("1");

        // MAP -> Create UserProfileDTO
        UserLoginDTO userLoginDTO = DTOMapper.INSTANCE.convertEntityToUserLoginDTO(user);

        // check content
        assertEquals(user.getToken(), userLoginDTO.getToken());
    }

    @Test
    @Order(3)
    public void testGetUser_fromUser_toUserProfileDTO_success() {
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setBirthDate("00/00/0000");
        user.setUserId(1L);
        user.setCreationDate("11/11/1111");

        // MAP -> Create UserProfileDTO
        UserProfileDTO userProfileDTO = DTOMapper.INSTANCE.convertEntityToUserProfileDTO(user);

        // check content
        assertEquals(user.getUserId(), userProfileDTO.getUserId());
        assertEquals(user.getUsername(), userProfileDTO.getUsername());
        assertEquals(user.getStatus(), userProfileDTO.getStatus());
        assertEquals(user.getToken(), userProfileDTO.getToken());
        assertEquals(user.getBirthDate(), userProfileDTO.getBirthDate());
        assertEquals(user.getCreationDate(), userProfileDTO.getCreationDate());
    }

    @Test
    @Order(4)
    public void testGetGame_fromGame_toGameGetDTO_success() {
        // Create Users
        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);

        // create Game
        Game game = new Game();
        game.setGameId(1L);
        game.setPlayerWhite(user1);
        game.setPlayerBlack(user2);
        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(new PieceDB());
        game.setPieces(pieces);
        game.setGameStatus(GameStatus.FULL);

        // MAP -> Create GameGetDTO
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        // check content
        assertEquals(game.getGameId(), gameGetDTO.getGameId());
        assertEquals(game.getPlayerWhite(), gameGetDTO.getPlayerWhite());
        assertEquals(game.getPlayerBlack(), gameGetDTO.getPlayerBlack());
        assertEquals(game.getPieces(), gameGetDTO.getPieces());
        assertEquals(game.getGameStatus(), gameGetDTO.getGameStatus());
    }

}
