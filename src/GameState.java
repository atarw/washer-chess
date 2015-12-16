import java.util.Set;

public class GameState {
  
  private static final GameState game = new GameState ();
  private Board currentBoard;
  private MoveList moves;
  private AbstractEngine engine;
  
  //ADD STUFF
  
  public static GameState getInstance () {
    return game;
  }
  
  public Board getBoard () {
    return currentBoard;
  }
  
  public AbstractEngine getEngine () {
    return engine;
  }
  
  public void setEngine (AbstractEngine newEngine) {
    engine = newEngine;
  }
  
  public void removeEngine () {
    engine = null;
  }
  
  public boolean engineActive () {
    return engine != null;
  }
  
  public Ply getEngineMove () {
    if (!engine.choseMove ()) {
      Ply ply = engine.alphaBeta (currentBoard, 0, engine.getRoot ().getNode (), Integer.MIN_VALUE, Integer.MAX_VALUE);
      //Ply ply = engine.negamax (currentBoard, 0, engine.getRoot ().getNode ());
      engine.setChosenMove (ply);
    }
    //System.out.println (engine.getChosenMove ());
    return engine.getChosenMove ();
  }
  
  public BoardEntry [] getBoardArray () {
    return currentBoard.getBoard ();
  }
  
  public void resetState () {
    currentBoard = new Board ();
    moves = new MoveList ();
    currentBoard.generateLegalMoves ();
  }
  
  public void loadBoard (String FEN) {
    currentBoard.loadBoard (FEN);
  }
  
  public boolean hasPiece (int location) {
    return currentBoard.hasPiece (location);
  }
  
  public BoardEntry getEntryAt (int location) {
    return getBoardArray () [location];
  }
  
  public void makeMove (Ply ply, boolean generateAttributes) {
    currentBoard = currentBoard.makeMove (ply, generateAttributes);
    
    if (generateAttributes) {
      moves.addMove (ply);
      
      if (engineActive ()) {
        engine.clearChosenMove ();
        engine.getRoot ().setNode (new Node <Ply> (ply));
      }
    }
    
    //System.out.println ("OUTCOME: " + currentBoard.getOutcome () + " " + currentBoard.getPlyTable ().size ());
  }
  
  public Side getSide () {
    return currentBoard.getSide ();
  }
  
  public int getNumMoves () {
    return moves.getNumMoves ();
  }
  
  public int getLocation (int file, int rank) {
    return currentBoard.getLocation (file, rank);
  }
  
  public Piece getPieceAt (int location) {
    return getEntryAt (location).getPiece ();
  }
  
  public Side getColor (int location) {
    return currentBoard.getColor (location);
  }
  
  public MoveList getMoveList () {
    return moves;
  }
  
  public int getHalfmoveClock () {
    return currentBoard.getHalfmoveClock ();
  }
  
  public boolean getKWC () {
    return currentBoard.getKWC ();
  }
  
  public boolean getKBC () {
    return currentBoard.getKBC ();
  }
  
  public boolean getQWC () {
    return currentBoard.getQWC ();
  }
  
  public boolean getQBC () {
    return currentBoard.getQBC ();
  }
  
  public void addMove (Ply move) {
    moves.addMove (move);
  }
  
  public void removeMove () {
    moves.removeMove ();
  }
  
  public Ply getMoveAt (int index) {
    return moves.getMoveAt (index);
  }
  
  public /*Map <Integer, Set <Ply>>*/PlyTable getPlyTable () {
    return currentBoard.getPlyTable ();
  }
  
  public Set <Ply> getLegalMovesForSquare (int location) {
    return currentBoard.getPlyTable ().get (location);
  }
  
  public boolean isLegal (Ply ply) {
    return currentBoard.isLegal (ply);
  }
  
  public BoardEntry getKing (Side side) {
    return currentBoard.getKing (side);
  }
  
  public Outcome getOutcome () {
    return currentBoard.getOutcome ();
  }
  
  public boolean isGameOver () {
    return currentBoard.isGameOver ();
  }
  
  private GameState () {
    resetState ();
  }
}