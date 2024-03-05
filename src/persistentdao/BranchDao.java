package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import customexceptions.CustomException;
import model.Branch;
import persistentlayer.BranchManager;

public class BranchDao implements BranchManager {

	@Override
	public void addBranch(Branch branch) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection
				.prepareStatement("INSERT INTO branch(ifsc,location,city,state) values(?,?,?,?)")) {
			statement.setString(1, branch.getIfsc());
			statement.setString(2, branch.getLocation());
			statement.setString(3, branch.getCity());
			statement.setString(4, branch.getState());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Branch Creation failed!", e);
		}
	}

	@Override
	public Branch getBranch(int branchId) throws CustomException {
		Connection connection = DBConnection.getConnection();
		Branch branch = new Branch();
		try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM branch WHERE id = ?")) {
			statement.setInt(1, branchId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Branch fetch failed!", e);
		}
		return branch;
	}

	@Override
	public void removeBranch(int branchId) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement("DELETE FROM branch WHERE id = ?")) {
			statement.setInt(1, branchId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Branch Deletion failed!", e);
		}
	}

}
