package com.papa.component;

import com.papa.dto.QueueEnum;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CancelOrderSender {

    @Resource
    private AmqpTemplate template;

    //用户下单之后，发送延迟消息其中包含订单编号和延迟时间（过期时间）
    public void sendMessage(Integer orderId,final long delayTimes){
        template.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
                orderId,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                        return message;
                    }
                });
    }


}
