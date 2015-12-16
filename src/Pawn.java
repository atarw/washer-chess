import java.util.List;
import java.util.ArrayList;

public class Pawn extends Piece {
  
  private static final int [] BLACK_MOBILITY_BONUS = {
    0,  0,  0,  0,  0,  0,  0,  0,
    50, 50, 50, 50, 50, 50, 50, 50,
    10, 10, 20, 30, 30, 20, 10, 10,
    5,  5, 10, 25, 25, 10,  5,  5,
    0,  0,  0, 20, 20,  0,  0,  0,
    0, -5,-10,  0,  0,-10, -5,  0,
    5, 10, 10,-20,-20, 10, 10,  5,
    0,  0,  0,  0,  0,  0,  0,  0
  };
  
  private static final int [] WHITE_MOBILITY_BONUS = {
    0,  0,  0,  0,  0,  0,  0,  0,
    5, 10, 10,-20,-20, 10, 10,  5,
    0, -5,-10,  0,  0,-10, -5,  0,
    0,  0,  0, 20, 20,  0,  0,  0,
    5,  5, 10, 25, 25, 10,  5,  5,
    10, 10, 20, 30, 30, 20, 10, 10,
    50, 50, 50, 50, 50, 50, 50, 50,
    0,  0,  0,  0,  0,  0,  0,  0
  };
  
    public int [] getMobility () {
    return (getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS);
  }
  
  public void reevaluate (int location) {
    int value = Utilities.PAWN_VALUE + (this.getColor () == Side.WHITE ? WHITE_MOBILITY_BONUS : BLACK_MOBILITY_BONUS) [Board.to8x8 (location)];//to adjust bonuses for white and black accordingly
    setEvaluation (value);
  }
  
  public List <Ply> getLegalMoves (Board board, int location) {
    List <Ply> moves = new ArrayList <Ply> ();
    int [] movement = {Utilities.UP_DOWN_MOVEMENT, Utilities.UP_DOWN_MOVEMENT * 2, Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT, Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT};
    boolean isBlack = this.getColor () == Side.BLACK;
    
    for (Integer i : movement) {
      moves.addAll (findDirectionalMoves (board, i, location, isBlack));
    }
    
    return moves;
  }
  
  private List <Ply> findDirectionalMoves (Board board, int movement, int location, boolean isBlack) {
    List <Ply> directionalMoves = new ArrayList <Ply> ();
    int newLocation = (isBlack ? location - movement : location + movement), promotionRank = (isBlack ? 0 : 7), startRank = (isBlack ? 6 : 1);
    
    if (Board.isValidLocation (newLocation)) {
      if (movement == Utilities.UP_DOWN_MOVEMENT) {
        if (!board.hasPiece (newLocation)) {
          if (Board.getRank (newLocation) != promotionRank) {
            directionalMoves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), null, 0, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ()));
          }
          else {
            directionalMoves.addAll (getPromotionalMoves (board, location, newLocation, isBlack, false));
          }
        }
      }
      else if (movement == Utilities.UP_DOWN_MOVEMENT * 2) {
        int locationBelow = isBlack ? location - Utilities.UP_DOWN_MOVEMENT : location + Utilities.UP_DOWN_MOVEMENT;
        
        if (Board.getRank (location) == startRank && !board.hasPiece (newLocation) && !board.hasPiece (locationBelow)) {
          directionalMoves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), null, 0, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ()));
        }
      }
      else if (movement == Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT || movement == Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT) {
        Ply enPassentMove = getEnPassentMove (board, location, newLocation, movement, isBlack);
        
        if (enPassentMove != null) {
          directionalMoves.add (enPassentMove);
        }
        else {//if capture is not en passent
          if (board.hasPiece (newLocation) && !board.sameColor (location, newLocation)) {
            if (Board.getRank (newLocation) != promotionRank) {
              directionalMoves.add (new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), 0, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ()));
            }
            else {
              directionalMoves.addAll (getPromotionalMoves (board, location, newLocation, isBlack, true));
            }
          }
        }
      }
    }
    
    return directionalMoves;
  }
  
  private Ply getEnPassentMove (Board board, int location, int newLocation, int movement, boolean isBlack) {
    int hAdjacentLocation = 0;
    
    if (isBlack && movement == Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT) {
      hAdjacentLocation = location - Utilities.LEFT_RIGHT_MOVEMENT;
    }
    else if (isBlack && movement == Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT) {
      hAdjacentLocation = location + Utilities.LEFT_RIGHT_MOVEMENT;
    }    
    else if (!isBlack && movement == Utilities.LEFT_RIGHT_DIAGONAL_MOVEMENT) {
      hAdjacentLocation = location + Utilities.LEFT_RIGHT_MOVEMENT;
    }
    else if (!isBlack && movement == Utilities.RIGHT_LEFT_DIAGONAL_MOVEMENT) {
      hAdjacentLocation = location - Utilities.LEFT_RIGHT_MOVEMENT;
    }
    
    if (Board.isValidLocation (hAdjacentLocation) && board.getEnPassentLocation () == hAdjacentLocation && !board.sameColor (location, hAdjacentLocation)) {
      return new Ply (location, newLocation, Utilities.NORMAL_MOVE, board.getPieceAt (location), board.getPieceAt (newLocation), 0, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ());
    }
    return null;
  }
  
  private List <Ply> getPromotionalMoves (Board board, int location, int newLocation /*, int moveNumber*/, boolean isBlack, boolean capture) {
    List <Ply> promotionalMoves = new ArrayList <Ply> ();
    int [] promotionPieces = (isBlack ? new int [] {Piece.BLACK_QUEEN, Piece.BLACK_ROOK, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT} : new int [] {Piece.WHITE_QUEEN, Piece.WHITE_ROOK, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT});
    
    for (int i = 0; i < promotionPieces.length; i++) {
      promotionalMoves.add (new Ply (location, newLocation, promotionPieces [i], board.getPieceAt (location), board.getPieceAt (newLocation), 0, board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC (), board.getSide ()));
    }
    return promotionalMoves;
  }
  
  public Pawn (Side color) {
    super (Utilities.PAWN_VALUE, color == Side.WHITE ? Piece.WHITE_PAWN : Piece.BLACK_PAWN, color);
  }
}