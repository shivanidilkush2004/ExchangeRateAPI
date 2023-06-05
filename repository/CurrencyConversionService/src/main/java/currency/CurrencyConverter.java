package currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import consts.CurrencyConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Map;
import java.util.ArrayList;


/**
 *This is a class created for currency conversion
 */
public class CurrencyConverter extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method is used for making rest API to convert currency using http Get method
	 * Sample request :  http://localhost:8080/CurrencyConversionService/CurrencyConverter?amount=10&fromCurrency=USD&toCurrency=EUR
	 */
	


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		PrintWriter out = res.getWriter();
		boolean isvalidCurrency =false;

		String amount = req.getParameter(CurrencyConstants.AMOUNT);
		String fromCurrency = req.getParameter(CurrencyConstants.FROM_CURRENCY);
		String toCurrency = req.getParameter(CurrencyConstants.TO_CURRENCY);

		try {
			Integer.parseInt(amount);
		} catch (NumberFormatException nfe) {
			out.println("Amout should be a number");
			return;
		}

		isvalidCurrency = isValidCurrencyCode(fromCurrency, toCurrency, out);
		if(isvalidCurrency) {
		BigDecimal convertedValue = currencyConverterAPI(amount, fromCurrency,toCurrency);
        out.println(convertedValue);
		}
	}
	
	/**
	 * This method is used for making rest API to convert currency using http Post method
	 * while posting the request from a JSP 
	 */
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		PrintWriter out = res.getWriter();
		boolean isvalidCurrency =false;

		String amount = req.getParameter(CurrencyConstants.AMOUNT);
		String fromCurrency = req.getParameter(CurrencyConstants.FROM_CURRENCY);
		String toCurrency = req.getParameter(CurrencyConstants.TO_CURRENCY);

		try {
			Integer.parseInt(amount);
		} catch (NumberFormatException nfe) {
			out.println("Amout should be a number");
			return;
		}

		isvalidCurrency = isValidCurrencyCode(fromCurrency, toCurrency, out);
		if(isvalidCurrency) {
		BigDecimal convertedValue = currencyConverterAPI(amount, fromCurrency,toCurrency);
        out.println(amount+" " +fromCurrency+" is converted to "+toCurrency+" and the value is "+convertedValue);
		}
	}
	
	/**
	 * This method validates the currency symbols (FROM/TO) based on the EXCHANGE_RATES_DATA_API 
	 */

	private boolean isValidCurrencyCode(String fromCurrency, String toCurrency, PrintWriter out) throws RuntimeException {

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(CurrencyConstants.EXCHANGE_RATES_DATA_API)
				.addHeader(CurrencyConstants.API_KEY, CurrencyConstants.API_KEY_VALUE).method("GET", null).build();

		Response response;
		try {
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject Jobject = new JSONObject(jsonData);

			JSONObject posts = Jobject.getJSONObject("symbols");

			Map<String, Object> map = posts.toMap();

			ArrayList<String> list = new ArrayList<String>(map.keySet());

			if (!list.contains(fromCurrency)) {
				out.println("fromCurrency is not valid");
				return false;
			
			}
			if (!list.contains(toCurrency)) {
				out.println("toCurrency is not valid");
				return false;
			}
			
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * This method does the currency conversion by calling EXCHANGE_RATES_DATA_CONVERT_API
	 */



	private static BigDecimal currencyConverterAPI(String amount, String fromCurrency, String toCurrency)
			throws RuntimeException {

		BigDecimal result = null;

		String queryPath = CurrencyConstants.EXCHANGE_RATES_DATA_CONVERT_API+"to=" + toCurrency + "&from="
				+ fromCurrency + "&amount=" + amount;
		System.out.println(queryPath);

		OkHttpClient client = new OkHttpClient().newBuilder().build();

		Request request = new Request.Builder().url(queryPath)
				.addHeader(CurrencyConstants.API_KEY, CurrencyConstants.API_KEY_VALUE).method("GET", null).build();

		Response response;
		try {
			response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject Jobject = new JSONObject(jsonData);
			result = (BigDecimal) Jobject.get("result");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

}
