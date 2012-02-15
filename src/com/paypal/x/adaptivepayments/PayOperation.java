package com.paypal.x.adaptivepayments;

import java.util.ArrayList;
import java.util.List;

public class PayOperation extends AdaptivePaymentsOperation {
	private List<Receiver> receiverList;

	public PayOperation(AdaptivePayments ap) {
		super(ap);

		receiverList = new ArrayList<Receiver>();
	}

	@Override
	String getOperation() {
		return "Pay";
	}

	public Receiver receiverList(Integer n) {
		if (receiverList.size() == n) {
			receiverList.add(new Receiver(this, n));
		}

		return receiverList.get(n);
	}

	public void setActionType(String actionType) {
		getNvp().put("actionType", actionType);
	}

	public void setCancelUrl(String cancelUrl) {
		getNvp().put("cancelUrl", cancelUrl);
	}

	public void setCurrencyCode(String currencyCode) {
		getNvp().put("currencyCode", currencyCode);
	}

	public void setReturnUrl(String returnUrl) {
		getNvp().put("returnUrl", returnUrl);
	}
}