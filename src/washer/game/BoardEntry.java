package washer.game;

import washer.pieces.Piece;

public class BoardEntry {

	private Piece piece;
	private int location;

	public BoardEntry (BoardEntry entry) {
		this (entry.getLocation (), entry.getPiece ());
	}

	public BoardEntry (int location, Piece piece) {
		this.location = location;
		this.piece = piece;
	}

	public BoardEntry (int location) {
		this (location, null);
	}

	public static String toAlgebraicNotation (int location) {
		//if (this.hasPiece ()) {
		return "" + (char) (Board.getFile (location) + 96 + 1) + (Board.getRank (location) + 1);
		//}
	}

	public Piece getPiece () {
		return piece;
	}

	public void setPiece (Piece newPiece) {
		piece = newPiece;
	}

	public int getLocation () {
		return location;
	}

	public boolean hasPiece () {
		return piece != null;
	}

	public void removePiece () {
		piece = null;
	}

	@Override
	public String toString () {
		return "[byte: " + location + "           alg.notation: " + BoardEntry.toAlgebraicNotation (location) + "              rank: " + Board.getRank (location) + "             file: " + Board.getFile (location) + "                  valid: " + Board.isValidLocation (location) + "             piece: " + hasPiece () + "             " + piece + "\n]";
	}

	@Override
	public boolean equals (Object entry) {
		if (entry == this) {
			return true;
		}

		BoardEntry square = (BoardEntry) entry;

		return square != null && square.getLocation () == location && square.getPiece ().equals (piece);
	}
}