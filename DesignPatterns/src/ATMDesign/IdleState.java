package ATMDesign;

public class IdleState implements ATMState {
	private ATM atm;

	public IdleState(ATM atm) {
		System.out.println("ATM in IdleState. Insert Card to begin");
		this.atm = atm;
	}

	@Override
	public void insertCard() {
		System.out.println("Card Inserted.");
		System.out.println("Please enter your PIN.");
		atm.setState(new TransactionState(atm));
	}

	@Override
	public void enterPIN(int pin) {
		System.out.println("Please insert your card first.");
	}

	@Override
	public void withdrawCash(int amount) {
		System.out.println("Please insert your card first.");
	}

	@Override
	public void ejectCard() {
		System.out.println("No card to eject.");
	}


}
