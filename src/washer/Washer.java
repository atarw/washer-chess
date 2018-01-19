package washer;

import mdlaf.MaterialLookAndFeel;
import washer.ui.ChessFrame;

import javax.swing.UIManager;

public class Washer {

	public static void main (String[] args) {
		try {
			UIManager.setLookAndFeel (new MaterialLookAndFeel ());
			new ChessFrame ();
		}
		catch (Throwable t) {
			System.err.println (t.getCause ());
			t.printStackTrace ();
		}
	}
}