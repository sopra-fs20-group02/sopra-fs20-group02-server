package ch.uzh.ifi.seal.soprafs20.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostDTO {
    private String name;
    private String username;
    private String password;
    private String birthDate;
    private String token;
}
