package com.SpringMVC2.DAOI;

import entity.User;

public interface UserDAOI {

	boolean newUser(User user);

	boolean editUser(User user);

	boolean credCheck(String username, String password);

	//boolean removeUser(User user);
	
//	User findUserByName(String userName);
	
	
	
	
	
}
