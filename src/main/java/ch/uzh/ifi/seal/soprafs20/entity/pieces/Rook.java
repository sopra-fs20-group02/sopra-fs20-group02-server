package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Piece {
    public Rook(Vector position, Color color){
        super(position, color);
        this.movementVectors.addAll(this.straights);
        this.movementSteps = 8;
    }
}
