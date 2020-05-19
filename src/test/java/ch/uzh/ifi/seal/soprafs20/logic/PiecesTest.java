package ch.uzh.ifi.seal.soprafs20.logic;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PiecesTest {

    Game game;
    Board board;

    @BeforeEach
    public void setup() {
       this.board = new Board();
       this.game = new Game();
    }

    @Test
    public void kingTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(4);
        pieceDB.setYCord(4);
        pieceDB.setPieceType(PieceType.KING);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        King king = new King(pieceDB, this.board);
        assertEquals(8,king.getPossibleMoves().size());
    }

    @Test
    public void queenTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(1);
        pieceDB.setYCord(1);
        pieceDB.setPieceType(PieceType.QUEEN);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        Queen queen = new Queen(pieceDB, this.board);
        assertEquals(21,queen.getPossibleMoves().size());
    }

    @Test
    public void pawnTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(1);
        pieceDB.setYCord(1);
        pieceDB.setPieceType(PieceType.PAWN);
        pieceDB.setColor(Color.WHITE);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        Pawn pawn = new Pawn(pieceDB, this.board);
        assertEquals(2,pawn.getPossibleMoves().size());
    }

    @Test
    public void bishopTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(1);
        pieceDB.setYCord(1);
        pieceDB.setPieceType(PieceType.BISHOP);
        pieceDB.setColor(Color.WHITE);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        Bishop bishop = new Bishop(pieceDB, this.board);
        assertEquals(7,bishop.getPossibleMoves().size());
    }

    @Test
    public void knightTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(5);
        pieceDB.setYCord(5);
        pieceDB.setPieceType(PieceType.KNIGHT);
        pieceDB.setColor(Color.WHITE);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        Knight knight = new Knight(pieceDB, this.board);
        assertEquals(8,knight.getPossibleMoves().size());
    }

    @Test
    public void rookTest() {
        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(5);
        pieceDB.setYCord(5);
        pieceDB.setPieceType(PieceType.ROOK);
        pieceDB.setColor(Color.WHITE);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);
        Rook rook = new Rook(pieceDB, this.board);
        assertEquals(14,rook.getPossibleMoves().size());
    }
}
