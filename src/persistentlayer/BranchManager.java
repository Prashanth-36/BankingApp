package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Branch;
import utility.ActiveStatus;

public interface BranchManager {

	void addBranch(Branch branch) throws CustomException;

	Branch getBranch(int branchId) throws CustomException, InvalidValueException;

	void removeBranch(int branchId) throws CustomException;

	boolean isValidBranch(int branchId) throws InvalidValueException, CustomException;

	List<Branch> getBranches(ActiveStatus status) throws CustomException;
}
