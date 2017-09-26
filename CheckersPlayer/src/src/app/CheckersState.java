package src.app;

import java.util.ArrayList;
import java.util.List;

/*
 * Class to represent a state of a checkers board. Accounts for the location of each piece.
 * Includes 
 */
public class CheckersState
{
	private GamePiece[][] _board;
	private String _player;
	private ArrayList<GamePiece> _whitePieces;
	private ArrayList<GamePiece> _blackPieces;
	private final int _boardSize = 8;
	private int _searchDepth;	//the maximum depth we will travel in the search tree to find potential moves
	
	/*
	 * This constructor handles the start of the game
	 */
	public CheckersState(int depth)
	{
		_board = new GamePiece[_boardSize][_boardSize];
		_whitePieces = new ArrayList<GamePiece>();
		_blackPieces = new ArrayList<GamePiece>();
		for (int i = 0; i < _boardSize; i++)
		{
			for (int j = 0; j < _boardSize; j++)
			{
				if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
				{
					if (i < 3)
					{
						GamePiece p = new GamePiece("black", i, j);
						_blackPieces.add(p);
						_board[i][j] = p;
					}
					else if (i > 4)
					{
						GamePiece p = new GamePiece("white", i, j);
						_whitePieces.add(p);
						_board[i][j] = p;
					}
				}
				else
				{
					_board[i][j] = null;
				}
			}
			
		}
		_player = "white";
		_searchDepth = depth;
	}
	
	/*
	 * This constructor handles the state of the game after a move has been made
	 */
	public CheckersState(String input, String p, int depth)
	{ // takes in input just as it outputs it, but without the endlines
		_board = new GamePiece[_boardSize][_boardSize];
		_whitePieces = new ArrayList<GamePiece>();
		_blackPieces = new ArrayList<GamePiece>();
		_player = p;
		_searchDepth = depth;
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				switch (input.charAt(i * 8 + j))
				{
					case '-':
						_board[i][j] = null;
						break;
					case 'w':
						GamePiece p1 = new GamePiece("white", i, j);
						_whitePieces.add(p1);
						_board[i][j] = p1;
						break;
					case 'b':
						GamePiece p2 = new GamePiece("black", i, j);
						_blackPieces.add(p2);
						_board[i][j] = p2;
						break;
					case 'W':
						GamePiece p3 = new GamePiece("white", i, j, "king");
						_whitePieces.add(p3);
						_board[i][j] = p3;
						break;
					case 'B':
						GamePiece p4 = new GamePiece("black", i, j, "king");
						_blackPieces.add(p4);
						_board[i][j] = p4;
						break;
				}
			}
		}
	}
	
	/*
	 * This constructor will initialize the board given the lists of game pieces and the search depth
	 */
	public CheckersState(GamePiece[][] b, String p, ArrayList<GamePiece> wPieces, ArrayList<GamePiece> bPieces, int depth)
	{
		_board = b;
		_player = p;
		_whitePieces = wPieces;
		_blackPieces = bPieces;
		_searchDepth = depth;
	}
	
	/*
	 * 
	 */
	public CheckersState(CheckersState state)
	{
		_searchDepth = state._searchDepth;
		_board = state._board;
		GamePiece[][] tempBoard = _board.clone();
		for (int i = 0; i < tempBoard.length; i++)
		{ // recall that .clone() is shallow
			tempBoard[i] = tempBoard[i].clone();
		}
		_board = tempBoard;
		_player = state._player;
		_whitePieces = (ArrayList<GamePiece>)state._whitePieces.clone();
		_blackPieces = (ArrayList<GamePiece>)state._blackPieces.clone();
		for (int i = 0; i < _whitePieces.size(); i++)
		{
			_whitePieces.set(i, _whitePieces.get(i).clone());
		}
		for (int i = 0; i < _blackPieces.size(); i++)
		{
			_blackPieces.set(i, _blackPieces.get(i).clone());
		}
	}
	
	public String player()
	{
		return _player;
	}
	
	public ArrayList<GamePiece> whitePieces()
	{
		return _whitePieces;
	}
	
	public ArrayList<GamePiece> blackPieces()
	{
		return _blackPieces;
	}
	
	public GamePiece[][] board()
	{
		return _board;
	}
	
	public void setPlayer(String p)
	{
		_player = p;
	}
	
	public void setSearchDepth(int d)
	{
		_searchDepth = d;
	}
	
	/*
	 * Gets the list of available moves that a player can make given the current board state
	 */
	public List<Move> actions()
	{
		ArrayList<Move> retList = new ArrayList<Move>();
		
		if (_player.equals("white"))
		{
			for (GamePiece p : _whitePieces)
			{
				retList.addAll(getJumpMoves(p, p.getX(), p.getY(), new ArrayList<GamePiece>(), new ArrayList<Move>(), new ArrayList<Integer>(),
						new ArrayList<Integer>()));
			}
			if (retList.isEmpty())
			{
				for (GamePiece p : _whitePieces)
				{
					retList.addAll(getMoves(p));
				}
			}
		}
		else
		{
			for (GamePiece p : _blackPieces)
			{
				retList.addAll(getJumpMoves(p, p.getX(), p.getY(), new ArrayList<GamePiece>(), new ArrayList<Move>(), new ArrayList<Integer>(),
						new ArrayList<Integer>()));
			}
			if (retList.isEmpty())
			{
				for (GamePiece p : _blackPieces)
				{
					
					retList.addAll(getMoves(p));
				}
			}
		}
		if (retList.isEmpty())
		{
			System.out.println(_player + " loses!");
		}
		return retList;
	}
	
	/*
	 * Gets the board state of a given move
	 */
	public CheckersState result(Move m)
	{
		CheckersState state = new CheckersState(this);
		
		GamePiece p = m.getPiece().clone();
		state.blackPieces().remove(p);
		state.whitePieces().remove(p);
		state.board()[p.getX()][p.getY()] = null; // clear old space
		p.setX(m.getDestX());
		p.setY(m.getDestY());
		state.board()[p.getX()][p.getY()] = p; // set new space
		
		for (GamePiece c : m.getCaptures())
		{ // remove captured pieces
			state.board()[c.getX()][c.getY()] = null;
			state.whitePieces().remove(c);
			state.blackPieces().remove(c);
		}
		if (_player.equals("white"))
		{
			if (p.getX() == 0)
			{ // piggyback off of color check to check king status
				p.makeKing();
			}
			state.setPlayer("black");
			state.whitePieces().add(p);
			return state;
		} // returns new states rather than mutated states
		else
		{
			if (p.getX() == (_boardSize - 1))
			{ // black king check
				p.makeKing();
			}
			state.setPlayer("white");
			state.blackPieces().add(p);
			return state;
		}
	}
	
	/*
	 * Gets the list of potential moves for a game piece at a board state
	 */
	private ArrayList<Move> getMoves(GamePiece p)
	{
		ArrayList retList = new ArrayList();
		// since king can move both ways we treat them as both white and black for the sake of movement (not collision/jumping)
		if (p.getOwner().equals("white") || p.getType().equals("king"))
		{
			int targetRow = p.getX() - 1;
			int left = p.getY() - 1;
			int right = p.getY() + 1;
			if (targetRow >= 0 && targetRow <= (_boardSize - 1))
			{ // various 'stays-within-board' checks occur regularly
				if (left >= 0)
				{
					if (_board[targetRow][left] == null)
					{
						retList.add(new Move(p, targetRow, left));
					}
				}
				if (right <= (_boardSize - 1))
				{
					if (_board[targetRow][right] == null)
					{
						retList.add(new Move(p, targetRow, right));
					}
				}
			}
			
		}
		if (p.getOwner().equals("black") || p.getType().equals("king"))
		{
			int targetRow = p.getX() + 1;
			int left = p.getY() - 1;
			int right = p.getY() + 1;
			if (targetRow >= 0 && targetRow <= 7)
			{
				if (left >= 0)
				{
					if (_board[targetRow][left] == null)
					{
						retList.add(new Move(p, targetRow, left));
					}
				}
				if (right <= 7)
				{
					if (_board[targetRow][right] == null)
					{
						retList.add(new Move(p, targetRow, right));
					}
				}
				
			}
		}
		return retList;
	}
	
	/*
	 * Determines the potential jump moves that a piece could make from a given location. A lot of implementation-specific logic (traversing a 2-dimensional array)
	 */
	private ArrayList<Move> getJumpMoves(GamePiece p, int x, int y, ArrayList<GamePiece> captures, ArrayList<Move> moves, ArrayList<Integer> stepsX,
			ArrayList<Integer> stepsY)
	{
		boolean flag = true;
		ArrayList<GamePiece> capturesClone = (ArrayList<GamePiece>)captures.clone();
		ArrayList<Integer> stepsXClone = (ArrayList<Integer>)stepsX.clone();
		for (int i = 0; i < stepsXClone.size(); i++)
		{
			stepsXClone.set(i, Integer.valueOf(stepsXClone.get(i)));
		}
		ArrayList<Integer> stepsYClone = (ArrayList<Integer>)stepsY.clone();
		for (int i = 0; i < stepsYClone.size(); i++)
		{
			stepsYClone.set(i, Integer.valueOf(stepsYClone.get(i)));
		}
		int targetRow;
		if (p.getOwner().equals("white") || p.getType().equals("king"))
		{
			targetRow = x - 1;
			int left = y - 1;
			if (targetRow >= 0 && targetRow <= 7 && left >= 0)
			{
				if (_board[targetRow][left] != null)
				{
					if (!_board[targetRow][left].getOwner().equals(p.getOwner()))
					{
						GamePiece jumpPiece = _board[targetRow][left];
						targetRow--;
						
						left--;
						if (targetRow >= 0 && targetRow <= (_boardSize - 1) && left >= 0)
						{
							if (_board[targetRow][left] == null || _board[targetRow][left].equals(p))
							{ // note that the piece may come back to where it started from; this only matters for Kings
								if (!capturesClone.contains(jumpPiece))
								{ // prevents kings from jumping over a piece back and forth indefinitely
									capturesClone.add(jumpPiece);
									stepsXClone.add(x);
									stepsYClone.add(y);
									getJumpMoves(p, targetRow, left, capturesClone, moves, stepsXClone, stepsYClone);
									flag = false;
								}
							}
						}
						
						targetRow++; // clear changes made by left before using for right
					}
				}
			}
			// again, clearing changes made
			capturesClone = (ArrayList<GamePiece>)captures.clone();
			stepsXClone = (ArrayList<Integer>)stepsX.clone();
			stepsYClone = (ArrayList<Integer>)stepsY.clone();
			
			int right = y + 1;
			if (targetRow >= 0 && targetRow <= 7 && right <= 7)
			{
				if (_board[targetRow][right] != null)
				{
					if (!_board[targetRow][right].getOwner().equals(p.getOwner()))
					{
						GamePiece jumpPiece = _board[targetRow][right];
						
						targetRow--;
						
						right++;
						if (targetRow >= 0 && targetRow <= 7 && right <= 7)
						{
							if (_board[targetRow][right] == null || _board[targetRow][right].equals(p))
							{
								if (!capturesClone.contains(jumpPiece))
								{
									capturesClone.add(jumpPiece);
									stepsXClone.add(x);
									stepsYClone.add(y);
									getJumpMoves(p, targetRow, right, capturesClone, moves, stepsXClone, stepsYClone);
									flag = false;
								}
							}
						}
						targetRow++; // clear
					}
				}
			}
			
		}
		if (p.getOwner().equals("black") || p.getType().equals("king"))
		{
			capturesClone = (ArrayList<GamePiece>)captures.clone();
			targetRow = x + 1;
			int left = y - 1;
			if (targetRow >= 0 && targetRow <= 7 && left >= 0)
			{
				if (_board[targetRow][left] != null)
				{
					if (!_board[targetRow][left].getOwner().equals(p.getOwner()))
					{
						GamePiece jumpPiece = _board[targetRow][left];
						targetRow++;
						
						left--;
						if (targetRow >= 0 && targetRow <= 7 && left >= 0)
						{
							if (_board[targetRow][left] == null || _board[targetRow][left].equals(p))
							{
								if (!capturesClone.contains(jumpPiece))
								{
									capturesClone.add(jumpPiece);
									stepsXClone.add(x);
									stepsYClone.add(y);
									getJumpMoves(p, targetRow, left, capturesClone, moves, stepsXClone, stepsYClone);
									flag = false;
								}
							}
						}
						
						targetRow--; // clear
					}
				}
			}
			capturesClone = (ArrayList<GamePiece>)captures.clone();
			stepsXClone = (ArrayList<Integer>)stepsX.clone();
			stepsYClone = (ArrayList<Integer>)stepsY.clone();
			
			int right = y + 1;
			if (targetRow >= 0 && targetRow <= 7 && right <= 7)
			{
				if (_board[targetRow][right] != null)
				{
					if (!_board[targetRow][right].getOwner().equals(p.getOwner()))
					{
						GamePiece jumpPiece = _board[targetRow][right];
						
						targetRow++;
						
						right++;
						if (targetRow >= 0 && targetRow <= 7 && right <= 7)
						{
							if (_board[targetRow][right] == null || _board[targetRow][right].equals(p))
							{
								if (!capturesClone.contains(jumpPiece))
								{
									capturesClone.add(jumpPiece);
									stepsXClone.add(x);
									stepsYClone.add(y);
									getJumpMoves(p, targetRow, right, capturesClone, moves, stepsXClone, stepsYClone);
									flag = false;
								}
							}
						}
					}
				}
			}
		}
		if (flag)
		{
			if (!captures.isEmpty())
			{
				moves.add(new Move(p, x, y, captures, stepsX, stepsY));
			}
		}
		return moves;
	}
	
	/*
	 * Takes a string representing a move, coming from the server, and turns it into a Move object
	 */
	public Move translateServerMove(String move)
	{
		
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		ArrayList<GamePiece> captures = new ArrayList<GamePiece>();
		for (int i = 0; i < move.length(); i++)
		{
			if (i % (_boardSize - 2) == 1)	//if the move is greater than the length of the board
											// then per checkers rules, the piece can now move in the opposite direction (and be 'kinged')
			{
				x.add(flipBoard(Integer.parseInt((move.substring(i, i + 1)))));
			}
			if (i % (_boardSize - 2) ==  _boardSize/3)
			{
				y.add(Integer.parseInt((move.substring(i, i + 1))));
			}
			
		}
		
		GamePiece origin = _board[x.get(0)][y.get(0)];
		int destX = x.get(x.size() - 1);
		int destY = y.get(y.size() - 1);
		
		for (int i = 0; i < x.size(); i++)
		{
			if (i + 1 < x.size())
			{
				if ((x.get(i) + x.get(i + 1)) % 2 == 0 || (y.get(i) + y.get(i + 1)) % 2 == 0)
				{
					captures.add(_board[(x.get(i) + x.get(i + 1)) / 2][(y.get(i) + y.get(i + 1)) / 2]);
					System.out.println(_board[(x.get(i) + x.get(i + 1)) / 2][(y.get(i) + y.get(i + 1)) / 2]);
				}
				
			}
		}
		
		return new Move(origin, destX, destY, captures);
	}
	
	/*
	 * If a jump sequence is very long, we will end up in a situation where the piece makes it to one side of the board and now must double back
	 * To handle this, we 'flip' the board
	 */
	public int flipBoard(int i)
	{
		return (_boardSize - 1) - i;
	}
	
	/*
	 * Simple utility function: just gets the black pieces minus the white pieces to determine the value of a board state
	 */
	public double utility()
	{
		return _blackPieces.size() - _whitePieces.size();
	}
	
	/*
	 * From a list of potential moves, determines the best move
	 */
	public Move bestMove()
	{
		CheckersState state = new CheckersState(this);
		ArrayList<Move> actions = (ArrayList<Move>)state.actions();
		Move tempMove;
		Double tempNum;
		Move best = actions.get(0);
		double current;
		if (state.player().equals("white"))
		{
			current = maxValue(state.result(best), _searchDepth);
			for (int i = 1; i < actions.size(); i++)
			{
				tempMove = actions.get(i);
				tempNum = maxValue(state.result(tempMove), _searchDepth);
				if (tempNum < current)
				{
					best = tempMove;
					current = tempNum;
				}
			}
		}
		else
		{
			//because our utility function is 
			current = minValue(state.result(best), _searchDepth);
			for (int i = 1; i < actions.size(); i++)
			{
				tempMove = actions.get(i);
				tempNum = minValue(state.result(tempMove), _searchDepth);
				if (tempNum > current)
				{
					best = tempMove;
					current = tempNum;
				}
			}
		}
		return best;
	}	
	
	/*
	 * Methods for determining the minimum/maximum score for a game state. Calls the minimax algorithm 	
	 */
	private double minValue(CheckersState state, int searchDepth)
	{
		return miniMax(state, searchDepth, false);
	}
	
	
	private double maxValue(CheckersState state, int searchDepth)
	{
		return miniMax(state, searchDepth, true);
	}
	
	/*
	 * Performs a minimax search across the possible tree of moves from the current state. Recursion is capped my the search depth
	 */
	private double miniMax(CheckersState state, int searchDepth, boolean isMax){
		
		//if the search depth is zero, we need only calculate the utility for the current board state.
		//This serves as our base case
		if (searchDepth == 0)
		{
			return state.utility();
		}
		ArrayList<Move> actions = (ArrayList<Move>)state.actions();

		if(actions.size() == 0)
		{
			if(state.player().equals("white"))
				return Double.POSITIVE_INFINITY;
			else
				return Double.NEGATIVE_INFINITY;
		}
		if(isMax){
			double max = Double.NEGATIVE_INFINITY;
			for (Move m : actions)
			{
				max = Math.max(max, minValue(state.result(m), searchDepth - 1));
			}
			return max;
		}else{
			double min = Double.POSITIVE_INFINITY;
			for (Move m : actions)
			{
				min = Math.min(min, maxValue(state.result(m), searchDepth - 1));
			}
			return min;
		}
	}
	
	/*
	 * Prints a board state as best we can with plain text - "draws" the board with the pieces labeled in the right places
	 */
	public void printState()
	{
		String s = "";
		for (int i = 0; i < _boardSize; i++)
		{
			for (int j = 0; j < _boardSize; j++)
			{
				if (_board[i][j] == null)
				{
					s += "-";
				}
				else if (_board[i][j].getOwner().equals("white"))
				{
					if (_board[i][j].getType().equals("king"))
					{
						s += "W";
					}
					else
					{
						s += "w";
					}
				}
				else if (_board[i][j].getOwner().equals("black"))
				{
					if (_board[i][j].getType().equals("king"))
					{
						s += "B";
					}
					else
					{
						s += "b";
					}
				}
			}
			s += "\n";
		}
		s += _player + "'s move";
		System.out.println(s);
		System.out.println("white pieces: " + _whitePieces);
		System.out.println("black pieces: " + _blackPieces);
		System.out.println();
	}
	
}
