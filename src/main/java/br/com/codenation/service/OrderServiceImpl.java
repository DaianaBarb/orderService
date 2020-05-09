package br.com.codenation.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.corba.se.spi.activation.Repository;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.scenario.effect.impl.prism.PrDrawable;

import br.com.codenation.model.OrderItem;
import br.com.codenation.model.Product;
import br.com.codenation.repository.ProductRepository;
import br.com.codenation.repository.ProductRepositoryImpl;

public class OrderServiceImpl implements OrderService {
	 private static final double DISCOUNT = 0.2;
	private ProductRepository productRepository = new ProductRepositoryImpl();

	@Override
	public Double calculateOrderValue(List<OrderItem> items) {
		/*
		 return items
				.stream()
				.mapToDouble((orderItem)-> {
			Optional<Product> produto=productRepository.findById(orderItem.getProductId());
			Double valor= produto.get().getValue();
			if(produto.get().getIsSale()) valor-=valor*DESCONTO;
			return orderItem.getQuantity()*valor;
			
		}).sum();
		 */
		
		
			return items.stream().mapToDouble(order->order.getQuantity()*getProductValue(productRepository.findById(order.getProductId()))).sum();
	}

	@Override
	public Set<Product> findProductsById(List<Long> ids) {
		
		

		return ids.stream()
				.map(id->productRepository
				.findById(id))
				.filter(Optional->Optional.isPresent())
				.map(Optional->Optional.get()).collect(Collectors.toSet());
		
		}

	@Override
	public Double calculateMultipleOrders(List<List<OrderItem>> orders) {
		
		return orders.stream()
				.mapToDouble(this::calculateOrderValue)
				.sum();
	}


	@Override
	public Map<Boolean, List<Product>> groupProductsBySale(List<Long> productIds) {
		return   findProductsById(productIds)
				.stream()
				.collect(Collectors
				.groupingBy(product->product.getIsSale()));
	}
	
	private Double getProductValue(Optional<Product> p) {
		
		if(!(p.isPresent())) {
			throw new NullPointerException("Product not found");
		}
		
		Product product=p.get();
		return product.getIsSale()?product.getValue()-(product.getValue()*DISCOUNT):product.getValue();
		
	}

}