package pers.me.monday.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    //****此方案适用于手动配置Lettuce多数据库的连接,根据需要自行修改@Value注解和RedisTemplate
    //database00
    @Bean
    public RedisTemplate<String,String> redisTemplate00(
            @Value("${spring.redis.custom.database00}") int database,
            @Value("${spring.redis.lettuce.pool.max-active}") int maxActive,
            @Value("${spring.redis.lettuce.pool.max-wait}") int maxWait,
            @Value("${spring.redis.lettuce.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.lettuce.pool.min-idle}") int minIdle,
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password}") String password) {
        var connectionFactory = genericConnectionFactory(
                database,maxActive,
                maxWait, maxIdle,
                minIdle,port,
                hostName,password);
        return createStringRedisTemplate(connectionFactory);
    }

    //database01
    @Bean
    public RedisTemplate<String,String> redisTemplate01(
            @Value("${spring.redis.custom.database01}") int database,
            @Value("${spring.redis.lettuce.pool.max-active}") int maxActive,
            @Value("${spring.redis.lettuce.pool.max-wait}") int maxWait,
            @Value("${spring.redis.lettuce.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.lettuce.pool.min-idle}") int minIdle,
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password}") String password) {
        var connectionFactory = genericConnectionFactory(
                database,maxActive,
                maxWait, maxIdle,
                minIdle,port,
                hostName,password);
        return createStringRedisTemplate(connectionFactory);
    }
    //database02
    @Bean
    public RedisTemplate<String,String> redisTemplate02(
            @Value("${spring.redis.custom.database02}") int database,
            @Value("${spring.redis.lettuce.pool.max-active}") int maxActive,
            @Value("${spring.redis.lettuce.pool.max-wait}") int maxWait,
            @Value("${spring.redis.lettuce.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.lettuce.pool.min-idle}") int minIdle,
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password}") String password) {
        var connectionFactory = genericConnectionFactory(
                database,maxActive,
                maxWait, maxIdle,
                minIdle,port,
                hostName,password);
        return createStringRedisTemplate(connectionFactory);
    }
    //database03
    @Bean
    public RedisTemplate<String,String> redisTemplate03(
            @Value("${spring.redis.custom.database03}") int database,
            @Value("${spring.redis.lettuce.pool.max-active}") int maxActive,
            @Value("${spring.redis.lettuce.pool.max-wait}") int maxWait,
            @Value("${spring.redis.lettuce.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.lettuce.pool.min-idle}") int minIdle,
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password}") String password) {
        var connectionFactory = genericConnectionFactory(
                database,maxActive,
                maxWait, maxIdle,
                minIdle,port,
                hostName,password);
        return createStringRedisTemplate(connectionFactory);
    }

    //database04
    @Bean
    public RedisTemplate<String,String> redisTemplate04(
            @Value("${spring.redis.custom.database04}") int database,
            @Value("${spring.redis.lettuce.pool.max-active}") int maxActive,
            @Value("${spring.redis.lettuce.pool.max-wait}") int maxWait,
            @Value("${spring.redis.lettuce.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.lettuce.pool.min-idle}") int minIdle,
            @Value("${spring.redis.host}") String hostName,
            @Value("${spring.redis.port}") int port,
            @Value("${spring.redis.password}") String password) {
        var connectionFactory = genericConnectionFactory(
                database,maxActive,
                maxWait, maxIdle,
                minIdle,port,
                hostName,password);
        return createStringRedisTemplate(connectionFactory);
    }

    /* ========= 创建 String Redis Template ========= */
    private RedisTemplate<String,String> createStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    private LettuceConnectionFactory genericConnectionFactory(
            int database, int maxActive,
            int maxWait,int maxIdle,
            int minIdle,int port,
            String hostName, String password){

        /* ========= 基本配置 ========= */
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(database);
        configuration.setHostName(hostName);
        configuration.setPort(port);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

        /* ========= lettuce pool ========= */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, builder.build());
        connectionFactory.afterPropertiesSet();

        return connectionFactory;
    }
//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        return createRedisTemplate(redisConnectionFactory);
//    }

}
