package ch.uzh.ifi.seal.soprafs20.logic;
import ch.uzh.ifi.seal.soprafs20.entity.GameDB;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.*;

import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

/*
COORDINATE SPACE:
White always plays positive Y axis
Black plays negative Y axis
Tile at (1,1) is of BLACK color
 */
public class Board {
    // All indices are from 1 - 8 (including 8)
    // We ignore the piece at 0, to avoid confusion
    // Empty fields is indicated with a null
    private Piece[][] board = new Piece[9][9];
    private ArrayList<Piece> piecesOutGame;

    public Piece getPieceOnTile(Vector vector) {
        return this.board[vector.getX()][vector.getY()];
    }

    public void emptyBoard(){
        this.piecesOutGame = new ArrayList<Piece>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                this.board[i][j] = null;
            }
        }
    }

    public List<PieceDB> getAllPieces(){
        List<PieceDB> allPieces = new ArrayList<PieceDB>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (this.board[i][j] != null){
                    allPieces.add(this.board[i][j].convertToDB());
                }
            }
        }
        for (Piece piece : piecesOutGame){
            allPieces.add(piece.convertToDB());
        }
        return allPieces;
    }

    public Piece getById(Long id){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (this.board[i][j].getPieceId() == id){
                    return this.board[i][j];
                }
            }
        }

        for (Piece piece : piecesOutGame){
            if (piece.getPieceId() == id){
                return piece;
            }
        }

        // TODO: throw piece not in game exception
        throw new Error();
    }

    public ArrayList<Vector> getPossibleMoves(PieceDB pieceDB){
        return getById(pieceDB.getPieceId()).getPossibleMoves();
    }

    public void makeMove(Long pieceId, Vector moveTo){
        getById(pieceId).move(moveTo);
    }

    public void setGame(GameDB game){
        this.emptyBoard();
        List<PieceDB> pieces = game.getPieces();

        for (PieceDB piece : pieces){
            int x = piece.getXCord();
            int y = piece.getYCord();
            switch(piece.getPieceType()) {
                case BISHOP:
                    this.board[x][y] = new Bishop(piece, this);
                    break;
                case KING:
                    this.board[x][y] = new King(piece, this);
                    break;
                case KNIGHT:
                    this.board[x][y] = new Knight(piece, this);
                    break;
                case PAWN:
                    this.board[x][y] = new Pawn(piece, this);
                    break;
                case QUEEN:
                    this.board[x][y] = new Queen(piece, this);
                    break;
                case ROOK:
                    this.board[x][y] = new Rook(piece, this);
                    break;
                default :
                    break;
            }
        }
    }

}