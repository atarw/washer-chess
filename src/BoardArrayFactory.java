public final class BoardArrayFactory {
  
  public static BoardEntry [] copyBoard (BoardEntry [] array) {
    BoardEntry [] copy = new BoardEntry [array.length];
    
    for (int i = 0; i < array.length; i++) {
      copy [i] = new BoardEntry (array [i]);
    }
    
    return copy;
  }
  
  private BoardArrayFactory () {}
}