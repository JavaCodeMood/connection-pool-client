package org.darkphoenixs.pool.redis;

import org.darkphoenixs.pool.PoolConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class RedisClusterConnPoolTest {

    @Before
    public void before() throws Exception {

        Thread th = new Thread(new Runnable() {

            private ServerSocket serverSocket;

            @Override
            public void run() {

                try {
                    serverSocket = new ServerSocket(RedisConfig.DEFAULT_PORT);

                    serverSocket.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.setDaemon(true);
        th.start();
    }

    @Test
    public void test() throws Exception {

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(RedisConfig.DEFAULT_HOST, RedisConfig.DEFAULT_PORT));
        RedisClusterConnPool pool = new RedisClusterConnPool(jedisClusterNodes);

        JedisCluster cluster = pool.getConnection();
        Assert.assertNotNull(cluster);

        pool.returnConnection(cluster);
        pool.invalidateConnection(cluster);

        Properties properties = new Properties();
        properties.setProperty(RedisConfig.CLUSTER_PROPERTY, RedisConfig.DEFAULT_HOST + ":" + RedisConfig.DEFAULT_PORT);
        pool = new RedisClusterConnPool(properties);
        cluster = pool.getConnection();
        Assert.assertNotNull(cluster);

        pool.close();

        pool = new RedisClusterConnPool(new PoolConfig(), jedisClusterNodes);

        pool.close();

    }

    @Test
    public void test1() throws Exception {

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(RedisConfig.DEFAULT_HOST, RedisConfig.DEFAULT_PORT));
        RedisClusterConnPool pool = new RedisClusterConnPool(jedisClusterNodes);

        JedisCluster cluster = pool.getConnection();
        Assert.assertNotNull(cluster);

        pool.returnConnection(cluster);
        pool.invalidateConnection(cluster);

        Properties properties = new Properties();
        properties.setProperty(RedisConfig.CLUSTER_PROPERTY, RedisConfig.DEFAULT_HOST + ":" + RedisConfig.DEFAULT_PORT);
        pool = new RedisClusterConnPool(properties);
        cluster = pool.getConnection();
        Assert.assertNotNull(cluster);

        pool.close();

        pool = new RedisClusterConnPool(jedisClusterNodes, RedisConfig.DEFAULT_TIMEOUT, RedisConfig.DEFAULT_MAXATTE, new PoolConfig());

        pool.close();

    }
}