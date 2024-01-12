/**
 * @ClassName LockTest
 * @Description
 * @Author zhenwang
 * @Date 2024/1/12
 * @Version 1.0
 */
public class LockTest {
    public static void main(String[] args) {
        Ticket12306 ticket12306 = new Ticket12306();
        Thread t1 = new Thread(ticket12306, "携程");
        Thread t2 = new Thread(ticket12306, "飞猪");
        Thread t3 = new Thread(ticket12306, "马蜂窝");

        t1.start();
        t2.start();
        t3.start();
    }
}
