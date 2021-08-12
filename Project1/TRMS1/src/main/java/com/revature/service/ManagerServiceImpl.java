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
import com.revature.forms.*;
import java.util.List;


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
Manager
pre: create valid list of employees or specify the range 1-100 etc

get the list of employees in pending state

1. validate empid in the form 
2. check if the form is submitted 
3. pull the forms of empid 
4. verify results 
5. forward to Dean with form  
*/

@Log
public class ManagerServiceImpl implements ManagerService {
	
	private Logger log = LogManager.getLogger(ServiceImpl.class);
	public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);
	public FormOps fop = new FormOps();

	public boolean isManagerApproved(Integer EmpId, String name) {
		UserDef emp  = dop.getUserWithId(EmpId);
		UserDef mgr  = dop.getUser(name);
		UserType pos = mgr.getUserType();
		
		if (pos == UserType.MANAGER) {
			if (emp.getPaymentStatus() == PaymentStatus.MANAGER_APPROVED) 
				return true;
		}
		return false;

	}
	
	public boolean isDirectorApproved(Integer EmpId, String name) {
		UserDef emp  = dop.getUserWithId(EmpId);
		UserDef mgr  = dop.getUser(name);
		UserType pos = mgr.getUserType();
		
		if (pos == UserType.DIRECTOR) {
			if (emp.getPaymentStatus() == PaymentStatus.DIRECTOR_APPROVED) 
				return true;
		}

		return false;

	}
	
	public boolean fwdApproval (ReimbursalForm form,  UserDef ud) {
		// check manger role in the form , if Dir forward to HR
		if (ud.getUserType() == UserType.MANAGER ) {
			form.setManager(ud.getManager());
		}
		if (ud.getUserType() == UserType.DIRECTOR ) {
			form.setManager(ud.getManager());
		}
		return true;
	}
	
	public boolean approvalChecklistManager(ReimbursalForm form) {
		
		UserDef Emp = dop.getUserWithId(form.getEmpId());
		String course = form.getcourseType().toLowerCase();
		
		if (form.getPercent() != form.getMarkSheet().getPercent()) {
			form.setMessage("Percent Mismatch. Upload the form again.");
			Emp.setPaymentStatus(PaymentStatus.REJECTED);
			return false;
		}
		if (form.getMarkSheet().getPercent() < 90.0) {
			form.setMessage("Percent didnt meet cutoff.");
			Emp.setPaymentStatus(PaymentStatus.REJECTED);
			return false;
		}
		if (( course != "university" && course != "seminar" && course != "certification")) {
			form.setMessage("Course not eligible for reimbursement.");
			Emp.setPaymentStatus(PaymentStatus.MANAGER_SKIPPED);
			return false;
		}
		
		Emp.setPaymentStatus(PaymentStatus.MANAGER_APPROVED);
		return true;
	}
	
	public boolean approvalChecklistDirector(ReimbursalForm form) {

		UserDef Emp = dop.getUserWithId(form.getEmpId());
		String course = form.getcourseType().toLowerCase();
		
		// If employee is Manager or Director
		if (Emp.getUserType() == UserType.DIRECTOR || Emp.getUserType() == UserType.MANAGER ) {
			if(this.approvalChecklistManager(form))
				Emp.setPaymentStatus(PaymentStatus.DIRECTOR_APPROVED);
		}
		
		if ( Emp.getPaymentStatus() == PaymentStatus.MANAGER_SKIPPED ) {

			if(form.getMarkSheet().getPercent() > 98.0) {
				form.setMessage("Approving for the exceptional marks.");
				return true;
			}
			else if(course == "coresera" || course == "udemy" || course == "linkdln") {
				form.setMessage("Approving because of standard online course.");
				return true;
			}
			else if (form.getPaidTuition() < 100.0) {
				form.setMessage("Approving as it is not expensive.");
				return true;
			}
			else {
				form.setMessage("Course not eligible for reimbursement.");
				Emp.setPaymentStatus(PaymentStatus.REJECTED);
				return false;
			}
		}
		
		Emp.setPaymentStatus(PaymentStatus.DIRECTOR_APPROVED);
		return true;
	}
	
	
	public boolean setApproval (UserDef f) {
		
		List <ReimbursalForm> approvalForms = fop.FormsNeedApproval(f.getUsername());
		if (f.getUserType() == UserType.MANAGER) {
			for (ReimbursalForm i : approvalForms) {
					if (isManagerApproved(i.getEmpId(), f.getUsername())) {
						continue;
					}
					else {
						if (this.approvalChecklistManager(i)) {
							// only success forward.
							this.fwdApproval(i, f);
						}
						// even if not success we need to save.
						dop.saveForm(i);
					}
				}
		}
		if (f.getUserType() == UserType.DIRECTOR) {
			for (ReimbursalForm i : approvalForms) {
					if (isDirectorApproved(i.getEmpId(), f.getUsername())) {
						continue;
					}
					else {
						if (this.approvalChecklistDirector(i)) {
							// only success forward.
							this.fwdApproval(i,  f);
						}
						// even if not success we need to save.
						dop.saveForm(i);
					}
				}
		}
		return true;
	}
	
}
