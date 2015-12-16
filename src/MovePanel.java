import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovePanel extends JPanel implements ActionListener {
  
  private JTextArea moveField;
  private JPanel buttonPane;
  private JButton [] buttons = new JButton [4];
  
  private GameState stateManager;
  private GUIState lookManager;
  
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
    stateManager.makeMove (stateManager.getEngineMove (), true);
    lookManager.repaint ();
  }
  
  private void previousMove () {
    
  }
  
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
    
    String [] text = {"START", "PREVIOUS", "NEXT", "END"};
    for (int i = 0; i < buttons.length; i++) {
      buttons [i] = new MaterialButton (text [i], lookManager.getGUITheme ().getInactiveBorderlessButtonBackground (), lookManager.getGUITheme ().getBorderlessButtonText (), lookManager.getGUITheme ().getActiveBorderlessButtonBackground ());
      buttons [i].addActionListener (this);
      buttonPane.add (buttons [i]);
    }
    
    setLayout (new BorderLayout ());
    add (scroller, BorderLayout.CENTER);
    add (buttonPane, BorderLayout.SOUTH);
    
    setVisible (true);
  }
}