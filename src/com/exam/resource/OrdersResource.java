package com.exam.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.exam.RestAns;
import com.exam.ans.OrderListAns;
import com.exam.db.OrderDB;
import com.exam.entity.OrderStatus;

@Path("/orders")
public class OrdersResource {
	private static final Logger logger = LogManager.getLogger(OrdersResource.class);

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	public OrdersResource(){
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public OrderListAns findOrdersByStatus(@QueryParam("status") String status) {
		logger.info(" get orders->status="+status);
		OrderListAns ans = new OrderListAns();
		if (StringUtils.isBlank(status) || OrderStatus.from(status) != null) {
			ans.setOrders(OrderDB.findByStatus(OrderStatus.from(status)));
			ans.setSuccess(true);
			return ans;
		} else {
			ans.setMessage("Invalid input .");
			return ans;
		}
	}

	@Path("order")
	public OrderResource getOrder() {
		return new OrderResource(uriInfo, request);
	}
	
	@DELETE
	public RestAns clearOrders(){
		RestAns ans = new RestAns();
		try {
			OrderDB.clear();
			ans.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			ans.setMessage("clear error.");
		}
		return ans;
	}

}
