package com.firstspringboot.springbootapp.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.firstspringboot.springbootapp.model.Todo;
import com.firstspringboot.springbootapp.service.TodoRepository;
import com.firstspringboot.springbootapp.service.TodoService;

@Controller

public class TodoController {

  @Autowired
	TodoService service;
  @Autowired  
  TodoRepository repository;
  
  @InitBinder
  public void initbinder(WebDataBinder binder) {
	  //Date - dd/MM/yyyy
	  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
  }
  
  
  
	
	
	@RequestMapping(value="/list-todos",method=RequestMethod.GET)
	public String showtodo( ModelMap model) {
		String name=getLoggedInUserName(model);
		model.put("todos",repository.findByUser(name));
		//model.put("todos", service.retrieveTodos(name));
		return "list-todos";
	}

	@RequestMapping(value="/add-todo",method=RequestMethod.GET)
	public String addtodo( ModelMap model) {
		model.addAttribute("todo",new Todo(0,getLoggedInUserName(model),"",new Date(),false));
				return "todo";
	}

	@RequestMapping(value="/delete-todo",method=RequestMethod.GET)
	public String deletetodo( ModelMap model,@RequestParam int id) {
		repository.deleteById(id);
		//service.deleteTodo(id);
				return "redirect:/list-todos";
	}
	
	
	

	@RequestMapping(value="/update-todo",method=RequestMethod.GET)
	public String updatetodo( ModelMap model,@RequestParam int id) {
		Todo todo=repository.findById(id).get();
		//Todo todo=service.retrieveTodo(id);
		model.put("todo",todo);
				return "todo";
	}
	
	
	
	@RequestMapping(value="/update-todo",method=RequestMethod.POST)
	public String showupdatetodo(@Valid Todo todo,BindingResult result,ModelMap model) {
		
		todo.setUser(getLoggedInUserName(model));
		if(result.hasErrors()) {
			return "todo";
		}
		
		
		repository.save(todo);
		
				return "redirect:/list-todos";
	}
	
	
	
	@RequestMapping(value="/add-todo",method=RequestMethod.POST)
	public String showaddtodo( ModelMap model,@Valid Todo todo,BindingResult result) {
		if(result.hasErrors()) {
			return "todo";
		}
		
		todo.setUser(getLoggedInUserName(model));
		repository.save(todo);
		
		//service.addTodo(getLoggedInUserName(model), todo.getDesc(), todo.getTargetDate(), false);
				return "redirect:/list-todos";
	}





	private String getLoggedInUserName(ModelMap model) {
		Object principal=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			return ((UserDetails)principal).getUsername();
		}
		return principal.toString();
	}
}