package com.exam.ans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.exam.RestAns;
import com.exam.entity.Order;
@XmlRootElement
public class OrderListAns extends RestAns {

	private static final long serialVersionUID = 2015L;

	private List<Order> orders;

	public List<Order> getOrders() {
		if (orders == null)
			orders = new ArrayList<>();
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
