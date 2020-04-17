package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.LoginException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserException;
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
    private final UserStatsRepository userStatsRepository;

    private Board board;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, PieceRepository pieceRepository, UserStatsRepository userStatsRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.pieceRepository = pieceRepository;
        this.userStatsRepository = userStatsRepository;
        this.board = new Board(pieceRepository);
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game findGameByGameId(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        if(game != null) {
            return game;
        }
        else {
            throw new UserException("Game with id "+gameId+" was not found.");
        }
    }

    public User findUserByUserId(Long userId) {
        User user = userRepository.findByUserId(userId);
        if(user != null) {
            return user;
        }
        else {
            throw new UserException("User with id "+userId+" was not found.");
        }
    }

    public Game createNewGame(User userInput) {
        User player = findUserByUserId(userInput.getUserId());

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
        // white starts
        game.setIsWhiteTurn(true);
        //createNewBoard(game.getBoard());

        initPieces(game.getPieces());

        // Save game entity into the database
        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    public void joinGame(User userInput, Game game){
        User player = findUserByUserId(userInput.getUserId());
        if (game.getPlayerBlack() == null && game.getPlayerWhite() != player){
            game.setPlayerBlack(player);
        }
        else if(game.getPlayerWhite() == null && game.getPlayerBlack() != player){
            game.setPlayerWhite(player);
        }
        else {
            throw new SopraServiceException("User is already a member of the game."); //Todo add specific expeption
        }
        game.setGameStatus(GameStatus.FULL);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public void leaveGame(Long gameId, User userInput){
        User player = findUserByUserId(userInput.getUserId());
        Game game = findGameByGameId(gameId);
        if(player == game.getPlayerBlack()) {
            game.setWinner(game.getPlayerWhite());
        }
        else {
            game.setWinner(game.getPlayerBlack());
        }

        endGame(game);

        gameRepository.save(game);
        gameRepository.flush();
    }

    public Game makeMove(Long gameId, Long pieceId, int x, int y){
        Game game = gameRepository.findByGameId(gameId);
        if (game.getGameStatus() != GameStatus.FULL){
            // TODO: specific exception
            throw new SopraServiceException("Game is either finished or hasn't started yet");
        }

        if (
            (pieceRepository.getOne(pieceId).getColor() == Color.WHITE && !game.getIsWhiteTurn()) ||
            (pieceRepository.getOne(pieceId).getColor() == Color.BLACK && game.getIsWhiteTurn())
        ){
            // TODO: specific exception
            throw new SopraServiceException("Other users turn");
        }

        game.setIsWhiteTurn(!game.getIsWhiteTurn());

        this.board.setPieces(game);
        this.board.makeMove(pieceId, new Vector(x,y));
        // Updates all pieces in repository and saves it to the database game instance
        game.setPieces(this.board.saveAndGetPieces());
        List<PieceDB> pieceHistory = game.getPieceHistory();
        pieceHistory.addAll(game.getPieces());
        game.setPieceHistory(pieceHistory);
        return game;
    }

    public List<Vector> getPossibleMoves(Long gameId, Long pieceId){
        Game game = gameRepository.findByGameId(gameId);
        if (game.getGameStatus() != GameStatus.FULL){
            // TODO: specific exception
            throw new SopraServiceException("Game is either finished or hasn't started yet");
        }
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

    private void updateUserStats(User user, Boolean winner, int time){
        UserStats userStats = user.getUserStats();
        if(winner) {
            userStats.setNumberOfWinnings(userStats.getNumberOfWinnings()+1);
        }
        else {
            userStats.setNumberOfLosses(userStats.getNumberOfLosses()+1);
        }
        userStats.setTotalTimePlayed(userStats.getTotalTimePlayed()+time);
        userStatsRepository.save(userStats);
        userStatsRepository.flush();
    }

    //Todo: update gameStats
    private void endGame(Game game) {
        User winner = game.getWinner();

        // update userStats
        if(winner == game.getPlayerBlack()) {
            //updateUserStats(game.getPlayerBlack(),true,);
            //updateUserStats(game.getPlayerWhite(),false,);
        }
        else {
            //updateUserStats(game.getPlayerBlack(),false,);
            //updateUserStats(game.getPlayerWhite(),true,);
        }

        // update game history
        game.getPlayerBlack().getGameHistory().add(game);
        game.getPlayerWhite().getGameHistory().add(game);
        userRepository.save(game.getPlayerBlack());
        userRepository.save(game.getPlayerWhite());
        userRepository.flush();
    }
}
