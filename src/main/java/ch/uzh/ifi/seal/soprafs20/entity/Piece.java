package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;

/*
Abstract class of pieces
 */
public abstract class Piece {
    protected Color color;
    protected Vector position;
    protected Boolean captured;
    protected Vector[] moves;

    public Piece(Vector initial, Color color){
        this.position = initial;
        this.color = color;
        this.captured = false;
    }

    public void move(Vector moveTo){
        // TODO: check for illegal move
    }

    public Vector[] getPossibleMoves(){
        return null;
    };

    public Vector getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getState(){
        return captured;
    }
}
