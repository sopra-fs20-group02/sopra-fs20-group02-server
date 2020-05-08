package ch.uzh.ifi.seal.soprafs20.logic;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidMoveException;
import ch.uzh.ifi.seal.soprafs20.exceptions.PieceNotInGameException;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.*;
import lombok.Getter;
import lombok.Setter;

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

    @Setter
    private Game currentGame;

    @Getter
    @Setter
    private Boolean isWhiteTurn;

    @Getter
    private GameStatus status;

    public Board() {}


    public Piece getPieceOnTile(Vector vector) {
        return this.board[vector.getX()][vector.getY()];
    }

    public void emptyBoard(){
        this.piecesOutGame = new ArrayList<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                this.board[i][j] = null;
            }
        }
    }

    public List<Piece> getPieces(){
        List<Piece> pieces = new ArrayList<>();
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

    public Color getIsTurnColor(){
        if(isWhiteTurn) {
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    public Piece getById(Long id){
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (this.board[i][j] != null && this.board[i][j].getPieceId().equals(id)){
                    return this.board[i][j];
                }
            }
        }

        for (Piece piece : piecesOutGame){
            if (piece.getPieceId().equals(id)){
                return piece;
            }
        }

        throw new PieceNotInGameException("Piece with " +id+ " could not be found.");
    }

    private Piece getColorsKing(Color color) {
        for (Piece piece: getPieces()) {
            if (piece.getColor() == color && piece.getPieceType() == PieceType.KING) {
                return piece;
            }
        }
        return null;
    }

    public ArrayList<Vector> getPossibleMoves(Long pieceId){
        Piece piece = getById(pieceId);
        ArrayList<Vector> possibleMoves = piece.getPossibleMoves();
        // check castling in case of a king
        if (piece.getPieceType() == PieceType.KING && !piece.getHasMoved()) {
            ArrayList<Vector> possibleCastling = checkForCastle(pieceId);
            if (!possibleCastling.isEmpty()) {
                possibleMoves.addAll(possibleCastling);
            }
        }
        else if (piece.getPieceType() != PieceType.KING)
            for (Vector move: piece.getPossibleMoves()) {
                if (!isSaveMove(move, pieceId)) {
                    possibleMoves.remove(move);
                }
            }

        return possibleMoves;
    }


    private Boolean isSaveMove(Vector dest, Long pieceId) {
        Board copiedBoard = this.copyBoard();
        copiedBoard.makeMove(pieceId, dest);
        copiedBoard.setIsWhiteTurn(!this.getIsWhiteTurn());
        return !copiedBoard.checkForCheck();
    }


    public ArrayList<Vector> getPlayersPossibleNextMoves(Color playerColor) {
        ArrayList<Vector> possibleMoves = new ArrayList<>();
        for (Piece piece: getPieces()) {
            if (piece.getColor() == playerColor && !piece.getCaptured()) {
                for (Vector vector: piece.getPossibleMoves()) {
                    if (!possibleMoves.contains(vector)) {
                        possibleMoves.add(vector);
                    }
                }
            }
        }

        return possibleMoves;
    }


    public ArrayList<Vector> checkForCastle(Long pieceId) {
        Piece piece = getById(pieceId);
        ArrayList<Vector> possibleCastling = new ArrayList<>();
        Color enemyColor = Color.BLACK;
        if (piece.getColor() == Color.BLACK) {
            enemyColor = Color.WHITE;
        }

        // white king
        if (piece.getPosition().equals(new Vector(5,1))) {

            if (board[4][1] == null && board[3][1] == null && board[2][1] == null && !board[1][1].getHasMoved()
                    && !canPlayerReach(enemyColor, new Vector(2,1))) {
                possibleCastling.add(new Vector(1,1));
            }
            if (board[6][1] == null && board[7][1] == null && !board[8][1].getHasMoved()
                    && !canPlayerReach(enemyColor, new Vector(7,1))) {
                possibleCastling.add(new Vector(8,1));
            }
        }

        // black king
        else if (piece.getPosition().equals(new Vector(5,8))) {

            if (board[4][8] == null && board[3][8] == null && board[2][8] == null && !board[1][8].getHasMoved()
                    && !canPlayerReach(enemyColor, new Vector(2,8))) {
                possibleCastling.add(new Vector(1,8));
            }
            if (board[6][8] == null && board[7][8] == null && !board[8][8].getHasMoved()
                    && !canPlayerReach(enemyColor, new Vector(7,8))) {
                possibleCastling.add(new Vector(8,8));
            }
        }

        return possibleCastling;
    }

    private boolean canPlayerReach(Color color, Vector vector) {
        List<Vector> moves = getPlayersPossibleNextMoves(color);
        for (Vector v: moves) {
            if (v.equals(vector)) {
                return true;
            }
        }
        return false;
    }

    public void makeMove(Long pieceId, Vector moveTo){
        Piece piece = getById(pieceId);
        boolean hasCastled = false;

        // check for valid move
        /*ArrayList<Vector> possibleMoves = getPossibleMoves(pieceId);
        if (!possibleMoves.contains(moveTo)) {
            throw new InvalidMoveException("This piece can not move to the desired position");
        }*/



        Piece captured = this.getPieceOnTile(moveTo);

        // if there is a piece on the moveTo position
        if (captured != null) {
            // Capture Piece
            if (captured.getColor() != piece.getColor()){
                captured.setCaptured(true);
                board[captured.getPosition().getX()][captured.getPosition().getY()] = null ;
                captured.setPosition(new Vector(0, 0));
                this.piecesOutGame.add(captured);
            }

            // Castling --> king captures own rook
            else if (piece.getColor() == captured.getColor() && piece.getPieceType() == PieceType.KING) {
                if (piece.getPosition().equals(new Vector(5,1))) {
                    if (moveTo.equals(new Vector(1,1))) {
                        executeCastle(piece, captured, new Vector(2,1), new Vector(3,1));
                        hasCastled = true;
                    }
                    else if (moveTo.equals(new Vector(8,1))) {
                        executeCastle(piece, captured, new Vector(7,1), new Vector(6,1));
                        hasCastled = true;
                    }
                }

                else if (piece.getPosition().equals(new Vector(5,8))) {
                    if (moveTo.equals(new Vector(1,8))) {
                        executeCastle(piece, captured, new Vector(2,8), new Vector(3,8));
                        hasCastled = true;
                    }
                    else if (moveTo.equals(new Vector(8,8))) {
                        executeCastle(piece, captured, new Vector(7,8), new Vector(6,8));
                        hasCastled = true;
                    }
                }
            }
        }

        // execute the move
        if (!hasCastled) {
            this.board[piece.position.getX()][piece.position.getY()] = null;
            this.board[moveTo.getX()][moveTo.getY()] = piece;
            piece.move(moveTo);
        }

    }

    // Effective castling, king and rook get on their new position
    public void executeCastle(Piece king, Piece rook, Vector kingDest, Vector rookDest) {
        this.board[kingDest.getX()][kingDest.getY()] = king;
        this.board[rookDest.getX()][rookDest.getY()] = rook;
        rook.move(rookDest);
        king.move(kingDest);
    }

    public boolean checkForCheck() {
        Color myColor = Color.BLACK;
        Color enemyColor = Color.WHITE;

        if (isWhiteTurn) {
            myColor = Color.WHITE;
            enemyColor = Color.BLACK;
        }
        Piece king = getColorsKing(enemyColor);
        if (king == null) {
            return false;
        }
        ArrayList<Vector> nextMoves = getPlayersPossibleNextMoves(myColor);
        for (Vector vector: nextMoves) {
            if (vector.equals(king.getPosition())) {
                return true;
            }
        }
        return false;
    }


    // needs some improvements to make it reusable
    public boolean checkForCheckmate() {
        Color enemyColor = Color.WHITE;
        if (isWhiteTurn) {
            enemyColor = Color.BLACK;
        }
        this.setIsWhiteTurn(!this.isWhiteTurn);
        ArrayList<Vector> possibleMoves = new ArrayList<>();
        for (Piece piece: getPieces()) {
            if (piece.getColor() == enemyColor && !piece.getCaptured()) {
                ArrayList<Vector> moves = this.getPossibleMoves(piece.getPieceId());
                for (Vector vector: moves) {
                    System.out.print(piece.getPieceId() + " - " + piece.getColor());
                    System.out.println(" - " + piece.getPieceType() + " - move: x=" + vector.getX() + " y="+ vector.getY());
                    possibleMoves.add(vector);
                }
            }
        }
        this.setIsWhiteTurn(!this.isWhiteTurn);
        System.out.println("--------------------");
        if (possibleMoves.isEmpty()) {
            return true;
        }
        return false;
    }

    // TODO: implement a check for stalemate
    public void checkForStalemate() {
        Piece king = getColorsKing(Color.WHITE);
    }

    private Board copyBoard() {
        Board boardCopy = new Board();
        boardCopy.setGame(currentGame);
        return boardCopy;
    }

    public void setGame(Game game){
        this.setCurrentGame(game);
        this.emptyBoard();
        this.isWhiteTurn = game.getIsWhiteTurn();
        List<PieceDB> pieces = game.getPieces();
        this.status = game.getGameStatus();

        for (PieceDB piece : pieces){
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
