package com.fcc.commons.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.query.Query;
import org.springframework.stereotype.Service;

import com.fcc.commons.data.ListPage;
import com.fcc.commons.utils.PageUtil;

/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
@Service
public class ProcessBaseService {

	/**
	 * 构建ListPage
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public ListPage queryPage(int pageNo, int pageSize, Query query) {
		int firstResultIndex = PageUtil.getFirstResultIndex(pageNo, pageSize);
		List result = query.listPage(firstResultIndex, pageSize);
		ListPage listPage = null;
		int totalCount = (int) query.count();
        if (totalCount == 0 || firstResultIndex > totalCount) {
	        listPage = ListPage.EMPTY_PAGE;
	        listPage.setDataList(new ArrayList(1));
        } else {
          	listPage = new ListPage();
          	listPage.setTotalSize(totalCount);
          	listPage.setDataList(result);
        }
    	listPage.setCurrentPageNo(pageNo);
      	listPage.setCurrentPageSize(pageSize);
      	return listPage;
	}
	
}
