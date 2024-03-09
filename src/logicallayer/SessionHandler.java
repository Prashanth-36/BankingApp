package logicallayer;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.User;
import persistentdao.SessionDao;
import persistentlayer.SessionManager;
import utility.Utils;

public class SessionHandler {

	public User authenticate(int userId, String password) throws CustomException, InvalidValueException {
		Utils.checkNull(password);
		SessionManager sessionManager = new SessionDao();
		return sessionManager.authenticate(userId, password);
	}

//	public View authorize(User currentUser) throws CustomException {
//		try {
//			if (currentUser instanceof Customer) {
//				Customer customer = (Customer) currentUser;
//				return new CustomerView(customer);
//			} else if (currentUser instanceof Employee) {
//				Employee employee = (Employee) currentUser;
//				switch (employee.getPrivilege()) {
//				case EMPLOYEE: {
//					return new EmployeeView(employee);
//				}
//
//				case ADMIN: {
//					return new AdminView(employee);
//				}
//				default:
//					throw new CustomException("Invalid employee privilege!");
//				}
//			} else {
//				throw new CustomException("Couldn't authorize user");
//			}
//
//		} catch (CustomException e) {
//			throw new CustomException("Authentication failed!", e);
//		}
//	}
}
