package persistentdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import customexceptions.CustomException;

public class DBConnection {
	private DBConnection() {
	}

	private static class ConnectionHelper {
		private static Connection connection;

		static Connection getConnection() throws CustomException {
			try {
				if (connection == null) {
					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");
				}
				return connection;
			} catch (SQLException e) {
				throw new CustomException("DB Connection failed!", e);
			}
		}
	}

	public static Connection getConnection() throws CustomException {
		return ConnectionHelper.getConnection();
	}

	public static void closeConnection() throws CustomException {
		try {
			if (ConnectionHelper.connection != null) {
				ConnectionHelper.connection.close();
			}
		} catch (SQLException e) {
			throw new CustomException("Failed to close Connection.", e);
		}
	}

}
