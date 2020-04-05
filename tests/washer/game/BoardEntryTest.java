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

    @Mock
    Piece piece;

    @Mock
    Object entry;


    @Rule public MockitoRule rule = MockitoJUnit.rule();

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

    @Test
    public void test_getPiece_identity(){
        when(piece.getIdentity()).thenReturn(2);
        assertEquals(2 ,piece.getIdentity());
        verify(piece).getIdentity();
    }

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

    @Test (expected = ClassCastException.class)
    public void test_equals1(){
        BoardEntry boardEntry = new BoardEntry(5,piece);
            when(boardEntry.equals(entry)).thenReturn(true);
            assertFalse(boardEntry.equals(entry));
            verify(entry).equals(entry);
    }

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