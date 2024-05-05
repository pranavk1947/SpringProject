package VendingMachine.VendingMachineStates;

import java.util.List;

import VendingMachine.Coin;
import VendingMachine.Item;
import VendingMachine.VendingMachine;

public class HasMoneyState implements State {

	public HasMoneyState() {
		System.out.println("VM in hasMoneyState");
	}
	
	@Override
	public void clickOnInsertCoinButton(VendingMachine machine) throws Exception {
		return;

	}

	@Override
	public void clickOnStartProductSelectionButton(VendingMachine machine) throws Exception {
		machine.setVendingMachineState(new SelectionState());
			
	}

	@Override
	public void insertCoin(VendingMachine machine, Coin coin) throws Exception {
		System.out.println("Accepted the coin");
		machine.getCoinList().add(coin);

	}

	@Override
	public void chooseProduct(VendingMachine machine, int codeNumber) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int getChange(int returnChangeMoney) throws Exception {
		throw new Exception("Invalid Operation Selected");
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
