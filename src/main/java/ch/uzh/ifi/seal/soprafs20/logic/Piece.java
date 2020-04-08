package ch.uzh.ifi.seal.soprafs20.logic;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public abstract class Piece {
    protected Color color;
    protected Vector position;
    protected Long pieceId;
    protected PieceType pieceType;

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

    public Piece(PieceDB piece, Board board){
        this.position = new Vector(piece.getXCord(), piece.getYCord());
        this.color = piece.getColor();
        this.pieceId = piece.getPieceId();

        this.captured = piece.isCaptured();
        this.hasMoved = piece.isHasMoved();

        this.board = board;

        this.initializeDefaultVectors();
    }

    public void initializeDefaultVectors(){
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
        // TODO: check if piece gets captured
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

    public PieceDB convertToDB(){
        PieceDB pieceDB = new PieceDB();
        pieceDB.setPieceId(this.pieceId);
        pieceDB.setPieceType(this.pieceType);
        pieceDB.setColor(this.color);
        pieceDB.setXCord(this.position.getX());
        pieceDB.setYCord(this.position.getY());
        pieceDB.setCaptured(this.captured);
        pieceDB.setHasMoved(this.hasMoved);
        return pieceDB;
    }
}