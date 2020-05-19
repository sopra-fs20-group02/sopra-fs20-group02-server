package ch.uzh.ifi.seal.soprafs20.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

    private Vector vector1;
    private Vector vector2;

    @BeforeEach
    public void setup() {
        vector1 = new Vector(1,1);
        vector2 = new Vector(2,2);
    }

    @Test
    public void setVectorTest() {
        vector1.set(vector2);
        assertEquals(vector1.getX(),2 );
        assertEquals(vector1.getX(), 2);
    }

    @Test
    public void setCordVectorTest() {
        vector1.set(2,2);
        assertEquals(vector1.getX(),2 );
        assertEquals(vector1.getX(), 2);
    }

    @Test
    public void addVectorTest() {
        vector1.add(vector2);
        assertEquals(vector1.getX(),3 );
        assertEquals(vector1.getX(), 3);
    }

    @Test
    public void addCordVectorTest() {
        vector1.add(2,2);
        assertEquals(vector1.getX(),3 );
        assertEquals(vector1.getX(), 3);
    }

    @Test
    public void mulSVectorTest() {
        vector1.mulS(3);
        assertEquals(vector1.getX(),3 );
        assertEquals(vector1.getX(), 3);
    }

    @Test
    public void checkBoundsTest_True() {
        assertTrue(vector2.checkBounds());
    }

    @Test
    public void checkBoundsTest_False() {
        Vector vector3 =new Vector(0,0);
        assertFalse(vector3.checkBounds());
    }

    @Test
    public void getXCord() {
        assertEquals(vector1.getX(), 1);
    }

    @Test
    public void getYCord() {
        assertEquals(vector1.getY(),1);
    }

    @Test
    public void initFromVector() {
        Vector vector3 = new Vector(vector1);
        assertEquals(vector3.getX(),vector1.getX() );
        assertEquals(vector3.getX(), vector1.getY());
    }


}
