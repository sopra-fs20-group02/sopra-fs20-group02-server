package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Board;
import ch.uzh.ifi.seal.soprafs20.entity.BoardRow;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.BoardRowRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
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
    private final BoardRepository boardRepository;
    private final BoardRowRepository boardRowRepository;


    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, BoardRepository boardRepository, BoardRowRepository boardRowRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.boardRowRepository = boardRowRepository;
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game createNewGame(User user) {
        Game game = new Game(); // PlayerBlack is not known yet
        game.setPlayerWhite(user);
        game.setBoard(createNewBoard());

        // Save game entity into the database
        gameRepository.save(game);
        gameRepository.flush();

        return game;
    }

    private Board createNewBoard() {
        Board board = new Board();
        BoardRow boardRow;
        for(int i=1;i<=8;i++) {
            boardRow = new BoardRow();
            board.getBoard().add(boardRow);
            boardRowRepository.save(boardRow);
        }
        boardRepository.save(board);
        boardRowRepository.flush();
        boardRepository.flush();

        return board;
    }

}
