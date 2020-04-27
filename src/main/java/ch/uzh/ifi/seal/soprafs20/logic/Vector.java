package ch.uzh.ifi.seal.soprafs20.logic;

import java.io.Serializable;
import java.util.ArrayList;

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

    public ArrayList<Vector> getAlongVector(Vector vector, Integer num) {
        ArrayList<Vector> vectors = new ArrayList<>();
        for (int i = 1 ; i < num; ++i){
            Vector current = new Vector(vector).mulS(i).add(this);
            if (checkBounds(current)){
                vector.add(current);
            }
        }
        return vectors;
    }

    public Boolean checkBounds(){
        return this.checkBounds(this);
    }

    private boolean checkBounds(Integer x, Integer y){
        if (x > boardSize || x < 1 || y > boardSize || y < 1 ){
            return false;
        }
        return true;
    }

    private boolean checkBounds(Vector vector){
        if (vector.x > boardSize || x < 1 || y > boardSize || y < 1 ){
            return false;
        }
        return true;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vector)) {
            return false;
        }
        if (this.getX() == ((Vector) other).getX() && this.getY() == ((Vector) other).getY()) {
            return true;
        }
        else {
            return false;
        }
    }
}
