package com.eshore.socket;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Server {
	
	private ServerSocket serverSocket;
	private List<Socket> clientList;

	public static void main(String[] args) throws IOException{
		new Server().startServer();
	}
	
	
	public Server() throws IOException{
		
		String path = this.getClass().getResource("/").getPath();
		Properties pps = new Properties();
		pps.load(new FileInputStream(path + "/config.properties"));
		String serverPort = pps.getProperty("server_port");
		
		Integer port = Integer.parseInt(serverPort);
		
		serverSocket = new ServerSocket(8888);
		clientList = new ArrayList<Socket>();
	}
	
	
	public void startServer() throws IOException{
		while(true){
			Socket client = serverSocket.accept();
			clientList.add(client);
			new ServerClientThread(this , client).start();
		}
	}
	
	public void sendAllClient(String msg){
		
		for(int i =0 ; i< clientList.size() ; i++){
			Socket client = clientList.get(i);
			try{
				OutputStream os = client.getOutputStream();
				os.write(msg.getBytes());
				os.flush();
			}catch(IOException e){
				clientList.remove(client);
				i--;
			}
		}
		
	}
	
	
}
