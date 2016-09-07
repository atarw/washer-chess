import java.util.ArrayList;

public class WasherEngine extends AbstractEngine {
  
  private static final int CASTLE_BONUS = 25;
  
  public int evaluate (Board board) {
    
    int value = board.getPieces (Side.WHITE).score () - board.getPieces (Side.BLACK).score ();
    
    //value = incrementEvaluation (board.getSide (), value, board.getPlyTable ().size ());
    
    //for (Side i : Side.values ()) {
    //  value = incrementEvaluation (i, value, board.canCastle (i) ? CASTLE_BONUS : -CASTLE_BONUS);
    //}
    
    if (board.getOutcome () == Outcome.WHITE) {
      value = Utilities.KING_VALUE;
    }
    else if (board.getOutcome () == Outcome.BLACK) {
      value = -Utilities.KING_VALUE;
    }
    else if (board.isDraw ()) {
      value = 0;
    }
    else if (board.isUnclaimedDraw () && (value > 100 && board.getSide () == Side.BLACK || value < -100 && board.getSide () == Side.WHITE)) {
      value = 0;
    }
    
    return value;
  }
  
  public Ply alphaBeta (Board board, int depth, Node <Ply> node, int alpha, int beta) {
    Ply best = null;
    Ply current = null;
    
    if (depth == getMaxDepth () || board.getPlyTable ().isEmpty ()) {
      node.getData ().setEvaluation (evaluate (board));
      return node.getData ();
    }
    
    if (!node.hasChildren ()) {
      node.addChildren (new ArrayList <Ply> (board.getPlyTable ().getList ()));
    }
    
    for (Node <Ply> i : node.getChildren ()) {
      Board analysis = board.makeMove (i.getData (), true);
      i.getData ().setEvaluation (evaluate (analysis));
      current = i.getData ();
      
      if (best == null) {
        best = current;
      }
      
      Ply futureMove = alphaBeta (analysis, depth + 1, i, alpha, beta);
      
      if (compareEvaluation (board.getSide (), (best == null ? Integer.MIN_VALUE : best.getEvaluation ()), futureMove.getEvaluation ()) == futureMove.getEvaluation ()) {
        best = current;
        best.setEvaluation (futureMove.getEvaluation ());
      }
      
      if (compareEvaluation (board.getSide (), alpha, futureMove.getEvaluation ()) == futureMove.getEvaluation ()) {
        alpha = futureMove.getEvaluation ();
      }
      
      if (alpha >= beta) {
        break;
      }
    }
    return best;
  }
  
  private String indent (int depth) {
    String indent = "";
    
    for (int i = 0; i < depth; i++) {
      indent += "\t";
    }
    return indent;
  }
  
  public void start () {
    
  }
  
  public void stop () {
    
  }
  
  public WasherEngine (Board board) {
    this (DEFAULT_DEPTH, DEFAULT_MOVE_TIME, DEFAULT_MAX_NODES, board);
  }
  
  public WasherEngine (int depth, int moveTime, int maxNodes, Board board) {
    super (depth, moveTime, maxNodes, "Washer", board);
  }
}