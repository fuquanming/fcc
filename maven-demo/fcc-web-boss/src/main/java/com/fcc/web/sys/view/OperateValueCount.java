package com.fcc.web.sys.view;
/**
 * 操作值和总数
 * @version v1.0
 * @author 傅泉明
 */
public class OperateValueCount {
    
    private Long value;
    
    private Long count;

    public OperateValueCount() {
    }

    public OperateValueCount(Long value, Long count) {
        super();
        this.value = value;
        this.count = count;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
    
}
