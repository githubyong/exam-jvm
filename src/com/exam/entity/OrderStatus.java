package com.exam.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易状态
 * 
 * @author yong
 *
 */
public enum OrderStatus {
	/**
	 * 成功
	 */
	SUCCEED,
	/**
	 * 失败
	 */
	FAILED,
	/**
	 * 等待付款
	 */
	WAIT4PAYMENT;
	private static final Map<String, OrderStatus> CODE_MAP = new HashMap<>();
	static {
		for (OrderStatus code : values()) {
			CODE_MAP.put(code.name(), code);
		}
	}

	public static OrderStatus from(String code) {
		if (CODE_MAP.containsKey(code)) {
			return CODE_MAP.get(code);
		}
		return null;
	}
}
