import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
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
public class CuratorWatcherTest {

    CuratorFramework client2;

    @Before
    public void testConnect() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //方式-2
        client2 = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build();
        client2.start();
    }


    /**
     * NodeCache
     * @throws Exception
     */
    @Test
    public void testNodeCache() throws Exception {
        //1\. 创建NodeCache
        NodeCache nodeCache = new NodeCache(client2, "/app1");
        //2\. 设置监听器和处理过程
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("变化了");
                System.out.println("路径为：" + nodeCache.getCurrentData().getPath());
                System.out.println("数据为：" + new String(nodeCache.getCurrentData().getData()));
                System.out.println("状态为：" + nodeCache.getCurrentData().getStat());
            }
        });
        //3\. 启动NodeCache
        nodeCache.start(true);
        while (true) {
        }
    }

    /**
     * PathChildrenCache
     */
    @Test
    public void testNodePathChildrenCache() throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client2, "/app2", true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("变化了");
                System.out.println("路径为：" + pathChildrenCacheEvent.getData().getPath());
            }
        });

        //3\. 启动NodeCache
        pathChildrenCache.start(true);
        while (true) {
        }
    }
    /**
     * TreeCache
     */
    @Test
    public void testTreeCache() throws Exception {
        TreeCache treeCache = new TreeCache(client2, "/app2");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("变化了");
                System.out.println("路径为：" + treeCacheEvent.getData().getPath());
            }
        });

        //3\. 启动NodeCache
        treeCache.start();
        while (true) {
        }
    }

    @After
    public void testClose() {
        if (client2 != null) {
            client2.close();
        }
    }

}
