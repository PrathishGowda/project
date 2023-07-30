package com.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.UserRepository;
import com.contact.entites.User;
import com.contact.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home- Smart Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "Home- Smart Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Home- Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	// this handler for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registeruser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.println("you have not agreed the terms and conditions");
				throw new Exception("you have not agreed the terms and conditions");
			}

			if (result1.hasErrors()) {
				System.out.println("error" + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			System.out.println("agreement =" + agreement);
			System.out.println("User" + user);
			User result = userRepository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered  ", "alert-success"));

			return "signup";

		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong " + e.getMessage(), "alert-danger"));// danger
			
			return "signup";
		}

	}
	//handler for custom login
	
			@GetMapping("/signin")
			public String CustomLogin(Model model){
				model.addAttribute("title","Login Page");
				
				return "login"; 
			}
		

//	@Autowired
//	private UserRepository repository;
//	
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		
//		User u=new User();
//		u.setName("Prathish");
//		u.setRole("java developer");
//		return "first pages ";
//	}
}
