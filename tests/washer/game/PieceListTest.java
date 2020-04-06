package washer.game;

import org.junit.Test;
import org.mockito.Mock;
import washer.pieces.King;
import washer.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PieceListTest {

    @Mock
    BoardEntry boardEntry;


    @Test(expected = NullPointerException.class)
    public void test_size(){
        Map<Integer,BoardEntry> map = new HashMap<Integer,BoardEntry>();
        map.put(1,boardEntry);
        PieceList pieceList = new PieceList(map);
        assertEquals(0,pieceList.size());
    }

    //Testing the size of empty PieceList
    @Test
    public void test_size_is_zero(){
        PieceList pieceList = new PieceList();
        assertEquals(0,pieceList.size());
    }


}