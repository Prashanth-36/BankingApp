package logicalLayer;

import CustomExceptions.CustomException;
import CustomExceptions.InvalidOperationException;
import model.Customer;
import persistentLayer.CustomerDao;
import persistentLayer.CustomerManager;

public class EmployeeHandler {

	static CustomerManager customerManager = new CustomerDao();

	public void addCustomer(Customer customer) throws InvalidOperationException, CustomException {
		int customerId = customerManager.getCustomerId(customer.getPanNo());
		if (customerId != -1) {
			throw new InvalidOperationException("Customer already exists with id:" + customerId);
		}
		customerManager.addCustomer(customer);
	}

	public void removeCustomer(int customerId) throws CustomException {
		customerManager.removeCustomer(customerId);
	}

}
