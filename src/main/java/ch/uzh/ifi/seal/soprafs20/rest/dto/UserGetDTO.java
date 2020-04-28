package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserGetDTO {
    private Long userId;
    private String name;
    private String username;
    private UserStatus status;
    private String password;
    private String creationDate;
    private String birthDate;
    private String token;
    //private List<Game> gameHistory;
    private UserStats userStats;
}
