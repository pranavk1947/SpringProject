package ATMDesign;

public class OutOfCashState implements ATMState{
	ATM atm = ATM.getInstance();
	
	
	@Override
	public void insertCard() {
		System.out.println("ATM out of cash. Cannot accept card");
		
	}

	@Override
	public void enterPIN(int pin) {
		System.out.println("ATM out of cash");
	}

	public void withdrawCash(int amount) {
		System.out.println("ATM out of cash");
	}

	@Override
	public void ejectCard() {
		System.out.println("ATM out of cash");
		atm.setState(new IdleState(atm));
	}

}
