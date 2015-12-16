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
  
  /*public Ply negamax (Board board, int depth, Node <Ply> node) {
   * 
   Ply best = null;
   Ply current = null;
   
   if (depth == getMaxDepth () || board.getPlyTable ().isEmpty ()) {
   return node.getData ();
   }
   
   if (!node.hasChildren ()) {
   for (Set <Ply> i : board.getPlyTable ().values ()) {
   node.addChildren (new ArrayList <Ply> (i));
   }
   }
   
   for (Node <Ply> i : node.getChildren ()) {
   Board analysis = board.makeMove (i.getData (), true);
   i.getData ().setEvaluation (evaluate (analysis));
   current = i.getData ();
   
   if (best == null) {
   best = current;
   }
   
   Ply futureMove = negamax (analysis, depth + 1, i);
   
   if (compareEvaluation (board.getSide (), (best == null ? Integer.MIN_VALUE : best.getEvaluation ()), futureMove.getEvaluation ()) == futureMove.getEvaluation ()) {
   best = current;
   }
   }
   return best;
   }*/
  
  /*
   for (int i = depth; i <= getMaxDepth (); i++) {
   for (each child) {
   new_board = make child x on board
   evaluate new_board
   if new_board evaluation is better than old board, keep analyzing, or go onto next move
   
   }
   } 
   */
  
  /*public Ply negamax (Board board, int depth, Node <Ply> node) {
   * 
   Ply best = null;
   Ply current = null;
   
   int currentValue = Integer.MIN_VALUE;
   
   if (depth == getMaxDepth () || board.getPlyTable ().isEmpty ()) {
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + ". MAX DEPTH REACHED/TABLE EMPTY, RETURNED: " + node.getData ());
   return node.getData ();
   }
   
   if (!node.hasChildren ()) {
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + ". NO CHILDREN, GENERATING CHILDREN");
   for (Set <Ply> i : board.getPlyTable ().values ()) {
   node.addChildren (new ArrayList <Ply> (i));
   }
   }
   
   for (Node <Ply> i : node.getChildren ()) {
   Board analysis = board.makeMove (i.getData (), true);
   i.getData ().setEvaluation (evaluate (analysis));
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + ". MOVE: " + i.getData ());
   current = i.getData ();
   
   Ply futureMove = negamax (analysis, depth + 1, i);
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + ". FUTURE MOVE: " + futureMove);
   
   if (compareEvaluation (analysis.getSide (), i.getData ().getEvaluation (), futureMove.getEvaluation ()) == futureMove.getEvaluation ()) {//maybe
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + ". FUTURE MOVE HIGHER EVALUATION THAN CURRENT MOVE");
   currentValue = futureMove.getEvaluation ();
   best = i.getData ();//maybe
   best.setEvaluation (currentValue);
   }
   }
   //System.out.println (indent (depth) + depth + "/" + getMaxDepth () + "RETURNED BEST: " + best);
   return best;
   }*/
  
  /*public Ply negamax (Board board, int depth, Node <Ply> node) {
   int bestValue = Integer.MIN_VALUE;
   int currentValue = Integer.MIN_VALUE;
   
   Ply best = null;
   Ply current;
   
   if (depth == getMaxDepth () || board.getPlyTable ().isEmpty ()) {
   node.getData ().setEvaluation (evaluate (board));
   return node.getData ();
   }
   
   for (Set <Ply> i : board.getPlyTable ().values ()) {
   node.addChildren (new ArrayList <Ply> (i));
   }
   
   for (Node <Ply> i : node.getChildren ()) {
   current = i.getData ();
   Board analysis = board.makeMove (current, true);
   //current.setEvaluation (evaluate (analysis));
   
   currentValue = negamax (analysis, depth + 1, new Node <Ply> (current)).getEvaluation ();
   // if (compareEvaluation (board.getSide (), currentValue, bestValue) == currentValue) {
   if (currentValue > bestValue) {
   bestValue = currentValue;
   best = current;
   }
   }
   return best;
   }*/
  
  /*public Ply negamax (Board board, int depth, Node <Ply> node) {
   * 
   if (depth == getMaxDepth () || board.getPlyTable ().isEmpty ()) {
   return node.getData ();
   }
   
   Ply bestPly = null;
   Ply currentPly = null;
   int currentEvaluation = 0;
   
   for (Set <Ply> i : board.getPlyTable ().values ()) {
   node.addChildren (new ArrayList <Ply> (i));
   }
   
   for (Node <Ply> i : node.getChildren ()) {
   currentPly = i.getData ();
   currentPly.setEvaluation (evaluate (board.makeMove (currentPly, true)));
   
   if (bestPly == null || compareEvaluation (board.getSide (), bestPly.getEvaluation (), currentPly.getEvaluation ()) == currentPly.getEvaluation ()) {
   bestPly = currentPly;
   }
   }
   return bestPly;
   }*/
  
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