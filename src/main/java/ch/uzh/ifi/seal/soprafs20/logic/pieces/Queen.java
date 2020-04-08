package ch.uzh.ifi.seal.soprafs20.logic.pieces;

import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;


public class Queen extends Piece {
    public Queen(PieceDB pieceDB, Board board){
        super(pieceDB, board);
        this.movementVectors.addAll(this.straights);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 8;
        this.pieceType = PieceType.QUEEN;
    }
}
