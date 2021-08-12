package com.revature.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;

import com.revature.factory1.Log;
import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.util.CassandraUtil;
import com.revature.forms.*;

@Log
public class DataOpsImpl implements DataOps{
	
	
	private CqlSession session = CassandraUtil.getInstance().getSession();
	//private ReimbursalForm[] forms;
	
    //@Override
	public UserDef addUser(String username, String password, String email, UserType userType, String manager) {
		
		Integer EmpID = 1001 + this.getUsers().size();
		String query = "Insert into Employees (username, userType, email, empId, manager, password, paymentstatus, tuitionamnt, netPaid) values (?, ?, ?, ?, ?, ?,?, ?, ?);";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(username, userType.toString(), email, EmpID, manager, password, PaymentStatus.NONE.toString(), 0.0, 0.0);
		session.execute(bound);
		return getUser(username);
		//return null;

		
	}
	
	public List<UserDef> getUsers() {
		String query = "Select username, userType, email, empId, manager,"
				+ " password, paymentstatus, tuitionamnt, netPaid from Employees ";

		SimpleStatement s = new SimpleStatementBuilder(query).build();
		ResultSet rs = session.execute(s);
		List<UserDef> users = new ArrayList<>();
		rs.forEach(row -> {
			UserDef u = new UserDef();
			u.setUsername(row.getString("username"));
			u.setUserType(UserType.valueOf(row.getString("userType")));
			u.setEmail(row.getString("email"));
			u.setEmpId(row.getInt("empId"));
			u.setManager(row.getString("manager"));
			u.setPassword(row.getString("password"));
			u.setPaymentStatus(PaymentStatus.valueOf(row.getString("paymentStatus")));
			u.setTuitionAmnt(row.getDouble("tuitionAmnt"));
			u.setTuitionAmnt(row.getDouble("netPaid"));

			users.add(u);
		});
		return users;
	}
	
	
	@Override
	public UserDef getUser(String username) {

		String query = "Select username, userType, email, empId, manager, password, paymentStatus, tuitionAmnt, netPaid from Employees  where username=? ";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(username);
		// ResultSet is the values returned by my query.
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			// if there is no return values
			return null;
		}
		
		UserDef u = new UserDef();
		u.setUsername(row.getString("username"));
		u.setUserType(UserType.valueOf(row.getString("userType")));
		u.setEmail(row.getString("email"));
		u.setEmpId(row.getInt("empId"));
		u.setManager(row.getString("manager"));
		u.setPassword(row.getString("password"));
		u.setPaymentStatus(PaymentStatus.valueOf(row.getString("paymentstatus")));
		u.setTuitionAmnt(row.getDouble("tuitionamnt"));
		u.setTuitionAmnt(row.getDouble("netPaid"));

		return u;
	}
	
	@Override  
	public UserDef saveUser(UserDef u) {
		String query = "Update user set userType = ?, email = ?, empId = ?, manager = ?, password = ?,"
				+ " paymentstatus = ?, tuitionamnt = ?, netPaid = ? where username = ?;";

		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(u.getUserType().toString(), u.getEmail(), u.getEmpId(), u.getManager(), u.getPassword(),
						u.getPaymentStatus().toString(), u.getTuitionAmnt(), u.getNetPaid());
		session.execute(bound);
		return u;
	}

	
	
	public List<UserDef> getAllEmployeesOf(String manager) {
		String query = "Select all user from employees where manager=?";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(manager);
		// ResultSet is the values returned by my query.
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			// if there is no return values
			return null;
		}
		List<UserDef> getAllEmployees = row.getList(manager, UserDef.class);
		return getAllEmployees;
	}
	
	public List<ReimbursalForm> getForms() {
		String query = "Select empId, percentage, paidTuition, manager, markSheet,"
				+ " description, courseType, message, submitTime from user ";

		SimpleStatement s = new SimpleStatementBuilder(query).build();
		ResultSet rs = session.execute(s);
		List <ReimbursalForm> forms = new ArrayList <ReimbursalForm> ();
		rs.forEach(row -> {
			ReimbursalForm f = new ReimbursalForm(row.getInt("empId"), row.getDouble("percentage"), row.getDouble("paidTuition"),
					row.getString("manager"), row.getList("marks", MarkSheet.class).get(0), row.getString("description"),
					row.getString("courseType"), row.getString("message"), row.getLocalDate("submitTime"));

			forms.add(f);
		});
		return forms;
	}
	
	public  ReimbursalForm getFormsOf(Integer EmpId) {	
		String query = "Select reimbursalform of employee from Forms  where empId=?";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(EmpId);
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			return null;
		}
		ReimbursalForm f = new ReimbursalForm(row.getInt("empId"), row.getDouble("percentage"), row.getDouble("paidTuition"),
				row.getString("manager"), row.getList("marks", MarkSheet.class).get(0), row.getString("description"),
				row.getString("courseType"), row.getString("message"), row.getLocalDate("submitTime"));
		
		return f;
	} 
	
	public  ReimbursalForm saveForm(ReimbursalForm f) {	
		List<MarkSheet> marks = new ArrayList<MarkSheet>();
		marks.add(f.getMarkSheet());
		String query = "update form set courseType = ?, description = ?,, manager = ?, marks = ?, message = ?, paidTuition = ?, percentage = ?, SubmitTime = ? where  empId = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(f.getcourseType(), f.getDescription(),  f.getManager(),
						marks, f.getPaidTuition(), f.getPercent(), f.getSubmitTime(), f.getEmpId());
		session.execute(bound);
		return f;
		
	}

	
	
	
	public  UserDef getUserWithId(Integer EmpId) {	
		String query = "Select user from Employees where empId=?";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(EmpId);
		// ResultSet is the values returned by my query.
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			// if there is no return values
			return null;
		}
		
		UserDef u = new UserDef();
		u.setEmpId(row.getInt("empId"));
        return u;
	}
		
		
	
	
		
	
	/*public UserDef setUser(String username, String password,
            String email, UserType userType, String manager) {
			Integer EmpId = users.size() + 1000;
			UserDef ud = new UserDef(EmpId, username, password, email,
	                 userType, manager, 0.0, PaymentStatus.NONE);
			users.add(ud);
			this.writeUsersToFile();
			return ud;
}

	
	
	
		
	
		*private static String filename1 = "users.dat";
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
		}*/
		
		
		
		
		
		
		/*public  List<UserDef> getAllEmployeesOf(String manager) {
			List<UserDef> myUsers = new ArrayList<UserDef>();
			for(UserDef user : users) {
				if(user.getManager().equals(manager)) {
					myUsers.add(user);
				}
			}
			return myUsers;
		}
		
		public ReimbursalForm saveForm(ReimbursalForm f) {			
			// you may want to delete the form by index(empId) of list and add
			forms.add(f);
	        this.writeFormsToFile();
			return f;
		}
		
		
		public void writeUsersToFile() {
			new DataSerializer<UserDef>().writeObjectsToFile(users, "users.dat");
		}

		
		public void writeFormsToFile() {
			new DataSerializer<ReimbursalForm>().writeObjectsToFile(forms, "forms.dat");
		}
		
		

		
		public UserDef saveUser(UserDef u) {			
			// you may want to delete the user by index of list and add
			users.add(u);
	        this.writeUsersToFile();
			return u;
		}*/
	
		
		
}
