package DecoratorPattern.Product;

public abstract class Product {
	String name;
	double originalPrice;
	ProductType type;
	
	public Product()
	{
		
	}

	public abstract double getPrice();


	public ProductType getType() {
		return type;
	}

	public Product(String name, double originalPrice, ProductType type) {
		this.name = name;
		this.originalPrice = originalPrice;
		this.type = type;
	}
	
	
	
}
