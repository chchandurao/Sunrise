package com.selenium.keywordfw.standard;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	public static boolean SendEmail(String list[]){
		String toAddress=list[2];
		String emailSubject=list[3];
		String emailBody=list[4];
	    String from = "cchinthalapudi@sunrisesw.co.uk";
	    String host = "172.120.1.10";
	    Properties properties = System.getProperties();
	    properties.setProperty("mail.smtp.host", host);
	    Session session = Session.getDefaultInstance(properties);
	    try{
	       MimeMessage message = new MimeMessage(session);
	       message.setFrom(new InternetAddress(from));
	       message.addRecipient(Message.RecipientType.TO,
	                                new InternetAddress(toAddress));
	       message.setSubject(emailSubject);
	       message.setText(emailBody);
	       Transport.send(message);
	       System.out.println("Sent message successfully....");
	    }catch (MessagingException mex) {
	       mex.printStackTrace();
	    }
	    return true;
	 }

}
