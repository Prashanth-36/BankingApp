package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Account;
import persistentlayer.AccountManager;
import utility.ActiveStatus;

public class AccountDao implements AccountManager {

	@Override
	public void createAccount(Account account) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO account(customerId,openDate,branchId,status,isPrimaryAccount,mpin) values(?,?,?,?,?,?)")) {
			statement.setInt(1, account.getCustomerId());
			statement.setLong(2, System.currentTimeMillis());
			statement.setInt(3, account.getBranchId());
			statement.setString(4, ActiveStatus.ACTIVE.name());
			statement.setBoolean(5, account.isPrimaryAccout());
			if (account.getMpin() == null) {
				account.setMpin("0000");
			}
			statement.setString(6, account.getMpin());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Account Creation failed!", e);
		}
	}

	@Override
	public void deleteAccount(int accountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"UPDATE account SET status = " + ActiveStatus.INACTIVE + " WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Account Deletion failed!", e);
		}
	}

	@Override
	public double getCurrentBalance(int accountNo) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT currentBalance FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet resultSet = statement.executeQuery();) {
				if (resultSet.next()) {
					return resultSet.getDouble(1);
				}
			}
			throw new InvalidValueException("Invalid account number!");
		} catch (SQLException e) {
			throw new CustomException("Balance fetch failed!", e);
		}
	}

	@Override
	public double getTotalBalance(int customerId) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT SUM(currentBalance) FROM account WHERE customerId = ?")) {
			statement.setInt(1, customerId);
			try (ResultSet resultSet = statement.executeQuery();) {
				if (resultSet.next()) {
					return resultSet.getDouble(1);
				}
			}
			throw new InvalidValueException("Invalid account number!");
		} catch (SQLException e) {
			throw new CustomException("Balance fetch failed!", e);
		}
	}

	@Override
	public boolean isValidAccount(int accountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) as count FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					if (result.getInt("count") == 1) {
						return true;
					}
				}
			}
			return false;
		} catch (SQLException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	@Override
	public void setAccountStatus(int accountNo, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE account SET status = ? WHERE accountNo = ?")) {
			statement.setString(1, status.name());
			statement.setInt(2, accountNo);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Account Deletion failed!", e);
		}
	}

	@Override
	public Account getAccount(int accountNo) throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					Account account = new Account();
					account.setAccountNo(result.getInt("accountNo"));
					account.setBranchId(result.getInt("branchId"));
					account.setCurrentBalance(result.getDouble("currentBalance"));
					account.setCustomerId(result.getInt("customerId"));
					account.setOpenDate(result.getLong("openDate"));
					account.setPrimaryAccout(result.getBoolean("isPrimaryAccount"));
					account.setStatus(ActiveStatus.valueOf(result.getString("status")));
					return account;
				}
			}
			throw new InvalidValueException("Invalid Account number!");
		} catch (SQLException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	@Override
	public List<Account> getAccounts(int customerId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM account WHERE customerId = ?")) {
			statement.setInt(1, customerId);
			try (ResultSet result = statement.executeQuery();) {
				List<Account> accounts = new ArrayList<Account>();
				while (result.next()) {
					accounts.add(resultSetToAccount(result));
				}
				return accounts;
			}
		} catch (SQLException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	private Account resultSetToAccount(ResultSet result) throws SQLException {
		Account account = new Account();
		account.setAccountNo(result.getInt("accountNo"));
		account.setBranchId(result.getInt("branchId"));
		account.setCurrentBalance(result.getDouble("currentBalance"));
		account.setCustomerId(result.getInt("customerId"));
		account.setOpenDate(result.getLong("openDate"));
		account.setPrimaryAccout(result.getBoolean("isPrimaryAccount"));
		account.setStatus(ActiveStatus.valueOf(result.getString("status")));
		return account;
	}

	@Override
	public void setPrimaryAccount(int customerId, int accountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement offStatement = connection.prepareStatement(
						"UPDATE account SET isPrimaryAccount = 0 WHERE customerId = ? and isPrimaryAccount = 1");
				PreparedStatement setStatement = connection
						.prepareStatement("UPDATE account SET isPrimaryAccount = 1 WHERE accountNo = ?")) {
			offStatement.setInt(1, customerId);
			setStatement.setInt(1, accountNo);
			try {
				connection.setAutoCommit(false);
				offStatement.executeUpdate();
				setStatement.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new CustomException("Primary Account Modification failed!", e);
		}
	}

	@Override
	public void checkValidRequest(int customerId, String mpin, int accountNo)
			throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT customerId,mpin FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
//			statement.setString(2, mpin);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					if (customerId != resultSet.getInt(1)) {
						throw new InvalidValueException("Invalid account number and customer id!");
					}
					if (!mpin.equals(resultSet.getString(2))) {
						throw new InvalidValueException("Invalid MPIN!");
					}
				} else {
					throw new InvalidValueException("Invalid account number!");
				}
//				List<String> mpins = new ArrayList<>();
//				while (resultSet.next()) {
//					accounts.add(resultSet.getInt(1));
//				}
//				if (accounts.isEmpty()) {
//					throw new InvalidValueException("Customer id and mpin doesn't match!");
//				}
//				if (!accounts.contains(accountNo)) {
//					throw new InvalidValueException("Invalid account number!");
//				}
			}
		} catch (SQLException e) {
			throw new CustomException("Request Validation failed!");
		}
	}

	@Override
	public void setMpin(int accountNo, String newPin) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE account SET mPin = ? WHERE accountNo = ?")) {
			statement.setString(1, newPin);
			statement.setInt(2, accountNo);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("MPIN Updation failed!", e);
		}
	}

}
