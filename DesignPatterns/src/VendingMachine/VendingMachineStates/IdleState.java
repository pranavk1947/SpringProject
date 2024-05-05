package VendingMachine.VendingMachineStates;

import java.util.ArrayList;
import java.util.List;

import VendingMachine.Coin;
import VendingMachine.Item;
import VendingMachine.VendingMachine;

public class IdleState implements State{
	
	public IdleState() {
		System.out.println("VM in Idle State");
	}
	
	public IdleState(VendingMachine machine) {
		System.out.println("VM in Idle State");
		machine.setCoinList(new ArrayList<>());
	}
	
	@Override
	public void clickOnInsertCoinButton(VendingMachine machine) throws Exception {
		machine.setVendingMachineState(new HasMoneyState());
		
	}

	@Override
	public void clickOnStartProductSelectionButton(VendingMachine machine) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Invalid Operation Selected");
		
	}

	@Override
	public void insertCoin(VendingMachine machine, Coin coin) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public void chooseProduct(VendingMachine machine, int codeNumber) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public int getChange(int returnChangeMoney) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public Item dispenseProduct(VendingMachine machine, int codeNumber) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public List<Coin> refundFullMoney(VendingMachine machine) throws Exception {
		throw new Exception("Invalid Operation Selected");
	}

	@Override
	public void updateInventory(VendingMachine machine, Item item, int codeNumber) throws Exception {
		// TODO Auto-generated method stub
		machine.getInventory().addItem(item, codeNumber);
	}

}
