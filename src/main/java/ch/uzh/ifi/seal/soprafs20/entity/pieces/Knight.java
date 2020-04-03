package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

import java.util.Arrays;

public class Knight extends Piece {
    public Knight(Vector position, Color color, Integer localId){
        super(position, color, localId);
        this.movementVectors.addAll(Arrays.asList(
                new Vector(-1,2),
                new Vector(1,2),
                new Vector(-1,-2),
                new Vector(1,-2),
                new Vector(2,-1),
                new Vector(2,1),
                new Vector(-2,-1),
                new Vector(-2,-1)
        ));
        this.movementSteps = 1;
    }
}
