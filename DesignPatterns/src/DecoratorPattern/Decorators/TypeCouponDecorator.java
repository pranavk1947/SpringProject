package DecoratorPattern.Decorators;

import java.util.ArrayList;
import java.util.List;

import DecoratorPattern.Product.Product;
import DecoratorPattern.Product.ProductType;

public class TypeCouponDecorator extends CouponDecorator {

	Product product;
	int discountpercent;
	ProductType type;
	static List<ProductType> eligibleTypes = new ArrayList<>();
	
	static {
		eligibleTypes.add(ProductType.FURNITURE);
	}
	
	public TypeCouponDecorator(Product product, int discountpercent, ProductType type) {
		this.product = product;
		this.discountpercent = discountpercent;
		this.type = type;
	}



	@Override
	public double getPrice() {
		double price = product.getPrice();
		if(eligibleTypes.contains(type)) {
			return price - (price*discountpercent)/100;
		}
		return price;
	}
	

}
