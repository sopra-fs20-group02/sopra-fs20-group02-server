package ch.uzh.ifi.seal.soprafs20.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "USERSTATS")
public class UserStats implements Serializable {

    @Id
    @GeneratedValue
    private Long gameStatsId;

    @Column()
    private Integer numberOfWinnings;

    @Column()
    private Integer numberOfLosses;

    @Column()
    private Integer totalTimePlayed;


}
