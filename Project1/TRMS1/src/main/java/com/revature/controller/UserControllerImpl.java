package com.revature.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.factory1.BeanFactory;
import com.revature.service.Service;
import com.revature.service.ServiceImpl;
import com.revature.service.EmpService;
import com.revature.service.EmpServiceImpl;
import com.revature.userdef.UserDef;
import com.revature.factory1.Log;
import io.javalin.http.Context;
import com.revature.forms.*;

@Log
public class UserControllerImpl implements UserController {
	
		private static Logger log = LogManager.getLogger(UserControllerImpl.class);
		private Service us = (Service) BeanFactory.getFactory1().get(Service.class, ServiceImpl.class);
		private EmpService es = (EmpService) BeanFactory.getFactory1().get(EmpService.class, EmpServiceImpl.class);

		
		
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
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			log.trace(ctx.body());
			ReimbursalForm rf = ctx.bodyAsClass(ReimbursalForm.class);
			if(currentuser == null || !currentuser.getUsername().equals(username)) {
					ctx.status(403);
					return;
			}
			else if (currentuser.getEmpId() != rf.getEmpId()){
				// Safety concern, but in case user forgot empID you dont want to get stuck.
				// Later implement with forgot Emp ID.
				ctx.html("EMP ID mismatch. Your EmpId is :" + currentuser.getEmpId().toString());
			}
			else {
				// marksheet , message  and time is null. time = null as it is not handled. 
			    if(es.CreateAppForm(rf.getEmpId(), rf.getPercent(), rf.getPaidTuition(),
			    					rf.getManager(), null, rf.getDescription(),
			    					rf.getcourseType(), null, null )) {
			    	ctx.html("Marks submitted successfully.");
			    } else {
					ctx.status(409);
			    	ctx.html("Marksheet is Incomplete. Please add all the marks.");
			    }
			}
		}

		
		public void submitMark(Context ctx) {
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			log.trace(ctx.body());
			MarkSheet mks = ctx.bodyAsClass(MarkSheet.class);
			if(currentuser == null || !currentuser.getUsername().equals(username)) {
					ctx.status(403);
					return;
			}
			else if (currentuser.getEmpId() != mks.getEmpId()){
				// Safety concern, but in case user forgot empID you dont want to get stuck.
				// Later implement with forgot Emp ID.
				ctx.html("EMP ID mismatch. Your EmpId is :" + currentuser.getEmpId().toString());
			}
			else {
			    if(es.CreateMarksForm(mks.getEmpId(), mks.getSubA(),
			    		              mks.getSubB(), mks.getSubC(), mks.getSubD())) {
			    	ctx.html("Marks submitted successfully.");
			    } else {
					ctx.status(409);
			    	ctx.html("Marksheet is Incomplete. Please add all the marks.");
			    }
			}
		}
		
		
		public void checkStatus(Context ctx) {
			 // only for employee else 403 not authorized
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			String reason = es.GetStatus(username);
			ctx.html(reason);
		}
		
		
		public void manageApproval(Context ctx) {
			UserDef currentuser = ctx.sessionAttribute("currentUser");
			String username = ctx.pathParam("username");
			if(currentuser == null || !currentuser.getUsername().equals(username)) {
					ctx.status(403);
					return;
			} else {
				us.processApprovel(currentuser);
			}
		}
		

		public void logout(Context ctx) {
			ctx.req.getSession().invalidate();
			ctx.status(204);
		}
	
}

	
	
	
	
	