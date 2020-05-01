package ch.uzh.ifi.seal.soprafs20.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Simple integer vector class
 */

public class Vector implements Serializable {
    private Integer x;
    private Integer y;

    Integer boardSize = 8;

    public Vector(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public Vector set(Integer x, Integer y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector set(Vector vector){
        this.x = vector.getX();
        this.y = vector.getY();
        return this;
    }

    public Vector add(Integer x, Integer y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector add(Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        return this;
    }

    public Vector mulS(Integer scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Boolean checkBounds(){
        return this.checkBounds(this);
    }

    private boolean checkBounds(Vector vector){
        return vector.x <= boardSize && x >= 1 && y <= boardSize && y >= 1;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector)) {
            return false;
        }
        Vector vector = (Vector) o;
        return x.equals(vector.x) && y.equals(vector.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
