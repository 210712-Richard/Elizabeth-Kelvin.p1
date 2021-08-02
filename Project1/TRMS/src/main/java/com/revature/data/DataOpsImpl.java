package com.revature.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.revature.factory1.Log;
import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.forms.ReimbursalForm;

@Log
public class DataOpsImpl implements DataOps{
	
		private static String filename1 = "users.dat";
		private static String filename2 = "forms.dat";

		private static List<UserDef> users;
		private static List<ReimbursalForm> forms;

		static {
			DataSerializer<UserDef> ds = new DataSerializer<UserDef>();
			users = ds.readObjectsFromFile("users.dat");
			DataSerializer<ReimbursalForm> fs = new DataSerializer<ReimbursalForm>();
			forms = fs.readObjectsFromFile("forms.dat");
			
			// If no users exist create a default user
			if(users == null) {
				users = new ArrayList<UserDef>();
				UserDef ud = new UserDef(1000 + users.size(), "eliana", "1234", "eliana@gmail.com",
						                  UserType.EMPLOYEE, "samantha", 0.0, PaymentStatus.NONE);
				users.add(ud);
			}
		}
		
		public UserDef getUser(String name) {
			for(UserDef user : users) {
				if(user.getUsername().equals(name)) {
					return user;
				}
			}
			return null;
		}
		
		public UserDef setUser(String username, String password,
				               String email, UserType userType, String manager) {
			Integer EmpId = users.size() + 1000;
			UserDef ud = new UserDef(EmpId, username, password, email,
					                 userType, manager, 0.0, PaymentStatus.NONE);
			users.add(ud);
	        this.writeUsersToFile();
			return ud;
		}
		
		
		public  List<UserDef> getAllEmployees() {
			return users;
		}
		
		public  List<UserDef> getAllEmployeesOf(String manager) {
			return users;
		}
		
		public  List<UserDef> getAllEmployeesWith(PaymentStatus payStatus) {
			return users;
		}
		
		public  List<ReimbursalForm> getFormsOfAllEmployees() {
			return forms;
		}
		
		public  ReimbursalForm getFormsOf(Integer EmpId) {		
			for(ReimbursalForm r : forms) {
				if(r.getEmpId().equals(EmpId)) {
					return r;
				}
			}
			return null;
		} 

		public UserDef modBalance(UserDef u, Double Bal) {
					
			if (Bal > 0) {
				//u.setBalance(Bal);
			}
			else {
				//u.setBalance(0.0);
			}
			users.add(u);
	        this.writeUsersToFile();
			return u;
		}
		
		public void writeUsersToFile() {
			new DataSerializer<UserDef>().writeObjectsToFile(users, "users.dat");
		}

		public void writeMarksToFile() {
			new DataSerializer<ReimbursalForm>().writeObjectsToFile(forms, "forms.dat");
		}
}
