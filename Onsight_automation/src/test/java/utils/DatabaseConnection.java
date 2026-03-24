package utils;

//import cucumber.Reporter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

	PropertyReader propertyreader = new PropertyReader();
	Connection conn = null;
	Statement stmt = null;
	ResultSet executeQuery = null;
	String JDBC_DRIVER = null;
	String DB_URL = null;
	String user = null;
	String pass = null;
	int updateQuery;

	public DatabaseConnection() {

		try {
			JDBC_DRIVER = propertyreader.readProperty("JDBC_DRIVER");
			DB_URL = propertyreader.readProperty("DB_URL");
			user = propertyreader.readProperty("DB_USER");
			pass = propertyreader.readProperty("DB_PASS");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(DB_URL, user, pass);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Database connection is established !");
	}

	public ResultSet runMySqlQuery(String sqlQuery) throws ClassNotFoundException, SQLException {
		executeQuery = stmt.executeQuery(sqlQuery);

		return executeQuery;
	}

	/**
	 * # This method execute query and check it will return one result at least
	 * 
	 * @param query -- data base query
	 * @return -- boolean if query return at least one record
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean checkQueryResult(String query) throws ClassNotFoundException, SQLException {
		ResultSet rs = this.runMySqlQuery(query);
		boolean status = false;
		while (rs.next()) {
			status = true;			
		}
		return status;
	}
	
	/**
	 * get total number of results
	 * @param query : query to execute 
	 * @return : return total number of result 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int getTotalResultCount(String query) throws ClassNotFoundException, SQLException {
		ResultSet rs = this.runMySqlQuery(query);	
		int count = 0;
		while (rs.next()) {			
			count++;
		}
		return count;
	}

	/**
	 * Close DB connection
	 */
	public void close() {
		try {
			if (executeQuery != null) {
				executeQuery.close();
				System.out.println("Result set is closed");
			}

			if (stmt != null) {
				stmt.close();
				System.out.println("Statement is closed");
			}

			if (conn != null) {
				conn.close();
				System.out.println("Database connection is closed");
			}
		} catch (Exception e) {

		}
	}

	public int updateSqlQuery(String sqlQuery) throws ClassNotFoundException, SQLException {
		updateQuery = stmt.executeUpdate(sqlQuery);
		return updateQuery;
	}

}
