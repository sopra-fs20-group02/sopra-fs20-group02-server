package ch.uzh.ifi.seal.soprafs20.logic.pieces;
import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;

public class Bishop extends Piece {
    public Bishop(PieceDB pieceDB, Board board){
        super(pieceDB, board);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 8;
        this.pieceType = PieceType.BISHOP;
    }
}
