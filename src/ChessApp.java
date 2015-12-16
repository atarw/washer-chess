public class ChessApp {
  
  public static void main (String [] args) {
    try {
      new ChessFrame ();
    }
    catch (Throwable t) {
      System.err.println (t.getCause ());
      t.printStackTrace ();
    }
  }
}