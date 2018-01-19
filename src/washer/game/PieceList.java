package washer.game;

import washer.Utilities;
import washer.pieces.King;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PieceList {

	private int totalScore;
	private Map<Integer, BoardEntry> entries;
	private BoardEntry king;

	public PieceList () {
		entries = new HashMap<Integer, BoardEntry> ();
	}

	public PieceList (Map<Integer, BoardEntry> entries) {
		this.entries = entries;

		for (BoardEntry i : entries.values ()) {
			if (i.getPiece () instanceof King) {
				king = i;
				break;
			}
		}

		totalScore = calculateScore ();
	}

	public PieceList (PieceList pieceList) {
		this (PieceListFactory.copy (pieceList));
	}

	public Map<Integer, BoardEntry> getEntries () {
		return entries;
	}

	public Collection<BoardEntry> values () {
		return getEntries ().values ();
	}

	public int size () {
		return entries.size ();
	}

	public int score () {
		return totalScore;
	}

	public BoardEntry getKing () {
		return king;
	}

	public void setKing (BoardEntry newKing) {
		king = newKing;
	}

	public BoardEntry getEntry (int location) {
		return entries.get (new Integer (location));
	}

	public void addEntry (BoardEntry entry) {
		if (entry != null) {
			entries.put (new Integer (entry.getLocation ()), entry);

			if (entry.hasPiece ()) {
				totalScore += entry.getPiece ().getEvaluation ();

				if (entry.getPiece () instanceof King) {
					king = entry;
				}
			}
		}
	}

	public void removeEntry (int location) {
		if (entries.containsKey (location)) {
			totalScore -= getEntry (location).getPiece ().getEvaluation ();
			entries.remove (location);
		}
	}

	public int calculateScore () {
		int score = 0;

		for (BoardEntry i : entries.values ()) {
			i.getPiece ().reevaluate (i.getLocation ());
			score += i.getPiece ().getEvaluation ();
		}

		return score - Utilities.KING_VALUE;
	}

	@Override
	public String toString () {
		return entries.toString ();
	}
}