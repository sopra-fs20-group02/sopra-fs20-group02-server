package ch.uzh.ifi.seal.soprafs20.entity.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;

// TODO: make sure this class works, as it has differenct functionalities during different steps of the game
@Entity
@Table(name = "PAWN")
public class Pawn extends Piece {
     /*ArrayList<Vector> captureVectors;
    public Pawn(Vector position, Color color, Integer localId ){
       super(position, color, localId);
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
    }

    // precondition: is legal move
    @Override
    public void move(Vector moveTo){
        this.position.set(moveTo);
        this.movementSteps = 1;
    }

    // For the pawn the possible captures differ from the possible moves
    @Override
    public ArrayList<Vector> getPossibleCaptures() {
        return (ArrayList<Vector>) this.captureVectors.clone();
    }*/
}

