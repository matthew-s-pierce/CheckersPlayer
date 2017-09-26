package src.app;

/*
 * Class which represents a Checkers piece. We track its color, its board position, and type (whether it is a King)
 */

public class GamePiece
{
	private String _owner;
	private int _x, _y;
	private String _type;
	
	public GamePiece(String o)
	{
		_owner = o;
		_type = "pawn";
	}
	
	/*
	 * By default this constructor will create a "pawn" game piece (not a King)
	 */
	public GamePiece(String o, int x, int y)
	{
		_owner = o;
		_type = "pawn";
		_x = x;
		_y = y;
	}
	
	public GamePiece(String o, int x, int y, String t)
	{
		_owner = o;
		_type = t;
		_x = x;
		_y = y;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_owner == null) ? 0 : _owner.hashCode());
		result = prime * result + ((_type == null) ? 0 : _type.hashCode());
		result = prime * result + _x;
		result = prime * result + _y;
		return result;
	}
	
	/*
	 * Equality among game pieces is defined as having the same type, position, and owner
	 *  */
	@Override
	public boolean equals(Object obj)
	{
		GamePiece p = (GamePiece)obj;
		if (_owner.equals(p.getOwner()) && _type.equals(p.getType()) && _x == p.getX() && _y == p.getY())
			return true;
		else
			return false;
	}
	
	@Override
	public String toString()
	{
		if (_type.equals("king"))
			return Character.toUpperCase(_owner.charAt(0)) + "(" + (7 - _x) + "," + _y + ")";
		else
			return _owner.charAt(0) + "(" + (7 - _x) + "," + _y + ")";
		
	}
	
	public String getOwner()
	{
		return _owner;
	}
	
	public void setX(int x)
	{
		_x = x;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public void setY(int y)
	{
		_y = y;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public String getType()
	{
		return _type;
	}
	
	public void makeKing()
	{
		this._type = "king";
	}
	
	@Override
	public GamePiece clone()
	{
		GamePiece retPiece = new GamePiece(_owner, _x, _y, _type);
		return retPiece;
	}
}
