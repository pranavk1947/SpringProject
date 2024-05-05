package ATMDesign;

public class TransactionState implements ATMState{
	ATM atm = ATM.getInstance();

    public TransactionState(ATM atm) {
        this.atm = atm;
    }
	@Override
	public void insertCard() {
		System.out.println("Card already Inserted");
		
	}

	@Override
	public void enterPIN(int pin) {
		boolean valid = AuthenticationManager.verifyPin(pin);
		if(valid == true) {
			System.out.println("Valid Pin. Enter Amout to Withdraw");
		}
		else {
			System.out.println("Invalid Pin Entered. Re-insert Card and Enter Correct Pin");
			atm.setState(new IdleState(atm));
		}
		
	}

	@Override
	public void withdrawCash(int amount) {
		System.out.println("Cash Requested: " + amount);
		//System.out.println("Ejecting Cash : ");
		boolean cashTaken = atm.getCashManager().withdrawCash(amount);
		if(cashTaken) {
			System.out.println("Plz collect Cash and eject Card");
			//this.ejectCard();
		}
		else {
			System.out.println("Cash Withdrawal Failed");
			//this.ejectCard();
		}
		
	}

	@Override
	public void ejectCard() {
		System.out.println("Transaction Completed. Please Collect Your Card");
		atm.setState(new IdleState(atm));
		
	}

}
