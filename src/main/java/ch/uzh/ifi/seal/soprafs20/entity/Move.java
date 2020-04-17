package ch.uzh.ifi.seal.soprafs20.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {
    private Long userId;
    private Long pieceId;
    private Integer x;
    private Integer y;
}
