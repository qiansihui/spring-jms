package spring.mq.listener;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by root on 10/5/15.
 */
public class MQTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("jms.xml");
        MQPublisher mqPublisher = (MQPublisher) applicationContext.getBean("MQPublisher");
        mqPublisher.send();

    }
}
