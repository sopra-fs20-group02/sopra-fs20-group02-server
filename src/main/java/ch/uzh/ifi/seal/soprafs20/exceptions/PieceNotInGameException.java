package ch.uzh.ifi.seal.soprafs20.exceptions;

public class PieceNotInGameException extends RuntimeException{

    public PieceNotInGameException(String message) {
        super(message);
    }
}

