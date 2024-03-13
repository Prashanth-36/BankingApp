package persistentlayer;

import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import logicallayer.LRUCache;
import model.Account;
import utility.ActiveStatus;

public interface AccountManager {

	Cache<Integer, Account> accountCache = new LRUCache<>(50);

	Cache<Integer, Map<Integer, Account>> userAccountsCache = new LRUCache<>(10);

	default public Map<Integer, Account> getCachedUserAccounts(int customerId) throws CustomException {
		Map<Integer, Account> accounts = userAccountsCache.get(customerId);
		if (accounts != null) {
			return accounts;
		}
		accounts = getAccounts(customerId);
		userAccountsCache.set(customerId, accounts);
		return accounts;
	}

	default public Account getCachedAccount(int accountNo) throws CustomException, InvalidValueException {
		Account account = accountCache.get(accountNo);
		if (account != null) {
			return account;
		}
		account = getAccount(accountNo);
		accountCache.set(accountNo, account);
		return account;
	}

	void createAccount(Account account) throws CustomException, InvalidValueException;

	void deleteAccount(int accountNo) throws CustomException;

	double getCurrentBalance(int accountNo) throws CustomException, InvalidValueException;

	double getTotalBalance(int customerId) throws CustomException, InvalidValueException;

	boolean isValidAccount(int accountNo) throws CustomException;

	void setAccountStatus(int accountNo, ActiveStatus status) throws CustomException;

	void setPrimaryAccount(int customerId, int accountNo) throws CustomException;

	Account getAccount(int accountNo) throws InvalidValueException, CustomException;

	Map<Integer, Account> getAccounts(int customerId) throws CustomException;

	void checkValidRequest(int customerId, String mpin, int accountNo) throws InvalidValueException, CustomException;

	void setMpin(int accountNo, String newPin) throws CustomException, InvalidValueException;

}
