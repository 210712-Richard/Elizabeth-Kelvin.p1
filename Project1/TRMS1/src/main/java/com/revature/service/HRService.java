package com.revature.service;

import com.revature.forms.ReimbursalForm;
import com.revature.userdef.UserDef;


public interface HRService {
	boolean isApproved(Integer EmpId);
	boolean approvalChecklist(ReimbursalForm form);
	Double getReimburseAmount(ReimbursalForm f);
	boolean completePayment(ReimbursalForm f, Double amount);
	boolean setApproval (UserDef f);
}
