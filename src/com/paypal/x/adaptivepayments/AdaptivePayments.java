package com.paypal.x.adaptivepayments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class AdaptivePayments {
	public final static String HOST = "svcs.paypal.com";
	public final static String SANDBOX_APPID = "APP-80W284485P519543T";
	public final static String SANDBOX_HOST = "svcs.sandbox.paypal.com";

	private String appId;
	private String userId;
	private String password;
	private String signature;
	private Boolean sandbox;

	public AdaptivePayments(String appId, String userId, String password,
			String signature) {

		this.appId = appId;
		this.userId = userId;
		this.password = password;
		this.signature = signature;
		this.sandbox = appId.equals(SANDBOX_APPID);
	}

	public AdaptivePayments(String userId, String password, String signature) {
		this(SANDBOX_APPID, userId, password, signature);
	}

	public Map<String, String> execute(AdaptivePaymentsOperation o) {
		Map<String, String> requestNvp = o.getNvp();
		Map<String, String> responseNvp = new HashMap<String, String>();

		StringBuilder sb = new StringBuilder();
		sb.append("https://");
		sb.append(sandbox ? SANDBOX_HOST : HOST);
		sb.append("/AdaptivePayments/");
		sb.append(o.getOperation());

		HttpsURLConnection conn = getConnection(sb.toString());

		try {
			conn.setRequestProperty("X-PAYPAL-SECURITY-USERID", userId);
			conn.setRequestProperty("X-PAYPAL-SECURITY-PASSWORD", password);
			conn.setRequestProperty("X-PAYPAL-SECURITY-SIGNATURE", signature);
			conn.setRequestProperty("X-PAYPAL-APPLICATION-ID", appId);
			conn.setRequestProperty("X-PAYPAL-REQUEST-DATA-FORMAT", "NV");
			conn.setRequestProperty("X-PAYPAL-RESPONSE-DATA-FORMAT", "NV");

			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());

			sb = new StringBuilder();

			for (Entry<String, String> nvp : requestNvp.entrySet()) {
				sb.append(nvp.getKey());
				sb.append("=");
				sb.append(URLEncoder.encode(nvp.getValue(), "UTF-8"));
				sb.append("&");
			}
			
			sb.append( "requestEnvelope.errorLanguage=en_US" );
			
			writer.write(sb.toString());
			writer.flush();
			writer.close();

			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			try {
				sb = new StringBuilder();
				BufferedReader reader = new BufferedReader(in);

				String data = null;

				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}

				data = sb.toString();
				
				String pairs[] = data.split("&");

				for (String current : pairs) {
					String[] pair = current.split("=");

					responseNvp.put(pair[0],
							URLDecoder.decode(pair[1], "UTF-8"));
				}
			} finally {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
		} catch (IOException e) {
			Logger.getLogger(AdaptivePayments.class.getName()).log(
					Level.SEVERE, null, e);
		}

		return responseNvp;
	}

	protected HttpsURLConnection getConnection(String spec) {
		URL url;
		HttpsURLConnection conn;

		try {
			url = new URL(spec);

			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			return conn;
		} catch (IOException e) {
			Logger.getLogger(AdaptivePayments.class.getName()).log(
					Level.SEVERE, null, e);
		}

		return null;
	}

	public PayOperation pay() {
		return pay("PAY");
	}

	public PayOperation pay(String actionType) {
		PayOperation payOperation = new PayOperation(this);
		payOperation.setActionType(actionType);

		return payOperation;
	}
}