import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {
  
  private static final int [] BLACK_MOBILITY_BONUS = {
    -50,-40,-30,-30,-30,-30,-40,-50,
    -40,-20,  0,  0,  0,  0,-20,-40,
    -30,  0, 10, 15, 15, 10,  0,-30,
    -30,  5, 15, 20, 20, 15,  5,-30,
    -30,  0, 15, 20, 20, 15,  0,-30,
    -30,  5, 10, 15, 15, 10,  5,-30,
    -40,-20,  0,  5,  5,  0,-20,-40,
    -50,-40,-30,-30,-30,-30,-40,-50
  };
  
  private static final int [] WHITE_MOBILITY_BONUS = {
    -50,-40,-30,-30,-30,-30,-40,-50,
    -40,-20,  0,  5,  5,  0,-20,-40,
    -30,  5, 10, 15, 15, 10,  5,-30,
    -30,  5, 15, 20, 20, 15,  5,-30,
    -30,  0, 15, 20, 20, 15,  0,-30,
    -30,  0, 10, 15, 15, 10,  0,-30,
    -40,-20,  0,  0,  0,  0,-20,-40,
    -50,-40,-30,-30,-30,-30,-40,-50
  };
  
    public int [] getMobility () {
    return (getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS);
  }
  
  public void reevaluate (int location) {
    int value = Utilities.KNIGHT_VALUE + (this.getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS) [Board.to8x8 (location)];//to adjust bonuses for white and black accordingly
    setEvaluation (value);
  }
  
  public List <Ply> getLegalMoves (Board board, int location) {
    List <Ply> moves = new ArrayList <Ply> ();
    
    for (int x = 0; x < 2; x++) {
      for (int i = 0; i < Utilities.KNIGHT_MOVEMENT.length; i++) {
        moves.addAll (findDirectionalMoves (board, Utilities.KNIGHT_MOVEMENT [i], location, x == 0));
      }
    }
    return moves;
  }
  
  private List <Ply> findDirectionalMoves (Board board, int movement, int location, boolean addNumbers) {//PROBLEM IS HERE
    
    List <Ply> directionalMoves = new ArrayList <Ply> ();
    int newLocation = 0;
    
    if (addNumbers) {
      newLocation = location + movement;
    }
    else {
      newLocation = location - movement;
    }
    
    if (Board.isValidLocation (newLocation) && (!board.hasPiece (newLocation) || !board.sameColor (location, newLocation))) {
      directionalMoves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), board.hasPiece (newLocation) ? 0 : board.getHalfmoveClock () + 1, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ()));
    }
    return directionalMoves;
  }
  
  public Knight (Side color) {
    super (Utilities.KNIGHT_VALUE, color == Side.WHITE ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT, color);
  }
}