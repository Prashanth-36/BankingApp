package persistentLayer;

import model.Account;
import model.Customer;

public interface AccountManager {
	
	void createAccount(Customer customer,Account account);
	
	void createAccount(int customerId,Account account);
	
	void deleteAccount(int accountNo);
	
}
