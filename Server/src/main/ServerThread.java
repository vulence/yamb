package main;

import java.net.*;

public class ServerThread extends Thread {
	
	private Socket s = null;
	
	public ServerThread(Socket s) {
		this.s = s;
		System.out.println("New client joined");
	}
	
	public void run() {
		while (true) {}
	}
}
