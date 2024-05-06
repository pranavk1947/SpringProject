package ObserverPattern;

import ObserverPattern.Observable.IphoneObservableImpl;
import ObserverPattern.Observable.StocksObservable;
import ObserverPattern.Observer.EmailAlertObserverImpl;
import ObserverPattern.Observer.MobileAlertObserverImpl;
import ObserverPattern.Observer.NotificationAlertObserver;

public class Store {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StocksObservable iphoneStockObservable = new IphoneObservableImpl();
		
		NotificationAlertObserver obs1 = new EmailAlertObserverImpl("abc@gmail.com", iphoneStockObservable);
		NotificationAlertObserver obs2 = new EmailAlertObserverImpl("def@gmail.com", iphoneStockObservable);
		NotificationAlertObserver obs3 = new MobileAlertObserverImpl("123456789", iphoneStockObservable);
		
		
		iphoneStockObservable.add(obs1);
		iphoneStockObservable.add(obs2);
		iphoneStockObservable.add(obs3);
		
		iphoneStockObservable.setStockCount(10);
		
	}

}
