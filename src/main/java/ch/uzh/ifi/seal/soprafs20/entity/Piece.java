package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.constant.Color;
//import ch.uzh.ifi.seal.soprafs20.constant.Vector;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
Abstract class of pieces
A possible move is not yet checked for other pieces on the field
 */

@Entity
@Table(name = "PIECE")
public class Piece {

    @Id
    @GeneratedValue
    public Long pieceId;

    public Piece() {

    }

}
