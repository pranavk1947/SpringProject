package ATMDesign;

public class AuthenticationManager {

	public static boolean verifyPin(int pin) {
		if(pin == 1234) {
			return true;
		}
		return false;
	}
	
}
