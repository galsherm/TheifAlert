package application;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {

	// File Name SendEmail.java

	public static int sendmail(String picturePath,String recepient,String messageSubject,String messageContent) throws MessagingException {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "*");
		String myAccountEmail = "your username@gmail.com";		//write the sender email
		String Password = "Your password";								//write the sender password				
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, Password);

			}
		});

		Message message = prepareMessage(picturePath,session, myAccountEmail, recepient, messageSubject, messageContent);
		Transport.send(message);
		System.out.println("Message succesfuly");
		return 1;
	}

	public static Message prepareMessage(String picturePath,Session session, String myAccountEmail, String recepient,String messageSubject,String messageContent) {
		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			//message.setSubject("my first email-myfuel");			//mail Subject
			//message.setText("hey Station manager From Myfuel System");					//mail SetEmailText
			message.setSubject(messageSubject);			//mail Subject
			

	         // This mail has 2 part, the BODY and the embedded image
	         MimeMultipart multipart = new MimeMultipart("related");

	         // first part (the html)
	         BodyPart messageBodyPart = new MimeBodyPart();
	         String htmlText = "<H1>! This is the Theif </H1><img src=\"cid:image\">";
	         messageBodyPart.setContent(htmlText, "text/html");
	         // add it
	         multipart.addBodyPart(messageBodyPart);

	         // second part (the image)
	         messageBodyPart = new MimeBodyPart();
	         DataSource fds = new FileDataSource(
	        		 picturePath +"\\pic.JPG");

	         messageBodyPart.setDataHandler(new DataHandler(fds));
	         messageBodyPart.setHeader("Content-ID", "<image>");
	        
	         // add image to the multipart
	         multipart.addBodyPart(messageBodyPart);

	         // put everything together
	         message.setContent(multipart);
	         
			return message;
			
			//message.setText(messageContent);					//mail SetEmailText
			
			

		} catch (Exception ex) {
			Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}