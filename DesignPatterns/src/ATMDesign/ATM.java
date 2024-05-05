package ATMDesign;

//ATM class acting as the context and implementing the Singleton pattern
class ATM {
	private static ATM instance = null;
	private ATMState state;
	private CashManager cashManager;
	// private int balance; // Balance in the ATM

	private ATM() {
		cashManager = new CashManager();
	}
	public static ATM getInstance() {
		if (instance == null) {
			instance = new ATM();
		}
		return instance;
	}
	
	public void initialzeATM() {
		instance.state = new IdleState(instance);
	}

	public ATMState getState() {
		return state;
	}

	public void setState(ATMState state) {
		this.state = state;
	}

	public void insertCard() {
		state.insertCard();
	}

	public void enterPIN(int pin) {
		state.enterPIN(pin);
	}

	public void withdrawCash(int amount) {
		state.withdrawCash(amount);
	}

	public int getBalance() {
		return cashManager.getBalance();
	}

	public boolean addMoney(Notes notetype, int count) {
		cashManager.addNotes(notetype, count);

		return false;

	}

	public void ejectCard() {
		state.ejectCard();
	}

	public CashManager getCashManager() {
		return cashManager;
	}

}
