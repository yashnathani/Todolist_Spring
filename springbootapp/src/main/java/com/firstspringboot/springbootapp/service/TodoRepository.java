package com.firstspringboot.springbootapp.service;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstspringboot.springbootapp.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer>  {

	List<Todo> findByUser(String user);
	
	
}
