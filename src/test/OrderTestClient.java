package test;

import java.math.BigDecimal;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

import com.exam.RestAns;
import com.exam.ans.OrderCreateAns;
import com.exam.ans.OrderListAns;
import com.exam.entity.Currency;
import com.exam.entity.Order;
import com.exam.entity.OrderStatus;
import com.exam.entity.OrderType;
import com.exam.entity.User;
import com.exam.util.JsonUtils;
import com.exam.util.MyObjectMapperProvider;

public class OrderTestClient {

	private static Logger logger = LogManager.getLogger(OrderTestClient.class);
	Client client = ClientBuilder.newBuilder().register(MyObjectMapperProvider.class).register(JacksonFeature.class).build();
	WebTarget target = client.target("http://localhost:8080/exam-jvm/rest/").register(HttpAuthenticationFeature.basicBuilder().credentials("admin", "123").build());
	

	@Test
	public void testGetOrders() {
		String status = "";
		OrderListAns ans = target.path("orders").queryParam("status", status).request().get(OrderListAns.class);
		logger.info("ans = " + ans.getOrders());
	}

	//1.使用用户张三创建一条86元5角人民币的交易记录，验证交易状态为等待付款，并且交易的金额是正确的。
	@Test
	public void testAddOrder() {
		Order order = new Order();
		order.setAmount(BigDecimal.valueOf(86.5));
		order.setCurrency(Currency.RMB);
		order.setOrderStatus(OrderStatus.WAIT4PAYMENT);
		order.setUser(new User(1l, "张三"));
		Entity<String> entityJson = Entity.entity(JsonUtils.print(order), MediaType.APPLICATION_JSON);
		Response response = target.path("orders").path("order").request().post(entityJson);
		OrderCreateAns ans = response.readEntity(OrderCreateAns.class);
		Assert.assertTrue(ans.isSuccess());
	}

	//2.初始化3条交易成功信息，2条交易失败信息。验证查询接口按全部状态查询共5条交易，只查询成功的交易是3条，只查询失败的交易是2条
	@Test
	public void initData() {
		// 先清空原来的信息
		Assert.assertTrue("delete success:", target.path("orders").request().delete().readEntity(RestAns.class).isSuccess());
		// 插入5条数据
		Assert.assertTrue("create success:",
				generateOrder(OrderStatus.SUCCEED, OrderType.PAYMENT, BigDecimal.valueOf(50), Currency.RMB, new User(1l, "张")));
		Assert.assertTrue("create success:",
				generateOrder(OrderStatus.SUCCEED, OrderType.PAYMENT, BigDecimal.valueOf(60), Currency.RMB, new User(1l, "张")));
		Assert.assertTrue("create success:",
				generateOrder(OrderStatus.SUCCEED, OrderType.PAYMENT, BigDecimal.valueOf(80), Currency.RMB, new User(1l, "张")));
		Assert.assertTrue("create success:",
				generateOrder(OrderStatus.FAILED, OrderType.PAYMENT, BigDecimal.valueOf(30), Currency.RMB, new User(2l, "王")));
		Assert.assertTrue("create success:",
				generateOrder(OrderStatus.FAILED, OrderType.REFUND, BigDecimal.valueOf(40), Currency.RMB, new User(1l, "王")));

		// 查询 全部状态的5条
		Assert.assertTrue("5 items ",
				target.path("orders").queryParam("status", "").request().get(OrderListAns.class).getOrders().size() == 5);
		// 查询 成功状态的3条
		Assert.assertTrue("3 of items are succeed",
				target.path("orders").queryParam("status", OrderStatus.SUCCEED.name()).request().get(OrderListAns.class).getOrders().size() == 3);
		// 查询 失败状态的2条
		Assert.assertTrue("2 of items are failed",
				target.path("orders").queryParam("status", OrderStatus.FAILED.name()).request().get(OrderListAns.class).getOrders().size() == 2);
	}

	private boolean generateOrder(OrderStatus orderStatus, OrderType orderType, BigDecimal amount, Currency currency, User user) {
		Order order = new Order(orderStatus, orderType, amount, currency, user);
		Entity<String> orderJson = Entity.entity(JsonUtils.print(order), MediaType.APPLICATION_JSON);
		return target.path("orders").path("order").request().post(orderJson).readEntity(OrderCreateAns.class).isSuccess();
	}

}
