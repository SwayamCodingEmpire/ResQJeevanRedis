package com.spm.resqjeevanredis.config;

import com.spm.resqjeevanredis.controller.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ChannelTopic locationChannelTopic(){
        return new ChannelTopic("pubsub:location-channel");
    }
    @Bean
    public ChannelTopic requestToDepotChannelTopic(){
        return new ChannelTopic("pubsub:depot-channel");
    }


    @Bean
    public MessageListenerAdapter messageListenerAdapter(){
        return new MessageListenerAdapter(new Receiver());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter(),locationChannelTopic());
        container.addMessageListener(messageListenerAdapter(),requestToDepotChannelTopic());
        return container;
    }
}
