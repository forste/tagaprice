package org.tagaprice.client.gwt.server.mock;

import java.util.*;

import org.tagaprice.client.gwt.shared.entities.*;
import org.tagaprice.client.gwt.shared.entities.dump.*;
import org.tagaprice.client.gwt.shared.entities.productmanagement.*;
import org.tagaprice.client.gwt.shared.entities.productmanagement.Package;
import org.tagaprice.client.gwt.shared.logging.*;
import org.tagaprice.client.gwt.shared.rpc.productmanagement.IProductService;
import org.tagaprice.core.entities.Unit;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProductServiceImpl extends RemoteServiceServlet implements
IProductService {

	MyLogger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	int productIdCounter = 1;
	HashMap<IRevisionId, IProduct> productsAllRevisions = new HashMap<IRevisionId, IProduct>();
	HashMap<Long, IProduct> productsLatest = new HashMap<Long, IProduct>();
	ArrayList<ICategory> categories = new ArrayList<ICategory>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 5070553288173843014L;

	/**
	 * Initialization with some products and Categories.
	 */
	public ProductServiceImpl() {
		logger.log("Load servlet...");
		ICategory root = new Category("root");
		this.categories.add(root);
		ICategory food = new Category("food");
		this.categories.add(food);
		ICategory vegetables = new Category("vegetables");
		this.categories.add(vegetables);
		ICategory beverages = new Category("beverages");
		this.categories.add(beverages);
		ICategory alcoholics = new Category("alcohol");
		this.categories.add(alcoholics);
		ICategory nonalcoholics = new Category("nonalcoholics");
		this.categories.add(nonalcoholics);

		nonalcoholics.setParentCategory(beverages);
		alcoholics.setParentCategory(beverages);
		vegetables.setParentCategory(food);
		beverages.setParentCategory(root);
		food.setParentCategory(root);

		//TestProduct
		Product bergkasese = new Product("Bergkäse", food, new Quantity(100, Unit.g));
		bergkasese.addPackage(new Package(new Quantity(500, Unit.g)));
		bergkasese.addPackage(new Package(new Quantity(750, Unit.g)));

		this.saveProduct(bergkasese);
		this.saveProduct(new Product("Extrawurst von der Theke", food, new Quantity(0, Unit.g)));
		this.saveProduct(new Product("Limonade", nonalcoholics, new Quantity(1.5, Unit.l)));

		System.out.println("ProductService startet. Size is " + this.productsAllRevisions.size() + ", " + this.productsLatest.size() + ". Counter is " + this.productIdCounter + ".");

	}


	@Override
	public IProduct getProduct(IRevisionId revionsId) {
		if(revionsId != null) {
			if(revionsId.getRevision() == 0L) {
				IProduct product = this.productsLatest.get(revionsId.getId());
				if(product != null) {
					return product.copy();
				} else {
					return null;
				}
			} else {
				IProduct product = this.productsAllRevisions.get(revionsId);
				if(product != null) {
					return product.copy();
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	@Override
	public IProduct saveProduct(final IProduct product) {
		logger.log("save product " + product);
		//Check productId and revisionId
		//if productId == 0 -> save as new product
		if(product.getRevisionId() == null || product.getRevisionId().getId() == 0L) {
			logger.log("new product");
			//SAVE
			//make a copy ... to get sure
			IProduct newProduct = product.copy();
			//set a productID and Revision 1
			newProduct.setRevisionId(new RevisionId(this.productIdCounter++, 1));
			//Save it into the hashmaps
			this.productsAllRevisions.put(newProduct.getRevisionId(), newProduct);
			this.productsLatest.put(newProduct.getRevisionId().getId(), newProduct);

			return newProduct;
		} else {
			logger.log("update product");
			//UPDATE
			IProduct updateProduct = this.productsAllRevisions.get(product.getRevisionId());
			if(updateProduct == null) {
				//ERROR
				return null;
			} else {
				//else
				//get product
				//get latest revision
				//compare revisionIds
				updateProduct = product.copy();
				updateProduct.getRevisionId().setRevision(updateProduct.getRevisionId().getRevision() + 1);
				this.productsAllRevisions.put(updateProduct.getRevisionId(), updateProduct);
				this.productsLatest.put(updateProduct.getRevisionId().getId(), updateProduct);
				return updateProduct;
			}
		}
	}

	@Override
	public ArrayList<IProduct> getProducts(IProduct searchCriteria) {
		logger.log("getProducts... searchCriteria: "+ searchCriteria);
		ArrayList<IProduct> products = new ArrayList<IProduct>();
		for (IProduct p: this.productsLatest.values()) {
			if(searchCriteria != null && p.getTitle().toLowerCase().contains((searchCriteria.getTitle().toLowerCase()))) {
				products.add(p.copy());
			} else if(searchCriteria == null) {
				products.add(p.copy());
			}
		}
		return products;
	}

	@Override
	public ArrayList<ICategory> getCategories() {
		logger.log("return categories");
		return this.categories;
	}
}
