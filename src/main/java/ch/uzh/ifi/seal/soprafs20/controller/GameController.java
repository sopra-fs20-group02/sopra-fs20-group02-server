package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles all the REST requests related to the game creation and join.
 * It delegates the execution to the GameService.
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Handles the request to create a new game
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game createNewGame(@RequestBody String token) {
        return gameService.createNewGame(token);
        // TODO: add tests
    }

    // Handles the request to get all games
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Game> getGames() {
        return gameService.getGames();
        // TODO: add tests
    }


}
