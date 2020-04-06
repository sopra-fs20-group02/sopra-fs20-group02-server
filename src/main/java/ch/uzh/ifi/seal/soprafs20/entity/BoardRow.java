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
    private Long id;

    @OneToMany
    @JoinColumn()
    private List<Piece> row;

    BoardRow() {
        row = new ArrayList<>();
    }

    public void addPiece(int index, Piece piece) {
        row.add(index, piece);
    }

}
