package ch.uzh.ifi.seal.soprafs20.rest;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceInterfaceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

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

    /**
     * Default test
     * @throws Exception
     */
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("The application is running.");
    }


    @Test
    public void getGames_validInput_success() throws Exception {
        JSONObject body = new JSONObject();

        body.put("userId", this.playerA.getUserId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(body.toString(), headers);

        ResponseEntity<String> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/games", String.class, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createAndJoinGame_validInput_success() throws Exception {
        // player A
        JSONObject body = new JSONObject();

        body.put("userId", this.playerA.getUserId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(body.toString(), headers);

        ResponseEntity<String> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/games",  request, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // player B

        body.put("userId", this.playerB.getUserId());

        headers.setContentType(MediaType.APPLICATION_JSON);

        request =
                new HttpEntity<String>(body.toString(), headers);

        this.restTemplate.put("http://localhost:" + port + "/games",  request, String.class);

        // get games

        body.put("userId", this.playerA.getUserId());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        request =
                new HttpEntity<String>(body.toString(), headers);

        response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/games", String.class, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).contains("pA");
        assertThat(response.getBody()).contains("pB");
        assertThat(response.getBody()).contains("FULL");

    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SopraServiceException(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
