package src.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnector {
	
	SocketConnector(){
	}
	
	/*
	 * Opens connection to Checkers server
	 */
	public static Socket openSocket(String user, String password, String opponent, String machine, int port, PrintWriter out, BufferedReader in)
	{
		Socket socket = null;
		try
		{
			socket = new Socket(machine, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (UnknownHostException e)
		{
			System.out.println("Unknown host: " + machine);
			System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("No I/O");
			System.exit(1);
		}
		return socket;
	}
}
