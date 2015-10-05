package activema.demo.mqtt;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import javax.security.auth.callback.Callback;
import java.net.URISyntaxException;

/**
 * Created by root on 10/5/15.
 */
public class Listener {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "1883"));
        final String destination = arg(args, 0, "/topic/event");

        final MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);

        final CallbackConnection connection = mqtt.callbackConnection();
        connection.listener(new org.fusesource.mqtt.client.Listener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                if ("SHUTDOWN".equals(body)) {
                    connection.disconnect(new org.fusesource.mqtt.client.Callback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.exit(0);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            throwable.printStackTrace();
                            System.exit(-2);
                        }
                    });
                } else {
                    System.out.println("received message is : " + body);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                System.exit(-2);
            }
        });

        connection.connect(new org.fusesource.mqtt.client.Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new org.fusesource.mqtt.client.Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                        System.exit(-2);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                System.exit(-2);
            }
        });

        synchronized (Listener.class) {
            while (true) {
                Listener.class.wait();
            }
        }
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
