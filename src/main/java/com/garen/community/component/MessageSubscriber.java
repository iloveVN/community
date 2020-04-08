package com.garen.community.component;

import com.garen.community.domain.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MessageSubscriber implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageSubscriber.class);

    public void onMessage(SimpleMessage message, String pattern) {
        logger.info("topic {} received {}", pattern, message.toString());
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            String channel = new String(message.getChannel());
            String msg = new String(message.getBody(), "UTF-8");
            logger.info("[*Redis*]Receive and loc rpt msg: channel="+channel+", data="+msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
