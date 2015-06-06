package com.exam.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.exam.entity.Order;
import com.exam.entity.OrderStatus;

public class OrderDB {

	private static final Map<OrderStatus, List<Order>> STATUS_KEY_MAP = new HashMap<>();

	private static final List<Order> ORDERS = new ArrayList<>();

	private static final AtomicLong IDGENERATOR = new AtomicLong(0);

	public static Order add(Order order) {
		if (order == null) {
			return null;
		}
		order.setId(IDGENERATOR.incrementAndGet());
		getByStatus(order.getOrderStatus()).add(order);
		ORDERS.add(order);
		return order;
	}

	private static List<Order> getByStatus(OrderStatus status) {
		if (!STATUS_KEY_MAP.containsKey(status))
			STATUS_KEY_MAP.put(status, new ArrayList<Order>());
		return STATUS_KEY_MAP.get(status);
	}

	public static List<Order> findByStatus(OrderStatus status) {
		if (status == null)
			return ORDERS;
		return new ArrayList<Order>(getByStatus(status));
	}
	
	public static void clear(){
		STATUS_KEY_MAP.clear();
		ORDERS.clear();
		IDGENERATOR.set(0);
	}

}
