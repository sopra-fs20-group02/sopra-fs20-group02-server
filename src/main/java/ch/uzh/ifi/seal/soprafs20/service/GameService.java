package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.GameDB;
import ch.uzh.ifi.seal.soprafs20.exceptions.LoginException;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PieceRepository pieceRepository;

    private Board board;


    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, PieceRepository pieceRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.pieceRepository = pieceRepository;
        this.board = new Board();
    }

    public List<GameDB> getGames() {
        return this.gameRepository.findAll();
    }

    public GameDB createNewGame(User userInput) {
        User playerWhite = userRepository.findByToken(userInput.getToken());
        if(playerWhite != null) {
            GameDB gameDB = new GameDB(); // PlayerBlack is not known yet
            gameDB.setPlayerWhite(playerWhite);
            gameDB.setGameStatus(GameStatus.CREATED);
            //createNewBoard(game.getBoard());

            initPieces(gameDB.getPieces());

            // Save game entity into the database
            gameRepository.save(gameDB);
            gameRepository.flush();

            return gameDB;
        }
        else {
            throw new LoginException("No game was created because no such user exists.");
        }
    }

    public GameDB makeMove(Long gameId, Long pieceId, int x, int y){
        GameDB game = gameRepository.findByGameId(gameId);
        this.board.setGame(game);
        this.board.makeMove(pieceId, new Vector(x,y));
        game.setPieces(this.board.getAllPieces());
        return game;
    }

    private void initPieces(List<PieceDB> pieces) {
        // Pawns
        for (int i = 1; i <= 8; i++){
            // White Pawns
            pieces.add(createPiece(PieceType.PAWN, Color.WHITE, i, 2));
            pieces.add(createPiece(PieceType.PAWN, Color.BLACK, i, 7));
        }

        // Rook
        pieces.add(createPiece(PieceType.ROOK, Color.WHITE, 1, 1));
        pieces.add(createPiece(PieceType.ROOK, Color.WHITE, 8, 1));
        pieces.add(createPiece(PieceType.ROOK, Color.BLACK, 1, 8));
        pieces.add(createPiece(PieceType.ROOK, Color.BLACK, 8, 8));

        // Knight
        pieces.add(createPiece(PieceType.KNIGHT, Color.WHITE, 2, 1));
        pieces.add(createPiece(PieceType.KNIGHT, Color.WHITE, 7, 1));
        pieces.add(createPiece(PieceType.KNIGHT, Color.BLACK, 2, 8));
        pieces.add(createPiece(PieceType.KNIGHT, Color.BLACK, 7, 8));

        // Bishop
        pieces.add(createPiece(PieceType.BISHOP, Color.WHITE, 3, 1));
        pieces.add(createPiece(PieceType.BISHOP, Color.WHITE, 6, 1));
        pieces.add(createPiece(PieceType.BISHOP, Color.BLACK, 3, 8));
        pieces.add(createPiece(PieceType.BISHOP, Color.BLACK, 6, 8));

        // Queen
        pieces.add(createPiece(PieceType.QUEEN, Color.WHITE, 4, 1));
        pieces.add(createPiece(PieceType.QUEEN, Color.BLACK, 4, 8));

        // King
        pieces.add(createPiece(PieceType.KING, Color.WHITE, 5, 1));
        pieces.add(createPiece(PieceType.KING, Color.BLACK, 5, 8));

        for (PieceDB pieceDB : pieces){
            this.pieceRepository.save(pieceDB);
        }
        pieceRepository.flush();
    }

    private PieceDB createPiece(PieceType pieceType, Color color, int x, int y) {
        PieceDB piece = new PieceDB();
        piece.setPieceType(pieceType);
        piece.setColor(color);
        piece.setXCord(x);
        piece.setYCord(y);
        return piece;
    }
}
