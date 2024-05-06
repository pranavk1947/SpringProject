package ObserverPattern.Observer;

import ObserverPattern.Observable.StocksObservable;

public class EmailAlertObserverImpl implements NotificationAlertObserver{
	
	String emailId;
	StocksObservable observable;
	
	
	
	public EmailAlertObserverImpl(String emailId, StocksObservable observable) {
		this.emailId = emailId;
		this.observable = observable;
	}



	@Override
	public void update() {
		// TODO Auto-generated method stub
		sendMail(emailId, "Product is in Stock! Hurry Up!!!");
	}



	private void sendMail(String emailId, String string) {
		System.out.println("Mail sent to : "+ emailId);
		
	}
	
	
	
	
}
