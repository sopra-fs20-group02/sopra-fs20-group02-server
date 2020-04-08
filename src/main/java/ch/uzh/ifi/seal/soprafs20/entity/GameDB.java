package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import lombok.Getter;
import lombok.Setter;
//import ch.uzh.ifi.seal.soprafs20.logic.Vector;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
// Auto generate getters and setters
@Getter
@Setter
@Table(name = "GAME")
public class GameDB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @OneToOne
    @JoinColumn()
    private User playerWhite;

    @OneToOne
    @JoinColumn()
    private User playerBlack;

    @OneToMany
    private List<PieceDB> pieces = new ArrayList<>();

    @Column()
    private Instant startTime;

    @Column()
    private Instant endTime;

    @Column()
    private Duration whiteDuration;

    @Column()
    private Duration blackDuration;

    @Column()
    private GameStatus gameStatus;

    @OneToOne
    @JoinColumn()
    private User winner;

}
