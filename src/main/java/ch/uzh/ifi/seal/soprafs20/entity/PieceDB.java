package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/*
Abstract class of pieces
A possible move is not yet checked for other pieces on the field
 */

@Entity
// Auto generate getters and setters
@Getter
@Setter
public class PieceDB {

    public PieceDB(){
        this.isCaptured = false;
        this.hasMoved = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long pieceId;

    @Column
    protected PieceType pieceType;

    @Column
    protected Color color;

    @Column
    protected int xCord;

    @Column
    protected int yCord;

    @Column
    protected boolean isCaptured;

    @Column
    protected boolean hasMoved;

}
