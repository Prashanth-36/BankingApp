package persistentLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import CustomExceptions.CustomException;
import model.Transaction;

public class TransactionDao implements TransactionManager {

	@Override
	public void initTransaction(Transaction transaction) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO transaction(type,time,amount,primaryAccount,transactionalAccount,description) values(?,?,?,?,?,?)");) {
			statement.setString(1, transaction.getType().name());
			statement.setTimestamp(2, Timestamp.from(Instant.now()));
			statement.setDouble(3, transaction.getAmount());
			statement.setInt(4, transaction.getPrimaryAccount());
			statement.setInt(5, transaction.getTransactionalAccount());
			statement.setString(6, transaction.getDescription());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Transaction failed!", e);
		}
	}

	@Override
	public void initTransaction(List<Transaction> transactions) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try {
			connection.setAutoCommit(false);
			for (Transaction transaction : transactions) {
				initTransaction(transaction);
			}
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new CustomException("Rollback failed!", e);
			}
			throw new CustomException("Transaction failed!", e);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new CustomException("Toggle Auto Commit failed!", e);
			}
		}
	}

	@Override
	public List<Transaction> getTransactions(int accountNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSameBankTransaction(int sourceAccountNo, int targetAccountNo) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT COUNT(*) as count FROM account WHERE accountNo IN (?,?)")) {
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				if (result.getInt("count") == 2) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new CustomException("Validation failed!", e);
		}
	}

}
