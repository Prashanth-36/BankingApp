package persistentLayer;

import CustomExceptions.CustomException;
import model.Customer;

public interface CustomerManager {

	void addCustomer(Customer customer) throws CustomException;

	void removeCustomer(int customerId) throws CustomException;

	Customer getCustomer(int id);

	int getCustomerId(String panNo) throws CustomException;

}
