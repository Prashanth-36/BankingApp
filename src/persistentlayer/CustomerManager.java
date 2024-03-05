package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import model.Customer;

public interface CustomerManager {

	void addCustomer(Customer customer) throws CustomException;

	void removeCustomer(int customerId) throws CustomException;

	Customer getCustomer(int id) throws CustomException;

	int getCustomerId(String panNo) throws CustomException;

	List<Customer> getCustomers(int branchId) throws CustomException;

}
