package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Customer;
import utility.ActiveStatus;

public interface CustomerManager {

	void addCustomer(Customer customer) throws CustomException;

	void removeCustomer(int customerId) throws CustomException;

	Customer getCustomer(int id) throws CustomException, InvalidValueException;

	int getCustomerId(String panNo) throws CustomException;

	List<Customer> getCustomers(int branchId, int offset, int limit, ActiveStatus status) throws CustomException;

	int getCustomersCount(int branchId, ActiveStatus status) throws CustomException;

	List<Integer> getCustomerBranches(int customerId) throws CustomException;

}
