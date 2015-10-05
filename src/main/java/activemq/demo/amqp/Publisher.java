package activemq.demo.amqp;

import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;
import java.util.Date;

/**
 * Created by root on 10/5/15.
 */
public class Publisher {
    public static void main(String[] args) throws JMSException {
        final String TOPIC_PREFIX = "topic://";
        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));

        String connectionURL = "amqp://" + host + ":" + port;
        String destinationName = arg(args, 0, "topic://event");


        //ConnectionFactory 接口（连接工厂） 用户用来创建到JMS提供者的连接的被管对象。
        // JMS客户通过可移植的接口访问连接，这样当下层的实现改变时，代码不需要进行修改。
        // 管理员在JNDI名字空间中配置连接工厂，这样，JMS客户才能够查找到它们。
        // 根据消息类型的不同，用户将使用队列连接工厂，或者主题连接工厂。
        JmsConnectionFactory factory = new JmsConnectionFactory(connectionURL);

        //Connection 接口（连接） 连接代表了应用程序和消息服务器之间的通信链路。
        // 在获得了连接工厂后，就可以创建一个与JMS提供者的连接。
        // 根据不同的连接类型，连接允许用户创建会话，以发送和接收队列和主题到目标。
        Connection connection = factory.createConnection(user, password);
        connection.start();

        /**
         * Session 接口（会话） 表示一个单线程的上下文，用于发送和接收消息。由于会话是单线程的，所以消息是连续的，
         * 就是说消息是按照发送的顺序一个一个接收的。会话的好处是它支持事务。如果用户选择了事务支持，会话上下文将保存一组消息，
         * 直到事务被提交才发送这些消息。在提交事务之前，用户可以使用回滚操作取消这些消息。一个会话允许用户创建消息生产者来发送消息，
         * 创建消息消费者来接收消息。
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /**
         * Destination 接口（目标） 目标是一个包装了消息目标标识符的被管对象，消息目标是指消息发布和接收的地点，
         * 或者是队列，或者是主题。JMS管理员创建这些对象，然后用户通过JNDI发现它们。和连接工厂一样，管理员可以创建两种类型的目标，
         * 点对点模型的队列，以及发布者/订阅者模型的主题。
         */
        Destination destination = null;
        if (destinationName.startsWith(TOPIC_PREFIX)) {
            destination = session.createTopic(destinationName.substring(TOPIC_PREFIX.length()));
        } else {
            destination = session.createQueue(destinationName);
        }

        /**
         * MessageProducer 接口（消息生产者） 由会话创建的对象，用于发送消息到目标。
         * 用户可以创建某个目标的发送者，也可以创建一个通用的发送者，在发送消息时指定目标。
         */
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        /**
         * Message 接口（消息） 是在消费者和生产者之间传送的对象，也就是说从一个应用程序传送到另一个应用程序。
         * 一个消息有三个主要部分： 消息头（必须）：包含用于识别和为消息寻找路由的操作设置。
         * 一组消息属性（可选）：包含额外的属性，支持其他提供者和用户的兼容。可以创建定制的字段和过滤器（消息选择器）。
         * 一个消息体（可选）：允许用户创建五种类型的消息（文本消息，映射消息，字节消息，流消息和对象消息）。
         * 消息接口非常灵活，并提供了许多方式来定制消息的内容。
         */
        TextMessage msg = session.createTextMessage("send clock is :" + new Date());
        msg.setIntProperty("id", 1);

        producer.send(msg);

    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }

    private static String arg(String[] args, int index, String defaultValue) {
        if (index < args.length)
            return args[index];
        else
            return defaultValue;
    }
}
