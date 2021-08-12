package com.revature.data;

import java.util.List;

import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.forms.ReimbursalForm;

public interface DataOps {

	
	UserDef addUser(String username, String password, String email, UserType userType, String manager);
	
	List<UserDef> getUsers();
	
	UserDef getUser(String username);
	
	UserDef saveUser(UserDef u);
	
	List<UserDef> getAllEmployeesOf(String manager);
	
	ReimbursalForm getFormsOf(Integer EmpId);
	
	ReimbursalForm saveForm(ReimbursalForm f);
	
	UserDef getUserWithId(Integer EmpId);
	
	/*UserDef getUserWithId(Integer ID);
	
	
	
	
	UserDef setUser(String username, String password, String email, UserType userType, String manager);
	
	//void writeUsersToFile();
	
	//void writeFormsToFile(); */
}