import java.util.List;
import java.util.ArrayList;

public class MoveList {
  
  private List <Ply> moves;
  
  public int getNumMoves () {
    return moves.size ();
  }
  
  public Ply getMoveAt (int index) {
    return moves.get (index);
  }
  
  public List <Ply> getAllMoves () {
    return moves;
  }
  
  public void addMove (Ply move) {
    moves.add (move);
  }
  
  public void removeMove () {
    if (moves.size () > 0) {
      moves.remove (moves.size () - 1);
    }
  }
  
  @Override
  public String toString () {
    StringBuilder moveNotation = new StringBuilder ();
    
    for (Ply i : moves) {
      moveNotation.append (i + " ");
    }
    return moveNotation.toString ();
  }
  
  public MoveList () {
    moves = new ArrayList <Ply> ();
  }
  
  public MoveList (List <Ply> moves) {
    this.moves = new ArrayList <Ply> (moves);
  }
}