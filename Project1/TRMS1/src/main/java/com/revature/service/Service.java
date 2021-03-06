package com.revature.service;


import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;


//import com.revature.beans.User;

public interface Service {

	UserDef ValidateUser(String name, String password);

	UserDef RegisterUser(String username, String password, String email, UserType userType, String manager);
	
	UserDef logout(UserDef ud);
	
	boolean processApprovel(UserDef currentuser);
	
}