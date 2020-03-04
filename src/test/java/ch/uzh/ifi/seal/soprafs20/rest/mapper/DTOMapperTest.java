package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserLoginDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("testpassword");
        userPostDTO.setBirthDate("00/00/00");
        userPostDTO.setToken("1");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getBirthDate(), user.getBirthDate());
        assertEquals(userPostDTO.getToken(), user.getToken());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setBirthDate("00/00/0000");
        user.setId(1L);
        user.setCreationDate("11/11/1111");
        user.setPassword("testpassword");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getName(), userGetDTO.getName());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getToken(), userGetDTO.getToken());
        assertEquals(user.getBirthDate(), userGetDTO.getBirthDate());
        assertEquals(user.getCreationDate(), userGetDTO.getCreationDate());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
    }

    @Test
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
    public void testGetUser_fromUser_toUserProfileDTO_success() {
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setBirthDate("00/00/0000");
        user.setId(1L);
        user.setCreationDate("11/11/1111");

        // MAP -> Create UserProfileDTO
        UserProfileDTO userProfileDTO = DTOMapper.INSTANCE.convertEntityToUserProfileDTO(user);

        // check content
        assertEquals(user.getId(), userProfileDTO.getId());
        assertEquals(user.getUsername(), userProfileDTO.getUsername());
        assertEquals(user.getStatus(), userProfileDTO.getStatus());
        assertEquals(user.getToken(), userProfileDTO.getToken());
        assertEquals(user.getBirthDate(), userProfileDTO.getBirthDate());
        assertEquals(user.getCreationDate(), userProfileDTO.getCreationDate());
    }

}
