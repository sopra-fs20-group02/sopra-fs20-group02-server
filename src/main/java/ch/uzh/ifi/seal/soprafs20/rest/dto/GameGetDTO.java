package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Board;
import ch.uzh.ifi.seal.soprafs20.entity.User;

import java.time.Duration;
import java.time.Instant;

public class GameGetDTO {
    private Long gameId;
    /*private User playerWhite;
    private User playerBlack;
    private Board board;
    private Instant startTime;
    private Instant endTime;
    private Duration playerWhiteElapsedTime;
    private Duration playerBlackElapsedTime;
    private GameStatus gameStatus;*/

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
