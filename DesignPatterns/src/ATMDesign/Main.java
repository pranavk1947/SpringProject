package ATMDesign;

public class Main {
    public static void main(String[] args) {
        // Get an instance of the ATM
        ATM atm = ATM.getInstance();
        atm.initialzeATM();
        atm.addMoney(Notes.Rs_500, 50);
        atm.addMoney(Notes.Rs_100, 20);
        System.out.println("Current ATM Balance :: "+atm.getBalance());
        
        // Insert card
        atm.insertCard();

        // Enter PIN
        atm.enterPIN(1234);

        // Withdraw cash
        atm.withdrawCash(50000);
        System.out.println(atm.getBalance());
        // Eject card
        atm.ejectCard();
    }
}
