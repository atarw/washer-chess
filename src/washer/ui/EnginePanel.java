package washer.ui;

import mdlaf.MaterialUIMovement;
import washer.game.GameState;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnginePanel extends JPanel implements ActionListener {

	private GUIState lookManager;
	private GameState stateManager;

	private JTextArea evaluationField;
	private JTextArea infoField;
	private JButton analyzeButton;

	public EnginePanel () {
		stateManager = GameState.getInstance ();
		lookManager = GUIState.getInstance ();

		setMinimumSize (new Dimension (400, 250));
		setBorder (lookManager.getGUITheme ().getDefaultBorder ());

		MaterialUIMovement animate = new MaterialUIMovement (lookManager.getGUITheme ().getActiveTextbox (), 5, 1000 / 30);

		evaluationField = new JTextArea ();
		evaluationField.setEditable (false);
		evaluationField.setLineWrap (true);
		evaluationField.setWrapStyleWord (true);
		animate.add (evaluationField);

		JScrollPane scroller = new JScrollPane (evaluationField);

		//infoField = new JTextArea ();
		infoField = new JTextArea (3, 20);
		infoField.setEditable (false);
		infoField.setLineWrap (true);
		infoField.setWrapStyleWord (true);
		infoField.setBackground (lookManager.getGUITheme ().getCard ());

		//analyzeButton = new MaterialButton (new Color (244, 67, 54), theme.getBorderedButtonText (), new Color (255, 100, 100), "RUN");
		analyzeButton = new JButton ("RUN");
		analyzeButton.setBackground (lookManager.getGUITheme ().getInactiveBorderedButtonBackground ());
		analyzeButton.setForeground (lookManager.getGUITheme ().getBorderedButtonText ());

		MaterialUIMovement animateButton = new MaterialUIMovement (lookManager.getGUITheme ().getActiveBorderedButtonBackground (), 5, 1000 / 30);
		animateButton.add (analyzeButton);
		analyzeButton.addActionListener (this);

		JPanel toolPane = new JPanel ();
		toolPane.add (infoField);
		toolPane.add (analyzeButton);

		setLayout (new BorderLayout ());
		add (scroller, BorderLayout.CENTER);
		add (toolPane, BorderLayout.NORTH);

		setVisible (true);
	}

	public void paintComponent (Graphics g) {
		super.paintComponent (g);

		if (stateManager.engineActive ()) {
			infoField.setText (stateManager.getEngine ().toString ());
			//System.out.println (stateManager.getEngineMove ().toString ());
			evaluationField.setText (stateManager.getEngineMove ().toString ());
		}
	}

	public void actionPerformed (ActionEvent ae) {
		if (ae.getActionCommand ().equals ("RUN")) {
			analyzeButton.setText ("STOP");
		}
		else if (ae.getActionCommand ().equals ("STOP")) {
			analyzeButton.setText ("RUN");
		}
	}

	public void setShownVariations (int newRows) {
		evaluationField.setRows (newRows);
	}
}