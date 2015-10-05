package activema.demo.mqtt;

import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by root on 10/5/15.
 */
public class Publisher {
    public static void main(String[] args) throws Exception {
        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));
        final String destination = arg(args, 0, "/topic/event");

        final MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);

        FutureConnection connection = mqtt.futureConnection();
        connection.connect().await();

        UTF8Buffer topic = new UTF8Buffer(destination);

        String body = "now clock is " + new Date();
        Buffer msg = new AsciiBuffer(body);
        connection.publish(topic, msg, QoS.AT_LEAST_ONCE,false);

        connection.disconnect().await();

    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
}
