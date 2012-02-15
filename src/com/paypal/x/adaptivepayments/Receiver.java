package com.paypal.x.adaptivepayments;

public class Receiver {
	private Integer n;
	private AdaptivePaymentsOperation o;

	public Receiver(AdaptivePaymentsOperation o, Integer n) {
		this.o = o;
		this.n = n;
	}

	public void setAmount(Integer amount) {
		o.getNvp().put("receiverList.receiver(" + n + ").amount",
				amount.toString());
	}
	
	public void setAmount(Double amount) {
		o.getNvp().put("receiverList.receiver(" + n + ").amount",
				amount.toString());
	}

	public void setEmail(String email) {
		o.getNvp().put("receiverList.receiver(" + n + ").email", email);
	}

	public void setPrimary(Boolean primary) {
		o.getNvp().put("receiverList.receiver(" + n + ").primary",
				primary ? "true" : "false");
	}
}