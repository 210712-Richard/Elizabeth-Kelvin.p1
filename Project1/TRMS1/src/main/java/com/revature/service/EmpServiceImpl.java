package com.revature.service;
import com.revature.userdef.UserDef;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.factory1.BeanFactory;
import com.revature.factory1.Log;
import com.revature.forms.*;
import java.time.LocalDate;


/*
     Employee operations
     1. create the form for reimbursement -> create diff class
     2. upload the marksheet
     3. upload the form , optional
     3. check payment status
     4. update tution amount
     
     when employee submit , change status to waiting
 */


@Log
public class EmpServiceImpl implements EmpService {
	private Logger log = LogManager.getLogger(ServiceImpl.class);
	public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);

	
	public boolean CreateAppForm(Integer empId, Double percent, Double paidTuition, String manager,
			              MarkSheet ms, String descrpt, String course,  String messg) {
		UserDef u = dop.getUserWithId(empId);
		if (u == null) {
			log.trace("User not registered!");
			return false;
		}
		
		if (ms == null) {
			MarkSheet tmp = new MarkSheet();
			tmp.setEmpId(empId);
			tmp.setSubA("");
			tmp.setSubB("");
			tmp.setSubC("");
			tmp.setSubD("");
			ms = tmp;
		}
		if (messg == null) {
			messg = "";
		}
		ReimbursalForm f = new ReimbursalForm (empId, percent, paidTuition, manager, ms, descrpt, course, messg);
		dop.saveForm(f);
		return true;
	}
	
	public boolean CreateMarksForm(Integer empId, String mrkA, String mrkB, String mrkC, String mrkD) {
		ReimbursalForm f = dop.getFormsOf(empId);
		if (f == null) {
			log.trace("Form not Submitted!. First Submit form and then upload marks.");
			return false;
		}
		
		MarkSheet ms = new MarkSheet (empId, mrkA, mrkB, mrkC, mrkD);
		f.setMarkSheet(empId, mrkA, mrkB, mrkC, mrkD);
		dop.saveForm(f);
		return true;
	}
	
	
	public String GetStatus(String username) {
		UserDef u = dop.getUser(username);
		if (u == null) {
			log.trace("User not registered!");
			return null;
		}
		return dop.getFormsOf(u.getEmpId()).getMessage();
	}
	
}
