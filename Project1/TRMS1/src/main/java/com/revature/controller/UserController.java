package com.revature.controller;

import io.javalin.http.Context;

public interface UserController {
	
	void login(Context ctx);

	void register(Context ctx);

	void logout(Context ctx);
	
	void submitForm(Context ctx);
	
	void submitMark(Context ctx);
	
	void checkStatus(Context ctx);
	
	void manageApproval(Context ctx);

}
