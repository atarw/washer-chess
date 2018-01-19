package washer.pieces;

import washer.Utilities;
import washer.game.Board;
import washer.game.Ply;
import washer.game.Side;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	private static final int[] BLACK_MIDDLE_GAME_MOBILITY_BONUS = {
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-20, -30, -30, -40, -40, -30, -30, -20,
			-10, -20, -20, -20, -20, -20, -20, -10,
			20, 20, 0, 0, 0, 0, 20, 20,
			20, 30, 10, 0, 0, 10, 30, 20
	};
	private static final int[] WHITE_MIDDLE_GAME_MOBILITY_BONUS = {
			20, 30, 10, 0, 0, 10, 30, 20,
			20, 20, 0, 0, 0, 0, 20, 20,
			-10, -20, -20, -20, -20, -20, -20, -10,
			-20, -30, -30, -40, -40, -30, -30, -20,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30
	};
	private static final int[] BLACK_END_GAME_MOBILITY_BONUS = {
			-50, -40, -30, -20, -20, -30, -40, -50,
			-30, -20, -10, 0, 0, -10, -20, -30,
			-30, -10, 20, 30, 30, 20, -10, -30,
			-30, -10, 30, 40, 40, 30, -10, -30,
			-30, -10, 30, 40, 40, 30, -10, -30,
			-30, -10, 20, 30, 30, 20, -10, -30,
			-30, -30, 0, 0, 0, 0, -30, -30,
			-50, -30, -30, -30, -30, -30, -30, -50
	};
	private static final int[] WHITE_END_GAME_MOBILITY_BONUS = {
			-50, -30, -30, -30, -30, -30, -30, -50,
			-30, -30, 0, 0, 0, 0, -30, -30,
			-30, -10, 20, 30, 30, 20, -10, -30,
			-30, -10, 30, 40, 40, 30, -10, -30,
			-30, -10, 30, 40, 40, 30, -10, -30,
			-30, -10, 20, 30, 30, 20, -10, -30,
			-30, -20, -10, 0, 0, -10, -20, -30,
			-50, -40, -30, -20, -20, -30, -40, -50
	};
	private boolean checked;

	public King (Side color) {
		super (Utilities.KING_VALUE, color == Side.WHITE ? Piece.WHITE_KING : Piece.BLACK_KING, color);
		this.checked = false;
	}

	public int[] getMobility () {
		return (getColor () == Side.WHITE ? WHITE_MIDDLE_GAME_MOBILITY_BONUS : BLACK_MIDDLE_GAME_MOBILITY_BONUS);
	}

	public boolean isChecked () {
		return checked;
	}

	public void setChecked (boolean newChecked) {
		checked = newChecked;
	}

	public void reevaluate (int location) {
		int value = Utilities.KING_VALUE + (this.getColor () == Side.WHITE ? WHITE_MIDDLE_GAME_MOBILITY_BONUS : BLACK_MIDDLE_GAME_MOBILITY_BONUS)[Board.to8x8 (location)];//to adjust bonuses for white and black accordingly
		setEvaluation (value);
	}

	public List<Ply> getLegalMoves (Board board, int location) {
		List<Ply> moves = new ArrayList<Ply> ();
		int[] movement = {Utilities.UP_DOWN_MOVEMENT, Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT, Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT, Utilities.LEFT_RIGHT_MOVEMENT};
		boolean add = false;
		int newLocation = 0;

		boolean kwc = board.getSide () == Side.WHITE ? false : board.getKWC ();
		boolean kbc = board.getSide () == Side.BLACK ? false : board.getKBC ();
		boolean qwc = board.getSide () == Side.WHITE ? false : board.getQWC ();
		boolean qbc = board.getSide () == Side.BLACK ? false : board.getQBC ();

		for (int x = 0; x < 2; x++, add = !add) {
			for (int i = 0; i < movement.length; i++) {
				newLocation = (add ? location + movement[i] : location - movement[i]);

				if (Board.isValidLocation (newLocation) && (!board.hasPiece (newLocation) || !board.sameColor (location, newLocation)) && !board.isLocationAttacked (newLocation, getColor () == Side.WHITE ? Side.BLACK : Side.WHITE)) {
					moves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), board.hasPiece (newLocation) ? 0 : board.getHalfmoveClock () + 1, kwc, kbc, qwc, qbc, board.getSide ()));
				}
			}
		}
		moves.addAll (getCastleMoves (board, location, kwc, kbc, qwc, qbc));

		return moves;
	}

	private List<Ply> getCastleMoves (Board board, int location, boolean kwc, boolean kbc, boolean qwc, boolean qbc) {
		List<Ply> moves = new ArrayList<Ply> ();
		int[] rooks = new int[2];

		if (board.canCastle (board.getSide ()) && !this.isChecked ()) {
			if (getColor () == Side.WHITE) {
				rooks[0] = 0;
				rooks[1] = 7;
				kwc = false;
				qwc = false;
			}
			else {
				rooks[0] = 112;
				rooks[1] = 119;
				kbc = false;
				qbc = false;
			}

			for (int i : rooks) {
				if (i == 0 ? board.getQWC () : i == 7 ? board.getKWC () : i == 112 ? board.getQBC () : board.getKBC ()) {
					if (checkCastleSquares (board, Math.min (i, location) + Utilities.LEFT_RIGHT_MOVEMENT, Math.max (i, location))) {
						moves.add (new Ply (location, i == rooks[0] ? location - Utilities.LEFT_RIGHT_MOVEMENT * 2 : location + Utilities.LEFT_RIGHT_MOVEMENT * 2, Utilities.CASTLE_MOVE, board.getPieceAt (location), null, board.getHalfmoveClock () + 1, kwc, kbc, qwc, qbc, board.getSide ()));
					}
				}
			}
		}
		return moves;
	}

	private boolean checkCastleSquares (Board board, int from, int to) {
		//System.out.println ("CHECKING SQUARES: " + from + "-" + (to - 1));

		for (int i = from; i < to; i++) {
			if (board.hasPiece (i) || board.isLocationAttacked (i, getColor () == Side.WHITE ? Side.BLACK : Side.WHITE)) {
				return false;
			}
		}
		return true;
	}
}