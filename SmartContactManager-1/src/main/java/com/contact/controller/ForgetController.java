package com.contact.controller;

import java.util.Random;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.UserRepository;
import com.contact.entites.User;
import com.contact.service.EmailService;

@Controller
public class ForgetController {

	Random random = new Random(1000);// inbuilt method

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	// email id form open handler
	@RequestMapping("/forget")
	public String openEmailForm() {

		return "forget_email_form";
	}

	// send OTP

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		System.out.println("Email Id :" + email);

		// Generating OTP of a 4 digit

		int otp = random.nextInt(9999);// its 4 digit otp print 9999 more digit increase one position put 99999
		System.out.println("OTP :" + otp);

		// write code to Email id
		String subject = "OTP from Smart Contact Manager ";
		String message = "" + "<div style='border:1px solid #e2e2e2; pading:20px'>" + "<h1>" + "OTP is " + "<b>" + otp
				+ "</h1>" + "</div>";
		String to = email;

		boolean flag = this.emailService.sendemail(subject, message, to);

		if (flag) {

			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";

		} else {
			session.setAttribute("message", "please check your email id");

			return "forget_email_form";
		}

	}

	// verify otp
	@PostMapping("/verify-otp")
	public String verifyotp(@RequestParam("otp") int otp, HttpSession session) {
		int myotp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myotp == otp) {
			// password change form
			User user = this.userRepository.getUserByUserName(email);
			if (user == null) {
				// send error message
				session.setAttribute("message", "User does not exist with this email id");

				return "forget_email_form";
			}

			else {
				// send change password form
			}

			return "password_change_form";

		} else {
			session.setAttribute("message", "you have entered wrong OTP");
			return "verify_otp";

		}
	}
	
	
	//change-password
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("newpassword")String newpassword,HttpSession session) {
		
		String email = (String) session.getAttribute("email");
		User user=this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		
		//session.setAttribute("message", "you have entered wrong OTP");
		return "redirect:/signin?change=password changed successfully..";

		
		//ka09eh5290
		
	}

	
}
