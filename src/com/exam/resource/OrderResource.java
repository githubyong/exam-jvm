package com.exam.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.exam.RestAns;
import com.exam.ans.OrderCreateAns;
import com.exam.db.OrderDB;
import com.exam.entity.Order;
import com.exam.util.JsonUtils;

public class OrderResource {
	private static final Logger logger = LogManager.getLogger(OrderResource.class);

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	public OrderResource(UriInfo uriInfo, Request request) {
		this.uriInfo = uriInfo;
		this.request = request;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public RestAns createOrder(@Context HttpHeaders herders, byte[] in) {
		logger.info("to add order->header：" + herders.getMediaType() + " json = " + new String(in));
		OrderCreateAns ans = new OrderCreateAns();
		try {

			if (MediaType.APPLICATION_JSON_TYPE.equals(herders.getMediaType())) {
				Order order = JsonUtils.parse(new String(in), Order.class);
				order.setCreationTime(new Date());
				OrderDB.add(order);
				ans.setSuccess(true);
			} else {
				ans.setMessage("Invalid input type.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			ans.setMessage("system error.");
		}
		return ans;
	}

}
