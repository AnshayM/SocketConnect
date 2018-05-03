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
import java.net.URL;
import java.net.URLConnection;

public class HttpMain {

	public static void main(String[] args) throws IOException{
		URL url = new URL("http://localhost:8080/test/testHttp");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.connect();
		OutputStream os = connection.getOutputStream();
		os.write("name=abc&age=12".getBytes());
		InputStream is = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is , "UTF-8"));
		String readMsg = null;
		while( (readMsg = reader.readLine()) != null ){
			System.out.println(readMsg);
		}
		
		
	}
	
	
	public static void httpServer() throws IOException{
		ServerSocket ss = new ServerSocket(8888);
		while(true){
			Socket s = ss.accept();
			BufferedReader reader = new BufferedReader( new InputStreamReader( s.getInputStream() ) );
			String readStr = null;
			while( (readStr = reader.readLine()) != null ){
				System.out.println(readStr);
				if(readStr.length() == 0){
					break;
				}
			}
			
			PrintWriter writer = new PrintWriter( new OutputStreamWriter( s.getOutputStream() ) );
		
			writer.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答  
			writer.println("Content-Type:text/html;charset=UTF-8");  
			writer.println();// 根据 HTTP 协议, 空行将结束头信息  
			
			writer.println("<h1>你好</h1></br>");
			writer.println("<label>用户名</label><input type='text'></br>");
			writer.println("<label>密码</label><input type='password'></br>");
			writer.println("<input type='button' value='登录'>");
			writer.flush();
			writer.close();
			s.close();
		}
		
	}

}
