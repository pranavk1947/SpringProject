package ATMDesign;

import java.util.HashMap;
import java.util.Map;


class CashManager {
	private Map<Notes, Integer> noteCounts; // Map to store counts of available notes (key: note value, value: count)
    private int balance;
    public CashManager() {
        noteCounts = new HashMap<>();
        balance = 0;
    }

    public void addNotes(Notes noteValue, int count) {
        noteCounts.put(noteValue, noteCounts.getOrDefault(noteValue, 0) + count);
        balance+=noteValue.getValue()*count; 
    }
    
    public int getBalance() {
    	int bal = 0;
    	for(Map.Entry<Notes, Integer> entry : noteCounts.entrySet()) {
    		Notes note = entry.getKey();
            int noteValue = note.getValue();
            int count = entry.getValue();
            
            bal += noteValue*count; 
    	}
		return bal;
    }

    public void getNotesStatus() {
        System.out.println("Rs_500 Notes :: " + noteCounts.getOrDefault(Notes.Rs_500, 0));
        System.out.println("Rs_100 Notes :: " + noteCounts.getOrDefault(Notes.Rs_100, 0));
        System.out.println("ATM Balance is : " + getBalance());
    }

    public boolean withdrawCash(int amount) {
        if (this.getBalance() >= amount) {
            // Start dispensing the cash
            Map<Notes, Integer> notesToDispense = new HashMap<>();

            for (Map.Entry<Notes, Integer> entry : noteCounts.entrySet()) {
                Notes note = entry.getKey();
                int noteValue = note.getValue();
                int count = entry.getValue();

                int notesToWithdraw = Math.min(amount / noteValue, count); // Calculate how many notes of this value to withdraw
                if (notesToWithdraw > 0) {
                    notesToDispense.put(note, notesToWithdraw);
                    amount -= notesToWithdraw * noteValue;
                }

                if (amount == 0) {
                    // Successfully dispensed all required cash
                    // Update noteCounts after successful withdrawal
                    for (Map.Entry<Notes, Integer> dispensedNote : notesToDispense.entrySet()) {
                        noteCounts.put(dispensedNote.getKey(), noteCounts.get(dispensedNote.getKey()) - dispensedNote.getValue());
                        
                    }
                    balance -= amount; // Update balance
                    this.setBalance(balance);
                    
                    System.out.println("Cash dispensed successfully");
                    System.out.println("Current Balance of ATM: " + balance);
                    return true;
                }
            }
            // If we reach here, it means the withdrawal cannot be fully dispensed with available notes
            System.out.println("Unable to dispense cash due to insufficient denominations.");
        } else {
            System.out.println("Unable to dispense cash. Insufficient balance.");
        }
        return false;
    }


	public Map<Notes, Integer> getNoteCounts() {
		return noteCounts;
	}

	public void setNoteCounts(Map<Notes, Integer> noteCounts) {
		this.noteCounts = noteCounts;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
