package com.revature.forms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.factory1.BeanFactory;
import com.revature.service.ServiceImpl;
import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.factory1.Log;

@Log
public class FormOps {
/*
 *  1. Get forms of employees with manager
 *  2. Get forms of employees with marksheet submitted
 *  3. get forms of employees with percentage > 90
 *  4. get forms of employees not paid and not rejected.
 *  5. function to allow only managers to modify the form.
 *  6. calculate the amount to reimburse , max is 1000$ else % based on type of course
 */
	
	
	private Logger log = LogManager.getLogger(ServiceImpl.class);
	public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);

	
	public List<ReimbursalForm> getFormsOfReportees(String name) {
		UserDef u = dop.getUser(name);
		if (u == null) {
			log.trace("User not registered!");
			return null;
		}
		else if ( u.getUserType() == UserType.EMPLOYEE ) {
			log.trace("Employee is not permitted to do this operation.");
			return null;
		}
		else {
			List<UserDef> emps = dop.getAllEmployeesOf(name);
			List<ReimbursalForm> fms = new ArrayList<ReimbursalForm> ();
			for (UserDef ob : emps) {
				ReimbursalForm tmp = dop.getFormsOf(ob.getEmpId());
				if ( tmp != null )
					fms.add(tmp);
			}
			return fms;
		}
	}
	
	// check if already paid status complete
	// check if was rejected
	// percentage not 90 below
	// check if netpaid == 1000 , already done for the year
	
	public Integer isEligible(Integer EmpId) {
		UserDef e = dop.getUserWithId(EmpId);
		ReimbursalForm f = dop.getFormsOf(EmpId);
		if( f == null) {
			return 0;
		}
		else {
			if ( e.getPaymentStatus() == PaymentStatus.NONE) {
				return 1;
			}
			else if ((e.getPaymentStatus() == PaymentStatus.REJECTED)
					&& (f.getMessage() != null)) {
				return 2;
			}
			else if (e.getPaymentStatus() == PaymentStatus.REJECTED) {
				return 3;
			}
			else if (e.getPaymentStatus() == PaymentStatus.COMPLETED) {
				return 4;
			}
			else if ( f.getPercent() < 90.0) {
				return 5;
			}
			else if ( e.getNetPaid() == 1000.0 ) {
				return 6;
			}
			else {
				return 7;
			}
		}
	}
	
	

	public List<ReimbursalForm> FormsNeedApproval(String name) {
		UserDef u = dop.getUser(name);
		List<ReimbursalForm> needApprovForms = new ArrayList <ReimbursalForm> (); 
		
		if (u == null) {
			log.trace("User not registered!");
			return null;
		}
		else if (u.getUserType() == UserType.EMPLOYEE) {
			log.trace("User not Autherized!");
			return null;	
		}
		else {
			List<ReimbursalForm> forms = getFormsOfReportees(u.getUsername());
			for (ReimbursalForm f : forms) {
				switch (this.isEligible(f.getEmpId())) {
					case 0: f.setMessage("Form Empty."); break;
					case 1: f.setMessage("Form Not Submitted. Submit again."); break;
					case 2: break;  // Message already set 
					case 3: f.setMessage("Rejected.Contact HR for details."); break;
					case 4: f.setMessage("Approved."); break;
					case 5: f.setMessage("Rejected. Percentage didnt meet."); break;
					case 6: f.setMessage("Rejected. Crossed max reimbursement."); break;
					case 7: needApprovForms.add(f); break;
				}
			}
		}
		return needApprovForms;
	}
	
	

	
}
