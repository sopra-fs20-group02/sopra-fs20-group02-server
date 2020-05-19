package ch.uzh.ifi.seal.soprafs20.logic;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.exceptions.PieceNotInGameException;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.King;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.Pawn;
import ch.uzh.ifi.seal.soprafs20.logic.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {

    Game game;
    Board board;
    King king;
    Rook rook1;
    Rook rook2;
    Pawn capturedPawn;

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
        pieceDB.setCaptured(false);

        PieceDB rookDB1 = new PieceDB();
        rookDB1.setXCord(1);
        rookDB1.setYCord(1);
        rookDB1.setPieceType(PieceType.ROOK);
        rookDB1.setPieceId((long) 2);
        rookDB1.setColor(Color.WHITE);
        rookDB1.setHasMoved(false);
        rookDB1.setCaptured(false);

        PieceDB rookDB2 = new PieceDB();
        rookDB2.setXCord(8);
        rookDB2.setYCord(1);
        rookDB2.setPieceType(PieceType.ROOK);
        rookDB2.setPieceId((long) 3);
        rookDB2.setColor(Color.WHITE);
        rookDB2.setHasMoved(false);
        rookDB2.setCaptured(false);

        PieceDB captured = new PieceDB();
        captured.setXCord(0);
        captured.setYCord(0);
        captured.setPieceType(PieceType.PAWN);
        captured.setPieceId((long) 4);
        captured.setColor(Color.WHITE);
        captured.setHasMoved(false);
        captured.setCaptured(true);

        List<PieceDB> pieces = new ArrayList<>();
        pieces.add(pieceDB);
        pieces.add(rookDB1);
        pieces.add(rookDB2);
        pieces.add(captured);

        this.game.setPieces(pieces);
        this.board.setGame(this.game);

        this.king = new King(pieceDB, this.board);
        this.rook1 = new Rook(rookDB1, this.board);
        this.rook2 = new Rook(rookDB2, this.board);
        this.capturedPawn = new Pawn(captured, this.board);
    }

    @Test
    public void emptyBoardTest() {
        assertEquals(4,this.board.getPieces().size());
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
        assertEquals(this.rook2.getPieceType(), this.board.getById((long) 3).getPieceType());
        assertEquals(this.capturedPawn.getPieceType(), this.board.getById((long) 4).getPieceType());
    }

    @Test
    public void getByIdTest_PieceNotInGameException() {
        assertThrows(
                PieceNotInGameException.class,
                () -> board.getById(5L)
        );
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

    @Test
    public void promotionTest() {
        Piece pawn = new Pawn(capturedPawn.convertToDB(), board);
        pawn.setPosition(new Vector(1,8));
        pawn.setCaptured(false);

        board.promotion(pawn);

        Piece piece = board.getPieceOnTile(new Vector(1,8));

        assertEquals(piece.getPieceId(), pawn.getPieceId());
        assertEquals(piece.getPieceType(), PieceType.QUEEN);
    }



}
