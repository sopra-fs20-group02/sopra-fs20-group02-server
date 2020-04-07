package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.BoardRow;
import ch.uzh.ifi.seal.soprafs20.entity.Piece;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.pieces.Pawn;
import ch.uzh.ifi.seal.soprafs20.entity.pieces.Rook;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.ArrayList;
import java.util.List;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to the game
 */
@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final BoardRowRepository boardRowRepository;
    private final PieceRepository pieceRepository;


    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository,
                       BoardRowRepository boardRowRepository, PieceRepository pieceRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.boardRowRepository = boardRowRepository;
        this.pieceRepository = pieceRepository;
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game createNewGame(User user) {
        Game game = new Game(); // PlayerBlack is not known yet
        game.setPlayerWhite(user);
        //createNewBoard(game.getBoard());

        initPieces(game.getPieces());

        // Save game entity into the database
        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    private void createNewBoard(List<BoardRow> board) {
        for(int i=1;i<=8;i++) {
            BoardRow boardRow = new BoardRow();
            board.add(boardRow);
            boardRowRepository.save(boardRow);
        }
        //initPieces(board);
        boardRowRepository.flush();
    }

    private void initPieces(List<Piece> pieces) {
        // Pawns
        for (int i = 1; i <= 8; i++){
            // White Pawns
            Pawn pawnW = new Pawn();
            pawnW.setColor(Color.WHITE);
            pawnW.setPieceType(PieceType.PAWN);
            pawnW.setXCord(i);
            pawnW.setYCord(2);
            pieces.add(pawnW);
            pieceRepository.save(pawnW);

            // Black Pawns
            Pawn pawnB = new Pawn();
            pawnB.setColor(Color.WHITE);
            pawnB.setPieceType(PieceType.PAWN);
            pawnB.setXCord(i);
            pawnB.setYCord(7);
            pieces.add(pawnB);
            pieceRepository.save(pawnB);
        }

        // Rook
        pieces.add(createRook(Color.WHITE, 1, 1));
        pieces.add(createRook(Color.WHITE, 8, 1));
        pieces.add(createRook(Color.BLACK, 1, 8));
        pieces.add(createRook(Color.BLACK, 8, 8));


        // Knight

        // Bishop

        // Queen

        // King

        pieceRepository.flush();

        /*for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                board.get(i).getRow().add(null);
            }
            Pawn pawn1 = new Pawn();
            pieceRepository.save(pawn1);
            board.get(i).getRow().add(pawn1);
            board.get(i).getRow().add(null);
            board.get(i).getRow().add(null);
            Pawn pawn2 = new Pawn();
            pieceRepository.save(pawn2);
            board.get(i).getRow().add(pawn2);

        }*/
    }

    private  Rook createRook(Color c, int x, int y) {
        Rook rook = new Rook();
        rook.setPieceType(PieceType.ROOK);
        rook.setColor(c);
        rook.setXCord(x);
        rook.setYCord(y);
        pieceRepository.save(rook);
        return rook;
    }

}
