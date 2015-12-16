import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

public class Utilities {
  
  public static final int KING_VALUE = 10000;
  public static final int QUEEN_VALUE = 900;
  public static final int ROOK_VALUE = 500;
  public static final int BISHOP_VALUE = 330;
  public static final int KNIGHT_VALUE = 320;
  public static final int PAWN_VALUE = 100;
  
  public static final int LEFT_RIGHT_DIAGONAL_MOVEMENT = 17;
  public static final int RIGHT_LEFT_DIAGONAL_MOVEMENT = 15;
  public static final int UP_DOWN_MOVEMENT = 16;
  public static final int LEFT_RIGHT_MOVEMENT = 1;
  
  public static final int [] KNIGHT_MOVEMENT = {18, 14, 31, 33};
  
  public static final int NORMAL_MOVE = 0;
  public static final int CASTLE_MOVE = -1;
  
  public static BufferedImage getImage (String fileName) {
    try {
      return ImageIO.read (new File ("./resources/imgs/" + fileName));
    }
    catch (IOException e) {
      System.out.println (fileName + " = null");
      return null;
    }
  }
}