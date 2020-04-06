package washer.game;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import washer.pieces.Piece;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class BoardEntryTest {

    /*Creating mock objects for Piece */
    @Mock
    Piece piece;
    @Mock
    Object entry;

    //Rule to allow Mockito run with Junit
    @Rule public MockitoRule rule = MockitoJUnit.rule();

    //Testing the location item by sending a fixed integer
    @Test
    public void test_BoardEntry(){
        BoardEntry boardEntry = new BoardEntry(5);
        assertEquals(5,boardEntry.getLocation());
    }

    //Testing second constructor of BoardEntry
    @Test
    public void test_getPiece(){
        BoardEntry boardEntry = new BoardEntry(0,null);
        assertEquals(null,boardEntry.getPiece());
    }

    //Testing getIdentity of Piece class by mocking piece object
    @Test
    public void test_getPiece_identity(){
        when(piece.getIdentity()).thenReturn(2);
        assertEquals(2 ,piece.getIdentity());
        verify(piece).getIdentity();
    }

    //Testing the location method
    @Test
    public void test_location(){
        BoardEntry boardEntry = new BoardEntry(5,piece);
        assertEquals(5,boardEntry.getLocation());
    }

    @Test
    public void test_removePiece(){
        BoardEntry boardEntry = new BoardEntry(5);
        assertFalse(boardEntry.hasPiece());
    }

    //Expected to throw error when wrong object is passed for testing
    @Test (expected = ClassCastException.class)
    public void test_equals1(){
        BoardEntry boardEntry = new BoardEntry(5,piece);
            when(boardEntry.equals(entry)).thenReturn(true);
            assertFalse(boardEntry.equals(entry));
            verify(entry).equals(entry);
    }

    //Testing the equal method using the mock object generated
    @Test
    public void test_equals2(){
        BoardEntry boardEntry = new BoardEntry(5,piece);
        assertTrue(boardEntry.equals(boardEntry));
    }

    @Test
    public void test_equals3(){
        BoardEntry boardEntry = mock(BoardEntry.class);
        boolean check = boardEntry.equals(boardEntry);
        assertTrue(check);
    }

}