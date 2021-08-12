package com.revature.service;

import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.service.ManagerService;
import com.revature.service.ManagerServiceImpl;
import com.revature.service.HRService;
import com.revature.service.HRServiceImpl;

import com.revature.factory1.BeanFactory;
import com.revature.factory1.Log;

@Log
public class ServiceImpl implements Service {
	
		private Logger log = LogManager.getLogger(ServiceImpl.class);
		public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);
		public ManagerService mng = (ManagerService) BeanFactory.getFactory1().get(ManagerService.class, ManagerServiceImpl.class);
		public HRService hro = (HRService) BeanFactory.getFactory1().get(HRService.class, HRServiceImpl.class);

		
		public UserDef ValidateUser(String name, String password) {
			UserDef u = dop.getUser(name);
			log.trace(password + " -- " + u.getPassword());
			if (u == null) {
				log.trace("User not registered!");
				return null;
			}
			else if (!u.getPassword().equals(password) ) {
				log.trace("Username and password not matching!");
				return null;
			}
			else
				return u;
		}
		
		
		public UserDef RegisterUser(String username, String password,
				                    String email, UserType userType, String manager) {
			UserDef u = dop.addUser(username, password, email, userType, manager);
			return u;
		}
		
		
		
        /* based on the user split the task between managers or HR */
		public boolean processApprovel(UserDef currentuser) {
			if (currentuser.getUserType() == UserType.HR) {
				return hro.setApproval(currentuser);
			}
			else if ((currentuser.getUserType() == UserType.MANAGER) ||
					 (currentuser.getUserType() == UserType.DIRECTOR) ) {
				return mng.setApproval(currentuser);
			}
			return true;
		}
		
		
		
		public UserDef logout(UserDef ud)
		{
			return null;
		}
		
}

