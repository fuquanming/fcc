package com.fcc.web.sys.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcc.commons.core.service.BaseService;
import com.fcc.commons.data.ListPage;
import com.fcc.commons.execption.RefusedException;
import com.fcc.web.sys.dao.OperateDao;
import com.fcc.web.sys.model.Operate;
import com.fcc.web.sys.service.OperateService;
import com.fcc.web.sys.view.OperateValueCount;
/**
 * <p>Description:系统模块操作</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@SuppressWarnings("unchecked")
@Service
public class OperateServiceImpl implements OperateService {
	
	@Resource
	private BaseService baseService;
	@Resource
	private OperateDao operateDao;
	
	public static List<Long> factorialPush;
	
	public static List<Long> factorialPop;
	
	static {
		setFactorialMap();
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void create(Operate o) throws RefusedException {
		synchronized (factorialPop) {
		    Operate oldOperate = (Operate) baseService.get(Operate.class, o.getOperateId());
			if (oldOperate != null) {
				throw new RefusedException("操作ID重复！");
			}
			OperateValueCount ov = operateDao.getMaxOperateValueAndCount();
			long count = ov.getCount();
			long value = 1;
			if (ov.getValue() != null) {
				value = ov.getValue().longValue() * 2;
			}
			int maxCount = 63;
			if (count >= maxCount) {
				throw new RefusedException("操作值超过最大值！");
			} else if ((value > Long.MAX_VALUE && count < maxCount)) {
				List<Operate> list = baseService.getAll(Operate.class);
				Long o_value = null;
				setFactorialMap();				
				for (Iterator<Operate> it = list.iterator(); it.hasNext();) {
					o_value = it.next().getOperateValue();					
					if (OperateServiceImpl.factorialPop.contains(o_value)) {
						OperateServiceImpl.factorialPop.remove(o_value);												
					}
				}
				for (Iterator<Long> it = factorialPop.iterator(); it.hasNext();) {
					o_value = it.next();
					value = o_value.intValue();
					break;
				}
				OperateServiceImpl.factorialPop.clear();
			}			
			o.setOperateValue(value);
			baseService.create(o);
		}
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void update(Operate o) {
		baseService.update(o);
	}
	
	@Transactional(rollbackFor = Exception.class)//事务申明
	public void delete(String[] ids) {
		operateDao.delete(ids);
	}
	
	@Transactional(readOnly = true)//只查事务申明
	public ListPage queryPage(int pageNo, int pageSize, Map<String, Object> param) {
		return operateDao.queryPage(pageNo, pageSize, param);
	}
	
	public static List<Long> setFactorialMap() {
		if (OperateServiceImpl.factorialPush == null) {			
			OperateServiceImpl.factorialPush = new ArrayList<Long>(62);
			OperateServiceImpl.factorialPop = new ArrayList<Long>(62);
			for (int i = 0; i < 63; i++) {
				OperateServiceImpl.factorialPush.add(Long.valueOf(getFactorial(i)));
			}
			OperateServiceImpl.factorialPop.addAll(OperateServiceImpl.factorialPush);
		} else {			
			OperateServiceImpl.factorialPop.addAll(OperateServiceImpl.factorialPush);
		}
		return OperateServiceImpl.factorialPush;
	}
	
	public static long getFactorial(int index) {
		if (index - 1 >= 0) {
			//乘以2
			return getFactorial(index - 1) << 1;
		} else {
			return 1;
		}
	}

	public static void main(String [] args) {
//		OperateService.setFactorialMap();
		long value = getFactorial(62);
		System.out.println(value);
		System.out.println(Long.MAX_VALUE);
	}
}
