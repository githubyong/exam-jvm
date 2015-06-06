package com.exam.ans;

import javax.xml.bind.annotation.XmlRootElement;

import com.exam.RestAns;
import com.exam.entity.Order;
@XmlRootElement
public class OrderCreateAns extends RestAns {

	private static final long serialVersionUID = 2015L;

	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
