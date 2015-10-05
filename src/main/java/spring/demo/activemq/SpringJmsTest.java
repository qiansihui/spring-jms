package spring.demo.activemq;

import javafx.application.Application;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by root on 10/5/15.
 */
public class SpringJmsTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-jms.xml");

        SpringPublisher publisher = (SpringPublisher) applicationContext.getBean("springProducer");
        publisher.send();

        SpringConsumer consumer = (SpringConsumer) applicationContext.getBean("springConsumer");
        consumer.receive();

    }
}
