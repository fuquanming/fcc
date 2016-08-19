package com.fcc.app.action;

import com.fcc.commons.core.dao.BaseDao;
import com.fcc.commons.core.dao.impl.BaseDaoImpl;
import com.fcc.commons.fcc_commons.App;


public class TestApp {

	public static void main(String[] args) {
		App app = new App();
		BaseDao<Object> baseDao = new BaseDaoImpl<Object>() {
		};
		System.out.println(baseDao == null);
		System.out.println(app.getAge());
	}
	
}
