package spring.mq.listener;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by root on 10/5/15.
 */
public class MQListener implements MessageListener{

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ActiveMQMessage) {
                ActiveMQMapMessage mqMessage = (ActiveMQMapMessage) message;
                String type = mqMessage.getString("type");
                switch (type) {
                    case "log":
                        String time = mqMessage.getString("time");
                        String event = mqMessage.getString("event");
                        System.out.println("time:" + time + ",event :" + event);
                        break;
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
