package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

public class Queen extends Piece {
    public Queen(Vector position, Color color){
        super(position, color);
        this.movementVectors.addAll(this.straights);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 8;
    }
}
