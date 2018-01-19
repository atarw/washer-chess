package washer.game;

import washer.Utilities;
import washer.pieces.Piece;

public class Ply implements Comparable<Ply> {

	private int startLocation, endLocation;
	private int moveType; //0 = normal, -1 = castle
	private int evaluation;
	private int halfmoveClock;
	private Side side;

	private Piece pieceMoved;
	private Piece captured;

	private boolean kbc, qbc; //king/queen black castle rights
	private boolean kwc, qwc; //king/queen white castle rights
	//private boolean check;

	public Ply (int startLocation, int endLocation, int moveType, Piece pieceMoved, Piece captured, int halfmoveClock/*, boolean check*/, boolean kwc, boolean kbc, boolean qwc, boolean qbc, Side side) {
		this (startLocation, endLocation, moveType, pieceMoved, captured, halfmoveClock, 0, /*check,*/ kwc, kbc, qwc, qbc, side);
	}

	public Ply (int startLocation, int endLocation, int moveType, Piece pieceMoved, Piece captured, int halfmoveClock, int evaluation/*, boolean check*/, boolean kwc, boolean kbc, boolean qwc, boolean qbc, Side side) {
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.moveType = moveType;

		this.pieceMoved = pieceMoved;
		this.captured = captured;

		this.halfmoveClock = halfmoveClock;
		this.evaluation = evaluation;

		//this.check = check;
		this.kwc = kwc;
		this.kbc = kbc;
		this.qwc = qwc;
		this.qbc = qbc;

		this.side = side;
	}

	public Ply (Ply ply) {
		this (ply.getStart (), ply.getEnd (), ply.getType (), ply.getMoved (), ply.getCaptured (), ply.getHalfmoveClock (), ply.getEvaluation (), ply.getKWC (), ply.getKBC (), ply.getQWC (), ply.getQBC (), ply.getSide ());
	}

	public static Ply compare (Ply p1, Ply p2) {
		if (p1.getEvaluation () >= p2.getEvaluation ()) {
			return p1;
		}
		else {
			return p2;
		}
	}

	//public boolean isCheck () {
	//   return check;
	// }

	public int getStart () {
		return startLocation;
	}

	public int getEnd () {
		return endLocation;
	}

	public int getType () {
		return moveType;
	}

	public Piece getMoved () {
		return pieceMoved;
	}

	public int getEvaluation () {
		return evaluation;
	}

	public void setEvaluation (int newEvaluation) {
		evaluation = newEvaluation;
	}

	public Piece getCaptured () {
		return captured;
	}

	public boolean getKWC () {
		return kwc;
	}

	public boolean getQWC () {
		return qwc;
	}

	public boolean getKBC () {
		return kbc;
	}

	public boolean getQBC () {
		return qbc;
	}

	public Side getSide () {
		return side;
	}

	public int getHalfmoveClock () {
		return halfmoveClock;
	}

	public boolean capturedPiece () {
		return captured != null;
	}

	@Override
	public boolean equals (Object move) {
		if (move == this) {
			return true;
		}

		Ply ply = (Ply) move;

		return ply != null && ply.getStart () == startLocation
				       && ply.getEnd () == endLocation && ply.getType () == moveType && (!ply.capturedPiece () && !this.capturedPiece () || ply.getCaptured ().equals (captured))
				       && ply.getKWC () == kwc && ply.getQWC () == qwc && ply.getKBC () == kbc && ply.getQBC () == qbc
				       && ply.getSide () == side && ply.getHalfmoveClock () == halfmoveClock;
		//&& ply.isCheck () == check;
	}

	@Override
	public int compareTo (Ply ply) {
		int thisValue = 0;
		int plyValue = 0;

		thisValue += pieceMoved.getEvaluation ();
		plyValue += ply.getMoved ().getEvaluation ();

		thisValue += (capturedPiece () ? captured.getEvaluation () : 0);
		plyValue += (ply.capturedPiece () ? ply.getCaptured ().getEvaluation () : 0);

		thisValue += (moveType != Utilities.NORMAL_MOVE && moveType != Utilities.CASTLE_MOVE ? moveType : 0);
		plyValue += (ply.getType () != Utilities.NORMAL_MOVE && ply.getType () != Utilities.CASTLE_MOVE ? ply.getType () : 0);

		thisValue += (pieceMoved.getMobility ()[Board.to8x8 (startLocation)] - pieceMoved.getMobility ()[Board.to8x8 (endLocation)]);
		plyValue += (ply.getMoved ().getMobility ()[Board.to8x8 (startLocation)] - ply.getMoved ().getMobility ()[Board.to8x8 (endLocation)]);

		return thisValue - plyValue;
	}

	@Override
	public String toString () {
		//return "START " + startLocation + " END " + endLocation + " TYPE " + moveType + " CLOCK " + halfmoveClock + " SIDE " + side + " PIECE MOVED " + pieceMoved + " CAPTURED " + captured +
		// " KBC " + kbc + " KWC " + kwc + " QBC " + qbc + " QWC " + qwc;

		return BoardEntry.toAlgebraicNotation (startLocation) + "-" + BoardEntry.toAlgebraicNotation (endLocation) + ": " + (evaluation / 100.00);// + " TYPE: " + moveType;
	}

	@Override
	public int hashCode () {
		int hash = 5;

		hash = 97 * hash + startLocation;
		hash = 97 * hash + endLocation;
		hash = 97 * hash + moveType;
		hash = 97 * hash + pieceMoved.getIdentity () + (capturedPiece () ? captured.getIdentity () : 56);
		hash = 97 * hash + halfmoveClock;
		//hash = 97 * hash + evaluation; //MAY CAUSE ISSUES WITH FINDING CORRECT MOVE
		hash = 97 * hash + (kwc ? 1 : 0) + (kbc ? 2 : 3) + (qwc ? 5 : 9) + (qbc ? 7 : 4) + (side == Side.WHITE ? 10 : 11);
		hash = 97 * hash + (int) (startLocation ^ (startLocation >>> 32));

		return hash;
	}
}