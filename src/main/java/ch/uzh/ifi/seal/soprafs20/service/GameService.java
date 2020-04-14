package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
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
import java.util.Random;

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
        this.board = new Board(pieceRepository);
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game createNewGame(User userInput) {
        User player = userRepository.findByToken(userInput.getToken());
        if(player != null) {
            // randomly assign player to a game
            Game game = new Game();
            Random rand = new Random();
            if (rand.nextBoolean()){
                game.setPlayerWhite(player);
            }
            else {
                game.setPlayerBlack(player);
            }
            game.setGameStatus(GameStatus.WAITING);
            //createNewBoard(game.getBoard());

            initPieces(game.getPieces());

            // Save game entity into the database
            gameRepository.save(game);
            gameRepository.flush();

            return game;
        }
        else {
            throw new LoginException("No game was created because no such user exists.");
        }
    }

    public void joinGame(User userInput, Game game){
        if (game.getPlayerBlack() == null){
            game.setPlayerBlack(userInput);
        }
        else{
            game.setPlayerWhite(userInput);
        }
        game.setGameStatus(GameStatus.FULL);
    }

    public Game makeMove(Long gameId, Long pieceId, int x, int y){
        Game game = gameRepository.findByGameId(gameId);
        this.board.setPieces(game);
        this.board.makeMove(pieceId, new Vector(x,y));
        // Updates all pieces in repository and saves it to the database game instance
        game.setPieces(this.board.saveAndGetPieces());
        return game;
    }

    public List<Vector> getPossibleMoves(Long gameId, Long pieceId){
        Game game = gameRepository.findByGameId(gameId);
        this.board.setPieces(game);
        return this.board.getPossibleMoves(pieceId);
        // nothing in the database needs to be updated
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

        this.savePieces(pieces);
    }

    private void savePieces(List<PieceDB> pieces){
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
