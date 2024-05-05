package com.udemy.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	
	private static int userscount = 0;
	
	static {
		users.add(new User(++userscount,"Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++userscount,"Eve", LocalDate.now().minusYears(25)));
		users.add(new User(++userscount,"Jim", LocalDate.now().minusYears(20)));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User save(User user) {
		user.setId(++userscount);
		users.add(user);
		return user;
	}
	
	public User findOne(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id); 
		return users.stream().filter(predicate).findFirst().get();
	}
}
