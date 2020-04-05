package washer.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardEntryTest {
    @Test
    public void test_BoardEntry(){
        BoardEntry boardEntry = new BoardEntry(5);
        assertEquals(5,boardEntry.getLocation());
    }

    @Test
    public void test_getPiece(){
        BoardEntry boardEntry = new BoardEntry(0,null);
        assertEquals(null,boardEntry.getPiece());
    }
}