package com.emp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.emp.entities.Employee;
import com.emp.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService service;
	
	@GetMapping("/")
	public String home(Model model){
		List<Employee> emp=service.getAllEmp();
		model.addAttribute("emp",emp);

		return "index";
	}
	
	
	
	@GetMapping("/addemp")
	public String addempform() {
		
		return "add_emp";
	}
	
	@PostMapping("/register")
	public String empRegister(@ModelAttribute Employee emp,HttpSession session) 
	{
		
		System.out.println("Employee-->"+emp);
		
		service.addemp(emp);
		session.setAttribute("msg","Employee Data Added Successfully..." );
		
		
		return "redirect:/";
		
	}
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model m) {
		Employee e = service.getEMpById(id);
		m.addAttribute("emp", e);
		return "edit_emp";
	}

	@PostMapping("/update")
	public String updateEmp(@ModelAttribute Employee e, HttpSession session) {
		service.addemp(e);
		session.setAttribute("msg", "Emp Data Update Sucessfully..");
		return "redirect:/";
	}

	@GetMapping("/delete/{id}")
	public String deleteEMp(@PathVariable int id, HttpSession session) {

		service.deleteEMp(id);
		session.setAttribute("msg", "Emp Data Delete Sucessfully..");
		return "redirect:/";
	}

	@GetMapping("/page/{pageno}")
	public String findPaginated(@PathVariable int pageno, Model m) {

		Page<Employee> emplist = service.getEMpByPaginate(pageno, 2);
		m.addAttribute("emp", emplist);
		m.addAttribute("currentPage", pageno);
		m.addAttribute("totalPages", emplist.getTotalPages());
		m.addAttribute("totalItem", emplist.getTotalElements());
		return "index";
	}
}
