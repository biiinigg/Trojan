package com.dsc.publicmethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.DatatypeConverter;

public class eMail {
	static private String ac = "Y2NsZW1vbmlzYWdvb2Rkcmlua0BnbWFpbC5jb20=", pw = "YWFhMTIzNDU2Nzg5";
	public eMail(){
		String host = "smtp.gmail.com";
		int port = 587;
		ac = parse(ac);
		pw = parse(pw);
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", port);
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ac, pw);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ac));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ac));
			message.setSubject("IP");
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			pubFunc fun=new pubFunc();
			//System.out.println(fun.getIP());
			message.setText(sdf.format(cal.getTime()) + "Dear Lemon, \n\n "+fun.getIP());
			Transport transport = session.getTransport("smtp");
			transport.connect(host, port, ac, pw);

			Transport.send(message);
			transport.close();
			System.out.println("寄送email結束.");

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String print(String plain) {
		String encoded = "";
		try {
			byte[] cipherData = plain.getBytes("UTF-8");
			encoded = DatatypeConverter.printBase64Binary(cipherData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return encoded;
	}

	public static String parse(String str) {
		String decodedstr = "";
		try {
			byte[] decoded = DatatypeConverter.parseBase64Binary(str);
			decodedstr = new String(decoded, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return decodedstr;
	}
}
