package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import visual.Startup;

public class Comms extends Thread{
	private String in;
	private String out;
	
	public Comms() {
		in = "";
		out = "";
	}
	
	@Override
	public void run() {
		//first check if there is a server on lan that is running
		try (Socket socket = new Socket("localhost", Startup.portNum);) {
			System.out.println("Became the user");
			//server and client have connected, locked in to tango until one loses
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream output = socket.getOutputStream();
			PrintWriter write = new PrintWriter(output, true);
			//declare the writers and readers outside of the infinite loop
			while(true) {
				String packet = reader.readLine();
				int length = 1;
				do {
					length = out.length();
				}while(length != 0);
				out = packet;
				
				length = 0;
				do {
					length = in.length();
				}while(length == 0);
				
				write.println(in);
				in = "";
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Became the server");
			//server not found, we must become the server
			try (ServerSocket serverSocket = new ServerSocket(Startup.portNum)) {
				//here we stop the thread's actions bc all we have to do is wait for another client
				Socket socket = serverSocket.accept();
				OutputStream output = socket.getOutputStream();
				PrintWriter write = new PrintWriter(output, true);
				
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				//declare the writers and readers outside of the infinite loop
				while(true) {
					
					int length = 0;
					do {
						length = in.length();
					}while(length == 0);
					
					write.println(in);
					in = "";
					
					String packet = reader.readLine();
					length = 1;
					do {
						length = out.length();
					}while(length != 0);
					out = packet;
				}
			}catch (IOException ex) {}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void send(Packet p) {
		in = p.toString();
	}
	
	public Packet receive() { 
		Packet p = Packet.interpret(out);
		out = "";
		return p;
	}
}
