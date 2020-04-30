package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.UserStats;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.LeaveGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserException;
import ch.uzh.ifi.seal.soprafs20.logic.Board;
import ch.uzh.ifi.seal.soprafs20.logic.Piece;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
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
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("userRepository") UserRepository userRepository,
                       PieceRepository pieceRepository,
                       @Qualifier("userStatsRepository") UserStatsRepository userStatsRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.pieceRepository = pieceRepository;
        this.userStatsRepository = userStatsRepository;
        this.board = new Board();
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

        checkIfUserIsAllowedToJoinOrCreateGame(userInput);

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
        game.setBlackOffersDraw(false);
        game.setWhiteOffersDraw(false);
        // white starts
        game.setIsWhiteTurn(true);
        //createNewBoard(game.getBoard());

        initPieces(game.getPieces());

        // Save game entity into the database
        Game newGame = gameRepository.save(game);
        gameRepository.flush();

        player.setStatus(UserStatus.SEARCHING);
        userRepository.save(player);
        userRepository.flush();

        return newGame;
    }

    // TODO: add option to cancel game searching mode

    public void joinGame(User userInput, Game game){
        checkIfUserIsAllowedToJoinOrCreateGame(userInput);

        User player = findUserByUserId(userInput.getUserId());
        // Get assigned color
        User otherPlayer;
        if (game.getPlayerBlack() != null){
            otherPlayer = findUserByUserId(game.getPlayerBlack().getUserId());
        }
        else if (game.getPlayerWhite() != null){
            otherPlayer = findUserByUserId(game.getPlayerWhite().getUserId());
        }
        else{
            throw new JoinGameException("Corrupt game.");
        }

        // get empty slot and assign player to color
        if (game.getPlayerBlack() == null && !game.getPlayerWhite().equals(player)){
            game.setPlayerBlack(player);
        }
        else if(game.getPlayerWhite() == null && !game.getPlayerBlack().equals(player)){
            game.setPlayerWhite(player);
        }
        else if(game.getPlayerWhite() != null && game.getPlayerBlack() != null){
            throw new JoinGameException("Game is already full.");
        }
        else {
            throw new JoinGameException("User is already a member of the game.");
        }

        game.setStartTime(Instant.now());

        // adjust game status
        game.setGameStatus(GameStatus.FULL);
        gameRepository.save(game);
        gameRepository.flush();

        // adjust user status for both players
        player.setStatus(UserStatus.PLAYING);
        userRepository.save(player);
        userRepository.flush();

        otherPlayer.setStatus(UserStatus.PLAYING);
        userRepository.save(otherPlayer);
        userRepository.flush();
    }

    public void leaveGame(Long gameId, User userInput){
        User player = findUserByUserId(userInput.getUserId());
        Game game = findGameByGameId(gameId);

        if(game.getGameStatus() != GameStatus.WON ) {

            // Get other player
            User otherPlayer;
            if (!game.getPlayerBlack().equals(player)) {
                otherPlayer = findUserByUserId(game.getPlayerBlack().getUserId());
            }
            else if (!game.getPlayerWhite().equals(player)) {
                otherPlayer = findUserByUserId(game.getPlayerWhite().getUserId());
            }
            else {
                throw new LeaveGameException("Corrupt game");
            }

            // Other player wins the game
            if (player == game.getPlayerBlack()) {
                game.setWinner(game.getPlayerWhite().getUserId());
            }
            else {
                game.setWinner(game.getPlayerBlack().getUserId());
            }
            this.endGame(game, GameStatus.WON);

            gameRepository.save(game);
            gameRepository.flush();

            // adjust users statuses
            player.setStatus(UserStatus.ONLINE);
            userRepository.save(player);
            userRepository.flush();

            otherPlayer.setStatus(UserStatus.ONLINE);
            userRepository.save(otherPlayer);
            userRepository.flush();
        }
        else {
            throw new LeaveGameException("Game is already finished.");
        }

    }

    public Game makeMove(Long gameId, Long pieceId, int x, int y){
        Game game = this.findGameByGameId(gameId);
        if (!(game.getGameStatus() == GameStatus.FULL
                || game.getGameStatus() == GameStatus.WHITE_IN_CHECK
                || game.getGameStatus() == GameStatus.BLACK_IN_CHECK)){
            // TODO: specific exception
            throw new SopraServiceException("Game is either finished or hasn't started yet");
        }

        if (
            (pieceRepository.findByPieceId(pieceId).getColor() == Color.WHITE && !game.getIsWhiteTurn()) ||
            (pieceRepository.findByPieceId(pieceId).getColor() == Color.BLACK && game.getIsWhiteTurn())
        ){
            // TODO: specific exception
            throw new SopraServiceException("Other users turn");
        }

        game.setGameStatus(GameStatus.FULL);
        this.board.setGame(game);
        this.board.makeMove(pieceId, new Vector(x,y));

        // Updates all pieces in repository and saves it to the database game instance
        List<Piece> pieces = this.board.getPieces();
        for (Piece piece : pieces) {
            PieceDB pieceDB = pieceRepository.findByPieceId(piece.getPieceId());
            pieceDB.setXCord(piece.getPosition().getX());
            pieceDB.setYCord(piece.getPosition().getY());
            pieceDB.setCaptured(piece.getCaptured());
            pieceDB.setHasMoved(piece.getHasMoved());
            pieceRepository.save(pieceDB);
        }
        checkForCheckOrCheckmate(game);

        game.setIsWhiteTurn(!game.getIsWhiteTurn());

        gameRepository.save(game);
        pieceRepository.flush();
        gameRepository.flush();
        return game;
    }

    // winning condition
    public void checkForCheckOrCheckmate(Game game){
        Color myColor = board.getIsTurnColor();

        // checkmate, king got captured
        List<Piece> pieces = board.getPieces();
        for (Piece piece: pieces) {
            if (piece.getPieceType() == PieceType.KING && piece.getCaptured()) {
                if (piece.getColor() == Color.WHITE) {
                    game.setWinner(game.getPlayerBlack().getUserId());
                }
                else {
                    game.setWinner(game.getPlayerWhite().getUserId());
                }
                endGame(game, GameStatus.WON);
            }
        }

        // check for check
        if (this.board.checkForCheck() && game.getGameStatus() != GameStatus.WON
                && game.getGameStatus() != GameStatus.DRAW) {
            if (myColor.equals(Color.WHITE)) {
                game.setGameStatus(GameStatus.BLACK_IN_CHECK);
            }
            else {
                game.setGameStatus(GameStatus.WHITE_IN_CHECK);
            }
        }

        /*else if (this.board.checkForCheckmate()) {
            if (myColor.equals(Color.WHITE)) {
                game.setGameStatus(GameStatus.WON);
                game.setWinner(game.getPlayerWhite().getUserId());
            }
            else {
                game.setGameStatus(GameStatus.WON);
                game.setWinner(game.getPlayerBlack().getUserId());
            }
        }*/

    }

    public List<Vector> getPossibleMoves(Long gameId, Long pieceId){
        Game game = this.findGameByGameId(gameId);
        if (!(game.getGameStatus() == GameStatus.FULL
                || game.getGameStatus() == GameStatus.WHITE_IN_CHECK
                || game.getGameStatus() == GameStatus.BLACK_IN_CHECK)){
            // TODO: specific exception
            throw new SopraServiceException("Game is either finished or hasn't started yet");
        }
        this.board.setGame(game);
        return this.board.getPossibleMoves(pieceId);
        // nothing in the database needs to be updated
    }

    private void checkIfUserIsAllowedToJoinOrCreateGame(User userInput){
        User player = findUserByUserId(userInput.getUserId());
        if (player.getStatus() != UserStatus.ONLINE) {
            throw new JoinGameException("User is either already in a game or offline");
        }
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

    private void updateUserStats(User user, Boolean winner, Boolean draw, int time){
        UserStats userStats = user.getUserStats();
        if(!draw) {
            if (winner) {
                userStats.setNumberOfWinnings(userStats.getNumberOfWinnings() + 1);
            }
            else {
                userStats.setNumberOfLosses(userStats.getNumberOfLosses() + 1);
            }
        }
        else if(draw) {
            userStats.setNumberOfDraws(userStats.getNumberOfDraws()+1);
        }
        userStats.setTotalTimePlayed(userStats.getTotalTimePlayed()+time);
        userStatsRepository.save(userStats);
        userStatsRepository.flush();
    }

    //Todo: update gameStats - time
    private void endGame(Game game, GameStatus gameStatus) {
        game.setGameStatus(gameStatus);
        User playerBlack = findUserByUserId(game.getPlayerBlack().getUserId());
        User playerWhite = findUserByUserId(game.getPlayerWhite().getUserId());

        game.setEndTime(Instant.now());
        Long time = game.getEndTime().getEpochSecond() - game.getStartTime().getEpochSecond();

        if(game.getGameStatus() == GameStatus.WON) {
            User winner = findUserByUserId(game.getWinner());

            // update userStats
            updateUserStats(playerBlack, winner == game.getPlayerBlack(),false, time.intValue());
            updateUserStats(playerWhite, winner == game.getPlayerWhite(),false, time.intValue());

        }
        else if (game.getGameStatus() == GameStatus.DRAW) {
            updateUserStats(playerWhite,false, true, time.intValue());
            updateUserStats(playerBlack,false, true, time.intValue());
        }

        playerWhite.setStatus(UserStatus.ONLINE);
        playerBlack.setStatus(UserStatus.ONLINE);

        userRepository.save(playerBlack);
        userRepository.save(playerWhite);
        userRepository.flush();
    }

    public List<Game> getGameHistory(Long userId) {
        User user = findUserByUserId(userId);
        List<Game> gameHistory = new ArrayList<>();
        for(Game game: gameRepository.findAll()) {
            if(game.getPlayerBlack() == user || game.getPlayerWhite() == user) {
                gameHistory.add(game);
            }
        }
        return gameHistory;
    }

    // Delete game and update userStatus
    public void deleteGame(Long gameId) {
        Game game = findGameByGameId(gameId);
        User playerBlack = game.getPlayerBlack();
        User playerWhite = game.getPlayerWhite();
        if(playerWhite != null) {
            playerWhite.setStatus(UserStatus.ONLINE);
            userRepository.save(playerWhite);
            userRepository.flush();
        }
        if(playerBlack != null) {
            playerBlack.setStatus(UserStatus.ONLINE);
            userRepository.save(playerBlack);
            userRepository.flush();
        }
        List<PieceDB> pieces = game.getPieces();
        game.getPieces().clear();
        for(PieceDB piece: pieces){
            pieceRepository.delete(piece);
            pieceRepository.flush();
        }
        gameRepository.delete(game);
        gameRepository.flush();
    }

    public Game draw(Long gameId, Long userId) {
        Game game = findGameByGameId(gameId);
        User playerWhite = game.getPlayerWhite();
        User playerBlack = game.getPlayerBlack();

        // if white player offers draw
        if (playerWhite.getUserId().equals(userId)) {
            if (game.getBlackOffersDraw()) {
                game.setWhiteOffersDraw(true);
                endGame(game, GameStatus.DRAW);
            }
            else {
                game.setWhiteOffersDraw(true);
                gameRepository.save(game);
                gameRepository.flush();
            }
        }

        // if black player offers draw
        else if (playerBlack.getUserId().equals(userId)) {
            if (game.getWhiteOffersDraw()) {
                game.setBlackOffersDraw(true);
                endGame(game, GameStatus.DRAW);
            }
            else {
                game.setBlackOffersDraw(true);
                gameRepository.save(game);
                gameRepository.flush();

            }
        }

        return game;
    }
}
