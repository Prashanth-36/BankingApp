package persistentLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import CustomExceptions.CustomException;

public class DBConnection {

	private static class ConnectionHelper {
		private static Connection connection;

		static Connection getConnection() throws CustomException {
			try {
				if (connection == null) {
					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank");
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

	public static void close() throws CustomException {
		try {
			Connection connection = ConnectionHelper.getConnection();
			if (connection == null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new CustomException("Failed to close Connection.", e);
		}
	}

}
