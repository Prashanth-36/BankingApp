package logicallayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import model.Customer;
import persistentdao.CustomerDao;
import persistentlayer.CustomerManager;

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

	public Customer getCustomer(int customerId) throws CustomException {
		return customerManager.getCustomer(customerId);
	}

	public List<Customer> getCustomers(int branchId) throws CustomException {
		return customerManager.getCustomers(branchId);
	}

}
