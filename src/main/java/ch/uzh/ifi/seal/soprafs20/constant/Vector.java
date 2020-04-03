package ch.uzh.ifi.seal.soprafs20.constant;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Array;

public class Vector {
    private Integer x;
    private Integer y;

    Integer boardSize = 8;

    public Vector(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    void set(Integer x, Integer y){
        if (x > boardSize || x < 1 || y > boardSize || y < 1 ){
            // TODO: return illegal move exception
        }
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
