package spring.mq.listener;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;

/**
 * Created by root on 10/5/15.
 */
public class MQPublisher {
    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send() {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("type", "log");
                mapMessage.setString("time", String.valueOf(new Date()));
                mapMessage.setString("event", "sun qq login");
                return mapMessage;
            }
        });
    }
}
