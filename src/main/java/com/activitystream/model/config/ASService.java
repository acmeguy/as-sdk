package com.activitystream.model.config;

import com.activitystream.model.ASEvent;
import com.activitystream.model.interfaces.BaseStreamItem;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

/**
 * @author ivan
 */
public class ASService {

    private static final Logger logger = LoggerFactory.getLogger(ASService.class);

    public static enum ServiceLevel {
        DEVELOPMENT,
        POC,
        STAGING,
        PRODUCTION
    };

    private static String tenantLabel;
    private static String apiKey;
    private static ServiceLevel environment = ServiceLevel.DEVELOPMENT;

    private static String defaultCountryCode;
    private static String defaultCurrency;
    private static TimeZone defaultTimeZone;

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;

    public static void credentials(String tenantLabel, String apiKey, ServiceLevel environment) {

        if (apiKey != null) ASService.apiKey = apiKey;
        if (tenantLabel != null) ASService.tenantLabel = tenantLabel;
        if (environment != null) ASService.environment = environment;

    }

    public static void setDefaults(String countryCode, String currency, TimeZone timeZone) {

        if (currency != null) ASService.defaultCurrency = currency;

        if (countryCode != null) ASService.defaultCountryCode = countryCode;

        if (timeZone != null) {
            JacksonMapper.getMapper().setTimeZone(timeZone);
            DateTimeZone.setDefault(DateTimeZone.forTimeZone(timeZone));
            ASService.defaultTimeZone = timeZone;
        }
    }

    public static String getDefaultCountryCode() {
        return defaultCountryCode;
    }

    public static String getDefaultCurrency() { return defaultCurrency; }

    public static TimeZone getDefaultTimeZone() {
        return defaultTimeZone;
    }

    public static String getTenantLabel() { return tenantLabel; }

    public static String getApiKey() { return apiKey; }

    public static ServiceLevel getEnvironment() { return environment; }

    //todo - throw an exception
    public static void send(ASEvent event) {
        try {
            if (channel == null) {
                initiateRabbitConnection();
            }

            if (channel != null) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2).priority(1).userId("guest").build();
                channel.basicPublish("from-" + getTenantLabel(), "#",  properties, event.toJSON().getBytes());
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException while sending a stream item!" + e, e);
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException while sending a stream item! " + e, e);
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException while sending a stream item! " + e, e);
        } catch (IOException e) {
            logger.error("IOException while sending a stream item! " + e, e);
        } catch (TimeoutException e) {
            logger.error("TimeoutException while sending a stream item! " + e, e);
        }
    }

    private static void close() throws IOException, TimeoutException {
        if (channel  != null && channel.isOpen()) channel.close();
        if (connection != null && connection.isOpen()) connection.close();
    }

    private static void initiateRabbitConnection() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setUri("amqp://" + getTenantLabel() + ":" + getApiKey() + "@localhost:5672/" + getTenantLabel());
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

}
