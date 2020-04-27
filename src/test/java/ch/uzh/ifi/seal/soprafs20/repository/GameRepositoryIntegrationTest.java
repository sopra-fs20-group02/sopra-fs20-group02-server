package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Test
    public void findByGameId_success() {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setGameStatus(GameStatus.WAITING);
        game.setIsWhiteTurn(true);

        // save game in database
        gameRepository.save(game);
        gameRepository.flush();

        // when
        Game found = gameRepository.findByGameId(game.getGameId());

        // then
        assertNotNull(found.getGameId());
        assertEquals(game.getGameStatus(), GameStatus.WAITING);
        assertTrue(game.getIsWhiteTurn());
    }
}
