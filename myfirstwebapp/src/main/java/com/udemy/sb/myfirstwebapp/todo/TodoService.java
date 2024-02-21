package com.udemy.sb.myfirstwebapp.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
@Service
public class TodoService {
	private static List<Todo> todos = new ArrayList<>();
	
	private static int todosCount = 0;
	
	static {
		todos.add(new Todo(++todosCount, "in28mins", "Get AWS Certified", 
				LocalDate.now().plusYears(1),false));
		todos.add(new Todo(++todosCount, "in28mins", "learn devOps", 
				LocalDate.now().plusYears(2), false));
		todos.add(new Todo(++todosCount, "in28mins", "learn Java", 
				LocalDate.now().plusYears(3), false));

	}
	
	public List<Todo> findByUsername(String username){
		return todos;
	}
	
	public void addTodo(String usename, String description, LocalDate targetDate, boolean done) {
		Todo todo = new Todo(++todosCount,usename,description,targetDate,done);
		todos.add(todo);
	}
	
	public void deleteById(int id) {
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		todos.removeIf(predicate);
	}

	public Todo findById(int id) {
		// TODO Auto-generated method stub
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		Todo todo = todos.stream().filter(predicate).findFirst().get();
		return todo;
	}

	public void updateTodo(@Valid Todo todo) {
		// TODO Auto-generated method stub
		deleteById(todo.getId());
		todos.add(todo);
		
	}
}
