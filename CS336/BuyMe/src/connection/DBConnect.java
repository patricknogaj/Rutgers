package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Patrick Nogaj | Gemuele Aludino | Chaand Patel | Jong Shim
 * @class CS336 Principles of Information & Data Management
 * Rutgers University | Spring 2019.
 */

public class DBConnect {

	private final String DB_URL = "jdbc:mysql://cs336buyme.cnnvlun9z7yl.us-east-2.rds.amazonaws.com:3306/login_test";
	private final String DB_USER = "cs336buyme";
	private final String DB_PASS = "Rutgers123";
	
	public DBConnect() {
		
	}
	
	/**
	 * Creates a connect by utilizing DriverManager.
	 * @return a connection object to the server.
	 */
	public Connection getConnection() {
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) {
			System.out.println("ERR: " + e.getMessage());
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			
			if(conn != null) 
				System.out.println("Connected to Database successfully...");
			else 
				System.out.println("ERR: Cannot connect to Database. Check user/pass.");
		} catch(Exception e) {
			System.out.println("ERR: " + e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Closes the connector.
	 * @param conn: This method accepts a connector object to close.
	 */
	public void closeConnection(Connection conn) {
		try {	
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBConnect dbc = new DBConnect();
		Connection connection = dbc.getConnection();
	
		dbc.closeConnection(connection);
	}
	
}
