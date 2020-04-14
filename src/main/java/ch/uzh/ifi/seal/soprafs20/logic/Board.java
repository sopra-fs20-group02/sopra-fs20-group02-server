package ch.uzh.ifi.seal.soprafs20.logic;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.*;
import ch.uzh.ifi.seal.soprafs20.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    private PieceRepository pieceRepository;

    public Board(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
    }

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

    public List<Piece> getPieces(){
        List<Piece> pieces = new ArrayList<Piece>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (this.board[i][j] != null){
                    pieces.add(this.board[i][j]);
                }
            }
        }
        for (Piece piece : piecesOutGame){
            pieces.add(piece);
        }
        return pieces;
    }

    public List<PieceDB> saveToRepository(){
        List<Piece> pieces = this.getPieces();
        ArrayList<PieceDB> databasePieces = new ArrayList<PieceDB>();
        for (Piece piece : pieces){
            PieceDB pieceDB = pieceRepository.findByPieceId(piece.getPieceId());
            pieceDB.setPieceType(piece.getPieceType());
            pieceDB.setColor(piece.getColor());
            pieceDB.setXCord(piece.getPosition().getX());
            pieceDB.setYCord(piece.getPosition().getY());
            pieceDB.setCaptured(piece.getCaptured());
            pieceDB.setHasMoved(piece.getHasMoved());
            databasePieces.add(pieceDB);
        }
        return databasePieces;
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

    public ArrayList<Vector> getPossibleMoves(Long pieceId){
        return getById(pieceId).getPossibleMoves();
    }

    public void makeMove(Long pieceId, Vector moveTo){
        Piece piece = getById(pieceId);
        this.board[piece.position.getX()][piece.position.getY()] = null;
        this.board[moveTo.getX()][moveTo.getY()] = piece;
        piece.move(moveTo);

        Piece captured = this.getPieceOnTile(moveTo);
        if (captured != null){
            captured.setCaptured(true);
            this.piecesOutGame.add(captured);
        }
    }

    public void setPieces(Game game){
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