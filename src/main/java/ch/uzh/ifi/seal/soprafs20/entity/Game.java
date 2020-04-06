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

    @Column()
    private Duration playerWhiteElapsedTime;

    @Column()
    private Duration playerBlackElapsedTime;

    @Column()
    private GameStatus gameStatus;

    @OneToOne
    @JoinColumn()
    private User winner;

    public Game(User playerWhite){
        this.playerWhite = playerWhite;
        board = new Board();
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
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
