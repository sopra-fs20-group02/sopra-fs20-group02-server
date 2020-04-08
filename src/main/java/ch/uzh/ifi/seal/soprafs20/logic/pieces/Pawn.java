package ch.uzh.ifi.seal.soprafs20.logic.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: make sure this class works, as it has differenct functionalities during different steps of the game
public class Pawn extends Piece {
    ArrayList<Vector> captureVectors;
    public Pawn(PieceDB pieceDB, Board board){
       super(pieceDB, board);
        this.captureVectors = new ArrayList<Vector>();

        if (color == Color.WHITE){
            this.movementVectors.add(new Vector(0,1));
            this.captureVectors.addAll(
                    this.diagonalsFront
            );
        }

        if (color == Color.BLACK){
            this.movementVectors.add(new Vector(0,-1));
            this.captureVectors.addAll(
                    this.diagonalsBack
            );
        }
        this.movementSteps = 2;
        this.pieceType = PieceType.PAWN;
    }

    // precondition: is legal move
    @Override
    public void move(Vector moveTo){
        this.position.set(moveTo);
        this.movementSteps = 1;
    }
}

