package washer.game;

public enum Side {
	WHITE, BLACK;

	public Side opposite () {
		return this == WHITE ? BLACK : WHITE;
	}
}