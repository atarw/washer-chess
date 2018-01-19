package washer.pieces;

import washer.Utilities;
import washer.game.Board;
import washer.game.Ply;
import washer.game.Side;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	private static final int[] BLACK_MOBILITY_BONUS = {
			-20, -10, -10, -10, -10, -10, -10, -20,
			-10, 0, 0, 0, 0, 0, 0, -10,
			-10, 0, 5, 10, 10, 5, 0, -10,
			-10, 5, 5, 10, 10, 5, 5, -10,
			-10, 0, 10, 10, 10, 10, 0, -10,
			-10, 10, 10, 10, 10, 10, 10, -10,
			-10, 5, 0, 0, 0, 0, 5, -10,
			-20, -10, -10, -10, -10, -10, -10, -20
	};

	private static final int[] WHITE_MOBILITY_BONUS = {
			-20, -10, -10, -10, -10, -10, -10, -20,
			-10, 5, 0, 0, 0, 0, 5, -10,
			-10, 10, 10, 10, 10, 10, 10, -10,
			-10, 0, 10, 10, 10, 10, 0, -10,
			-10, 5, 5, 10, 10, 5, 5, -10,
			-10, 0, 5, 10, 10, 5, 0, -10,
			-10, 0, 0, 0, 0, 0, 0, -10,
			-20, -10, -10, -10, -10, -10, -10, -20
	};

	public Bishop (Side color) {
		super (Utilities.BISHOP_VALUE, color == Side.WHITE ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP, color);
	}

	public int[] getMobility () {
		return (getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS);
	}

	public List<Ply> getLegalMoves (Board board, int location) {
		List<Ply> moves = new ArrayList<Ply> ();
		int[] movementValues = {Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT, Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT};

		for (int x = 0; x < 2; x++) {
			for (int i = 0; i < movementValues.length; i++) {
				moves.addAll (findDirectionalMoves (board, movementValues[i], location, x == 0));
			}
		}

		return moves;
	}

	public void reevaluate (int location) {
		int value = Utilities.BISHOP_VALUE + (this.getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS)[Board.to8x8 (location)];//to adjust bonuses for white and black accordingly
		setEvaluation (value);
	}

	private List<Ply> findDirectionalMoves (Board board, int movement, int location, boolean addNumbers) {
		List<Ply> directionalMoves = new ArrayList<Ply> ();
		int newLocation = 0;
		Ply ply;

		for (int i = 1; ; i++) {

			if (addNumbers) {
				newLocation = location + movement * i;
			}
			else {
				newLocation = location - movement * i;
			}

			if (Board.isValidLocation (newLocation) && (!board.hasPiece (newLocation) || !board.sameColor (location, newLocation))) {

				ply = new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), board.hasPiece (newLocation) ? 0 : board.getHalfmoveClock () + 1, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ());

				directionalMoves.add (ply);

				if (board.hasPiece (newLocation)) {// && !Utilities.sameColor (location, newLocation)) {
					break;
				}
			}
			else {
				break;
			}
		}
		return directionalMoves;
	}
}