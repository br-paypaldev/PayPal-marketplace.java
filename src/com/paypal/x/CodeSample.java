package com.paypal.x;

import java.util.Map;
import com.paypal.x.adaptivepayments.AdaptivePayments;
import com.paypal.x.adaptivepayments.PayOperation;

public class CodeSample {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userId = "usuario";
		String password = "senha";
		String signature = "assinatura";

		AdaptivePayments ap = new AdaptivePayments(userId, password, signature);
		PayOperation pay = ap.pay();

		pay.setCurrencyCode( "USD" );
		pay.setCancelUrl("http://127.0.0.1:8080/cancel");
		pay.setReturnUrl("http://127.0.0.1:8080/return");
		pay.receiverList(0).setAmount(100);
		pay.receiverList(0).setEmail("neto_1306507007_biz@gmail.com");
		pay.receiverList(0).setPrimary(true);
		pay.receiverList(1).setAmount(100);
		pay.receiverList(1).setEmail("neto.j_1324471857_biz@gmail.com");

		Map<String, String> nvp = pay.execute();

		if (nvp.containsKey("responseEnvelope.ack")
				&& nvp.get("responseEnvelope.ack").equals("SUCCESS")) {

			String payKey = nvp.get("payKey");

			System.out.println(payKey);
		}
	}
}