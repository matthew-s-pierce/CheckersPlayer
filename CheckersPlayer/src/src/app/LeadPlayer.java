package src.app;

// RmCheckersClient.java is a client that interacts with Sam, a checkers 
// server. It is designed to communicate with the server
// in a minimal way (not be elegant ;). 

// Given the correct machine name and port for the server, a user id, and a 
// password (_machine, _port, _user, and _password in the code), running 
// this program will initiate connection and start a game with the default 
// player. (the _machine and _port values used should be correct, but check
// the protocol document.)

import java.io.*;
import java.net.*;
import java.util.List;

public class LeadPlayer
{
	
	private final static String _user = "id.here";
	private final static String _password = "password.to.access.server";
	private final static String _opponent = "id.of.opponent";
	private final static String _machine = "name.of.machine.here";
	private static int _port = 9999;
	private static Socket _socket;
	private static PrintWriter _out = null;
	private static BufferedReader _in = null;
	
	private String _gameID;
	private String _myColor;
	
	public LeadPlayer()
	{
		try {
			getSocket();
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
	}
	
	public Socket getSocket() throws IOException
	{
		_socket = src.utils.SocketConnector.openSocket(_user, _password, _opponent, _machine, _port, _out, _in);
		_out = new PrintWriter(_socket.getOutputStream(), true);
		_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		return _socket;
	}
	
	public PrintWriter getOut()
	{
		return _out;
	}
	
	public BufferedReader getIn()
	{
		return _in;
	}
	
	public void setGameID(String id)
	{
		_gameID = id;
	}
	
	public String getGameID()
	{
		return _gameID;
	}
	
	public void setColor(String color)
	{
		_myColor = color;
	}
	
	public String getColor()
	{
		return _myColor;
	}
	
	public static void main(String[] argv)
	{
		String readMessage;
		LeadPlayer myClient = new LeadPlayer();
		CheckersState state = new CheckersState(3);
		try
		{
			myClient.readAndEcho(); // start message
			myClient.readAndEcho(); // ID query
			myClient.writeMessageAndEcho(_user); // user ID
			
			myClient.readAndEcho(); // password query
			myClient.writeMessage(_password); // password
			
			myClient.readAndEcho(); // opponent query
			myClient.writeMessageAndEcho(_opponent); // opponent
			
			myClient.setGameID(myClient.readAndEcho().substring(5, 9)); // game
			myClient.setColor(myClient.readAndEcho().substring(6, 11)); // color
			
			state.setPlayer(myClient.getColor());
			
			System.out.println("I am playing as " + myClient.getColor() + " in game number " + myClient.getGameID());
			
			String opMove;
			
			if (myClient.getColor().equals("White"))
			{
				readMessage = myClient.readAndEcho();
				opMove = readMessage.substring(11);
				state = state.result(state.translateServerMove(opMove));
			}
			// depends on color--a black move if i am white, Move:Black:i:j
			// otherwise a query to move, ?Move(time):
			
			Move m;
			List<Move> moves;
			readMessage = myClient.readAndEcho(); // move query
			while (readMessage.charAt(0) == '?')
			{
				moves = state.actions();
				System.out.println(moves);
				m = (Move) moves.get(moves.size()-1);
				m = state.bestMove();
				myClient.writeMessageAndEcho(m.toString());
				state = state.result(m);
				state.printState();
				readMessage = myClient.readAndEcho(); // our move
				readMessage = myClient.readAndEcho(); // opponent's move or
														// result
				if (!readMessage.substring(0, 6).equals("Result"))
				{
					opMove = readMessage.substring(11);
					state = state.result(state.translateServerMove(opMove));
					state.printState();
					readMessage = myClient.readAndEcho(); // move query
				}
			}
			
			
			myClient.getSocket().close();
		}
		catch (IOException e)
		{
			System.out.println("Failed in read/close");
			System.exit(1);
		}
	}
	
	/*
	 * Reads a move and echos it back to the console
	 */
	public String readAndEcho() throws IOException
	{
		String readMessage = _in.readLine();
		System.out.println("read: " + readMessage);
		return readMessage;
	}
	
	public void writeMessage(String message) throws IOException
	{
		_out.print(message + "\r\n");
		_out.flush();
	}
	
	public void writeMessageAndEcho(String message) throws IOException
	{
		_out.print(message + "\r\n");
		_out.flush();
		System.out.println("sent: " + message);
	}
	
}