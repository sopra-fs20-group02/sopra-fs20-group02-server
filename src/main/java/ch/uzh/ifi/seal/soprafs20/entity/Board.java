package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
//import ch.uzh.ifi.seal.soprafs20.constant.Vector;
//import ch.uzh.ifi.seal.soprafs20.entity.pieces.*;
import ch.uzh.ifi.seal.soprafs20.entity.BoardRow;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
COORDINATE SPACE:
White always plays positive Y axis
Black plays negative Y axis
Tile at (1,1) is of BLACK color
 */
@Entity
@Table(name = "BOARD")
public class Board implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @OneToMany
    private List<Piece> piecesInGame = new ArrayList<>();

    @OneToMany
    private List<Piece> piecesOutGame = new ArrayList<>();

    @OneToMany
    private List<BoardRow> board = new ArrayList<BoardRow>();

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public List<Piece> getPiecesInGame() {
        return piecesInGame;
    }

    public void setPiecesInGame(List<Piece> piecesInGame) {
        this.piecesInGame = piecesInGame;
    }

    public void setPiecesOutGame(List<Piece> piecesOutGame) {
        this.piecesOutGame = piecesOutGame;
    }

    public List<Piece> getPiecesOutGame() {
        return piecesOutGame;
    }

    public void setBoard(List<BoardRow> board) {
        this.board = board;
    }

    public List<BoardRow> getBoard() {
        return board;
    }

    // All indices are from 1 - 8 (including 8)
    // We ignore the piece at 0, to avoid confusion
    // Empty fields is indicated with a null


    public Board() {
        /*for (int i = 1; i <= 8; i++) {
            this.board.add(new BoardRow());
        }*/
    }
      /*  // Pawns
        for (int i = 1; i <= 8; i++){
            this.board.get(i).addPiece(2, new Pawn(new Vector(i,2), Color.WHITE, i));
            this.board.get(i).addPiece(7, new Pawn(new Vector(i,7), Color.BLACK, 100+i));
        }
        // Rooks
        this.board.get(1).addPiece(1, new Rook(new Vector(1,1), Color.WHITE, 9));
        this.board.get(8).addPiece(1, new Rook(new Vector(8,1), Color.WHITE, 10));
        this.board.get(1).addPiece(8, new Rook(new Vector(1,8), Color.BLACK, 109));
        this.board.get(8).addPiece(8, new Rook(new Vector(8,8), Color.BLACK, 110));

        // Bishops
        this.board.get(2).addPiece(1, new Bishop(new Vector(2,1), Color.WHITE, 11));
        this.board.get(7).addPiece(1, new Bishop(new Vector(7,1), Color.WHITE, 12));
        this.board[2][8] = new Bishop(new Vector(2,8), Color.BLACK, 111);
        this.board[7][8] = new Bishop(new Vector(7,8), Color.BLACK, 112);

        // Knights
        this.board[3][1] = new Knight(new Vector(3,1), Color.WHITE, 13);
        this.board[6][1] = new Knight(new Vector(6,1), Color.WHITE, 14);
        this.board[3][8] = new Knight(new Vector(3,8), Color.BLACK, 113);
        this.board[6][8] = new Knight(new Vector(6,8), Color.BLACK, 114);

        // Queens
        this.board[4][1] = new Queen(new Vector(4,1), Color.WHITE, 15);
        this.board[4][8] = new Queen(new Vector(4,8), Color.BLACK, 115);

        // Kings
        this.board[5][1] = new Queen(new Vector(5,1), Color.WHITE, 16);
        this.board[5][8] = new Queen(new Vector(5,8), Color.BLACK, 116);

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                if (this.board[i][j] != null){
                    this.piecesInGame.add(this.board[i][j]);
                }
            }
        }
    }


    public Piece getPieceOnTile(Vector vector) {
        //return this.board[vector.getX()][vector.getY()];
        return null;
    }


    }*/
}
