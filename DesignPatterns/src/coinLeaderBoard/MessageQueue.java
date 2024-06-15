package coinLeaderBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageQueue {
    private static MessageQueue instance;
    private List<MessageListener> listeners;
    private static final Logger logger = Logger.getLogger(MessageQueue.class.getName());

    private MessageQueue() {
        this.listeners = new ArrayList<>();
        logger.info("MessageQueue initialized");
    }

    public static MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }
        return instance;
    }

    public void subscribe(MessageListener listener) {
        try {
            listeners.add(listener);
            logger.info("Subscribed a new listener: " + listener.getClass().getName());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error subscribing listener: " + listener.getClass().getName(), e);
        }
    }

    public void publish(CoinUpdateMessage message) {
        try {
            logger.info("Publishing message: " + message);
            for (MessageListener listener : listeners) {
                listener.onMessage(message);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error publishing message: " + message, e);
        }
    }
}
