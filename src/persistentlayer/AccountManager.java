package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Account;
import utility.ActiveStatus;

public interface AccountManager {

	void createAccount(Account account) throws CustomException;

	void deleteAccount(int accountNo) throws CustomException;

	double getCurrentBalance(int accountNo) throws CustomException, InvalidValueException;

	double getTotalBalance(int customerId) throws CustomException, InvalidValueException;

	boolean isValidAccount(int accountNo) throws CustomException;

	void setAccountStatus(int accountNo, ActiveStatus status) throws CustomException;

	void setPrimaryAccount(int customerId, int accountNo) throws CustomException;

	Account getAccount(int accountNo) throws InvalidValueException, CustomException;

	List<Account> getAccounts(int customerId) throws CustomException;

	void checkValidRequest(int customerId, String mpin, int accountNo) throws InvalidValueException, CustomException;

	void setMpin(int accountNo, String newPin) throws CustomException;

}
