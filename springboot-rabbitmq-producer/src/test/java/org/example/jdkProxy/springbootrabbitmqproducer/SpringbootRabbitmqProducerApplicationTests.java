package org.example.jdkProxy.springbootrabbitmqproducer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringbootRabbitmqProducerApplicationTests {

    @Resource
    MessageProducer messageProducer;

    @Test
    void contextLoads() {
    }



    @Test
    public void testSendMsg(){
        messageProducer.sendMsg(new Employee("3306" , "老齐" , 18));
    }

}
