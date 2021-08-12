package com.revature.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.data.DataOps;
import com.revature.data.DataOpsImpl;
import com.revature.factory1.BeanFactory;
import com.revature.forms.FormOps;
import com.revature.forms.ReimbursalForm;
import com.revature.userdef.PaymentStatus;
import com.revature.userdef.UserDef;
import com.revature.userdef.UserType;
import com.revature.factory1.Log;

/*
HR operations
1. validate empid in the form 
1. check if approval is there
2. check if employee is valid
3. reimburse the amount
4. update the payemnet status 
*/
@Log
public class HRServiceImpl implements HRService {

	private Logger log = LogManager.getLogger(ServiceImpl.class);
	public DataOps dop = (DataOps) BeanFactory.getFactory1().get(DataOps.class, DataOpsImpl.class);
	public FormOps fop = new FormOps();
	
	public boolean isApproved(Integer EmpId) {
		
		UserDef emp  = dop.getUserWithId(EmpId);
		if (emp.getPaymentStatus() == PaymentStatus.HR_APPROVED) 
				return true;
		return false;
	}

	public boolean approvalChecklist(ReimbursalForm form) {
		
		UserDef Emp = dop.getUserWithId(form.getEmpId());
		String course = form.getcourseType().toLowerCase();
		
		if (Emp.getPaymentStatus() != PaymentStatus.DIRECTOR_APPROVED ) {
			form.setMessage("Awaiting Director Approval.");
			return false;
		}
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
		if (( course != "university" && course != "seminar" && course != "certification"
				&& Emp.getPaymentStatus() !=  PaymentStatus.DIRECTOR_APPROVED ) ) {
			
			form.setMessage("Course not eligible for reimbursement.");
			Emp.setPaymentStatus(PaymentStatus.REJECTED);
			return false;
		}
		
		Emp.setPaymentStatus(PaymentStatus.HR_APPROVED);
		return true;
	}
	
	public Double getReimburseAmount(ReimbursalForm f) {
		UserDef emp        = dop.getUserWithId(f.getEmpId());
		Double netPaid     = emp.getNetPaid();
		Double tutionAmnt  = f.getPaidTuition();
		String course      = f.getcourseType().toLowerCase();
		Double reimbAmount = 0.0;
		// university 100% , Seminar 90%, certification 80% others 70%
		
		switch(course) {
			case "university"    : reimbAmount = tutionAmnt; break;
			case "seminar"       : reimbAmount = tutionAmnt * 0.9 ; break;
			case "certification" : reimbAmount = tutionAmnt * 0.8 ; break;
			default              : reimbAmount = tutionAmnt * 0.7 ; break;
		}
		// max allowed per year is 1000.
		if (reimbAmount > (1000.0 - netPaid)) {
			return  (1000.0 - netPaid) ;
		}
		return reimbAmount;
	}
	
	
	public void completePayment(ReimbursalForm f, Double amount) {
		UserDef emp  = dop.getUserWithId(f.getEmpId());
		f.setMessage("Congrats. Reimbursement approved for $" + amount.toString());
		emp.setPaymentStatus(PaymentStatus.COMPLETED);
		emp.setNetPaid(emp.getNetPaid() + amount);
	}
	
	
	public boolean setApproval (UserDef f) {
		// what all needs to be set ?
		// employee payment status with appropriate level
		// change manger in form to next level 
		// If HR set employee payment staus and reimburse the amount
		// this invloves setting the net amount also , validate empID
		Double amount = 0.0;
		List <ReimbursalForm> approvalForms = fop.FormsNeedApproval(f.getUsername());
		if (f.getUserType() == UserType.HR) {
			for (ReimbursalForm i : approvalForms) {
					if (isApproved(i.getEmpId())) {
						continue;
					}
					else {
						if (this.approvalChecklist(i)) {
							amount = this.getReimburseAmount(i);
							this.completePayment(i, amount);
						}
						// even if not success we need to save.
						dop.saveForm(i);
					}
				}
		}	
		return true;
	}
	

	

	
}
