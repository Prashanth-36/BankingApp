package inputlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.Customer;

public class CustomerView {
	static Logger logger = Logger.getLogger(Runner.class.getName());

	private Customer profile;

	public CustomerView(Customer currentUser) {
		this.setProfile(currentUser);
	}

	public void handler() {
		logger.log(Level.INFO, "Welcome " + profile.getName());
	}

	public Customer getProfile() {
		return profile;
	}

	public void setProfile(Customer profile) {
		this.profile = profile;
	}

}
