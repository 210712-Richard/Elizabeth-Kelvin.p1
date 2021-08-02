package com.revature.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.factory1.BeanFactory;
import com.revature.service.Service;
import com.revature.service.ServiceImpl;
import com.revature.userdef.UserDef;
import com.revature.factory1.Log;
import io.javalin.http.Context;

@Log
public class UserControllerImpl implements UserController {
	
		private static Logger log = LogManager.getLogger(UserControllerImpl.class);
		private Service us = (Service) BeanFactory.getFactory1().get(Service.class, ServiceImpl.class);
	
		
		public void login(Context ctx) {
				UserDef u = ctx.bodyAsClass(UserDef.class);
				log.debug(ctx.body());
				log.trace(u);
				
				u = us.ValidateUser(u.getUsername(), u.getPassword());
				log.debug(u.getUsername());
				
				if(u != null) {
						ctx.sessionAttribute("currentUser", u);
						ctx.json(u);
						return;
				}
				ctx.status(401);
		}
			
		public void register(Context ctx) {
				log.debug(ctx.body());
				UserDef u = ctx.bodyAsClass(UserDef.class);
	
				if(us.ValidateUser(u.getUsername(), u.getPassword()) == null) {
						UserDef newUser = us.RegisterUser(u.getUsername(),
								          u.getPassword(), u.getEmail(),
								          u.getUserType(), u.getManager());
						ctx.status(201);
						ctx.json(newUser);
				} else {
					ctx.status(409);
					ctx.html("Username exists!.");
				}
		}
		
		public void submitForm(Context ctx) {
		 // only for employee else 403 not authorized
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			if(currentuser == null || !currentuser.getUsername().equals(username)) {
					ctx.status(403);
					return;
			} else {
			    if(us.CreateAppForm(username)) {
			    	ctx.html("Application submitted successfully.");
			    } else {
					ctx.status(409);
			    	ctx.html("Form is Incomplete. Please add all the required fields.");
			    }
			}
		}

		public void submitMark(Context ctx) {
			 // only for employee else 403 not authorized
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			if(currentuser == null || !currentuser.getUsername().equals(username)) {
					ctx.status(403);
					return;
			} else {
			    if(us.CreateMarksForm(username)) {
			    	ctx.html("Marks submitted successfully.");
			    } else {
					ctx.status(409);
			    	ctx.html("Marksheet is Incomplete. Please add all the marks.");
			    }
			}
		}
		
		public void checkStatus(Context ctx) {
			 // only for employee else 403 not authorized
			String reason = us.getStatus(u.getUsername());
		}
		
		
		public void manageApproval(Context ctx) {
			 // only for employee else 403 not authorized
			// based on usertype split the functions between manager/ Director/ HR
		}
		

		public void logout(Context ctx) {
			ctx.req.getSession().invalidate();
			ctx.status(204);
		}
	
}

	
	
	
	
	