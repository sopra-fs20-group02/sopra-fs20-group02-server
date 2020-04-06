package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOARDROW")
public class BoardRow implements Serializable {

    @Id
    @GeneratedValue
    private Long boardRowId;
/*
    @OneToMany
    private List<Piece> row;*/

    public Long getId() {
        return boardRowId;
    }

    public void setId(Long boardRowId) {
        this.boardRowId = boardRowId;
    }
/*
    public List<Piece> getRow() {
        return row;
    }

    public void setRow(List<Piece> row) {
        this.row = row;
    }*/

    /*BoardRow() {
        row = new ArrayList<>();
    }

    public void addPiece(int index, Piece piece) {
        row.add(index, piece);
    }*/

}
