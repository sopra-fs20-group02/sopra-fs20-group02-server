package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Long userId;
    private String name;
    private String username;
    private UserStatus status;
    private String creationDate;
    private String birthDate;
    private String token;
    private UserStats userStats;
}
