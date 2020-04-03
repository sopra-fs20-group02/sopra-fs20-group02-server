package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

// TODO: make sure castling works
public class Bishop extends Piece {
    public Bishop(Vector position, Color color, Integer localId){
        super(position, color, localId);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 8;
    }
}
