package com.contact.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	
	public boolean sendemail(String subject, String message, String to) {

		String from = "prathishprathik@gmail.com";

		// rest of the code

		boolean f = false;

		// variable for gmail new host
		String host = "smtp.gmail.com";

		// get the system properties
		Properties properties = System.getProperties();
		System.out.println("Properties :" + properties);

		// setting important information to properties object

		// host set
		properties.put("mail.smtp.host", host);// key and value pair
		properties.put("mail.smtp.port", "465");// port number
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// step 1:to get the session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("prathishprathik@gmail.com", "mirslsatxocziaru");// password is you
																									// google created
																									// password

			}

		});
		session.setDebug(true);
		

		// step 2: compose the message[text,multi media]
		
		MimeMessage m = new MimeMessage(session);

		try {
			// from email
			m.setFrom(from);

			// adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to message
			m.setSubject(subject);

			// adding text to message
			m.setText(message);
			m.setContent(message,"text/html");
			

			// send
			
			// step 3:send message using transport class
			Transport.send(m);
			System.out.println("  sent success ....");

			f = true;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return f;
	}


	
}
