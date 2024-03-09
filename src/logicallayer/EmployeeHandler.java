package logicallayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Account;
import model.Customer;
import persistentdao.AccountDao;
import persistentdao.BranchDao;
import persistentdao.CustomerDao;
import persistentlayer.AccountManager;
import persistentlayer.BranchManager;
import persistentlayer.CustomerManager;
import utility.ActiveStatus;
import utility.Utils;

public class EmployeeHandler {

	static CustomerManager customerManager = new CustomerDao();

	static BranchManager branchManager = new BranchDao();

	static AccountManager accountManager = new AccountDao();

	public void addCustomer(Customer customer)
			throws InvalidOperationException, CustomException, InvalidValueException {
		Utils.checkNull(customer);
		int customerId = customerManager.getCustomerId(customer.getPanNo());
		if (customerId != -1) {
			throw new InvalidOperationException("Customer already exists with id:" + customerId);
		}
		customerManager.addCustomer(customer);
	}

	public void removeCustomer(int customerId) throws CustomException {
		customerManager.removeCustomer(customerId);
	}

	public Customer getCustomer(int customerId) throws CustomException, InvalidValueException {
		return customerManager.getCustomer(customerId);
	}

	public int getCustomerId(String panNo) throws CustomException, InvalidValueException {
		return customerManager.getCustomerId(panNo);
	}

	public List<Customer> getCustomers(int branchId, int pageNo, int limit, ActiveStatus status)
			throws CustomException, InvalidValueException {
		int offset = Utils.pagination(pageNo, limit);
		return customerManager.getCustomers(branchId, offset, limit, status);
	}

	public int getCustomerPageCount(int branchId, int limit, ActiveStatus status)
			throws InvalidValueException, CustomException {
		if (!branchManager.isValidBranch(branchId)) {
			throw new InvalidValueException("Invalid Branch id");
		}
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int totalCustomers = customerManager.getCustomersCount(branchId, status);
		int pageCount = (int) Math.ceil((double) totalCustomers / limit);
		return pageCount;
	}

	public void createAccount(Account account)
			throws CustomException, InvalidValueException, InvalidOperationException {
		Utils.checkNull(account);
		int customerId = account.getCustomerId();
		customerManager.getCustomer(customerId); // to validate existing customer
		List<Integer> customerBranches = customerManager.getCustomerBranches(customerId);
		if (customerBranches.contains(account.getBranchId())) {
			throw new InvalidOperationException("Customer has an existing account in this branch!");
		}
		if (customerBranches.isEmpty()) {
			account.setPrimaryAccout(true);
		} else {
			account.setPrimaryAccout(false);
		}
		accountManager.createAccount(account);
	}

	public void deleteAccount(int accountNo) throws CustomException {
		accountManager.deleteAccount(accountNo);
	}

	public Account getAccount(int accountNo) throws InvalidValueException, CustomException {
		return accountManager.getAccount(accountNo);
	}

}
