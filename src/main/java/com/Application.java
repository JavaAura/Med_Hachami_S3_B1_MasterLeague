package com;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.service.IPlayerService;



public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        IPlayerService myService = (IPlayerService) context.getBean("playerService");
        myService.doSomething();
    }
}
