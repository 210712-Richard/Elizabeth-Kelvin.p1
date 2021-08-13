package com.revature.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.TupleType;
import com.datastax.oss.driver.api.core.data.TupleValue;
import com.revature.controller.UserControllerImpl;
import com.revature.factory1.Log;
import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.util.CassandraUtil;
import com.revature.forms.*;

@Log
public class DataOpsImpl implements DataOps{
	
	private static Logger log = LogManager.getLogger(UserControllerImpl.class);
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static final TupleType MARKS_TUPLE = DataTypes.tupleOf(DataTypes.INT, DataTypes.TEXT, DataTypes.TEXT,  DataTypes.TEXT,  DataTypes.TEXT);

	
	public UserDef addUser(String username, String password, String email, UserType userType, String manager) {
		
		Integer EmpID = 1006 + this.getUsers().size();
		String query = "Insert into Employees (username, userType, email,"
				+ " empId, manager, password, paymentstatus, tuitionamnt,"
				+ " netPaid) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(username, userType.toString(),
					  email, EmpID, manager, password, PaymentStatus.NONE.toString(), 0.0, 0.0);
		
		session.execute(bound);
		return getUser(username);
		
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
			try {
				u.setNetPaid(row.getDouble("netPaid"));
			}
			catch (NullPointerException e){
				u.setNetPaid(0.0);
			}
			users.add(u);
		});
		return users;
	}
	
	
	@Override
	public UserDef getUser(String username) {

		String query = "Select username, userType, email, empId, manager,"
				+ " password, paymentStatus, tuitionAmnt, netPaid from Employees  where username=? ";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(username);
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			return null;
		}
		
		UserDef u = new UserDef();
		u.setUsername(row.getString("username"));
		u.setEmail(row.getString("email"));
		u.setEmpId(row.getInt("empId"));
		u.setManager(row.getString("manager"));
		u.setPassword(row.getString("password"));
		u.setTuitionAmnt(row.getDouble("tuitionamnt"));
		u.setUserType(UserType.valueOf(row.getString("userType")));
		u.setPaymentStatus(PaymentStatus.valueOf(row.getString("paymentstatus")));

		try {
			u.setNetPaid(row.getDouble("netPaid"));
		}
		catch (NullPointerException e){
			u.setNetPaid(0.0);
		}
		
		return u;
	}
	
	
	public  UserDef getUserWithId(Integer EmpId) {	
		String query = "Select username, userType, email, manager, password, paymentStatus, tuitionAmnt, netPaid from Employees  where empId = ? ALLOW FILTERING";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(EmpId);
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			return null;
		}
		
		UserDef u = new UserDef();
		u.setUsername(row.getString("username"));
		u.setUserType(UserType.valueOf(row.getString("userType")));
		u.setEmail(row.getString("email"));
		u.setEmpId(EmpId);
		u.setManager(row.getString("manager"));
		u.setPassword(row.getString("password"));
		u.setPaymentStatus(PaymentStatus.valueOf(row.getString("paymentStatus")));
		u.setTuitionAmnt(row.getDouble("tuitionAmnt"));
		
		try {
			log.trace("getUserWithId : 1. " + row.getString("username") + " 2. " + UserType.valueOf(row.getString("userType")) + " 3. " + row.getString("email") +  " 4. " + EmpId + " 5. " + row.getString("manager")
		           + " 6. " + row.getString("password") + " 7." + PaymentStatus.valueOf(row.getString("paymentStatus")) + " 8. " + row.getDouble("tuitionAmnt") + " 9. " + row.getDouble("netPaid"));

			u.setNetPaid(row.getDouble("netPaid"));
		}
		catch (NullPointerException e){
			u.setNetPaid(0.0);
		}
		return u;
	}	
	
	
	@Override  
	public UserDef saveUser(UserDef u) {
		String query = "Update Employees set userType = ?, email = ?, empId = ?, manager = ?, password = ?,"
				+ " paymentstatus = ?, tuitionAmnt = ?, netPaid = ? where username = ?;";

		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(u.getUserType().toString(), u.getEmail(),
						u.getEmpId(), u.getManager(), u.getPassword(),
						u.getPaymentStatus().toString(), u.getTuitionAmnt(), u.getNetPaid(), u.getUsername());
		session.execute(bound);
		return u;
	}

	
	
	public List<UserDef> getAllEmployeesOf(String manager) {
		String query = "Select username, userType, email, empId,"
				+ " password, paymentstatus, tuitionamnt, netPaid from Employees where manager=? ALLOW FILTERING";

		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(manager);
		ResultSet rs = session.execute(bound);
		List<UserDef> users = new ArrayList<>();
		rs.forEach(row -> {
			UserDef u = new UserDef();
			u.setUsername(row.getString("username"));
			u.setUserType(UserType.valueOf(row.getString("userType")));
			u.setEmail(row.getString("email"));
			u.setEmpId(row.getInt("empId"));
			u.setManager(manager);
			u.setPassword(row.getString("password"));
			u.setPaymentStatus(PaymentStatus.valueOf(row.getString("paymentStatus")));
			u.setTuitionAmnt(row.getDouble("tuitionAmnt"));
			try {
				u.setNetPaid(row.getDouble("netPaid"));
			}
			catch (NullPointerException e){
				u.setNetPaid(0.0);
			}
			users.add(u);
		});
		return users;
	}
	
	
	
	public List<ReimbursalForm> getForms() {
		String query = "Select empId, percentage, paidTuition, manager, markSheet,"
				+ " description, course, message, submitTime from Forms1 ";

		SimpleStatement s = new SimpleStatementBuilder(query).build();
		ResultSet rs = session.execute(s);
		List <ReimbursalForm> forms = new ArrayList <ReimbursalForm> ();
		rs.forEach(row -> {
			
			TupleValue tm = row.getTupleValue("marks");
			MarkSheet marksheet = new MarkSheet();
			marksheet.setEmpId(tm.get(0, Integer.class));
			marksheet.setSubA(tm.get(1, String.class));
			marksheet.setSubB(tm.get(2, String.class));
			marksheet.setSubC(tm.get(3, String.class));
			marksheet.setSubD(tm.get(4, String.class));
			
			ReimbursalForm f = new ReimbursalForm(row.getInt("empId"), row.getDouble("percentage"), row.getDouble("paidTuition"),
					row.getString("manager"), marksheet, row.getString("description"),
					row.getString("course"), row.getString("message"));

			forms.add(f);
		});
		return forms;
	}
	
	
	public  ReimbursalForm getFormsOf(Integer EmpId) {	
		String query = "Select course, description, manager, marks, message,"
					+ " paidTuition, percentage from Forms1 where  empId = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(EmpId);
		ResultSet rs = session.execute(bound);
		Row row = rs.one();
		if(row == null) {
			return null;
		}
		TupleValue tm = row.getTupleValue("marks");
		MarkSheet marksheet = new MarkSheet();
		marksheet.setEmpId(tm.get(0, Integer.class));
		marksheet.setSubA(tm.get(1, String.class));
		marksheet.setSubB(tm.get(2, String.class));
		marksheet.setSubC(tm.get(3, String.class));
		marksheet.setSubD(tm.get(4, String.class));
		
		ReimbursalForm f = new ReimbursalForm(EmpId, row.getDouble("percentage"), row.getDouble("paidTuition"),
				row.getString("manager"), marksheet, row.getString("description"),
				row.getString("course"), row.getString("message"));
		log.trace("getFormsOf: " + f);
		return f;
	} 
	
	
	public  ReimbursalForm saveForm(ReimbursalForm f) {	
		MarkSheet m = f.getMarkSheet();
		UserDef ud = this.getUserWithId(f.getEmpId());

		TupleValue marks = MARKS_TUPLE
				.newValue(m.getEmpId(), m.getSubA(), m.getSubB(), m.getSubC(), m.getSubD());
		String query = "update Forms1 set course = ?, description = ?, manager = ?,"
				+ " marks = ?, message = ?, paidTuition = ?, percentage = ? where  empId = ?";
		
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		
		log.trace(" " + f.getcourseType() + " " + f.getDescription() 
				+ " " + f.getManager() + " " + f.getMessage());

		if (f.getPaidTuition() > 0.0) {
			ud.setTuitionAmnt(f.getPaidTuition());
			ud.setManager(f.getManager());
			log.trace("Save Form , Status: " + ud.getPaymentStatus());
			UserDef tmp = this.saveUser(ud);
		}
		
		// handle re-submission if already rejected
		if (ud.getPaymentStatus() == PaymentStatus.REJECTED && f.getMessage() == null) {
			ud.setPaymentStatus(PaymentStatus.NONE);
		}

		BoundStatement bound = session.prepare(s)
			.bind(f.getcourseType(), f.getDescription(),  f.getManager(),
			marks, f.getMessage(), f.getPaidTuition(), f.getPercent(), f.getEmpId());
		
		session.execute(bound);
		return f;
		
	}



}
