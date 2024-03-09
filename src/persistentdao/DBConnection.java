package persistentdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private DBConnection() {
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");
	}
//	private static class ConnectionHelper {
//		private static Connection connection;
//
//		static Connection getConnection() throws CustomException {
//			try {
//				if (connection == null) {
//					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");
//				}
//				return connection;
//			} catch (SQLException e) {
//				throw new CustomException("DB Connection failed!", e);
//			}
//		}
//	}

//	public static Connection getConnection() throws SQLException {
//		return ConnectionHelper.getConnection();
//		return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");
//	}

//	public static void closeConnection() throws CustomException {
//		try {
//			if (ConnectionHelper.connection != null) {
//				ConnectionHelper.connection.close();
//			}
//		} catch (SQLException e) {
//			throw new CustomException("Failed to close Connection.", e);
//		}
//	}

}
