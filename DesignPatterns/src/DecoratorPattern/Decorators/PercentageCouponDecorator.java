package DecoratorPattern.Decorators;

import DecoratorPattern.Product.Product;

public class PercentageCouponDecorator extends CouponDecorator {

	Product product;
	int discountpercent;
	
	public PercentageCouponDecorator(Product product, int discountpercent) {
		this.product = product;
		this.discountpercent = discountpercent;
	}



	@Override
	public double getPrice() {
		double price = product.getPrice();
		return price - (price*discountpercent)/100;
	}
	

}
