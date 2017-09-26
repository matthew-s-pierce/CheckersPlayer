package src.app;
import java.util.ArrayList;

/*
 * Class which represents a single move of a game piece. Tracks the number of captured ('jumped') pieces along a move
 * Note that in checkers a piece can move many positions as once if they are jumping
 * Therefore our Move class tracks those steps as well in addition to the final destination
 */
public class Move {
	private GamePiece _mover;
	private ArrayList<GamePiece> _captures;
	private int _destX, _destY;
	ArrayList<Integer> _stepsX;
	ArrayList<Integer> _stepsY;
	private final int _boardSize = 8;

	public Move(GamePiece p, int x, int y) {
		_mover = p;
		_destX = x;
		_destY = y;
		_captures = new ArrayList<GamePiece>();
	}

	public Move(GamePiece p, int x, int y, ArrayList<GamePiece> c) {
		_mover = p;
		_destX = x;
		_destY = y;
		_captures = c;
	}

	public Move(GamePiece p, int x, int y, ArrayList<GamePiece> c,
			ArrayList<Integer> sx, ArrayList<Integer> sy) {
		_mover = p;
		_destX = x;
		_destY = y;
		_captures = c;
		_stepsX = sx;
		_stepsY = sy;
	}

	public Move(GamePiece p) {
		_mover = p;
		_captures = new ArrayList<GamePiece>();
	}

	public void addCapture(GamePiece p) {
		_captures.add(p);
	}

	public GamePiece getPiece() {
		return _mover;
	}

	public int getDestX() {
		return _destX;
	}

	public int getDestY() {
		return _destY;
	}

	public ArrayList<GamePiece> getCaptures() {
		return _captures;
	}

	public void setDestX(int x) {
		_destX = x;
	}

	public void setDestY(int y) {
		_destY = y;
	}

	public boolean isJump() { // are any pieces captured?
		if (_captures.size() == 0)
			return false;
		else
			return true;
	}

	@Override
	public String toString() {
		String retString = "";
		if (_captures.isEmpty()) {
			return "(" + flipBoard(_mover.getX()) + ":" + _mover.getY() + "):(" + flipBoard(_destX)
					+ ":" + _destY + ")";
		}
		else 
		{
			for (int i = 0; i < _stepsX.size(); i++) 
			{
				retString += "(" + flipBoard(_stepsX.get(i)) + ":" + _stepsY.get(i) + "):";
			}
			retString += "(" + flipBoard(_destX) + ":" + _destY + ")";
		}
		return retString;
	}
	
	public int flipBoard(int i)
	{
		return Math.abs(i - (_boardSize - 1));
	}
	
	
	public String toStringAlternative()
	{
		return "Move " + _mover + " to (" + _destX + "," + _destY + ") taking " + _captures;
	}
}
