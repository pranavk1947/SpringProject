package ObserverPattern.Observer;

import ObserverPattern.Observable.StocksObservable;

public class MobileAlertObserverImpl implements NotificationAlertObserver{
	String phoneNo;
	StocksObservable observable;
	
	
	
	public MobileAlertObserverImpl(String phoneNo, StocksObservable observable) {
		this.phoneNo = phoneNo;
		this.observable = observable;
	}



	@Override
	public void update() {
		// TODO Auto-generated method stub
		sendMessage(phoneNo, "Product is in Stock! Hurry Up!!!");
	}



	private void sendMessage(String phoneNo, String string) {
		System.out.println("Mail sent to : "+ phoneNo);
		
	}
	
	
	
}
