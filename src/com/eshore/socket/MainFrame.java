package com.eshore.socket;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class MainFrame {

	private JFrame mainWin;
	private JPanel centerPanel , southPanel;
	private JScrollPane scrollPane ;
	private JLabel msgLabel , nameLabel;
	private JButton sendMsgButton;
	private JTextField msgField , nameField;
	private JTextArea msgArea;
	private Socket client;
	private static final byte TAG_SEND_MSG = 1;
	private static final byte TAG_CLOSE = 2;
	
	
	
	public static void main(String[] args) throws IOException{
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.openWin(); 
		
		
	}
	
	public MainFrame() throws  IOException{
		client = new Socket("127.0.0.1" , 8080);
		
		mainWin = new JFrame("聊天");
		mainWin.setSize(500,400);
		mainWin.setLocationRelativeTo(null);
		mainWin.setLayout(new BorderLayout());
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		scrollPane = new JScrollPane(centerPanel);
		
		msgArea = new JTextArea();
		msgArea.setEnabled(false);
		msgArea.setForeground(Color.RED);
		centerPanel.add(msgArea,BorderLayout.CENTER);
		mainWin.add(scrollPane , BorderLayout.CENTER);
		
		southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		nameLabel = new JLabel("昵称:");
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(50 , 20));
		southPanel.add(nameLabel);
		southPanel.add(nameField);
		
		msgLabel = new JLabel("消息:");
		msgField = new JTextField();
		msgField.setPreferredSize(new Dimension(250 , 20));
		southPanel.add(msgLabel);
		southPanel.add(msgField);
		
		sendMsgButton = new JButton("发送");
		addButtonEvent();
		southPanel.add(sendMsgButton);
		
		mainWin.add(southPanel , BorderLayout.SOUTH);
		
	}
	
	public void openWin(){
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.setVisible(true);
		
		new SocketClientThread(this , client).start();
	}
	
	private void addButtonEvent(){
		sendMsgButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				
				String name = nameField.getText();
				String msg = msgField.getText();
				if(name.equals("") || name == null){
					name = "匿名";
				}
				if(msg == null){
					msg = "";
				}
				
				try {
					OutputStream os = client.getOutputStream();
					String sendMsg = name+" 说 ： "+msg + "\r\n";
					Integer len = sendMsg.getBytes().length;
					String lenStr = Integer.toHexString(len);
					while(lenStr.length() < 4){
						lenStr = "0"+lenStr;
					}
					System.out.println(lenStr+"   "+sendMsg.getBytes().length);
					os.write(MainFrame.TAG_SEND_MSG);
					os.write(MainFrame.hexToByte(lenStr));
					os.write(sendMsg.getBytes());
					os.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private static byte[] hexToByte(String hexStr){
		byte[] bts = new byte[hexStr.length()/2];
		for(int i =0 ; i < bts.length ; i++){
			String byteStr = hexStr.substring(i*2 , (i+1) * 2);
			byte b = Byte.parseByte(byteStr, 16);
			bts[i] = b;
		}
		
		return bts;
	}
	
	public static class SocketClientThread extends Thread{
		
		private MainFrame main;
		private Socket client;
		
		public SocketClientThread(MainFrame main , Socket client){
			this.main = main;
			this.client = client;
		}
		
		
		public void run(){
			try{
				InputStream is = client.getInputStream();
				byte[] readByte = new byte[1024];
				int readLen = 0;
				while( (readLen = is.read(readByte)) >0 ){
					main.msgArea.append(new String(readByte , 0 , readLen)+"\r\n");
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	}
}
