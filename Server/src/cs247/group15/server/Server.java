package cs247.group15.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs247.group15.data.Constants;

public class Server {
	
	public static void main(String[] args)
	{
		try {
			ServerSocket serverSocket = new ServerSocket(Constants.serverPort);
			System.out.println("Server has started.");
			while (true)
			{
				try
				{
					Socket clientSocket = serverSocket.accept();
					System.out.println("clientSocket has been made.");
					new DataSender(clientSocket).start();
					clientSocket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
