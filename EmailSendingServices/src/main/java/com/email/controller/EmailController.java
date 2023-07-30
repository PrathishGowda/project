package com.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.email.model.EmailRequest;
import com.email.service.EmailService;

@RestController
public class EmailController {
	
	
	@Autowired
	private EmailService emailService;

	@RequestMapping("/welcome")
	public String welcome() {
		
		return "hello this is email api";
	}
	
	
	//api to send email
	
	@RequestMapping(value = "/sendemail" ,method = RequestMethod.POST)
	public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request)
	{
		
		System.out.println(request);
		boolean  result=this.emailService.sendemail(request.getSubject(),request.getMessage(),request.getto());
		if(result)
		{
			return ResponseEntity.ok("Email is sent Successfully..");
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email Not Send");
		}
	}
}
