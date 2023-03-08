package main;

import java.io.IOException;
import java.net.*;

public class Server {

	private static final int PORT = 4444;
	
	private Socket socket;
	private ServerSocket serverSocket;
	
	public Server() {
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server online\nWaiting...");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server error");
		}
		
		while (true) {
			try {
				socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket);
				st.start();
			} catch (Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
	}
}
