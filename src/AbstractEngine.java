public abstract class AbstractEngine {
  
  private int maxDepth;//max search depth
  private int maxTime;//max ponder time
  private int maxNodes;//max branches to look into
  
  private Board currentBoard;//starting position
  
  private Tree <Ply> root;
  private Ply chosenMove;
  
  private String name;
  
  public static final int DEFAULT_DEPTH = 2;
  public static final int DEFAULT_MOVE_TIME = 10000;
  public static final int DEFAULT_MAX_NODES = 10;
  
  
  public Tree <Ply> getRoot () {
    return root;
  }
  
  public Board getCurrentBoard () {
    return currentBoard;
  }
  
  public int getMaxDepth () {
    return maxDepth;
  }
  
  public int getMaxTime () {
    return maxTime;
  }
  
  public int getMaxNodes () {
    return maxNodes;
  }
  
  public String getName () {
    return name;
  }
  
  public Ply getChosenMove () {
    return chosenMove;
  }
  
  public void setRoot (Tree <Ply> newRoot) {
    root = newRoot;
  }
  
  public void setDepth (int newMaxDepth) {
    maxDepth = newMaxDepth;
  }
  
  public void setMaxTime (int newMaxTime) {
    maxTime = newMaxTime;
  }
  
  public void setMaxNodes (int newMaxNodes) {
    maxNodes = newMaxNodes;
  }
  
  public void setChosenMove (Ply newChosenMove) {
    chosenMove = newChosenMove;
  }
  
  public int incrementEvaluation (Side side, int value, int increment) {//negative increment = punishment, positive = bonus
    
    //return (side == Side.WHITE ? value + increment : value - increment);
    
    if (increment >= 0) {
      if (side == Side.WHITE) {
        value += increment;
      }
      else {
        value -= increment;
      }
    }
    else {
      if (side == Side.WHITE) {
        value += increment;
      }
      else {
        value -= increment;
      }
    }
    
    return value;
  }
  
  public int compareEvaluation (Side side, int evaluation, int evaluation2) {
    return (side == Side.WHITE ? Math.max (evaluation, evaluation2) : Math.min (evaluation, evaluation2));
  }
  
  public void setBoard (Board newBoard) {
    currentBoard = new Board (newBoard);
  }
  
  public boolean choseMove () {
    return chosenMove != null;
  }
  
  public void clearChosenMove () {
    chosenMove = null;
  }
  
  @Override
  public String toString () {
    return name + " - max depth: " + maxDepth + " max nodes: " + maxNodes + " max thinking time: " + maxTime;
  }
  
  public abstract int evaluate (Board board);
  //public abstract Ply negamax (Board board, int depth, Node <Ply> node);
  public abstract Ply alphaBeta (Board board, int depth, Node <Ply> node, int alpha, int beta);
  public abstract void start ();
  public abstract void stop ();
  
  public AbstractEngine (String name) {
    this (DEFAULT_DEPTH, DEFAULT_MOVE_TIME, DEFAULT_MAX_NODES, name);
  }
  
  public AbstractEngine (int depth, int moveTime, int maxNodes, String name) {
    this (depth, moveTime, maxNodes, name, new Board ());
  }
  
  public AbstractEngine (int maxDepth, int maxTime, int maxNodes, String name, Board currentBoard) {
    this.maxDepth = maxDepth;
    this.maxTime = maxTime;
    this.maxNodes = maxNodes;
    
    this.name = name;
    
    this.currentBoard = new Board (currentBoard);
    
    this.root = new Tree <Ply> ();
  }
}