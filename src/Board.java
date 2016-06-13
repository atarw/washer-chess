import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

public class Board {
  
  private PlyTable plyTable;
  private BoardEntry [] board;
  private PieceList blackPieces, whitePieces;
  
  private int enPassentCapturableLocation;
  private int halfmoveClock;
  
  private boolean kwc, kbc, qwc, qbc;
  private Side side;
  private Outcome outcome;
  
  public static final int BOARD_VALIDATION = 0x88;
  public static final int BOARD_SIZE = 128;
  
  public static final int NO_ATTACK = 0;
  public static final int KQR_ATTACK = 1;
  public static final int QR_ATTACK = 2;
  public static final int KQBwP_ATTACK = 3;
  public static final int KQBbP_ATTACK = 4;
  public static final int QB_ATTACK = 5;
  public static final int N_ATTACK = 6;
  
  public static final int [] ATTACK_ARRAY =
  { 0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,2,0,0,0,     //0-19
    0,0,0,5,0,0,5,0,0,0,0,0,2,0,0,0,0,0,5,0,     //20-39
    0,0,0,5,0,0,0,0,2,0,0,0,0,5,0,0,0,0,0,0,     //40-59
    5,0,0,0,2,0,0,0,5,0,0,0,0,0,0,0,0,5,0,0,     //60-79
    2,0,0,5,0,0,0,0,0,0,0,0,0,0,5,6,2,6,5,0,     //80-99
    0,0,0,0,0,0,0,0,0,0,6,4,1,4,6,0,0,0,0,0,     //100-119
    0,2,2,2,2,2,2,1,0,1,2,2,2,2,2,2,0,0,0,0,     //120-139
    0,0,6,3,1,3,6,0,0,0,0,0,0,0,0,0,0,0,5,6,     //140-159
    2,6,5,0,0,0,0,0,0,0,0,0,0,5,0,0,2,0,0,5,     //160-179
    0,0,0,0,0,0,0,0,5,0,0,0,2,0,0,0,5,0,0,0,     //180-199
    0,0,0,5,0,0,0,0,2,0,0,0,0,5,0,0,0,0,5,0,     //200-219
    0,0,0,0,2,0,0,0,0,0,5,0,0,5,0,0,0,0,0,0,     //220-239
    2,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0};          //240-256
  
  public int squareAttacked (int attackedSquare, int attackingSquare) {
    int attack = ATTACK_ARRAY [attackedSquare - attackingSquare + 128];
    
    return attack;
  }
  
  public void generateLegalMoves () {
    plyTable = getAllLegalMoves ();
  }
  
  public void generateOutcome () {
    outcome = determineOutcome ();
  }
  
  private Outcome determineOutcome () {//NEED TO ADD THREEFOLD REPETITION DETECTION, INSUFFICIENT MATERIAL, 50 MOVE RULE, PLAYERS AGREEING TO DRAW
    
    for (Side i : Side.values ()) {
      
      if (getPieces (i).size () == 1 && (getPieces (i.opposite ()).size () == 2 || getPieces (i.opposite ()).size () == 3)) {
        List <BoardEntry> opponent = new ArrayList <BoardEntry> (getPieces (i.opposite ()).values ());
        boolean firstKnight = false, secondKnight = false, foundFirstKnight = false;
        
        for (BoardEntry x : opponent) {
          if (opponent.size () == 2 && (x.getPiece ().equals (new Knight (i.opposite ())) || x.getPiece ().equals (new Bishop (i.opposite ())))) {
            return Outcome.INSUFFICIENT_MATERIAL_DRAW;
          }
          else if (opponent.size () == 3 && x.getPiece ().equals (new Knight (i.opposite ()))) {
            if (foundFirstKnight) {
              secondKnight = true;
            }
            else {
              firstKnight = true;
              foundFirstKnight = true;
            }
            
            if (firstKnight && secondKnight) {
              return Outcome.INSUFFICIENT_MATERIAL_DRAW;
            }
          }
        }
      }
      
      if (side == i && plyTable.isEmpty ()) {
        if (((King) getKing (i).getPiece ()).isChecked ()) {
          return (i == Side.WHITE ? Outcome.BLACK : Outcome.WHITE);
        }
        else {
          return Outcome.STALEMATE_DRAW;
        }
      }
    }
    
    if (getHalfmoveClock () >= 50) {
      return Outcome.UNCLAIMED_50_MOVE_DRAW;
    }
    
    return Outcome.UNDECIDED;
  }
  
  public boolean isGameOver () {
    return outcome != Outcome.UNDECIDED && outcome != Outcome.UNCLAIMED_50_MOVE_DRAW && outcome != Outcome.UNCLAIMED_THREE_FOLD_DRAW;
  }
  
  public boolean isDraw () {
    return outcome == Outcome.INSUFFICIENT_MATERIAL_DRAW || outcome == Outcome.STALEMATE_DRAW;
  }
  
  public boolean isUnclaimedDraw () {
    return outcome == Outcome.UNCLAIMED_50_MOVE_DRAW || outcome == Outcome.UNCLAIMED_THREE_FOLD_DRAW;
  }
  
  private PlyTable getAllLegalMoves () {
    Map <Integer, Set <Ply>> table = new HashMap <Integer, Set <Ply>> ();
    Piece piece;
    Set <Ply> moveSet;
    
    List <Ply> moves;
    
    for (BoardEntry entry : getPieces (side).values ()) {//BLACK
      piece = entry.getPiece ();
      
      moves = piece.getLegalMoves (this, entry.getLocation ());
      
      if (moves != null) {
        moveSet = new HashSet <Ply> (moves);
        table.put (entry.getLocation (), moveSet);
      }
    }
    
    List <Set <Ply>> values = new ArrayList <Set <Ply>> (table.values ());
    Board copy = this;
    
    for (int i = values.size () - 1; i >= 0; i--) {
      Iterator <Ply> iterator = values.get (i).iterator ();
      while (iterator.hasNext ()) {
        Ply ply = iterator.next ();
        copy = this.makeMove (ply, false);
        
        if (((King) copy.getKing (copy.getSide ().opposite ()).getPiece ()).isChecked ()) {
          iterator.remove ();//REMOVES FROM PLY TABLE, DOESN'T UPDATE NUMBER OF MOVES
        }
      }
    }
    
    return new PlyTable (table);
  }
  
  public Set <Ply> getLegalMovesForSquare (int location) {
    return plyTable.get (location);
  }
  
  /*IMPLEMENT*/
  public void loadBoard (String FEN) {
    
  }
  
  /*IMPLEMENT*/
  public String generateFEN () {
    return null;
  }
  
  public int getLocation (int file, int rank) {
    return 16 * rank + file;
  }
  
  public static int getFile (int location) {
    return location & 7;
  }
  
  public static int getRank (int location) {
    return location >> 4;
  }
  
  public PlyTable getPlyTable () {
    return plyTable;
  }
  
  public int getHalfmoveClock () {
    return halfmoveClock;
  }
  
  public Outcome getOutcome () {
    return outcome;
  }
  
  public void setHalfmoveClock (int newHalfmoveClock) {
    halfmoveClock = newHalfmoveClock;
  }
  
  public BoardEntry getSquareAt (int location) {
    return board [location];
  }
  
  public boolean isLegal (Ply ply) {
    return plyTable.get (ply.getStart ()).contains (ply);
  }
  
  public Side getSide () {
    return side;
  }
  
  public boolean getKWC () {
    return kwc;
  }
  
  public boolean getKBC () {
    return kbc;
  }
  
  public boolean getQWC () {
    return qwc;
  }
  
  public boolean getQBC () {
    return qbc;
  }
  
  public void setKWC (boolean newKWC) {
    kwc = newKWC;
  }
  
  public void setKBC (boolean newKBC) {
    kbc = newKBC;
  }
  
  public void setQWC (boolean newQWC) {
    qwc = newQWC;
  }
  
  public void setQBC (boolean newQBC) {
    qbc = newQBC;
  }
  
  private Piece getPromotion (int type) {
    
    if (type == Piece.WHITE_KNIGHT) {
      return new Knight (Side.WHITE);
    }
    else if (type == Piece.WHITE_BISHOP) {
      return new Bishop (Side.WHITE);
    }
    else if (type == Piece.WHITE_ROOK) {
      return new Rook (Side.WHITE, false);
    }
    else if (type == Piece.WHITE_QUEEN) {
      return new Queen (Side.WHITE);
    }
    else if (type == Piece.BLACK_KNIGHT) {
      return new Knight (Side.BLACK);
    }
    else if (type == Piece.BLACK_BISHOP) {
      return new Bishop (Side.BLACK);
    }
    else if (type == Piece.BLACK_ROOK) {
      return new Rook (Side.BLACK, false);
    }
    else if (type == Piece.BLACK_QUEEN) {
      return new Queen (Side.BLACK);
    }
    return null;
  }
  
  public boolean canCastle (Side side) {
    return (side == Side.WHITE ? (kwc || qwc) : (kbc || qbc));
  }
  
  private BoardEntry getCastleRook (Ply ply) {
    int rook;
    
    if (ply.getSide () == Side.WHITE) {
      rook = 0;
    }
    else {
      rook = 112;
    }
    if (ply.getEnd () - ply.getStart () == 2) {//number of moves a king moves to castle (either 2 right or 2 left), -2 is for moving left
      rook += 7;
    }
    
    return getSquareAt (rook);
  }
  
  private boolean isKingSideCastle (Ply ply) {
    if (ply.getStart () < ply.getEnd ()) {
      return true;
    }
    else {
      return false;
    }
  }
  
  private int getCastleRookEndLocation (Ply ply) {
    
    if (isKingSideCastle (ply)) {
      return ply.getEnd () - Utilities.LEFT_RIGHT_MOVEMENT;
    }
    else {
      return ply.getEnd () + Utilities.LEFT_RIGHT_MOVEMENT;
    }
  }
  
  public Board makeMove (Ply ply, boolean generateAttributes) {
    if (ply != null) {
      Board copy = new Board (this);
      Piece movedPiece = copy.getPieceAt (ply.getStart ());
      
      copy.setKWC (ply.getKWC ());
      copy.setQWC (ply.getQWC ());
      copy.setKBC (ply.getKBC ());
      copy.setQBC (ply.getQBC ());
      copy.setHalfmoveClock (ply.getHalfmoveClock ());
      //((King) copy.getKing (copy.getSide ()).getPiece ()).setChecked (false); //should be impossible for your king to be in check after your turn
      
      if (ply.getType () == Utilities.CASTLE_MOVE) {
        BoardEntry rookLocation = getCastleRook (ply);
        copy.movePiece (rookLocation.getLocation (), getCastleRookEndLocation (ply), ply.getSide ());
      }
      else if (ply.getType () != Utilities.NORMAL_MOVE) {
        movedPiece = getPromotion (ply.getType ());
      }
      
      copy.movePiece (ply.getStart (), ply.getEnd (), ply.getSide (), movedPiece);
      
      if (copy.isLocationAttacked (copy.getKing (copy.getSide ().opposite ()), copy.getSide ())) { //if the other player's king is attacked by this turn, set their king checked to true, else false
        ((King) copy.getKing (copy.getSide ().opposite ()).getPiece ()).setChecked (true);
      }
      else {
        ((King) copy.getKing (copy.getSide ().opposite ()).getPiece ()).setChecked (false);
      }
      
      if (copy.isLocationAttacked (copy.getKing (copy.getSide ()), copy.getSide ().opposite ())) {
        ((King) copy.getKing (copy.getSide ()).getPiece ()).setChecked (true); //should be impossible for your king to be in check after your turn
      }
      else {
        ((King) copy.getKing (copy.getSide ()).getPiece ()).setChecked (false); //should be impossible for your king to be in check after your turn
      }
      
      copy.flipSide (); //IMPORTANT
      
      if (generateAttributes) {
        copy.generateLegalMoves ();
        copy.generateOutcome ();
      }
      
      return copy;
    }
    return this;
  }
  
  private void movePiece (int start, int end, Side side, Piece piece) {
    setPieceAt (piece, end);
    removePieceAt (start);
    
    getPieces (side).removeEntry (start);
    getPieces (side).addEntry (getSquareAt (end));
    
    getPieces (side.opposite ()).removeEntry (end);
  }
  
  private void movePiece (int start, int end, Side side) {
    movePiece (start, end, side, getPieceAt (start));
  }
  
  public Side getColor (int location) {
    return getPieceAt (location).getColor ();
  }
  
  public boolean hasPiece (int location) {
    return getPieceAt (location) != null;
  }
  
  public void setPieceAt (Piece piece, int location) {
    getSquareAt (location).setPiece (piece);
  }
  
  public void removePieceAt (int location) {
    getSquareAt (location).removePiece ();
  }
  
  public Piece getPieceAt (int location) {
    return getSquareAt (location).getPiece ();
  }
  
  public BoardEntry [] getBoard () {
    return board;
  }
  
  public PieceList getPieces (Side side) {
    return (side == Side.WHITE ? whitePieces : blackPieces);
  }
  
  public BoardEntry getKing (Side side) {
    return getPieces (side).getKing ();
  }
  
  public void flipSide () {
    side = side.opposite ();
  }
  
  public int getEnPassentLocation () {
    return enPassentCapturableLocation;
  }
  
  public void resetBoard () {
    int rank = 0;
    int file = 0;
    
    for (int i = 0; i < board.length; i++) {
      board [i] = new BoardEntry (i);
      
      if (isValidLocation (i)) {
        rank = getRank (board [i].getLocation ());
        
        if (rank == 0 || rank == 7) {
          file = getFile (board [i].getLocation ());
          
          if (file == 0 || file == 7) {
            board [i].setPiece (new Rook (rank == 0 ? Side.WHITE : Side.BLACK, file == 0 ? false : true));
          }
          else if (file == 1 || file == 6) {
            board [i].setPiece (new Knight (rank == 0 ? Side.WHITE : Side.BLACK));
          }
          else if (file == 2 || file == 5) {
            board [i].setPiece (new Bishop (rank == 0 ? Side.WHITE : Side.BLACK));
          }
          else if (file == 3) {
            board [i].setPiece (new Queen (rank == 0 ? Side.WHITE : Side.BLACK));
          }
          else {
            if (file == 4) {
              board [i].setPiece (new King (rank == 0 ? Side.WHITE : Side.BLACK));
            }
          }
          (rank == 0 ? whitePieces : blackPieces).addEntry (board [i]);
          
          if (board [i].hasPiece () && board [i].getPiece () instanceof King) {
            (rank == 0 ? whitePieces : blackPieces).setKing (board [i]);
          }
        }
        else {
          if (rank == 1 || rank == 6) {
            board [i].setPiece (new Pawn (rank == 1 ? Side.WHITE : Side.BLACK));
            (rank == 1 ? whitePieces : blackPieces).addEntry (board [i]);
          }
        }
      }
    }
  }
  
  private int getShallowAttack (int attackedLocation, int attackingLocation) {
    int attack = squareAttacked (attackedLocation, attackingLocation);
    
    if (attack == Board.KQR_ATTACK && (getPieceAt (attackingLocation) instanceof King || getPieceAt (attackingLocation) instanceof Queen || getPieceAt (attackingLocation) instanceof Rook)) {
      return attack;
    }
    else if (attack == Board.QR_ATTACK && (getPieceAt (attackingLocation) instanceof Queen || getPieceAt (attackingLocation) instanceof Rook)) {
      return attack;
    }
    else if (attack == Board.KQBwP_ATTACK && (getPieceAt (attackingLocation) instanceof King || getPieceAt (attackingLocation) instanceof Queen || getPieceAt (attackingLocation) instanceof Bishop || getPieceAt (attackingLocation) instanceof Pawn && getPieceAt (attackingLocation).getColor () == Side.WHITE)) {
      return attack;
    }
    else if (attack == Board.KQBbP_ATTACK && (getPieceAt (attackingLocation) instanceof King || getPieceAt (attackingLocation) instanceof Queen || getPieceAt (attackingLocation) instanceof Bishop || getPieceAt (attackingLocation) instanceof Pawn && getPieceAt (attackingLocation).getColor () == Side.BLACK)) {
      return attack;
    }
    else if (attack == Board.QB_ATTACK && (getPieceAt (attackingLocation) instanceof Queen || getPieceAt (attackingLocation) instanceof Bishop)) {
      return attack;
    }
    else if (attack == Board.N_ATTACK && getPieceAt (attackingLocation) instanceof Knight) {
      return attack;
    }
    else {
      return Board.NO_ATTACK;
    }
  }
  
  public boolean isLocationAttacked (BoardEntry entry, Side opponentColor) {
    return isLocationAttacked (entry.getLocation (), opponentColor);
  }
  
  public boolean isLocationAttacked (int location, Side opponentColor) {
    List <BoardEntry> opponentPieces = new ArrayList <BoardEntry> (getPieces (opponentColor).values ());
    int attack;
    boolean clearPath = false;
    
    for (BoardEntry i : opponentPieces) {
      attack = getShallowAttack (location, i.getLocation ());
      
      if (attack != Board.NO_ATTACK) {
        if (attack == Board.N_ATTACK || attack == Board.KQBbP_ATTACK || attack == Board.KQBwP_ATTACK || attack == Board.KQR_ATTACK) {
          return true;
        }
        
        clearPath = hasUnrestrictedPath (location, i.getLocation ());
        
        if (clearPath) {
          return true;
        }
      }
    }
    return false;
  }
  
  private List <BoardEntry> getRay (int start, int end) {
    List <BoardEntry> ray = new ArrayList <BoardEntry> ();
    int attack = getShallowAttack (start, end);
    int movement;
    int rankDifference = Math.abs (Board.getRank (start) - Board.getRank (end));
    
    if (attack != Board.NO_ATTACK && attack != Board.N_ATTACK) {
      if (rankDifference == 0) {
        rankDifference = Math.abs (start - end);
      }
      
      movement = Math.abs (start - end) / rankDifference;
      
      for (int x = Math.min (start, end) + movement; x < Math.max (start, end); x+= movement) {
        ray.add (getSquareAt (x));
      }
    }
    
    return ray;
  }
  
  /*private boolean hasUnrestrictedPath (int attackedLocation, int attackingLocation) {
   boolean blocked = false;
   
   int rankDifference = Math.abs (getRank (attackingLocation) - getRank (attackedLocation));
   
   if (rankDifference == 0) {
   rankDifference = Math.abs (attackedLocation - attackingLocation);
   }
   
   int movement = Math.abs (attackedLocation - attackingLocation) / rankDifference;
   
   for (int x = Math.min (attackedLocation, attackingLocation) + movement; x < Math.max (attackedLocation, attackingLocation); x+= movement) {
   //if (hasPiece (x) && !getPieceAt (x).equals (this)) { //WHY this, WHAT DOES THAT DO AND WHY DID THAT WORK BEFORE???, actually this might have been an unedited piece of code since it used to be in the Piece class
   if (hasPiece (x)) {
   blocked = true;
   }
   }
   if (!blocked) {
   return true;
   }
   return false;
   }*/
  
  private boolean hasUnrestrictedPath (int attackedLocation, int attackingLocation) {
    List <BoardEntry> ray = getRay (attackedLocation, attackingLocation);
    
    for (BoardEntry i : ray) {
      if (i.hasPiece ()) {
        return false;
      }
    }
    return true;
  }
  
  public boolean sameColor (int location, int location2) {
    return getColor (location) == getColor (location2);
  }
  
  public static boolean isValidLocation (int location) {
    return (location & BOARD_VALIDATION) == 0;
  }
  
  public static int to8x8 (int location) {
    return (location + (location & 7)) >> 1;
  }
  
  public boolean isEndGame () {
    if (blackPieces.size () + whitePieces.size () <= 10) {
      return true;
    }
    return false;
  }
  
  @Override
  public String toString () {
    StringBuilder s = new StringBuilder (100);
    
    /*for (BoardEntry i : board) {
     if (isValidLocation (i.getLocation ()))
     s.append (i.toString () + "\n");
     }*/
    s.append ("\n\n\nWHITE:\n\n").append (whitePieces).append ("\n\n\nBLACK:\n\n\n").append (blackPieces);
    return s.toString ();
  }
  
  public Board (BoardEntry [] board, PlyTable plyTable, PieceList whitePieces, PieceList blackPieces, int enPassentCapturableLocation, int halfmoveClock, boolean kwc, boolean kbc, boolean qwc, boolean qbc, Side side, Outcome outcome) {
    this.board = board;
    
    this.whitePieces = whitePieces;
    this.blackPieces = blackPieces;
    
    this.plyTable = plyTable;
    
    this.enPassentCapturableLocation = enPassentCapturableLocation;
    this.halfmoveClock = halfmoveClock;
    
    this.kwc = kwc;
    this.kbc = kbc;
    this.qwc = qwc;
    this.qbc = qbc;
    
    this.side = side;
    this.outcome = outcome;
  }
  
  public Board () {
    this (new BoardEntry [BOARD_SIZE], new PlyTable (), new PieceList (), new PieceList (), -1, 0, true, true, true, true, Side.WHITE, Outcome.UNDECIDED);
    resetBoard ();
  }
  
  public Board (Board board) {
    this (BoardArrayFactory.copyBoard (board.getBoard ()), new PlyTable (board.getPlyTable ()), new PieceList (board.getPieces (Side.WHITE)), new PieceList (board.getPieces (Side.BLACK))
            , board.getEnPassentLocation (), board.getHalfmoveClock ()
            , board.getKWC (), board.getKBC (), board.getQWC (), board.getQBC ()
            , board.getSide (), board.getOutcome ());
  }
  
  public Board (String FEN) {
    this.loadBoard (FEN);
  }
}