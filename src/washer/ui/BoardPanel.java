package washer.ui;

import washer.game.Board;
import washer.game.BoardEntry;
import washer.game.GameState;
import washer.game.Ply;
import washer.game.Side;
import washer.pieces.Piece;
import washer.pieces.Queen;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {

	private boolean blackSide = false;
	private boolean drag = false;
	private boolean showPromotion = false; //null = no, WHITE = white side, BLACK = black side
	private int promotionChoice = -1;//-1 = none, else its real
	private List<Integer> highlightedSquares;

	private PieceDragAndDropListener listener;
	private GameState stateManager;
	private GUIState lookManager;

	private int SIZE = 75;
	private int SPACE = 30;
	private int BOARD_LENGTH = SPACE * 2 + SIZE * 8;
	private int V_BOARD_GAP = (int) ((getSize ().getHeight () - BOARD_LENGTH) / 2);
	private int H_BOARD_GAP = (int) ((getSize ().getWidth () - BOARD_LENGTH) / 2);

	public BoardPanel () {
		stateManager = GameState.getInstance ();
		//stateManager.makeMove (new Ply (20, 52, Utilities.NORMAL_MOVE, stateManager.getPieceAt (20), null, 0, false, false, false, false, Piece.PieceColor.WHITE));
		//stateManager.makeMove (new Ply (99, 67, Utilities.NORMAL_MOVE, stateManager.getPieceAt (99), null, 0, false, false, false, false, Piece.PieceColor.BLACK));
		//stateManager.makeMove (new Ply (5, 65, Utilities.NORMAL_MOVE, stateManager.getPieceAt (5), null, 0, false, false, false, false, Piece.PieceColor.WHITE));
		//stateManager.makeMove (new Ply (3, 71, Utilities.NORMAL_MOVE, stateManager.getPieceAt (3), null, 0, false, false, false, false, Piece.PieceColor.WHITE));

		lookManager = GUIState.getInstance ();
		highlightedSquares = new ArrayList<Integer> ();
		listener = new PieceDragAndDropListener ();

		setMinimumSize (new Dimension (250, 300));
		setBorder (lookManager.getGUITheme ().getDefaultBorder ());

		this.addMouseListener (listener);
		this.addMouseMotionListener (listener);

		setDoubleBuffered (true);
		setVisible (true);
	}

	public void paintComponent (Graphics g) {
		super.paintComponent (g);

		if (getSize ().getHeight () > getSize ().getWidth ()) {
			SIZE = (int) (getSize ().getWidth () / 10);
		}
		else {
			SIZE = (int) (getSize ().getHeight () / 10);
		}

		SPACE = (int) (SIZE / 2);
		BOARD_LENGTH = SPACE * 2 + SIZE * 8;
		V_BOARD_GAP = (int) ((getSize ().getHeight () - BOARD_LENGTH) / 2);
		H_BOARD_GAP = (int) ((getSize ().getWidth () - BOARD_LENGTH) / 2);

		drawBoard (g);

		// g.setColor (Color.RED);
		//g.drawLine (0, 0, H_BOARD_GAP + BOARD_LENGTH  - SPACE, V_BOARD_GAP + BOARD_LENGTH - SPACE);

		if (drag) {
			dragPiece (g, listener.getPressedLocation (), listener.getX (), listener.getY ());
		}

		if (showPromotion) {
			// System.out.println (showPromotion);
			promotionChoice = displayPromotionChoice ();
		}
	}

	public int getPromotionChoice () {
		return promotionChoice;
	}

	public boolean getDrag () {
		return drag;
	}

	public void setDrag (boolean newDrag) {
		drag = newDrag;
	}

	public boolean getShowPromotion () {
		return showPromotion;
	}

	public void setShowPromotion (boolean newShowPromotion) {
		showPromotion = newShowPromotion;
	}
  
  /*public void displayLegalMoves (BoardEntry entry) {
   List <Ply> moves = new ArrayList <Ply> (entry.getPiece ().getLegalMoves (stateManager.getBoard (), entry.getLocation ()));
   
   for (Ply i : moves) {
   highlightedSquares.add (i.getEnd ());
   }
   
   //System.out.println (moves);
   repaint ();
   }*/

	public void flipBoard () {
		blackSide = !blackSide;
		repaint ();
	}

	public void displayLegalMoves (BoardEntry entry) {
		//System.out.println ("BOARD PANEL: " + stateManager.getPlyTable ());

		List<Ply> moves = new ArrayList<Ply> (stateManager.getPlyTable ().get (entry.getLocation ()));

		for (Ply i : moves) {
			highlightedSquares.add (i.getEnd ());
		}

		repaint ();
	}

	public int displayPromotionChoice () {
		JPanel content = new JPanel ();
		JButton[] options = {new JButton ("Queen"), new JButton ("Rook"), new JButton ("Bishop"), new JButton ("Knight")};

		content.add (new JLabel ("Choose a piece to promote to."));
		return ChessFrame.displayInternalWindow ("Promotion", content, JOptionPane.QUESTION_MESSAGE, new ImageIcon (lookManager.getBoardTheme ().getImg (new Queen (Side.WHITE))), options, 0);
	}

	public void resetHighlightedSquares () {
		highlightedSquares.clear ();
	}

	public int getBoardLocation (int x, int y) {
		int file = 0, rank = 0;

		for (int t = 0; t < 2; t++) {
			for (int i = 0; i < 8; i++) {
				if ((t == 0 ? H_BOARD_GAP : V_BOARD_GAP) + SPACE + i * SIZE > (t == 0 ? x : y)) {
					break;
				}

				if (t == 0) {
					file = (blackSide ? 7 - i : i);
				}
				else {
					rank = (blackSide ? i : 7 - i);
				}
			}
		}
		//System.out.println (file + ", " + rank);
		return 16 * rank + file;
	}

	public boolean isInsideBoard (int x, int y) {
		return x >= H_BOARD_GAP + SPACE && x <= BOARD_LENGTH + H_BOARD_GAP - SPACE && y >= V_BOARD_GAP + SPACE && y <= BOARD_LENGTH + V_BOARD_GAP - SPACE;
	}

	private void drawBoard (Graphics g) {
		boolean color = false;

		g.setColor (lookManager.getBoardTheme ().getBorder ());
		g.fillRect (H_BOARD_GAP, V_BOARD_GAP, BOARD_LENGTH, BOARD_LENGTH);//0, 0

		g.setColor (lookManager.getBoardTheme ().getBorderText ());
		g.setFont (new Font ("Arial", Font.PLAIN, SPACE / 2));

		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 8; y++) {
				if (x == 0) {
					g.drawString ("" + (blackSide ? (char) ('H' - y) : (char) ('A' + y)), H_BOARD_GAP + SIZE * y + SPACE + SIZE / 2, V_BOARD_GAP + (int) (SPACE * 1.7) + SIZE * 8);
				}
				else {
					g.drawString ((blackSide ? y + 1 : 8 - y) + "", H_BOARD_GAP + SPACE / 2, V_BOARD_GAP + SPACE + SIZE / 2 + SIZE * y);
				}
			}
		}

		int location = 0;

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				location = stateManager.getLocation ((blackSide ? 7 - x : x), (blackSide ? y : 7 - y));

				drawSquare (g, !color, H_BOARD_GAP + SPACE + x * SIZE, V_BOARD_GAP + SPACE + y * SIZE, location);

				if (Board.isValidLocation (location) && stateManager.getPieceAt (location) != null && (location != listener.getPressedLocation () || location == listener.getPressedLocation () && !drag)) {
					drawPiece (g, H_BOARD_GAP + x * SIZE + SPACE, V_BOARD_GAP + y * SIZE + SPACE, lookManager.getBoardTheme ().getImg (stateManager.getPieceAt (location)));
				}
				color = !color;
			}
			color = !color;
		}
	}

	private void dragPiece (Graphics g, int location, int x, int y) {
		Piece selected = stateManager.getPieceAt (location);

		if (selected != null) {
			BufferedImage selectedImg = lookManager.getBoardTheme ().getImg (selected);
			//g.drawImage (selectedImg, x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, null);
			drawPiece (g, x - SIZE / 2, y - SIZE / 2, selectedImg);
		}
	}

	private void drawSquare (Graphics g, boolean isLight, int x, int y, int location) {
		g.setColor (highlightedSquares.contains (location) ? lookManager.getBoardTheme ().getHighlight () : isLight ? lookManager.getBoardTheme ().getLight () : lookManager.getBoardTheme ().getDark ());
		g.fillRect (x, y, SIZE, SIZE);
	}

	private void drawPiece (Graphics g, int x, int y, BufferedImage img) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint (RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		img.setAccelerationPriority (1);

		g.drawImage (img, x, y, SIZE, SIZE, null);
	}
}