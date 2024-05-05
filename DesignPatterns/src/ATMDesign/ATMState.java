package ATMDesign;

public interface ATMState {
		public void insertCard();
		public void enterPIN(int pin);
		public void withdrawCash(int amount);
		public void ejectCard(); 
}
