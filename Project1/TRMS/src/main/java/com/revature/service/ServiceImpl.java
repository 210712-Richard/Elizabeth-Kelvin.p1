package com.revature.service;

import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.userdef.PaymentStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.factory1.BeanFactory;
import com.revature.factory1.Log;

@Log
public class ServiceImpl implements Service {
	
		private Logger log = LogManager.getLogger(ServiceImpl.class);
		public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);

		
		public UserDef ValidateUser(String name, String password) {
			UserDef u = dop.getUser(name);
			if (u == null) {
				log.trace("User not registered!");
				return null;
			}
			else if (u.getPassword() != password ) {
				log.trace("Username and password not matching!");
				return null;
			}
			else
				return u;
		}
		
		
		public UserDef RegisterUser(String username, String password,
				                    String email, UserType userType, String manager) {
			UserDef u = dop.setUser(username, password, email, userType, manager);
			return u;
		}
		
		
		public PaymentStatus GetStatus(String username) {
			// more actions based on the switch condition of payment type to be implemented
			UserDef u = dop.getUser(username);
			if (u == null) {
				log.trace("User not registered!");
				return null;
			}
			log.trace("Status:" + u.getPaymentStatus());
			return u.getPaymentStatus();
		}
		
		
		public boolean CreateAppForm(String username) {
			UserDef u = dop.getUser(username);
			if (u == null) {
				log.trace("User not registered!");
				return false;
			}
			log.trace("Status:" + u.getPaymentStatus());
			return true;
		}
		
		public boolean CreateMarksForm(String username) {
			UserDef u = dop.getUser(username);
			if (u == null) {
				log.trace("User not registered!");
				return false;
			}
			log.trace("Status:" + u.getPaymentStatus());
			return true;
		}
		
		/*
		     Employee operations
		     1. create the form for reimbursement -> create diff class
		     2. upload the marksheet
		     3. upload the form , optional
		     3. check payment status
		     4. update tution amount
		     
		     when employee submit , change status to waiting
		 */
		
		/*
		    Manager
		    pre: create valid list of employees or specify the range 1-100 etc
		    
		    get the list of employees in pending state
		    
		    1. validate empid in the form 
		    2. check if the form is submitted 
		    3. pull the forms of empid 
		    4. verify results 
		    5. forward to Dean with form  
		 */
		
		/*
		  Director operations
		  get the employees in 
		  1. verify the empid is valid
		  2. verify the marks
		  3. verify the fees with university
		  4. approve or reject
		  5. forward to HR
		 */
		
		/*
		  HR operations
		  1. check if approval is there
		  2. check if employee is valid
		  3. reimburse the amount
		  4. update the payemnet status 
		 */
		
		public UserDef createAccnt(UserDef ud, String accntType)
		{
			UserDef u = uop.modAccnt(ud, accntType);
			return u;
		}
		
		
		public UserDef logout(UserDef ud, String accntType)
		{
			return null;
		}
		
}

