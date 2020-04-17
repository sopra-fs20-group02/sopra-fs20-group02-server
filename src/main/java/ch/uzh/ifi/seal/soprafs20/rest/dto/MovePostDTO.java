package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MovePostDTO {
    private Long userId;
    private Long pieceId;
    private Integer x;
    private Integer y;
}
