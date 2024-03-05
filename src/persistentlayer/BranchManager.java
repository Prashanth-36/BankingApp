package persistentlayer;

import customexceptions.CustomException;
import model.Branch;

public interface BranchManager {

	void addBranch(Branch branch) throws CustomException;

	Branch getBranch(int branchId) throws CustomException;

	void removeBranch(int branchId) throws CustomException;

}
