package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: make sure castling works
public class King extends Piece {
    King(Vector position, Color color, Integer localId){
        super(position, color, localId);
        this.movementVectors.addAll(this.straights);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 1;
    }
}
