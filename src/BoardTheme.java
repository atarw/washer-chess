import java.awt.Color;
import java.awt.image.BufferedImage;

public class BoardTheme {
  private Color darkSquare, lightSquare, border, borderText, highlight;
  private static final char [] PIECE_IMAGE_ENDING = {'r', 'n', 'b', 'k', 'q', 'p'};
  private BufferedImage [][] pieces;
  
  public static final BoardTheme WOOD_THEME = new BoardTheme (new Color (235, 151, 78), new Color (253, 227, 167), new Color (175, 100, 50), Color.WHITE, Color.YELLOW, new char [] {'w', 'b'});
  
  public Color getDark () {
    return darkSquare;
  }
  
  public Color getLight () {
    return lightSquare;
  }
  
  public Color getBorder () {
    return border;
  }
  
  public Color getBorderText () {
    return borderText;
  }
  
  public Color getHighlight () {
    return highlight;
  }
  
  public BufferedImage getImg (Piece piece) {
    if (piece.getIdentity () == Piece.WHITE_PAWN) {
      return pieces [0][5];
    }
    else if (piece.getIdentity () == Piece.BLACK_PAWN) {
      return pieces [1][5];
    }
    else if (piece.getIdentity () == Piece.WHITE_KNIGHT) {
      return pieces [0][1];
    }
    else if (piece.getIdentity () == Piece.BLACK_KNIGHT) {
      return pieces [1][1];
    }
    else if (piece.getIdentity () == Piece.WHITE_BISHOP) {
      return pieces [0][2];
    }
    else if (piece.getIdentity () == Piece.BLACK_BISHOP) {
      return pieces [1][2];
    }
    else if (piece.getIdentity () == Piece.WHITE_ROOK) {
      return pieces [0][0];
    }
    else if (piece.getIdentity () == Piece.BLACK_ROOK) {
      return pieces [1][0];
    }
    else if (piece.getIdentity () == Piece.WHITE_QUEEN) {
      return pieces [0][4];
    }
    else if (piece.getIdentity () == Piece.BLACK_QUEEN) {
      return pieces [1][4];
    }
    else if (piece.getIdentity () == Piece.WHITE_KING) {
      return pieces [0][3];
    }
    return pieces [1][3];
    //else if (piece.getIdentity () == Utilities.BLACK_KING) {
    //   return pieces [1][3];
    //}
  }
  
  private void initializePieces (char [] color) {
    pieces = new BufferedImage [2][PIECE_IMAGE_ENDING.length * 2];
    
    for (int x = 0; x < pieces.length; x++) {
      for (int i = 0; i < PIECE_IMAGE_ENDING.length; i++) {
        pieces [x][i] = Utilities.getImage ("pieces/" + color [x] + PIECE_IMAGE_ENDING [i] + ".png");
      }
    }
  }
  
  public BoardTheme (Color darkSquare, Color lightSquare, Color border, Color borderText, Color highlight, char [] color) {
    this.darkSquare = darkSquare;
    this.lightSquare = lightSquare;
    this.border = border;
    this.borderText = borderText;
    this.highlight = highlight;
    initializePieces (color);
  }
}