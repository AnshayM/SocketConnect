package com.eshore.socket;

/**
 * @Author Anshay
 * @Date 2018年5月3日
 * @Explain 客户端
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws IOException {
		// 为避免端口冲突，所以这里使用基本不常用的8888端口
		Socket s = new Socket("127.0.0.1", 8888);
		OutputStream os = s.getOutputStream();
		new ClientThread(s).start();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String msg = scanner.nextLine() + "\r\n";
			os.write(msg.getBytes());

		}
	}

	public static void client() throws IOException {
		Socket s = new Socket("127.0.0.1", 8888);
		OutputStream os = s.getOutputStream();
		InputStream is = s.getInputStream();
		String msg = "张三,你好\r\n";
		os.write(msg.getBytes());
		s.shutdownOutput();

		byte[] readByte = new byte[1024];
		int readLen = 0;
		while ((readLen = is.read(readByte)) > 0) {
			System.out.print(new String(readByte, 0, readLen));
		}

		os.close();
		is.close();
		s.close();
	}

	public static class ClientThread extends Thread {

		private Socket s;

		public ClientThread(Socket s) {
			this.s = s;
		}

		public void run() {
			try {
				InputStream is = s.getInputStream();
				byte[] readByte = new byte[1024];
				int readLen = 0;
				while ((readLen = is.read(readByte)) > 0) {
					System.out.println("接收数据：" + new String(readByte, 0, readLen));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
