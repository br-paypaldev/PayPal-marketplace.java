package com.paypal.x.adaptivepayments;

import java.util.HashMap;
import java.util.Map;

public abstract class AdaptivePaymentsOperation {
	private AdaptivePayments ap;
	private Map<String, String> nvp;

	public AdaptivePaymentsOperation(AdaptivePayments ap) {
		this.ap = ap;

		nvp = new HashMap<String, String>();
	}

	public Map<String, String> execute() {
		return ap.execute(this);
	}

	Map<String, String> getNvp() {
		return nvp;
	}

	abstract String getOperation();
}