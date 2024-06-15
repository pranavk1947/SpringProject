package DecoratorPattern;

import DecoratorPattern.Product.Item1;
import DecoratorPattern.Product.Product;
import DecoratorPattern.Product.ProductType;
import DecoratorPattern.Product.ShoppingCart;

public class Main {
	Product item1 = new Item1("PHONE",10000,ProductType.ELECTRONIC);
	Product item2 = new Item1("SOFA",5000,ProductType.FURNITURE);
	
	ShoppingCart cart = new ShoppingCart();
	//add items to cart
}
