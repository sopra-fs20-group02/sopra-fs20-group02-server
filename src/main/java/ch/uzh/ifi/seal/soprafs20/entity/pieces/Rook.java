package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;

@Entity
@Table(name = "ROOK")
public class Rook extends Piece {
    /*public Rook(Vector position, Color color, Integer localId){
        super(position, color, localId);
        this.movementVectors.addAll(this.straights);
        this.movementSteps = 8;
    }*/
}


