package VendingMachine.VendingMachineStates;

import java.util.List;

import VendingMachine.Coin;
import VendingMachine.Item;
import VendingMachine.VendingMachine;

public class DispenseState implements State {
	
	
	DispenseState(VendingMachine machine, int codeNumber) throws Exception{
		System.out.println("VM in DispenseState");
		dispenseProduct(machine,codeNumber);
	}
	
	@Override
	public void clickOnInsertCoinButton(VendingMachine machine) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public void clickOnStartProductSelectionButton(VendingMachine machine) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public void insertCoin(VendingMachine machine, Coin coin) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public void chooseProduct(VendingMachine machine, int codeNumber) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

	@Override
	public int getChange(int returnChangeMoney) throws Exception {
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public Item dispenseProduct(VendingMachine machine, int codeNumber) throws Exception {
		System.out.println("Dispensing Product");
		Item item = machine.getInventory().getItem(codeNumber);
		machine.getInventory().updateSoldOutItem(codeNumber);
		machine.setVendingMachineState(new IdleState(machine));
		return item;
				
	}

	@Override
	public List<Coin> refundFullMoney(VendingMachine machine) throws Exception {
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public void updateInventory(VendingMachine machine, Item item, int codeNumber) throws Exception {
		throw new Exception("Invalid Operation Selected");

	}

}
