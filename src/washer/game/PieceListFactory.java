package washer.game;

import java.util.HashMap;
import java.util.Map;

public final class PieceListFactory {

	public static Map<Integer, BoardEntry> copy (PieceList list) {
		Map<Integer, BoardEntry> map = new HashMap<Integer, BoardEntry> ();

		for (BoardEntry i : list.values ()) {
			map.put (i.getLocation (), new BoardEntry (i));
		}

		return map;
	}
}