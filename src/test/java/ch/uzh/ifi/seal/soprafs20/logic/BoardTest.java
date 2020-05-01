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

public class BoardTest {

    Game game;
    Board board;
    King king;

    @BeforeEach
    public void setup() {
       this.board = new Board();
       this.game = new Game();

       this.game.setIsWhiteTurn(true);

        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(4);
        pieceDB.setYCord(4);
        pieceDB.setPieceType(PieceType.KING);
        pieceDB.setPieceId((long) 1);
        pieceDB.setColor(Color.WHITE);

        List<PieceDB> pieces = new ArrayList<PieceDB>();
        pieces.add(pieceDB);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);

        this.king = new King(pieceDB, this.board);

    }

    @Test
    public void emptyBoardTest() {
        assertEquals(1,this.board.getPieces().size());
        this.board.emptyBoard();
        assertEquals(0,this.board.getPieces().size());
    }

    @Test
    public void turnTest() {
        assertEquals(Color.WHITE,this.board.getIsTurnColor());
        this.game.setIsWhiteTurn(false);
        this.board.setGame(this.game);
        assertEquals(Color.BLACK,this.board.getIsTurnColor());
    }

    @Test
    public void makeMoveTest() {
        this.board.makeMove((long) 1, new Vector(5,5));
        assertEquals(this.king.getPieceType(), this.board.getPieceOnTile(new Vector(5,5)).getPieceType());
    }

    @Test
    public void getByIdTest() {
        assertEquals(this.king.getPieceType(), this.board.getById((long) 1).getPieceType());
    }

    @Test
    public void getPossibleMoves() {
        assertEquals(8, this.board.getPossibleMoves((long)1).size());
    }

    @Test
    public void getAllPossibleMoves() {
        assertEquals(8, this.board.getAllPossibleNextMoves(Color.WHITE).size());
        assertEquals(0, this.board.getAllPossibleNextMoves(Color.BLACK).size());
    }

}
