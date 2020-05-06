package ch.uzh.ifi.seal.soprafs20.rest;

import static org.assertj.core.api.Assertions.assertThat;

import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceInterfaceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Default test
     * @throws Exception
     */
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("The application is running.");
    }

    /**
     * User registration with valid information
     * @throws Exception
     */
    @Test
    @Order(0)
    public void userRegistration_givenValidInformation_shouldReturnURL() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Test User");
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(asJsonString(userPostDTO), headers);

        ResponseEntity<String> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/users", request, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    /**
     * User registration with invalid information
     * @throws Exception
     */
    @Test
    @Order(1)
    public void userRegistration_givenInvalidInformation_shouldReturnConflict() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Test User");
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(asJsonString(userPostDTO), headers);

        ResponseEntity<String> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/users", request, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
    }

    /**
     * User login with valid information
     * @throws Exception
     */
    @Test
    @Order(2)
    public void userLogin_givenValidInformation_shouldReturnUser() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<String>(asJsonString(userPostDTO), headers);

        ResponseEntity<String> response =
                restTemplate.exchange("http://localhost:" + port + "/login", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
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
