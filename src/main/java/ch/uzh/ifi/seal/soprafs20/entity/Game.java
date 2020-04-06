package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
//import ch.uzh.ifi.seal.soprafs20.constant.Vector;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @OneToOne
    @JoinColumn()
    private User playerWhite;

    @OneToOne
    @JoinColumn()
    private User playerBlack;

    @OneToOne
    @JoinColumn()
    private Board board;

    @Column()
    private Instant startTime;

    @Column()
    private Instant endTime;

    /*@Column()
    private Duration playerWhiteElapsedTime;

    @Column()
    private Duration playerBlackElapsedTime;*/

    @Column()
    private GameStatus gameStatus;

    @OneToOne
    @JoinColumn()
    private User winner;

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setPlayerWhite(User playerWhite) {
        this.playerWhite = playerWhite;
    }

    public User getPlayerWhite() {
        return playerWhite;
    }

    public User getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(User playerBlack) {
        this.playerBlack = playerBlack;
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

    /*
    ArrayList<Vector> getPossibleMoves(Piece piece){
        return piece.getPossibleMoves();
    }

    void makeMove(Piece piece, Vector moveTo){
        // TODO: check for invalid move
        // TODO: implement
    }

    public void addPlayer(User playerBlack) {
        this.playerBlack = playerBlack;
    }

    public User getWinner() {
        if (gameStatus != GameStatus.FINISHED){
            // TODO: throw game not finished exception
        }
        return winner;
    }

    public Duration elapsed(){
        return Duration.between(startTime, Instant.now());
    }
    */
}
