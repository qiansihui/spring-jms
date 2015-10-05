package spring.demo.activemq;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.TextMessage;

/**
 * Created by root on 10/5/15.
 */
public class SpringConsumer {
    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    //接收消息
    public void receive() {
        while (true) {
            try {
                //使用JMSTemplate接收消息
                TextMessage txtmsg = (TextMessage) jmsTemplate.receive();
                if (null != txtmsg) {
                    System.out.println("--- 收到消息内容为: " + txtmsg.getText());
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
