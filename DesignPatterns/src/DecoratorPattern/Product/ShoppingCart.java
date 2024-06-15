package DecoratorPattern.Product;

import java.util.ArrayList;
import java.util.List;

import DecoratorPattern.Decorators.PercentageCouponDecorator;
import DecoratorPattern.Decorators.TypeCouponDecorator;

public class ShoppingCart {
	List<Product> productList;
	
	public ShoppingCart() {
		productList = new ArrayList<>();
	}
	
	public void addToCart(Product product) {
		Product productsWithELigibleDiscount = new TypeCouponDecorator((new PercentageCouponDecorator(product, 10)), 5, product.getType());
		productList.add(productsWithELigibleDiscount);
	}
	
	public int getTotalPrice() {
		int totalPrice  = 0;
		for(Product product : productList) {
			totalPrice+=product.getPrice();
		}
		return totalPrice;
	}
}
