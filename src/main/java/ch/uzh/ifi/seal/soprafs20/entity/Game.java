package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Vector;

import java.util.ArrayList;

public class Game {
    User playerWhite;
    User playerBlack;
    Board board;

    Long gameID;
    Boolean isFinished;
    User winner;

    Game(User playerWhite, User playerBlack){
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        board = new Board();
    }

    ArrayList<Vector> getPossibleMoves(Piece piece){
        return piece.getPossibleMoves();
    }

    void makeMove(Piece piece, Vector moveTo){
        // TODO: check for invalid move
        // TODO: implement
    }

    public User getWinner() {
        if (!isFinished){
            // TODO: throw game not finished exception
        }
        return winner;
    }
}
