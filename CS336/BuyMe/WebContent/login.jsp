<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="connection.DBConnect"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>BuyMe - Login</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>

<div class="header-container">
	<div class="TopMenu">
		<ul class="social">
			<li><a href="https://twitter.com/RutgersU"><img src="data\img\social\twitter.png" height="25px" width="25px"></a></li>
			<li><a href="https://www.facebook.com/RutgersU"><img src="data\img\social\facebook.png" height="25px" width="25px"></a></li>
			<li><a href="https://www.instagram.com/RutgersU"><img src="data\img\social\instagram.png" height="25px" width="25px"></a></li>
		</ul>
		
		<ul class="Links">
			<li><a class="dt" id="dt"></a></li><br>
			<li class="links"><a href="login.jsp">Sign in</a> or <a href="signup.jsp">Create an Account</a></li>
		</ul>
	</div>
</div>

<div class="subheader">
	<a href="index.jsp"><img src="data\img\project\logo.png"></a>
</div>

<div class="content">
	<hr width="100%">
	
	<div class="grid pg_login">
	
		<div class="grid_item one-half create_acc">
			<div class="CreateAcc">
				<h3>Create an Account</h3>
			</div>
			<div style="Display :">
				<p>Create an account and you'll be able to:</p>
				<ul>
					<li>- Receive alerts on auctions</li><br>
					<li>- Interact with other users</li><br>
					<li>- Access your order history</li><br>
					<li>- Rate transactions</li><br>
					<li>- Utilize the helpdesk</li><br>
				</ul>
				
				<a href="signup.jsp" class="btn alt">Click here to create a new account.</a>
			</div>
			
		</div>
		
		<div class="grid_item one-half sign_in">
			<div class="SignIn">
				<h3>Sign In</h3>
			</div>
			<form method="post" action="login.jsp">
			<div style="Display: ">
			<p>Username:</p>
			<input type="username" name="username"></input>
			<p>Password:</p>
			<input type="password" name="password"></input>
			<p></p>
			<button class="btn alt">Sign In</button>
			</div>
			</form>
		</div>
	</div>
</div>

<div class="footer">
	<hr>
	
	<div class="container well">
		<p>Footer things to add later...</p>
	</div>
	
	<%
	
	try {
		DBConnect c = new DBConnect();
		Connection conn = c.getConnection();
		Statement statement = conn.createStatement();
		
		String _user = request.getParameter("username");
		String _pass = request.getParameter("password");
		
		if(_user.equals("") && _pass.equals("")) {
			%>
			<script>
				alert("Please enter your username and password.");
				window.location.href = "login.jsp";
			</script>
			<%
		} else {
			String example = "SELECT * FROM login l WHERE l.username='" + _user + "' and l.password='" + _pass + "'";
			ResultSet result = statement.executeQuery(example);
			
			if(result.next()) {
				HttpSession sess = request.getSession(true);
				sess.setAttribute("currentSessionUser", _user);
				%>
				<script>
					window.location.href = "home.jsp";
				</script>
				<%
			} else {
				System.out.println("NO USER FOUND.");
				%>
				<script>
					alert("User not found or password entered incorrectly.");
					window.location.href = "login.jsp";
				</script>
				<%
			}

		}
		conn.close();
	} catch(Exception e) {
		System.out.println("ERROR: " + e.getMessage());
	}
	
	%>
	
</div>

<script>
	var tday=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
	var tmonth=["January","February","March","April","May","June","July","August","September","October","November","December"];

	function GetClock(){
		var d=new Date();
		var nday=d.getDay(),nmonth=d.getMonth(),ndate=d.getDate(),nyear=d.getFullYear();
		var nhour=d.getHours(),nmin=d.getMinutes(),nsec=d.getSeconds(),ap;

		if(nhour == 0) {	
			ap=" AM";
			nhour = 12;
		} else if(nhour < 12) {
			ap=" AM";
		} else if(nhour == 12){
			ap=" PM";
		} else if(nhour > 12){
			ap=" PM";
			nhour -= 12;
		}

		if(nmin<=9) 
			nmin="0"+nmin;
		if(nsec<=9) 
			nsec="0"+nsec;

		var clocktext=""+tday[nday]+", "+tmonth[nmonth]+" "+ndate+", "+nyear+" "+nhour+":"+nmin+":"+nsec+ap+"";
		document.getElementById('dt').innerHTML=clocktext;
	}

	GetClock();
	setInterval(GetClock,1000);
</script>

</body>
</html>