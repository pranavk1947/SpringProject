package VendingMachine.VendingMachineStates;

import java.util.List;

import VendingMachine.Coin;
import VendingMachine.Item;
import VendingMachine.VendingMachine;

public class SelectionState implements State {

	public SelectionState() {
		System.out.println("VM in SelectionState");
	}
	
	@Override
	public void clickOnInsertCoinButton(VendingMachine machine) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public void clickOnStartProductSelectionButton(VendingMachine machine) throws Exception {
		return;

	}

	@Override
	public void insertCoin(VendingMachine machine, Coin coin) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public void chooseProduct(VendingMachine machine, int codeNumber) throws Exception {
		Item item = machine.getInventory().getItem(codeNumber);
		
		int paidByUser = 0;
		for(Coin coin : machine.getCoinList()) {
			paidByUser +=coin.value;
		}
		
		if(paidByUser >= item.getPrice()) {
			if(paidByUser > item.getPrice()) {
				getChange(paidByUser-item.getPrice());
			}
			machine.setVendingMachineState(new DispenseState(machine,codeNumber));
		}
	}

	@Override
	public int getChange(int returnChangeMoney) throws Exception {
		System.out.println("Returning Change : " + returnChangeMoney);
		return returnChangeMoney;
	}

	@Override
	public Item dispenseProduct(VendingMachine machine, int codeNumber) throws Exception {
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public List<Coin> refundFullMoney(VendingMachine machine) throws Exception {
		System.out.println("Returning full amount");
		machine.setVendingMachineState(new IdleState(machine));
		return machine.getCoinList();
	}

	@Override
	public void updateInventory(VendingMachine machine, Item item, int codeNumber) throws Exception {
		throw new Exception("Invalid Operation Selected");
	}

}
