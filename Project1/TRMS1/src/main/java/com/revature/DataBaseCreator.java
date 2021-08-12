package com.revature;

import java.time.LocalDate;

import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.util.CassandraUtil;

public class DataBaseCreator {
	private static DataOps dop = new DataOpsImpl();
	
	
	public static void dropTables() {
		StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS Employees;");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		
		sb = new StringBuilder("DROP TABLE IF EXISTS Forms;");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		
		//sb = new StringBuilder("DROP TABLE IF EXISTS Depthead;");
		//CassandraUtil.getInstance().getSession().execute(sb.toString());
		
		//sb = new StringBuilder("DROP TABLE IF EXISTS HR;");
		//CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void createTables() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Employees (" ) 
				.append("username text PRIMARY KEY, empId int, userType text, password text, ")
				.append("tuitionAmnt double, paymentStatus text, email text, manager text, netPaid double);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		
		sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Forms1 ( ")
				.append("empId int PRIMARY KEY, percentage double, paidTuition double,")
				.append(" manager text, marks  tuple<int, text, text, text, text>, description text, course text, message text);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
		
		
	}

	
       public static void populateUserTables() {
		
    	  dop.addUser("eliana", "1234", "eliana@gmail.com", UserType.EMPLOYEE, "Dan");
		
       }
}

	   /* public static void populateForms() {
		
		ReimbursalForm f = new ReimbursalForm(1000, 90.0, 1000, "Dan", "approved", "University", "form waiting");
		dop.addForm(f);*/
		
		

