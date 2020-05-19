package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.JoinGameException;
import ch.uzh.ifi.seal.soprafs20.exceptions.MakeMoveException;
import ch.uzh.ifi.seal.soprafs20.exceptions.NotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.logic.Vector;
import ch.uzh.ifi.seal.soprafs20.rest.dto.JoinPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MovePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GameControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;


    @Test
    public void getGamesTest_ReturnJsonArray() throws Exception {
        //given
        Game game;
        game = new Game();
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        List<Game> allGames = Collections.singletonList(game);

        // this mocks the GameService
        given(gameService.getGames()).willReturn(allGames);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isWhiteTurn", is(game.getIsWhiteTurn())))
                .andExpect(jsonPath("$[0].gameStatus", is(game.getGameStatus().toString())));

    }

    @Test
    public void createNewGameTest_ReturnJsonArray_success() throws Exception {
        //given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserId(1L);

        // this mocks the GameService
        given(gameService.createNewGame(Mockito.any())).willReturn(game);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));;

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated());

    }

    @Test
    public void createNewGameTest_ReturnJsonArray_throwException() throws Exception {
        //given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserId(1L);

        // this mocks the GameService
        given(gameService.createNewGame(Mockito.any()))
                .willThrow(new JoinGameException("User is either already in a game or offline"));

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$", is("User is either already in a game or offline")));

    }

    @Test
    public void joinGameTest_ReturnJsonArray_success() throws Exception {
        //given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        JoinPutDTO joinPutDTO = new JoinPutDTO();
        joinPutDTO.setGameId(1L);
        joinPutDTO.setUserId(2L);


        // when
        MockHttpServletRequestBuilder putRequest = put("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(joinPutDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk());

    }
/*
    @Test
    public void joinGameTest_ReturnJsonArray_throwException() throws Exception {
        //given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        JoinPutDTO joinPutDTO = new JoinPutDTO();
        joinPutDTO.setGameId(1L);
        joinPutDTO.setUserId(2L);

        given(gameService.joinGame(Mockito.any(),Mockito.any())).willThrow(new JoinGameException("Corrupt game"));

        // when
        MockHttpServletRequestBuilder putRequest = put("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(joinPutDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$",is("Corrupt game")));
    }
    */

    @Test
    public void getGameTest_ReturnJsonArray_success() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        Long gameId = 1L;

        // this mocks the GameService
        given(gameService.findGameByGameId(Mockito.any())).willReturn(game);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/"+game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameId));

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId",is(1)))
                .andExpect(jsonPath("$.isWhiteTurn",is(game.getIsWhiteTurn())))
                .andExpect(jsonPath("$.gameStatus",is(game.getGameStatus().toString())));
    }

    @Test
    public void getGameTest_ReturnJsonArray_throwException() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        Long gameId = 1L;

        // this mocks the GameService
        given(gameService.findGameByGameId(Mockito.any()))
                .willThrow(new NotFoundException("Game with id "+gameId+" was not found."));

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/"+game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameId));

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Game with id "+gameId+" was not found.")));
    }

    @Test
    public void leaveGameTest_ReturnJsonArray_success() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserId(1L);
        userPostDTO.setUsername("username");

        // when
        MockHttpServletRequestBuilder putRequest = put("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    @Test
    public void deleteGameTest_ReturnJsonArray_success() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        Long gameId = 1L;

        // when
        MockHttpServletRequestBuilder deleteRequest = delete("/games/"+game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gameId));

        // then
        mockMvc.perform(deleteRequest).andExpect(status().isOk());
    }

    @Test
    public void makeMoveTest_ReturnJsonArray_success() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        PieceDB piece = new PieceDB();
        piece.setPieceId(1L);
        piece.setPieceType(PieceType.PAWN);
        piece.setColor(Color.BLACK);
        piece.setXCord(1);
        piece.setYCord(3);
        piece.setHasMoved(false);
        piece.setCaptured(false);
        ArrayList<PieceDB> pieces = new ArrayList<>();
        pieces.add(piece);
        game.setPieces(pieces);

        MovePostDTO movePostDTO = new MovePostDTO();
        movePostDTO.setPieceId(1L);
        movePostDTO.setUserId(1L);
        movePostDTO.setX(1);
        movePostDTO.setY(3);


        // this mocks the GameService
        given(gameService.makeMove(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(game);

        // when
        MockHttpServletRequestBuilder putRequest = put("/games/"+game.getGameId()+"/"+piece.getPieceId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId",is(1)))
                .andExpect(jsonPath("$.isWhiteTurn",is(game.getIsWhiteTurn())))
                .andExpect(jsonPath("$.gameStatus",is(game.getGameStatus().toString())))
                .andExpect(jsonPath("$.pieces[0].pieceId",is(1)))
                .andExpect(jsonPath("$.pieces[0].pieceType",is(game.getPieces().get(0).getPieceType().toString())))
                .andExpect(jsonPath("$.pieces[0].color",is(game.getPieces().get(0).getColor().toString())))
                .andExpect(jsonPath("$.pieces[0].xcord",is(game.getPieces().get(0).getXCord())))
                .andExpect(jsonPath("$.pieces[0].ycord",is(game.getPieces().get(0).getYCord())))
                .andExpect(jsonPath("$.pieces[0].captured",is(game.getPieces().get(0).isHasMoved())))
                .andExpect(jsonPath("$.pieces[0].hasMoved",is(game.getPieces().get(0).isCaptured())));
    }

    @Test
    public void makeMoveTest_ReturnJsonArray_throwMakeMoveException() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        PieceDB piece = new PieceDB();
        piece.setPieceId(1L);
        piece.setPieceType(PieceType.PAWN);
        piece.setColor(Color.BLACK);
        piece.setXCord(1);
        piece.setYCord(3);
        piece.setHasMoved(false);
        piece.setCaptured(false);
        ArrayList<PieceDB> pieces = new ArrayList<>();
        pieces.add(piece);
        game.setPieces(pieces);

        MovePostDTO movePostDTO = new MovePostDTO();
        movePostDTO.setPieceId(1L);
        movePostDTO.setUserId(1L);
        movePostDTO.setX(1);
        movePostDTO.setY(3);


        // this mocks the GameService
        given(gameService.makeMove(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willThrow(new MakeMoveException("Game is either finished or hasn't started yet"));

        // when
        MockHttpServletRequestBuilder putRequest = put("/games/"+game.getGameId()+"/"+piece.getPieceId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Game is either finished or hasn't started yet")));
    }

    @Test
    public void makeMoveTest_ReturnJsonArray_throwOthersTurnException() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        PieceDB piece = new PieceDB();
        piece.setPieceId(1L);
        piece.setPieceType(PieceType.PAWN);
        piece.setColor(Color.BLACK);
        piece.setXCord(1);
        piece.setYCord(3);
        piece.setHasMoved(false);
        piece.setCaptured(false);
        ArrayList<PieceDB> pieces = new ArrayList<>();
        pieces.add(piece);
        game.setPieces(pieces);

        MovePostDTO movePostDTO = new MovePostDTO();
        movePostDTO.setPieceId(1L);
        movePostDTO.setUserId(1L);
        movePostDTO.setX(1);
        movePostDTO.setY(3);


        // this mocks the GameService
        given(gameService.makeMove(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willThrow(new MakeMoveException("Game is either finished or hasn't started yet"));

        // when
        MockHttpServletRequestBuilder putRequest = put("/games/"+game.getGameId()+"/"+piece.getPieceId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Game is either finished or hasn't started yet")));
    }

    @Test
    public void getMovesTest_ReturnJsonArray_success() throws Exception {
        // given
        Game game;
        game = new Game();
        game.setGameId(1L);
        game.setIsWhiteTurn(true);
        game.setGameStatus(GameStatus.WAITING);

        PieceDB piece = new PieceDB();
        piece.setPieceId(1L);
        piece.setPieceType(PieceType.PAWN);
        piece.setColor(Color.BLACK);
        piece.setXCord(1);
        piece.setYCord(3);
        piece.setHasMoved(false);
        piece.setCaptured(false);
        ArrayList<PieceDB> pieces = new ArrayList<>();
        pieces.add(piece);
        game.setPieces(pieces);

        Vector vector = new Vector(1,4);
        List<Vector> moves = new ArrayList<>();
        moves.add(vector);


        // this mocks the GameService
        given(gameService.getPossibleMoves(Mockito.anyLong(), Mockito.anyLong())).willReturn(moves);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/"+game.getGameId()+"/"+piece.getPieceId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].x",is(vector.getX())))
                .andExpect(jsonPath("$[0].y",is(vector.getY())));

    }

    @Test
    public void getGameHistoryTest_ReturnJsonArray_success() throws Exception {
        User user = new User();
        user.setUserId(1L);

        Game game1 = new Game();
        game1.setGameId(1L);
        Game game2 = new Game();
        game2.setGameId(2L);
        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);


        // this mocks the GameService
        given(gameService.getGameHistory(Mockito.anyLong())).willReturn(games);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/"+user.getUserId()+"/gameHistory")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gameId",is(1)))
                .andExpect(jsonPath("$[1].gameId",is(2)));
    }

    @Test
    public void offerOrAcceptDrawTest_ReturnJsonArray_success() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserId(1L);

        Game game = new Game();
        game.setGameId(1L);

        // this mocks the GameService
        given(gameService.offerOrAcceptDraw(Mockito.anyLong(), Mockito.any())).willReturn(game);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/"+game.getGameId()+"/draw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId",is(1)));
    }

    @Test
    public void getMovablePiecesTest_ReturnJsonArray_success() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserId(1L);

        Game game = new Game();
        game.setGameId(1L);
        PieceDB pieceDB1 = new PieceDB();
        PieceDB pieceDB2 = new PieceDB();
        pieceDB1.setPieceId(1L);
        pieceDB2.setPieceId(2L);
        List<PieceDB> movablePieces = new ArrayList<>();
        movablePieces.add(pieceDB1);
        movablePieces.add(pieceDB2);


        // this mocks the GameService
        given(gameService.getMovablePieces(Mockito.anyLong(), Mockito.any())).willReturn(movablePieces);

        // when
        MockHttpServletRequestBuilder putRequest = put("/games/"+game.getGameId()+"/movable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].pieceId",is(1)))
                .andExpect(jsonPath("$[1].pieceId",is(2)));
    }


    /**
     * Helper Method to convert DTOs into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SopraServiceException(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}