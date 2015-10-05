package activemq.demo.amqp;

import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;

/**
 * Created by root on 10/5/15.
 */
public class Listener {
    public static void main(String[] args) throws JMSException {
        final String TOPIC_PREFIX = "topic://";
        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));

        String connectionURL = "amqp://" + host + ":" + port;
        String destinationName = arg(args, 0, "topic://event");

        JmsConnectionFactory factory = new JmsConnectionFactory(connectionURL);

        Connection connection = factory.createConnection(user, password);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = null;
        if (destinationName.startsWith(TOPIC_PREFIX)) {
            destination = session.createTopic(destinationName.substring(TOPIC_PREFIX.length()));
        } else {
            destination = session.createQueue(destinationName);
        }

        MessageConsumer consumer = session.createConsumer(destination);
        System.out.println("waiting for messages ...");
        Message msg = consumer.receive();
        if (msg instanceof TextMessage) {
            String body = ((TextMessage) msg).getText();
            if ("SHUTDOWN".equals(body)) {
                System.out.println("shutdown listener");
                connection.close();
                System.exit(1);
            } else {
                System.out.println("receive message is :" + body);
            }
        } else {
            System.out.println("Unexpected message type :" + msg.getClass());
        }
    }

    private static String arg(String[] args, int index, String defaultValue) {
        if (index < args.length) {
            return args[index];
        } else {
            return defaultValue;
        }
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null) {
            return defaultValue;
        }
        return rc;
    }
}
