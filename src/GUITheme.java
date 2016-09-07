import java.awt.Font;
import java.awt.Color;

import javax.swing.border.Border;
//import javax.swing.plaf.ColorUIResource;

public class GUITheme {

  private Color inactiveTextbox, activeTextbox, textboxText;
  private Color card;
  private Color menuSelectionText, menuSelectionBackground;
  private Color menuDisabledText;
  private Color borderlessButtonText;
  private Color inactiveBorderlessButtonBackground, activeBorderlessButtonBackground;
  private Color borderedButtonText;
  private Color inactiveBorderedButtonBackground, activeBorderedButtonBackground;

  private Border menuBorder = new DropShadowBorder (Color.BLACK, 0, 5, 0.3f, 12, true, true, true, true);
  private Border defaultBorder = new DropShadowBorder (Color.BLACK, 5, 5, 0.3f, 12, true, true, true, true);

  private static final Font ITALIC = Utilities.getFont ("Roboto-Italic.ttf");
  private static final Font LIGHT = Utilities.getFont ("Roboto-Light.ttf");
  private static final Font BOLD = Utilities.getFont ("Roboto-Medium.ttf");
  private static final Font REGULAR = Utilities.getFont ("Roboto-Regular.ttf");
  private static final Font THIN = Utilities.getFont ("Roboto-Thin.ttf");
  private static final Font THIN_ITALIC = Utilities.getFont ("RobotoCondensed-LightItalic.ttf");

  public static final GUITheme LIGHT_THEME = new GUITheme (new Color (230, 230, 230), new Color (220, 220, 220), Color.BLACK, Color.WHITE, Color.BLACK, new Color (230, 230, 230), new Color (0, 0, 0, 100),
                                                           new Color (33, 150, 243), Color.WHITE, new Color (240, 240, 240), Color.WHITE, new Color (245, 0, 87), new Color (255, 64, 129));

  public static final GUITheme DARK_THEME = new GUITheme (new Color (90, 90, 90), new Color (120, 120, 120), Color.WHITE, new Color (75, 75, 75), Color.WHITE, new Color (90, 90, 90), new Color (255, 255, 255, 100),
                                                          Color.WHITE, new Color (75, 75, 75), new Color (100, 100, 100), Color.WHITE, new Color (0, 150, 136), new Color (38, 166, 154));

  public Color getInactiveTextbox () {
    return inactiveTextbox;
  }

  public Color getActiveTextbox () {
    return activeTextbox;
  }

  public Color getTextboxText () {
    return textboxText;
  }

  public Color getCard () {
    return card;
  }

  public Color getMenuSelectionText () {
    return menuSelectionText;
  }

  public Color getMenuSelectionBackground () {
    return menuSelectionBackground;
  }

  public Color getMenuDisabledText () {
    return menuDisabledText;
  }

  public Color getBorderlessButtonText () {
    return borderlessButtonText;
  }

  public Color getInactiveBorderlessButtonBackground () {
    return inactiveBorderlessButtonBackground;
  }

  public Color getActiveBorderlessButtonBackground () {
    return activeBorderlessButtonBackground;
  }

  public Color getBorderedButtonText () {
    return borderedButtonText;
  }

  public Color getInactiveBorderedButtonBackground () {
    return inactiveBorderedButtonBackground;
  }

  public Color getActiveBorderedButtonBackground () {
    return activeBorderedButtonBackground;
  }

  public Border getMenuBorder () {
    return menuBorder;
  }

  public Border getDefaultBorder () {
    return defaultBorder;
  }

  public Font getItalic () {
    return ITALIC;
  }

  public Font getLight () {
    return LIGHT;
  }

  public Font getBold () {
    return BOLD;
  }

  public Font getRegular () {
    return REGULAR;
  }

  public Font getThin () {
    return THIN;
  }

  public Font getThinItalic () {
    return THIN_ITALIC;
  }

  private GUITheme (Color inactiveTextbox, Color activeTextbox, Color textboxText, Color card, Color menuSelectionText, Color menuSelectionBackground,
                    Color menuDisabledText, Color borderlessButtonText, Color inactiveBorderlessButtonBackground,
                    Color activeBorderlessButtonBackground, Color borderedButtonText, Color inactiveBorderedButtonBackground, Color activeBorderedButtonBackground) {

    this.inactiveTextbox = inactiveTextbox;
    this.activeTextbox = activeTextbox;
    this.textboxText = textboxText;
    this.card = card;
    this.menuSelectionText = menuSelectionText;
    this.menuSelectionBackground = menuSelectionBackground;
    this.menuDisabledText = menuDisabledText;
    this.borderlessButtonText = borderlessButtonText;
    this.inactiveBorderlessButtonBackground = inactiveBorderlessButtonBackground;
    this.activeBorderlessButtonBackground = activeBorderlessButtonBackground;
    this.borderedButtonText = borderedButtonText;
    this.inactiveBorderedButtonBackground = inactiveBorderedButtonBackground;
    this.activeBorderedButtonBackground = activeBorderedButtonBackground;
  }
}
