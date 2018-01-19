package washer.game;

import mdlaf.MaterialUIMovement;
import washer.ui.GUIState;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovePanel extends JPanel implements ActionListener {

	private JTextArea moveField;
	private JPanel buttonPane;
	private JButton[] buttons = new JButton[4];

	private GameState stateManager;
	private GUIState lookManager;

	public MovePanel () {
		stateManager = GameState.getInstance ();
		lookManager = GUIState.getInstance ();

		setMinimumSize (new Dimension (400, 200));
		setBorder (lookManager.getGUITheme ().getDefaultBorder ());

		MaterialUIMovement animate = new MaterialUIMovement (lookManager.getGUITheme ().getActiveTextbox (), 5, 1000 / 30);

		moveField = new JTextArea ();
		moveField.setEditable (false);
		moveField.setLineWrap (true);
		moveField.setWrapStyleWord (true);
		animate.add (moveField);

		JScrollPane scroller = new JScrollPane (moveField);

		buttonPane = new JPanel ();
		buttonPane.setBorder (BorderFactory.createEmptyBorder ());
		MaterialUIMovement animateButton = new MaterialUIMovement (lookManager.getGUITheme ().getActiveBorderlessButtonBackground (), 5, 1000 / 30);

		String[] text = {"START", "PREVIOUS", "NEXT", "END"};
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton (text[i]);
			buttons[i].setBackground (lookManager.getGUITheme ().getInactiveBorderlessButtonBackground ());
			buttons[i].setForeground (lookManager.getGUITheme ().getBorderlessButtonText ());
			animateButton.add (buttons[i]);
			buttons[i].addActionListener (this);
			buttonPane.add (buttons[i]);
		}

		setLayout (new BorderLayout ());
		add (scroller, BorderLayout.CENTER);
		add (buttonPane, BorderLayout.SOUTH);

		setVisible (true);
	}

	public void paintComponent (Graphics g) {
		super.paintComponent (g);
		moveField.setText (stateManager.getMoveList ().toString ());
	}

	public void actionPerformed (ActionEvent ae) {
		if (ae.getActionCommand ().equals ("START")) {
			gotoMove ();
		}
		else if (ae.getActionCommand ().equals ("PREVIOUS")) {
			previousMove ();
		}
		else if (ae.getActionCommand ().equals ("NEXT")) {
			nextMove ();
		}
		else if (ae.getActionCommand ().equals ("END")) {
			gotoMove ();
		}
	}

	private void gotoMove () {

	}

	private void nextMove () {
		if (stateManager.engineActive ()) {
			stateManager.makeMove (stateManager.getEngineMove (), true);
			lookManager.repaint ();
		}
	}

	private void previousMove () {

	}
}