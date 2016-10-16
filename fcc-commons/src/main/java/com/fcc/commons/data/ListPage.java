package com.fcc.commons.data;

import java.util.List;
/**
 * 
 * 基于List的数据页对象
 */
public class ListPage {

    public static final ListPage EMPTY_PAGE = new ListPage();

    //页面记录数
    private int currentPageSize = 10;
    //总记录数
    private int totalSize;
    //当前页页码
    private int currentPageNo;
    //当前页记录列表
    private List<?> dataList;
    //当前记录总数
    private int dataSize;
    
    public ListPage() {
    }    
    /**
     * @return 返回 currentPageno。
     */
    public int getCurrentPageNo() {
        return currentPageNo;
    }
    /**
     * @param currentPageno 要设置的 currentPageno。
     */
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
    /**
     * @return 返回 currentPageSize。
     */
    public int getCurrentPageSize() {
        return currentPageSize;
    }
    /**
     * @param currentPageSize 要设置的 currentPageSize。
     */
    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }
    /**
     * @return 返回 dataList。
     */
    public List<?> getDataList() {
        return dataList;
    }
    /**
     * @param dataList 要设置的 dataList。
     */
    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
        if (dataList != null) {
        	this.dataSize = dataList.size();
        }
    }
    /**
     * @return 返回 totalSize。
     */
    public int getTotalSize() {
        return totalSize;
    }
    /**
     * @param totalSize 要设置的 totalSize。
     */
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
    
    
    public long getTotalPageCount(){
     	return (getTotalSize() - 1) / getCurrentPageSize() + 1 ;
    }
    
    /**
     * 是否有下一页
     * @return 是否有下一页
     */
    public boolean hasNextPage() {
      return (this.getCurrentPageNo() < this.getTotalPageCount());
    }

    /**
     * 是否有上一页
     * @return  是否有上一页
     */
    public boolean hasPreviousPage() {
      return (this.getCurrentPageNo() > 1);
    }
    
    /**
     * 判断是否为空页
     * @return 是否为空页
     */
    public boolean isEmpty(){
        return this == ListPage.EMPTY_PAGE;
    }
	public int getDataSize() {
		return dataSize;
	}
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
    
}
