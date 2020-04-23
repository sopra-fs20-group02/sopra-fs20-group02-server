package ch.uzh.ifi.seal.soprafs20.logic.pieces;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;

import java.util.ArrayList;
import java.util.List;


public class King extends Piece {
    public King(PieceDB pieceDB, Board board){
        super(pieceDB, board);
        this.movementVectors.addAll(this.straights);
        this.movementVectors.addAll(this.diagonals);
        this.movementSteps = 1;
        this.pieceType = PieceType.KING;
    }

    // A King cannot commit suicide, thus it needs to be checked weather a move can be made differently
    @Override
    public ArrayList<Vector> getPossibleMoves(){
        ArrayList<Vector> possibleMoves = new ArrayList<Vector>();
        for (Vector vector : movementVectors){
            for (int i = 1; i <= this.movementSteps; i++) {
                Vector current = new Vector(vector).mulS(i).add(this.position);
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

        ArrayList<Vector> possibleMovesNoSuicide = new ArrayList<Vector>();
        List<Piece> pieces = this.board.getPieces();

        // May be slow !
        for (Vector myMove : possibleMoves){
            Boolean valid = true;
            for (Piece piece : pieces){
                // only check for opponent moves
                if (piece.getColor() == this.getColor()){
                    continue;
                }
                // TODO: there is a special case for PAWN
                for (Vector otherMove : piece.getPossibleMoves()){
                    if (otherMove.equals(myMove)){
                        valid = false;
                    }
                }
            }
            if (valid){
                possibleMovesNoSuicide.add(myMove);
            }
        }

        return possibleMovesNoSuicide;
    };
}


