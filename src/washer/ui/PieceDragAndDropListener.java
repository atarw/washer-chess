package washer.ui;

import washer.game.GameState;
import washer.Utilities;
import washer.game.Board;
import washer.game.Ply;
import washer.game.Side;
import washer.pieces.King;
import washer.pieces.Pawn;
import washer.pieces.Piece;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Set;

public class PieceDragAndDropListener implements MouseListener, MouseMotionListener {

	int pressedLocation = -1;
	int x = 0, y = 0;
	private GameState stateManager;
	private GUIState lookManager;

	public PieceDragAndDropListener () {
		lookManager = GUIState.getInstance ();
		stateManager = GameState.getInstance ();
	}

	public int getPressedLocation () {
		return pressedLocation;
	}

	public int getX () {
		return x;
	}

	public int getY () {
		return y;
	}

	public void mousePressed (MouseEvent me) {
		if (lookManager.getBoardPanel ().isInsideBoard (me.getX (), me.getY ())) {
			pressedLocation = lookManager.getBoardPanel ().getBoardLocation (me.getX (), me.getY ());
			lookManager.getBoardPanel ().setDrag (false);

			if (stateManager.hasPiece (pressedLocation) && stateManager.getPieceAt (pressedLocation).getColor () == stateManager.getSide ()) {
				lookManager.getBoardPanel ().displayLegalMoves (stateManager.getEntryAt (pressedLocation));
				//System.out.println ("VALUE: " + stateManager.getPieceAt (pressedLocation).getEvaluation ());
			}
		}
	}

	public void mouseReleased (MouseEvent me) {
		if (lookManager.getBoardPanel ().getDrag ()) {
			int boardLocation = lookManager.getBoardPanel ().getBoardLocation (me.getX (), me.getY ());

			if (lookManager.getBoardPanel ().isInsideBoard (me.getX (), me.getY ()) && stateManager.hasPiece (pressedLocation) && stateManager.getPieceAt (pressedLocation).getColor () == stateManager.getSide ()) {
				Set<Ply> possibleMoves = stateManager.getLegalMovesForSquare (pressedLocation);
				int type = Utilities.NORMAL_MOVE;

				if (stateManager.getPieceAt (pressedLocation) instanceof King && Math.abs (pressedLocation - boardLocation) == Utilities.LEFT_RIGHT_MOVEMENT * 2) {//castled
					type = Utilities.CASTLE_MOVE;
				}
				//promotion
				else if (stateManager.getPieceAt (pressedLocation) instanceof Pawn && (stateManager.getPieceAt (pressedLocation).getColor () == Side.BLACK && Board.getRank (boardLocation) == 0 || stateManager.getPieceAt (pressedLocation).getColor () == Side.WHITE && Board.getRank (boardLocation) == 7)) {
					type = stateManager.getSide () == Side.WHITE ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN; //CHANGE FOR BETTER PROMOTION;
				}

				Iterator<Ply> iterator = possibleMoves.iterator ();
				Ply ply;

				while (iterator.hasNext ()) {
					ply = iterator.next ();

					if (ply.getEnd () == boardLocation && ply.getType () == type) {
						//System.out.println ("MOVED: " + ply);
						stateManager.makeMove (ply, true);
						break;
					}
				}

        /*int type = Utilities.NORMAL_MOVE;
         int halfmoveClock = manager.getHalfmoveClock ();

         boolean kwc = manager.getKWC (), kbc = manager.getKBC (), qwc = manager.getQWC (), qbc = manager.getQBC ();

         Piece.PieceColor side = manager.getSide ();
         Piece captured = manager.getPieceAt (boardLocation);
         Piece moved = manager.getPieceAt (pressedLocation);

         Ply ply;

         if (!(moved instanceof Pawn) && captured == null) {//if no piece was captured and no pawn was moved, increment 50 move rule count
         halfmoveClock++;
         }
         else {
         halfmoveClock = 0;
         }

         if (moved instanceof King) {//if moved piece is king, remove all castle rights for that side
         if (side == Piece.PieceColor.BLACK) {
         kbc = false;
         qbc = false;
         }
         else {
         kwc = false;
         qwc = false;
         }
         }

         if (moved instanceof Rook) {//if a rook is moved, remove all castle rights for that rook
         if (side == Piece.PieceColor.BLACK) {
         if (pressedLocation == 112) {
         qbc = false;
         }
         else if (pressedLocation == 119) {
         kbc = false;
         }
         }
         else {
         if (pressedLocation == 0) {
         qwc = false;
         }
         else if (pressedLocation == 7) {
         kwc = false;
         }
         }
         }

         if (moved instanceof King && Math.abs (pressedLocation - boardLocation) == Utilities.LEFT_RIGHT_MOVEMENT * 2) {//castled
         type = Utilities.CASTLE_MOVE;
         }
         //promotion
         else if (moved instanceof Pawn && (moved.getColor () == Piece.PieceColor.BLACK && Board.getRank (boardLocation) == 0 || moved.getColor () == Piece.PieceColor.WHITE && Board.getRank (boardLocation) == 7)) {
         type = Piece.WHITE_QUEEN;
         }

         ply = new Ply (pressedLocation, boardLocation, type, moved, captured, halfmoveClock, kwc, kbc, qwc, qbc, side);

         if (manager.isLegal (ply)) {
         manager.makeMove (ply);
         }*/
			}

			lookManager.getBoardPanel ().setCursor (new Cursor (Cursor.DEFAULT_CURSOR));
			lookManager.getBoardPanel ().setDrag (false);
			pressedLocation = -1;
		}
		lookManager.getBoardPanel ().resetHighlightedSquares ();
		lookManager.getBoardPanel ().repaint ();
		lookManager.getEnginePanel ().repaint ();
	}

	public void mouseDragged (MouseEvent me) {

		if (lookManager.getBoardPanel ().isInsideBoard (me.getX (), me.getY ())) {
			if (!lookManager.getBoardPanel ().getDrag ()) {
				lookManager.getBoardPanel ().setDrag (true);
				lookManager.getBoardPanel ().setCursor (new Cursor (Cursor.HAND_CURSOR));
				pressedLocation = lookManager.getBoardPanel ().getBoardLocation (me.getX (), me.getY ());
			}

			x = me.getX ();
			y = me.getY ();
			lookManager.getBoardPanel ().repaint ();
		}
		else {
			mouseReleased (me);
		}
	}

	public void mouseClicked (MouseEvent me) {}

	public void mouseExited (MouseEvent me) {}

	public void mouseEntered (MouseEvent me) {}

	public void mouseMoved (MouseEvent me) {}
}