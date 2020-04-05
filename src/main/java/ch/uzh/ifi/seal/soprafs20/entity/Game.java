package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Vector;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game {
    @Id
    @GeneratedValue
    Long gameId;

    @Column(nullable = false)
    User playerWhite;

    @Column
    User playerBlack;

    @Column(nullable = false, unique = true)
    Board board;

    @Column
    Instant startTime;

    @Column
    Instant endTime;

    @Column
    Duration playerWhiteElapsedTime;

    @Column
    Duration playerBlackElapsedTime;

    @Column
    Boolean isFinished;

    @Column
    User winner;

    public Game(User playerWhite){
        this.playerWhite = playerWhite;
        board = new Board();
    }

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
        if (!isFinished){
            // TODO: throw game not finished exception
        }
        return winner;
    }

    public Duration elapsed(){
        return Duration.between(startTime, Instant.now());
    }
}
