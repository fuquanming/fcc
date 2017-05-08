package com.fcc.web.workflow.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * 额度申请-判断是否超额
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:Copyright (c) 2009
 * </p>
 * 
 * @author 傅泉明
 * @version v1.0
 */
@Component
public class AmountApplyRouteService implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
	}

}
