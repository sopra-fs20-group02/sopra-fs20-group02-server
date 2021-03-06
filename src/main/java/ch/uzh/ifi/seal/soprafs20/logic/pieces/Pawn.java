package ch.uzh.ifi.seal.soprafs20.logic.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    ArrayList<Vector> captureVectors;
    public Pawn(PieceDB pieceDB, Board board){
       super(pieceDB, board);
        this.captureVectors = new ArrayList<>();

        if (color == Color.WHITE){
            this.movementVectors.add(new Vector(0,1));
            this.captureVectors.addAll(this.diagonalsFront);
        }

        if (color == Color.BLACK){
            this.movementVectors.add(new Vector(0,-1));
            this.captureVectors.addAll(this.diagonalsBack);
        }
        this.movementSteps = 2;
        this.pieceType = PieceType.PAWN;
    }

    // Pawn is the only piece with different freedoms in movement for captures and movements
    @Override
    public ArrayList<Vector> getPossibleMoves(){
        if (this.hasMoved){
            this.movementSteps = 1;
        }
        ArrayList<Vector> possibleMoves = new ArrayList<>();
        for (Vector vector : movementVectors){
            for (int i = 1; i <= this.movementSteps; i++) {
                Vector current = new Vector(vector).mulS(i).add(this.position);
                if (!current.checkBounds()){
                    break;
                }
                Piece piece = this.board.getPieceOnTile(current);
                if (piece != null){
                    break;
                }
                else{
                    possibleMoves.add(current);
                }
            }
        }

        for (Vector current : getCaptureMoves()){
            Piece piece = this.board.getPieceOnTile(current);
            if (piece != null && piece.getColor() != this.color){
                possibleMoves.add(current);
            }
        }

        return possibleMoves;
    }

    public ArrayList<Vector> getCaptureMoves() {
        ArrayList<Vector> captureMoves = new ArrayList<>();
        for (Vector vector : captureVectors) {
            Vector current = new Vector(vector).mulS(1).add(this.position);
            if (current.checkBounds()) {
                captureMoves.add(current);
            }
        }
        return captureMoves;
    }
}

