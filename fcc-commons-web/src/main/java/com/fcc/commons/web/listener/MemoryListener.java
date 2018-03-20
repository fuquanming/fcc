package com.fcc.commons.web.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.fcc.commons.utils.MemoryUtil;
import com.fcc.commons.web.config.Resources;

/**
 * 
 * <p>Description:Memory监听器</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
public class MemoryListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(MemoryListener.class);
    private Timer memoryTime = null;
    private float scale = 0.1f;
    private int cycle = 600000;
    
	public void contextInitialized(ServletContextEvent event) {
	    try {
            scale = Float.valueOf(Resources.CONFIG.getString("memory.scale"));
        } catch (Exception e) {
        }
	    try {
            cycle = Integer.valueOf(Resources.CONFIG.getString("memory.cycle")) * 60000;
        } catch (Exception e) {
        }
	    
        memoryTime = new Timer(true);
        memoryTime.schedule(new TimerTask() {
            private boolean running;
            @Override
            public void run() {
                if (running == true) return;
                running = true;
                try {
                    boolean flag = MemoryUtil.checkMemory(scale);
                    if (flag) {
                        logger.info("memory free...scale=" + scale);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    running = false;
                }
            }
        }, 10000, cycle);// 10秒后，每10分钟检查
        logger.info("memory check:cycle=" + cycle + ",scale=" + scale);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
	    if (memoryTime != null) {
	        memoryTime.cancel();
	    }
	}

}
