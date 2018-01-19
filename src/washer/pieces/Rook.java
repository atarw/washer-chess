package washer.pieces;

import washer.Utilities;
import washer.game.Board;
import washer.game.Ply;
import washer.game.Side;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

	private static final int[] BLACK_MOBILITY_BONUS = {
			0, 0, 0, 0, 0, 0, 0, 0,
			5, 10, 10, 10, 10, 10, 10, 5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			0, 0, 0, 5, 5, 0, 0, 0
	};
	private static final int[] WHITE_MOBILITY_BONUS = {
			0, 0, 0, 5, 5, 0, 0, 0,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			-5, 0, 0, 0, 0, 0, 0, -5,
			5, 10, 10, 10, 10, 10, 10, 5,
			0, 0, 0, 0, 0, 0, 0, 0
	};
	private boolean isKSide;

	public Rook (Side color, boolean isKSide) {
		super (Utilities.ROOK_VALUE, color == Side.WHITE ? WHITE_ROOK : BLACK_ROOK, color);
		this.isKSide = isKSide;
	}

	public int[] getMobility () {
		return (getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS);
	}

	public boolean isKSide () {
		return isKSide;
	}

	public void reevaluate (int location) {
		int value = Utilities.ROOK_VALUE + (this.getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS)[Board.to8x8 (location)];//to adjust bonuses for white and black accordingly
		setEvaluation (value);
	}

	public List<Ply> getLegalMoves (Board board, int location) {
		List<Ply> moves = new ArrayList<Ply> ();
		int[] movementValues = {Utilities.LEFT_RIGHT_MOVEMENT, Utilities.UP_DOWN_MOVEMENT};
		// int moveNumber = game.getMoveList ().getNumMoves ();

		for (int x = 0; x < 2; x++) {
			for (int i = 0; i < movementValues.length; i++) {
				moves.addAll (findDirectionalMoves (board, movementValues[i], location /*, moveNumber*/, x == 0));
			}
		}

		return moves;
	}

	private List<Ply> findDirectionalMoves (Board board, int movement, int location /*, int moveNumber*/, boolean addNumbers) {
		List<Ply> directionalMoves = new ArrayList<Ply> ();
		int newLocation = 0;
		boolean kwc = board.getSide () == Side.WHITE && location == 7 ? false : board.getKWC ();
		boolean kbc = board.getSide () == Side.BLACK && location == 119 ? false : board.getKBC ();
		boolean qwc = board.getSide () == Side.WHITE && location == 0 ? false : board.getQWC ();
		boolean qbc = board.getSide () == Side.BLACK && location == 112 ? false : board.getQBC ();

		for (int i = 1; ; i++) {

			if (addNumbers) {
				newLocation = location + movement * i;
			}
			else {
				newLocation = location - movement * i;
			}

			if (Board.isValidLocation (newLocation) && (!board.hasPiece (newLocation) || !board.sameColor (location, newLocation))) {
				directionalMoves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), board.hasPiece (newLocation) ? 0 : board.getHalfmoveClock () + 1, kwc, kbc, qwc, qbc, board.getSide ()));

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