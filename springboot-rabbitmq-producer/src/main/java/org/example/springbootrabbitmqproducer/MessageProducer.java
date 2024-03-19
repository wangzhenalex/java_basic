package org.example.springbootrabbitmqproducer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/19
 * @description：生产者
 * @version: 1.0
 */
@Component
public class MessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        /**
         * CorrelationData 消息的附加信息，即自定义id
         * isack 代表消息是否被broker（MQ）接收 true 代表接收 false代表拒收。
         * cause 如果拒收cause则说明拒收的原因，帮助我们进行后续处理
         */
        public void confirm(CorrelationData correlationData, boolean isack, String cause) {
            System.out.println(correlationData);
            System.out.println("ack:" + isack);
            if (!isack) {
                System.err.println(cause);
            }
        }
    };

    RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingkey) {
            System.err.println("Code:" + replyCode + ",Text:" + replyText);
            System.err.println("Exchange:" + exchange + ",RoutingKey:" + routingkey);
        }
    };
    public void sendMsg(Employee emp) {
        //CorrelationData对象的作用是作为消息的附加信息传递，通常我们用它来保存消息的自定义id
        CorrelationData cd = new CorrelationData(emp.getEmpno() + "-" + new Date().getTime());
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend("springboot-exchange", "hr.employee", emp, cd);
    }
}
