package ch.uzh.ifi.seal.soprafs20.logic.pieces;
import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import java.util.Arrays;

public class Knight extends Piece {
    public Knight(PieceDB pieceDB, Board board){
        super(pieceDB, board);
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
        this.pieceType = PieceType.KNIGHT;
    }
}


