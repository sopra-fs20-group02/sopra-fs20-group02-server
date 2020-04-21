package ch.uzh.ifi.seal.soprafs20.logic;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs20.exceptions.PieceNotInGameException;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.*;
import ch.uzh.ifi.seal.soprafs20.repository.PieceRepository;

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
    // Empty field is indicated with a null
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

    /**
     * Auto saves pieces to repository
     * @return
     */
    public List<PieceDB> saveAndGetPieces(){
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
                if (this.board[i][j] != null && this.board[i][j].getPieceId() == id){
                    return this.board[i][j];
                }
            }
        }

        for (Piece piece : piecesOutGame){
            if (piece.getPieceId() == id){
                return piece;
            }
        }

        throw new PieceNotInGameException("Piece with " +id+ " could not be found.");
    }

    public ArrayList<Vector> getPossibleMoves(Long pieceId){
        Piece piece = getById(pieceId);
        ArrayList<Vector> possibleMoves = piece.getPossibleMoves();
        if (piece.getPieceType() == PieceType.KING && !piece.getHasMoved()) {
            ArrayList<Vector> possibleCastling = checkForCastle(pieceId);
            if (!possibleCastling.isEmpty()) {
                possibleMoves.addAll(possibleCastling);
            }
        }

        return possibleMoves;
    }


    public ArrayList<Vector> checkForCastle(Long pieceId) {
        Piece piece = getById(pieceId);
        ArrayList<Vector> possibleCastling = new ArrayList<>();

        if (piece.getPosition().equals(new Vector(5,1))) {

            if (board[4][1] == null && board[3][1] == null && board[2][1] == null && !board[1][1].getHasMoved()) {
                possibleCastling.add(new Vector(1,1));
            }
            if (board[6][1] == null && board[7][1] == null && !board[8][1].getHasMoved()) {
                possibleCastling.add(new Vector(8,1));
            }
        }

        else if (piece.getPosition().equals(new Vector(5,8))) {

            if (board[4][8] == null && board[3][8] == null && board[2][8] == null && !board[1][8].getHasMoved()) {
                possibleCastling.add(new Vector(1,8));
            }
            if (board[6][8] == null && board[7][8] == null && !board[8][8].getHasMoved()) {
                possibleCastling.add(new Vector(8,8));
            }
        }

        return possibleCastling;
    }

    public void makeMove(Long pieceId, Vector moveTo){
        Piece piece = getById(pieceId);

        // check for valid move
        ArrayList<Vector> possibleMoves = getPossibleMoves(pieceId);
        for (Vector move : possibleMoves) {
            if (move.equals(moveTo)) {
                throw new InvalidMoveException("This piece can not move to the desired position");
            }
        }

        this.board[piece.position.getX()][piece.position.getY()] = null;
        this.board[moveTo.getX()][moveTo.getY()] = piece;
        piece.move(moveTo);

        Piece captured = this.getPieceOnTile(moveTo);

        // Capture Piece
        if (captured != null && captured.getColor() != piece.getColor()){
            captured.setCaptured(true);
            this.piecesOutGame.add(captured);
        }

        // Castling --> king captures own rook
        else if (piece.getColor() == captured.getColor() && piece.getPieceType() == PieceType.KING) {
            if (piece.getPosition().equals(new Vector(5,1))) {
                if (moveTo.equals(new Vector(1,1))) {
                    castle(piece, captured, new Vector(2,1), new Vector(3,1));
                }
                else if (moveTo.equals(new Vector(8,1))) {
                    castle(piece, captured, new Vector(7,1), new Vector(6,1));
                }
            }

            else if (piece.getPosition().equals(new Vector(5,8))) {
                if (moveTo.equals(new Vector(1,8))) {
                    castle(piece, captured, new Vector(2,8), new Vector(3,8));
                }
                else if (moveTo.equals(new Vector(8,8))) {
                    castle(piece, captured, new Vector(7,8), new Vector(6,8));
                }
            }
        }
    }

    // Effective castling, king and rook get on their new position
    public void castle(Piece king, Piece rook, Vector kingDest, Vector rookDest) {
        this.board[kingDest.getX()][kingDest.getY()] = king;
        this.board[rookDest.getX()][rookDest.getY()] = rook;
        rook.move(rookDest);
        king.move(kingDest);
    }

    public void updateGameStatus(Game game){
        // TODO: check for different win/draw conditions

        // TODO: checkmate

        // TODO: stalemate
    }

    public void setPieces(Game game){
        this.emptyBoard();
        List<PieceDB> pieces = game.getPieces();

        for (PieceDB piece : pieces){
            // TODO: handle caputered pieces
            int x = piece.getXCord();
            int y = piece.getYCord();

            Piece logicPiece = null;
            switch(piece.getPieceType()) {
                case BISHOP:
                    logicPiece = new Bishop(piece, this);
                    break;
                case KING:
                    logicPiece = new King(piece, this);
                    break;
                case KNIGHT:
                    logicPiece = new Knight(piece, this);
                    break;
                case PAWN:
                    logicPiece = new Pawn(piece, this);
                    break;
                case QUEEN:
                    logicPiece = new Queen(piece, this);
                    break;
                case ROOK:
                    logicPiece = new Rook(piece, this);
                    break;
                default:
                    break;
            }

            if (piece.isCaptured()){
                this.piecesOutGame.add(logicPiece);
            }
            else {
                this.board[x][y] = logicPiece;
            }
        }
    }
}
