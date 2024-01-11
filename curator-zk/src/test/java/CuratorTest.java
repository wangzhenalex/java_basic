import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName CuratorTest
 * @Description
 * @Author zhenwang
 * @Date 2024/1/11
 * @Version 1.0
 */
public class CuratorTest {

    CuratorFramework client2;

    @Before
    public void testConnect() {
        /* 方式-1
        connectString – list of servers to connect to
        sessionTimeoutMs – session timeout
        connectionTimeoutMs – connection timeout
        retryPolicy – retry policy to use
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework  client =  CuratorFrameworkFactory.newClient("localhost:2181", 5000, 3000, retryPolicy);
//        client.start();

        //方式-2
        client2 = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build();
        client2.start();
    }


    //================================================创建==================================================

    /**
     * 创建节点
     * 1. create() 创建节点
     * 2. 创建节点 带有数据
     * 3. 创建临时节点
     * 4. 创建多级节点 /app1/app2/app3
     */
    @Test
    public void testCreate() throws Exception {
        //1. create() 创建节点
        //如果创建节点没有带数据，则默认是当前客户端ip
//        String path = client2.create().forPath("/app1");
//        System.out.println(path);

        //2. 创建节点 带有数据
        String path2 = client2.create().forPath("/app2", "hello".getBytes());
        System.out.println(path2);
    }

    @Test
    public void testCreate3() throws Exception {
        //3. 创建临时节点
        String path2 = client2.create().withMode(CreateMode.EPHEMERAL).forPath("/app3", "hello".getBytes());
        System.out.println(path2);
    }

    @Test
    public void testCreate4() throws Exception {
        //创建多级节点 /app1/app2/app3
        String path2 = client2.create().creatingParentsIfNeeded().forPath("/app4/app5", "hello".getBytes());
        System.out.println(path2);
    }

    //================================================查询==================================================
    /**
     * 查询数据
     *
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        ///app4/app5
        byte[] bytes = client2.getData().forPath("/app4/app5");
        System.out.println(new String(bytes));
    }

    /**
     * 查询子节点
     *
     * @throws Exception
     */
    @Test
    public void testGetChildNode() throws Exception {
        client2.getChildren().forPath("/").forEach(System.out::println);
    }

    /**
     * 查询节点状态信息
     *
     * @throws Exception
     */
    @Test
    public void testGetNodeStatus() throws Exception {
        Stat stat = new Stat();
        client2.getData().storingStatIn(stat).forPath("/app4/app5");
        System.out.println(stat);
    }


    //================================================set==================================================
    /**
     * 修改数据
     * 带有版本号
     *
     * @throws Exception
     */
    @Test
    public void testSetData() throws Exception {
        Stat stat = new Stat();
        client2.getData().storingStatIn(stat).forPath("/app4/app5");
        client2.setData().withVersion(stat.getVersion()).forPath("/app4/app5", "hello world".getBytes());
    }

    //================================================delete==================================================

    /**
     * 删除
     */
    @Test
    public void testDeleteData() throws Exception {
        client2.delete().forPath("/app1");
    }

    /**
     * 删除所有
     */
    @Test
    public void testDeleteAll() throws Exception {
        client2.delete().deletingChildrenIfNeeded().forPath("/app4");
    }

    /**
     * 删除-必须删除成功
     * @throws Exception
     */
    @Test
    public void testDeleteMustTrue() throws Exception {
        client2.delete().guaranteed().forPath("/app1");
    }

    /**
     * 删除+回调
     * @throws Exception
     */
    @Test
    public void testDeleteWithCallBack() throws Exception {
        client2.delete().guaranteed().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        }).forPath("/app2");

    }


    @After
    public void testClose() {
        if (client2 != null) {
            client2.close();
        }
    }

}
