package washer.ui;

import washer.game.MovePanel;

public class GUIState {

	private static final GUIState manager = new GUIState ();

	private BoardPanel boardPanel;
	private MovePanel movePanel;
	private EnginePanel enginePanel;
	private BoardTheme boardTheme = BoardTheme.WOOD_THEME;
	private GUITheme theme = GUITheme.LIGHT_THEME;

	private GUIState () {}

	public GUITheme getGUITheme () {
		return theme;
	}

	public void setGUITheme (GUITheme theme) {
		this.theme = theme;
		init ();
	}

	public static GUIState getInstance () {
		return manager;
	}

	public BoardPanel getBoardPanel () {
		return boardPanel;
	}

	public MovePanel getMovePanel () {
		return movePanel;
	}

	public EnginePanel getEnginePanel () {
		return enginePanel;
	}

	public BoardTheme getBoardTheme () {
		return boardTheme;
	}

	public void setBoardTheme (BoardTheme boardTheme) {
		this.boardTheme = boardTheme;
		boardPanel.repaint ();
	}

	public void repaint () {
		repaintBoard ();
		repaintMove ();
		repaintEngine ();
	}

	public void repaintBoard () {
		boardPanel.repaint ();
	}

	public void repaintMove () {
		movePanel.repaint ();
	}

	public void repaintEngine () {
		enginePanel.repaint ();
	}

	public void init () {
		boardPanel = new BoardPanel ();
		movePanel = new MovePanel ();
		enginePanel = new EnginePanel ();
	}
}