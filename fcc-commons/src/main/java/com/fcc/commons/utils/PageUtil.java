package com.fcc.commons.utils;
/**
 * <p>Description:</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class PageUtil {
	/**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
     */
	public static int getFirstResultIndex(int pageNo, int pageSize) {
		//如果pageCount为0,则认为无限制,返回所有记录
    	int firstResultIndex = 0;
    	if (pageSize <= 0){
    		firstResultIndex = 0;
    	} else {
    		firstResultIndex = (pageNo - 1) * pageSize;
    	}
    	return firstResultIndex;
	}

}
