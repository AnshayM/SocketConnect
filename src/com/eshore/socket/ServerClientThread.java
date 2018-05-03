package com.eshore.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientThread extends Thread {

	private Socket client;
	private Server server;
	private static final byte TAG_SEND_MSG = 1;
	private static final byte TAG_CLOSE = 2;
	
	public ServerClientThread(Server server , Socket client){
		this.server = server;
		this.client = client;
	}
	
	public void run(){
		try{
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
//			
//			String readStr = null;
//			while( (readStr = reader.readLine()) != null ){
//				server.sendAllClient(readStr);
//			}
			int tag = -1;
			while((tag = is.read()) > 0 ){
				byte[] dataLenByte = new byte[2];
				is.read(dataLenByte);
				int dataLen = byteToInt(dataLenByte);
				byte[] data = new byte[dataLen];
				System.out.println("tag : " + tag);
				System.out.println("dataLen : " + dataLen);
				if(tag == TAG_SEND_MSG){
					is.read(data , 0 , data.length);
					String msg = new String(data);
					System.out.println("msg :  " + msg);
					server.sendAllClient(msg);
				}
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static int byteToInt(byte[] dataLenByte){
		String byte0 = Integer.toHexString(dataLenByte[0]);
		String byte1 = Integer.toHexString(dataLenByte[1]);
		if(byte0.length() == 1){
			byte0 = "0" + byte0;
		}
		if(byte1.length() == 1){
			byte1 = "0" + byte1;
		}
		
		String byteStr = byte0 + byte1;
		return Integer.parseInt(byteStr, 16);
	}
	
}
