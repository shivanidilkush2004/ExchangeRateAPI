<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title> Currency Converter </title>
	</head>	
	<body>		
	   <form action="CurrencyConverter"  method="post">			
		      
       Amount       <input type="text"  name="amount"><br> <br>
       From Currency<input type="text" name="fromCurrency"><br> <br>
       To Currency  <input type="text" name="toCurrency"><br> <br>
       
       <input type="submit" value="convert">
				
	   </form>		
	</body>	
</html>