package study.redis;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 *  <dependency>
 *	    <groupId>redis.clients</groupId>
 *	    <artifactId>jedis</artifactId>
 *	    <version>2.9.0</version>
 *	</dependency>
 * @author zhouyelin
 *
 */
public class RedisClient {

	public static void main(String[] args) {
		connectClusterRedis();
	}
	
	/**
	 * 连接单机版Redis（非集群）
	 */
	public void connectSingleRedis(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
	    // 最大连接数
	    poolConfig.setMaxTotal(2);
	    // 最大空闲数
	    poolConfig.setMaxIdle(2);
	    // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
	    // Could not get a resource from the pool
	    poolConfig.setMaxWaitMillis(1000);
	    JedisPool pool = new JedisPool(poolConfig, "172.28.20.132", 6379, 0, "123");
	    Jedis jedis = null;
	    try {
	        for (int i = 0; i < 5; i++) {
	            jedis = pool.getResource();
	            jedis.set("foo" + i, "bar" + i);
	            System.out.println("第" + (i + 1) + "个连接, 得到的值为" + jedis.get("foo" + i));
	            // 用完一定要释放连接
	            jedis.close();
	        }
	    } finally {
	        pool.close();
	    }
	}
	
	/**
	 * 连接集群Redis
	 */
	public static void connectClusterRedis(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
	    // 最大连接数
	    poolConfig.setMaxTotal(1);
	    // 最大空闲数
	    poolConfig.setMaxIdle(1);
	    // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
	    // Could not get a resource from the pool
	    poolConfig.setMaxWaitMillis(1000);
	    Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
	    nodes.add(new HostAndPort("172.28.20.132", 6379));
	    nodes.add(new HostAndPort("172.28.20.132", 6380));
	    nodes.add(new HostAndPort("172.28.20.132", 6381));
	    JedisCluster cluster = new JedisCluster(nodes, poolConfig);
	    String name = cluster.get("name");
	    System.out.println(name);
	    cluster.set("age", "18");
	    System.out.println(cluster.get("age"));
	    try {
	        cluster.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
