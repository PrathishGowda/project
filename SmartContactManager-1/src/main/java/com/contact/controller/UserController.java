package com.contact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entites.Contact;
import com.contact.entites.User;
import com.contact.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addcommondata(Model m, Principal principal) {

		String username = principal.getName();
		System.out.println("USERNAME " + username);

		// get the user using username(Email)

		User user = userRepository.getUserByUserName(username);
		System.out.println("User " + user);

		m.addAttribute("user", user);
	}

	// home dashboard
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");

		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openAddContectForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	// processing add contact form
	
	@PostMapping("/process-contact")
	public String processcontact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,HttpSession session)//principal user name fetch
	{
		
		
		try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);

				
		//processing and uploading file..
		if(file.isEmpty())
		{
			//if file is empty then try  our message
			
		System.out.println("file is empty");
		contact.setImage("contact.png");
		
		}
		else {
			//file the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			File savefile=new ClassPathResource("static/image").getFile();
			
			Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is Uploaded");
		}
		
		
		contact.setUser(user);
		user.getContacts().add(contact);

		this.userRepository.save(user);

		System.out.println("Data " + contact);

		System.out.println("added to data base");
		
		//message success
		session.setAttribute("message",new Message("Your contact is added","success"));
		
		}
		catch (Exception e) {

			System.out.println("Error :"+e.getMessage());
			e.printStackTrace();
			
			//error messages
			session.setAttribute("message",new Message("some went wrong ", "danger"));
			
		}
		return "normal/add_contact_form";
	}

	//show contacts handler
	//per page=5[n]
	//current page=0[page]
	@GetMapping("/show-contacts/{page}")
	public String showcontact(@PathVariable("page")Integer page ,Model m,Principal principal){
		m.addAttribute("title","show all contacts here");
		//contacts list show here
		String userName=principal.getName();
		
		User user=this.userRepository.getUserByUserName(userName);
		//current page
		//contact per page-5
		Pageable pageable=PageRequest.of(page, 5);//set page in pagination
		Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentpage",page); 
		m.addAttribute("totalpages",contacts.getTotalPages());
		return "normal/show_contacts";
		
	}
	
	//show particular contact details
	
	@RequestMapping("/{cid}/contact")
	public String showParticularContact(@PathVariable("cid")Integer cid,Model model,Principal principal) {
		System.out.println("cid ="+cid);
		Optional<Contact> contactOptional=this.contactRepository.findById(cid);
		
		Contact contact=contactOptional.get();
		
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact",contact);
			model.addAttribute("title",contact.getName());
		}
		return "normal/contact_detail";
	}
	//delete contact handler
	@GetMapping("/delete/{cid}")
	@Transactional
	public String deletecontact(@PathVariable("cid") Integer cid,Model model,HttpSession session,Principal principal) {
		
		Contact contact =this.contactRepository.findById(cid).get();
		
		
		//delete old photo
//		contact.setUser(null);
//		this.contactRepository.delete(contact);
		
		User user=this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		this.userRepository.save(user);
		
		System.out.println("deleted");
		session.setAttribute("message",new Message("Contact Deleted Successfully","success"));
		return "redirect:/user/show-contacts/0";
		
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateform(@PathVariable("cid")Integer cid, Model model) {
		
		model.addAttribute("title","update Contact");
		
		Contact contact=this.contactRepository.findById(cid).get();
		
		model.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
	
	
	//update contact handler
	@RequestMapping(value = "/process-update",method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model m,HttpSession session ,Principal principal) {
		
		try {
			//old contact details
			
				Contact oldContactdetail=this.contactRepository.findById(contact.getCid()).get();
			
			if(!file.isEmpty())
			{
				//file working rewrite
				
				//delete old photo
				
				File deleteFile=new ClassPathResource("static/image").getFile();
				File file1=new File(deleteFile,oldContactdetail.getImage());
				file1.delete();
				
				//update new photo
				
				File savefile=new ClassPathResource("static/image").getFile();
				
				Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldContactdetail.getImage());
			}
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			
			session.setAttribute("message",new Message("your contact is Updated","success"));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Contact name"+contact.getName());
		System.out.println("Contact Id"+contact.getCid());
		
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	
	//your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title","Profile Image");
		return "normal/profile";
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings() { 
		
		return "/normal/settings";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword")String oldpassword,@RequestParam("newpassword")String newpassword,Principal principal,HttpSession session) {
		
		System.out.println("old password "+oldpassword);
		System.out.println("new password "+newpassword);
		
		String username=principal.getName();
		User currentuser=this.userRepository.getUserByUserName(username);
		System.out.println("currentuser =>"+currentuser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldpassword,currentuser.getPassword()))
		{
			//change the password
			currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userRepository.save(currentuser);
			session.setAttribute("message",new Message("your password is successfully changed ", "success"));
		
		}
		else {
			//password not matched
			//error ...
			session.setAttribute("message",new Message("please enter current old password","danger"));
		
			return "redirect:/user/settings";
		}
		
		
		return "redirect:/user/index";
	}
}
