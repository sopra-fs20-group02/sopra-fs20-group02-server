package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameMode;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import lombok.Getter;
import lombok.Setter;

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
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column()
    private GameMode gameMode;

    @Column()
    private Boolean isWhiteTurn;

    @Column()
    private Long winner;

    @Column()
    private Boolean whiteOffersDraw;

    @Column()
    private Boolean blackOffersDraw;

}
