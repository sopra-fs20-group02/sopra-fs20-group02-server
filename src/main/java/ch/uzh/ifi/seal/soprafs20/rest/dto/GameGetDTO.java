package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.BoardRow;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;
import ch.uzh.ifi.seal.soprafs20.entity.User;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class GameGetDTO {
    private Long gameId;
    private User playerWhite;
    private User playerBlack;
    private List<Piece> pieces;
    private Instant startTime;
    private Instant endTime;
    /*private Duration playerWhiteElapsedTime;
    private Duration playerBlackElapsedTime;*/
    private GameStatus gameStatus;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public User getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(User playerWhite) {
        this.playerWhite = playerWhite;
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPlayerBlack(User playerBlack) {
        this.playerBlack = playerBlack;
    }

    public User getPlayerBlack() {
        return playerBlack;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
