package com.revature.data;

import java.util.List;

import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.forms.ReimbursalForm;

public interface DataOps {

	UserDef setUser(String username, String password,
            String email, UserType userType, String manager);

	UserDef getUser(String username);
	
	UserDef modBalance(UserDef u, Double Bal);

	List<UserDef> getAllEmployees();
	
	List<UserDef> getAllEmployeesOf(String manager);
	
	List<UserDef> getAllEmployeesWith(PaymentStatus payStatus);
	
	List<ReimbursalForm> getFormsOfAllEmployees();
	
	ReimbursalForm getFormsOf(Integer EmpId);
	
	void writeUsersToFile();
	
	void writeMarksToFile(); 

}