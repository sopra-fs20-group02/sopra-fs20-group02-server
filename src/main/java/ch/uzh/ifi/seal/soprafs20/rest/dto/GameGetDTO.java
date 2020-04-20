package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GameGetDTO {
    private Long gameId;
    private User playerWhite;
    private User playerBlack;
    private List<PieceDB> pieces;
    private Instant startTime;
    private Instant endTime;
    /*private Duration playerWhiteElapsedTime;
    private Duration playerBlackElapsedTime;*/
    private GameStatus gameStatus;
    private Boolean isWhiteTurn;
    private User winner;
}
