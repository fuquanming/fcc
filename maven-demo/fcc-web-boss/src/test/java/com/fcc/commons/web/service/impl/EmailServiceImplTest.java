package com.fcc.commons.web.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fcc.commons.web.service.EmailService;
import com.fcc.commons.web.service.TemplateService;

public class EmailServiceImplTest {

    static ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    
    BeanFactory factory;

    public static BeanFactory getBeanFactory() {
        BeanFactory factory = new ClassPathXmlApplicationContext(new String[] { "spring.xml" });
        return factory;
    }

    @Test
    public void testEmailService1() {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server  
        senderImpl.setHost("smtp.163.com");

        // 建立邮件消息  
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人 用数组发送多个邮件
        // String[] array = new String[]    {"sun111@163.com","sun222@sohu.com"};    
        // mailMessage.setTo(array);  
        mailMessage.setTo("67837343@qq.com");
        mailMessage.setFrom("qqwwee_0@163.com");
        mailMessage.setSubject("测试简单文本邮件发送！");
        mailMessage.setText("测试我的简单邮件发送机制！！" + RandomStringUtils.random(4, true, false));

        senderImpl.setUsername("qqwwee_0@163.com"); //  根据自己的情况,设置username 
        senderImpl.setPassword("fqm@5552726"); //  根据自己的情况, 设置password 

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); //  将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确 
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件  
        senderImpl.send(mailMessage);

        System.out.println(" 邮件发送成功.. ");
    }

    @Test
    public void testEmailService() {
        //        fail("Not yet implemented");
        factory = getBeanFactory();
        EmailService service = factory.getBean(EmailService.class);
        TemplateService templateService = factory.getBean(TemplateService.class);

        service.setSubject("你好");
        service.addTo("67837343@qq.com");
        Map<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("userName", "张三");
        contentMap.put("content", "注册成功！");
        try {
            service.setText(templateService.getMailContent("notify.html", contentMap), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.send();
    }
    
    @Test
    public void testEmailServiceWorker() {
//        for (int i = 0; i < 5; i++) {
//            testEmailService1();
//        }
//        
//        if (true) return;
        
        factory = getBeanFactory();
        
        int num = 1;
        // 等待多线程完成后执行
        CountDownLatch latch = new CountDownLatch(num);
        // 多线程一起等待一个事件开始
        CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
            @Override
            public void run() {
                System.out.println("go on");
            }
        });
        for (int i = 1; i <= num; i++) {
            pool.execute(new EmailWorker(barrier, latch));
        }
        try {
            System.out.println("main is wait " + num + ",working");
            latch.await();
            System.out.println("main is end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

    class EmailWorker implements Runnable {

        private CyclicBarrier barrier;
        
        private CountDownLatch latch;

        public EmailWorker(CyclicBarrier barrier, CountDownLatch latch) {
            this.barrier = barrier;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                long id = Thread.currentThread().getId();
                System.out.println(id + ":is wait");
                barrier.await();
                System.out.println(id + ":is go");

                long startTime = System.currentTimeMillis();
                
                EmailService service = factory.getBean(EmailService.class);
                TemplateService templateService = factory.getBean(TemplateService.class);
                service.setSubject("关于信息系统的评估");
                service.addTo("67837343@qq.com");
                Map<String, String> contentMap = new HashMap<String, String>();
                contentMap.put("userName", "用户系统" + RandomStringUtils.random(4, true, false));
                contentMap.put("content", "注册成功！");
                try {
                    String str = templateService.getMailContent("notify.html", contentMap);
                    str += "<img src=/\"cid:aaa/\"/>";
                    service.setText(str, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                service.addAttachment("排队机问题20161219.doc", new File("D:\\我的文档\\67837343\\FileRecv\\排队机问题20161219.doc"));
                service.addInline("aaa", new File("f:\\IMG_20151016_173252.jpg"));
                
                service.send();
                
                
                
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getId() + ":unlock,time=" + (endTime - startTime));
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
}
