package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.constant.Color;
//import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
Abstract class of pieces
A possible move is not yet checked for other pieces on the field
 */

@Entity
@Table(name = "PIECE")
public class Piece {

    @Id
    @GeneratedValue
    public Long pieceId;

    public Long getPieceId() {
        return pieceId;
    }

    public void setPieceId(Long pieceId) {
        this.pieceId = pieceId;
    }
    /*
    protected Color color;
    protected Vector position;
    protected Integer localId;

    protected Board board;

    protected Boolean captured;
    protected Boolean hasMoved;
    protected ArrayList<Vector> movementVectors;
    protected Integer movementSteps;

    protected ArrayList<Vector> diagonalsFront;
    protected ArrayList<Vector> diagonalsBack;
    protected ArrayList<Vector> diagonals;
    protected ArrayList<Vector> horizontals;
    protected ArrayList<Vector> verticals;
    protected ArrayList<Vector> straights;

    public Piece(Vector initial, Color color, Integer localId){
        this.position = initial;
        this.color = color;
        this.localId = localId;
        this.captured = false;
        this.hasMoved = false;

        this.diagonalsFront = new ArrayList<Vector>();
        this.diagonalsBack = new ArrayList<Vector>();
        this.diagonals = new ArrayList<Vector>();
        this.verticals = new ArrayList<Vector>();
        this.horizontals = new ArrayList<Vector>();
        this.straights = new ArrayList<Vector>();

        // initialize default vector lists
        this.diagonalsFront.addAll(Arrays.asList(new Vector(1,1), new Vector(1,-1)));
        this.diagonalsBack.addAll(Arrays.asList(new Vector(-1,1), new Vector(-1,-1)));
        this.diagonals.addAll(this.diagonalsFront);
        this.diagonals.addAll(this.diagonalsBack);
        this.verticals.addAll(Arrays.asList(new Vector(0,1), new Vector(0,-1)));
        this.horizontals.addAll(Arrays.asList(new Vector(1,0), new Vector(-1,0)));
        this.straights.addAll(this.verticals);
        this.straights.addAll(this.horizontals);
    }

    // precondition: is legal move
    public void move(Vector moveTo){
        this.hasMoved = true;
        this.position.set(new Vector(moveTo));
    }

    public void setCaptured(){
        this.captured = true;
    }

    public ArrayList<Vector> getPossibleMoves(){
        // TODO: test this !!
        // TODO: needs different logic for pawn
        ArrayList<Vector> possibleMoves = new ArrayList<Vector>();
        for (Vector vector : movementVectors){
            for (int i = 1; i < 8; i++) {
                Vector current = new Vector(vector).mulS(i);
                if (!current.checkBounds()){
                    break;
                }
                Piece piece = this.board.getPieceOnTile(current);
                if (piece != null){
                    if (piece.getColor() != this.color) {
                        possibleMoves.add(current);
                    }
                    break;
                }
                else{
                    possibleMoves.add(current);
                }
            }
        }
        return possibleMoves;
    };

    public ArrayList<Vector> getPossibleCaptures(){
        return this.getPossibleMoves();
    }

    public Vector getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getState(){
        return captured;
    }
    */
}
