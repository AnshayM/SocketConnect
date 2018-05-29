package com.eshore.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String path = Main.class.getResource("/").getPath();
		String path2 = Main.class.getResource("").getPath();
		System.out.println(path);
		System.out.println(path2);
		
		Properties pps = new Properties();
		pps.load(new FileInputStream(path + "/config.properties"));
		String serverPort = pps.getProperty("server_port");
		System.out.println(serverPort);
		
	}

}
