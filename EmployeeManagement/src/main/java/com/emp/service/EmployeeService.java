package com.emp.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.emp.entities.Employee;
import com.emp.repository.EmployeeRepository;


@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;
	
	
	public void addemp(Employee emp)
	{
		repository.save(emp);
	}
	
	public List<Employee> getAllEmp() {
		return repository.findAll();
	}

	public Employee getEMpById(int id) {
		Optional<Employee> e = repository.findById(id);
		if (e.isPresent()) {
			return e.get();
		}
		return null;
	}

	public void deleteEMp(int id) {
		repository.deleteById(id);
	}

	public Page<Employee> getEMpByPaginate(int currentPage, int size) {
		PageRequest p =PageRequest.of(currentPage, size);
		return repository.findAll(p);
	}
	
	
}
