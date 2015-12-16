import java.util.List;

public abstract class Piece {
  
  public static final int WHITE_PAWN = 1;
  public static final int WHITE_ROOK = 2;
  public static final int WHITE_BISHOP = 3;
  public static final int WHITE_KNIGHT = 4;
  public static final int WHITE_QUEEN = 5;
  public static final int WHITE_KING = 6;
  
  public static final int BLACK_PAWN = 7;
  public static final int BLACK_ROOK = 8;
  public static final int BLACK_BISHOP = 9;
  public static final int BLACK_KNIGHT = 10;
  public static final int BLACK_QUEEN = 11;
  public static final int BLACK_KING = 12;
  
  private int identity;//identity = type of piece
  private int evaluation;
  private Side color;
  
  public Side getColor () {
    return color;
  }
  
  public int getEvaluation () {
    return evaluation;
  }
  
  public int getIdentity () {
    return identity;
  }
  
  public void setEvaluation (int newEvaluation) {
    evaluation = newEvaluation;
  }
  
  @Override
  public String toString () {
    String representation;
    
    if (this instanceof Pawn) {
      representation = "P";
    }
    else if (this instanceof Knight) {
      representation = "N";
    }
    else if (this instanceof Bishop) {
      representation = "B";
    }
    else if (this instanceof Rook) {
      representation = "R";
    }
    else if (this instanceof Queen) {
      representation = "Q";
    }
    else {
      representation = "K";
    }
    return color + " " + representation + " ID " + identity;
  }
  //  public Ply (int startLocation, int endLocation, int moveType, Piece pieceMoved, Piece captured, int halfmoveClock, boolean kwc, boolean kbc, boolean qwc, boolean qbc, Piece.PieceColor side) {
  
  @Override
  public boolean equals (Object chessPiece) {
    if (chessPiece == this) {
      return true;
    }
    
    Piece piece = (Piece) chessPiece;
    
    return piece != null && piece.getIdentity () == identity && piece.getColor () == color;
  }
  
  public abstract List <Ply> getLegalMoves (Board board, int location);
  public abstract void reevaluate (int location);//implementations may depend on the ply table being initialized
  public abstract int [] getMobility ();
  
  public Piece (int evaluation, int identity, Side color) {
    this.evaluation = evaluation;
    this.identity = identity;
    this.color = color;
  }
}