package ch.uzh.ifi.seal.soprafs20.entity;

public class Game {
    User playerWhite;
    User playerBlack;

    Piece[] piecesInGame;
    Piece[] piecesOutGame;

    Game(User playerWhite, User playerBlack){
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
    }


}
