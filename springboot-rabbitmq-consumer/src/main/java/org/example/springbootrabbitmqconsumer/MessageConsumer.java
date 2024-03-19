package org.example.springbootrabbitmqconsumer;

import com.rabbitmq.client.Channel;
import org.example.springbootrabbitmqproducer.Employee;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/19
 * @description：消费者
 * @version: 1.0
 */
@Component
public class MessageConsumer {
    //@RabbitListener注解用于声明式定义消息接受的队列与exhcange绑定的信息
    //在SpringBoot中，消费者这端使用注解获取消息
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value="springboot-queue" , durable="true"),
                    exchange = @Exchange(value = "springboot-exchange" , durable = "true" , type = "topic") ,
                    key = "#"
            )
    )
    //用于接收消息的方法
    @RabbitHandler //通知SpringBoot下面的方法用于接收消息。
    // 这个方法运行后将处于等待的状态，有新的消息进来就会自动触发下面的方法处理消息
    //@Payload 代表运行时将消息反序列化后注入到后面的参数中
    public void handleMessage(@Payload Employee employee , Channel channel ,
                              @Headers Map<String,Object> headers) {
        System.out.println("=========================================");
        System.out.println("接收到" + employee.getEmpno() + ":" + employee.getName());
        //所有消息处理后必须进行消息的ack，channel.basicAck()
        Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            channel.basicAck(tag , false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=========================================");
    }
}
