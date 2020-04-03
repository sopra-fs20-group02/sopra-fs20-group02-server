package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

public class Rook extends Piece {
    Rook(Vector vector, Color color){
        super(vector, color);
    }

    public Vector[] getPossibleMoves(){

        return null;
    };
}
