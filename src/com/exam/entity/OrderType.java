package com.exam.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易类型
 * 
 * @author yong
 *
 */
public enum OrderType {
	/**
	 * 支付
	 */
	PAYMENT,
	/**
	 * 退款
	 */
	REFUND;
	private static final Map<String, OrderType> CODE_MAP = new HashMap<>();
	static {
		for (OrderType code : values()) {
			CODE_MAP.put(code.name(), code);
		}
	}

	public static OrderType from(String code) {
		if (CODE_MAP.containsKey(code)) {
			return CODE_MAP.get(code);
		}
		return null;
	}
}
