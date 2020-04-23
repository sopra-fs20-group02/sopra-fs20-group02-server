package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinPutDTO {
    Long gameId;
    Long userId;
}
