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
    Rook rook1;
    Rook rook2;

    @BeforeEach
    public void setup() {
       this.board = new Board();
       this.game = new Game();

       this.game.setIsWhiteTurn(true);

        PieceDB pieceDB = new PieceDB();
        pieceDB.setXCord(5);
        pieceDB.setYCord(1);
        pieceDB.setPieceType(PieceType.KING);
        pieceDB.setPieceId((long) 1);
        pieceDB.setColor(Color.WHITE);
        pieceDB.setHasMoved(false);

        PieceDB rookDB1 = new PieceDB();
        rookDB1.setXCord(1);
        rookDB1.setYCord(1);
        rookDB1.setPieceType(PieceType.ROOK);
        rookDB1.setPieceId((long) 2);
        rookDB1.setColor(Color.WHITE);
        rookDB1.setHasMoved(false);

        PieceDB rookDB2 = new PieceDB();
        rookDB2.setXCord(8);
        rookDB2.setYCord(1);
        rookDB2.setPieceType(PieceType.ROOK);
        rookDB2.setPieceId((long) 3);
        rookDB2.setColor(Color.WHITE);
        rookDB2.setHasMoved(false);

        List<PieceDB> pieces = new ArrayList<PieceDB>();
        pieces.add(pieceDB);
        pieces.add(rookDB1);
        pieces.add(rookDB2);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);

        this.king = new King(pieceDB, this.board);
        this.rook1 = new Rook(rookDB1, this.board);
        this.rook2 = new Rook(rookDB2, this.board);
    }

    @Test
    public void emptyBoardTest() {
        assertEquals(3,this.board.getPieces().size());
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
        this.board.makeMove((long) 1, new Vector(2,6));
        assertEquals(this.king.getPieceType(), this.board.getPieceOnTile(new Vector(2,6)).getPieceType());
    }

    @Test
    public void getByIdTest() {
        assertEquals(this.king.getPieceType(), this.board.getById((long) 1).getPieceType());
        assertEquals(this.rook1.getPieceType(), this.board.getById((long) 2).getPieceType());
    }

    @Test
    public void getPossibleMoves() {
        assertEquals(7, this.board.getPossibleMoves((long)1).size());
    }

    @Test
    public void getAllPossibleMoves() {
        assertEquals(22, this.board.getPlayersPossibleNextMoves(Color.WHITE).size());
        assertEquals(0, this.board.getPlayersPossibleNextMoves(Color.BLACK).size());
    }

    @Test
    public void copyBoardTest() {
        Board copiedBoard = board.copyBoard();
        assertEquals(board.getIsTurnColor(), copiedBoard.getIsTurnColor());
        assertEquals(board.getIsWhiteTurn(), copiedBoard.getIsWhiteTurn());
        assertEquals(board.getStatus(), copiedBoard.getStatus());
        assertEquals(board.getPossibleMoves(king.getPieceId()), copiedBoard.getPossibleMoves(king.getPieceId()));
    }

    @Test
    public void possibleCastleTest() {
        ArrayList<Vector> expectedCastling = new ArrayList<>();
        expectedCastling.add(new Vector(1,1));
        expectedCastling.add(new Vector(8,1));
        ArrayList<Vector> possibleCastling = this.board.checkForCastle(king.getPieceId());
        assertEquals(possibleCastling, expectedCastling);
    }

}
