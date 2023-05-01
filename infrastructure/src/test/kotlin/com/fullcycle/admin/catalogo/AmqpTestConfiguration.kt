package com.fullcycle.admin.catalogo

import com.rabbitmq.client.Channel
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.Connection
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.test.RabbitListenerTest
import org.springframework.amqp.rabbit.test.TestRabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Creates proxy around each class annotated with @{@link org.springframework.amqp.rabbit.annotation.RabbitListener}
 * that can be used to verify incoming messages via {@link org.springframework.amqp.rabbit.test.RabbitListenerTestHarness}.
 */
@Configuration
@RabbitListenerTest(spy = false, capture = true)
class AmqpTestConfiguration {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): TestRabbitTemplate {
        return TestRabbitTemplate(connectionFactory)
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val factory = Mockito.mock(ConnectionFactory::class.java)
        val connection = Mockito.mock(Connection::class.java)
        val channel = Mockito.mock(Channel::class.java)
        BDDMockito.willReturn(connection).given(factory).createConnection()
        BDDMockito.willReturn(channel).given(connection).createChannel(Mockito.anyBoolean())
        BDDMockito.given(channel.isOpen).willReturn(true)
        return factory
    }

    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        return factory
    }

}