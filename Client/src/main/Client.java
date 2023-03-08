package main;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private static final int PORT = 4444;
	private Socket socket;
	
	public Client() {
		try {
			socket = new Socket("localhost", 4444);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
