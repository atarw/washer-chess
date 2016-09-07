import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

//import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;

public class ChessFrame extends JFrame implements ActionListener {
  
  private GUIState lookManager;
  private GameState stateManager;
  
  public void actionPerformed (ActionEvent ae) {
    if (ae.getActionCommand ().equals ("New")) {
      stateManager.resetState ();
      lookManager.repaint ();
    }
    else if (ae.getActionCommand ().equals ("Save")) {
      
    }
    else if (ae.getActionCommand ().equals ("Save As")) {
      
    }
    else if (ae.getActionCommand ().equals ("Preferences")) {
      
    }
    else if (ae.getActionCommand ().equals ("Quit")) {
      quitProgram ();
    }
    else if (ae.getActionCommand ().equals ("Undo")) {
      
    }
    else if (ae.getActionCommand ().equals ("Redo")) {
      
    }
    else if (ae.getActionCommand ().equals ("Cut")) {
      
    }
    else if (ae.getActionCommand ().equals ("Copy")) {
      
    }
    else if (ae.getActionCommand ().equals ("Paste")) {
      
    }
    else if (ae.getActionCommand ().equals ("Get FEN")) {
      
    }
    else if (ae.getActionCommand ().equals ("Paste FEN")) {
      
    }
    else if (ae.getActionCommand ().equals ("Flip Board")) {
      lookManager.getBoardPanel ().flipBoard ();
    }
    else if (ae.getActionCommand ().equals ("Show Engine")) {
      
    }
    else if (ae.getActionCommand ().equals ("Invert Theme")) {
      if (lookManager.getGUITheme () == GUITheme.LIGHT_THEME) {
        lookManager.setGUITheme (GUITheme.DARK_THEME);
      }
      else {
        lookManager.setGUITheme (GUITheme.LIGHT_THEME);
      }
    }
    else if (ae.getActionCommand ().equals ("Start Analysis")) {
      
    }
    else if (ae.getActionCommand ().equals ("Stop Analysis")) {
      
    }
    else if (ae.getActionCommand ().equals ("Play Best Ply")) {
      
    }
    else if (ae.getActionCommand ().equals ("About")) {
      
    }
  }
  
  private JComponent configureComponents () {
    JSplitPane [] components = new JSplitPane [2];
    components [0] = new JSplitPane (JSplitPane.VERTICAL_SPLIT, true, lookManager.getMovePanel (), lookManager.getEnginePanel ()); //engine + move list pane
    components [1] = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, true, lookManager.getBoardPanel (), components [0]); // board + other splitpane
    
    for (JSplitPane i : components) {
      i.setUI (new BasicSplitPaneUI () {
        public BasicSplitPaneDivider createDefaultDivider () {
          return new BasicSplitPaneDivider (this) {
            public void paint (Graphics g) {
              g.setColor (lookManager.getGUITheme ().getCard ());
              g.fillRect (0, 0, getSize ().width, getSize ().height);
              super.paint (g);
            }};}});
    }
    
    components [0].setResizeWeight (0.333);
    components [1].setResizeWeight (0.9);
    
    return components [1];
    //add (components [1], BorderLayout.CENTER);
  }
  
  public static int displayInternalWindow (String title, JPanel content, int messageType, Icon icon, Object [] options, int initialValue) {
    return JOptionPane.showOptionDialog (null, content, title, JOptionPane.YES_NO_OPTION, -1, null, options, options [0]);
  }
  
  private void configureWindow () {
    setSize (1100, 700);
    setPreferredSize (new Dimension (1100, 700)); 
    setMinimumSize (new Dimension (600, 400));
    setLayout (new BorderLayout ());
    
    System.setProperty("sun.awt.noerasebackground", "true");
    System.setProperty("sun.java2d.noddraw", "true");
    System.setProperty("sun.java2d.opengl", "true");
    System.setProperty ("sun.java2d.noddraw", "true");
    
    setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo (null);
    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        quitProgram ();
      }});
    
    JMenuBar bar = configureMenuBar ();
    JComponent component = configureComponents ();
    
    JPanel content = new JPanel (new BorderLayout ());
    content.setBorder (BorderFactory.createEmptyBorder ());
    content.add (bar, BorderLayout.PAGE_START);
    content.add (component, BorderLayout.CENTER);
    
    add (content, BorderLayout.CENTER);
    
    pack ();
    setVisible (true);
  }
  
  private JMenuBar configureMenuBar () {
    JMenuBar bar = new JMenuBar ();
    bar.setPreferredSize (new Dimension ((int) getSize ().getWidth (), 40));
    JMenu [] menus = {new JMenu ("File"), new JMenu ("Edit"), new JMenu ("View"), new JMenu ("Engine"), new JMenu ("Help")};
    
    JMenuItem [][] items = {
      {new JMenuItem ("New"), new JMenuItem ("Save"), new JMenuItem ("Save As"), new JMenuItem ("Preferences"), new JMenuItem ("Quit")},
      {new JMenuItem ("Undo"), new JMenuItem ("Redo"), new JMenuItem ("Cut"), new JMenuItem ("Copy"), new JMenuItem ("Paste"), new JMenuItem ("Get FEN"), new JMenuItem ("Paste FEN")},
      {new JMenuItem ("Flip Board"), new JMenuItem ("Show Engine"), new JMenuItem ("Invert Theme")},
      {new JMenuItem ("Start Analysis"), new JMenuItem ("Stop Analysis"), new JMenuItem ("Play Now")},
      {new JMenuItem ("About")}};
    
    MaterialUIMovement animate = new MaterialUIMovement (lookManager.getGUITheme ().getMenuSelectionBackground (), 5, 1000 / 30);
    
    for (int i = 0; i < menus.length; i++) {
      for (int x = 0; x < items[i].length; x++) {
        items [i][x].addActionListener (this);
        items [i][x].setPreferredSize (new Dimension (100, 30));
        animate.add (items [i][x]);
        menus [i].add (items [i][x]);
      }
      animate.add (menus [i]);
      bar.add (menus [i]);
    }
    return bar;
  }
  
  private void quitProgram () {
    dispose ();
    System.exit (0);
  }
  
  public ChessFrame () {    
    super ("Washer - Chess GUI & Engine");
    
    stateManager = GameState.getInstance ();
    
    lookManager = GUIState.getInstance ();
    lookManager.configureUI ();
    configureWindow ();
    
    stateManager.setEngine (new WasherEngine (stateManager.getBoard ()));
  }
}