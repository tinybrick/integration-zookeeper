package net.tinybrick.integration.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import net.tinybrick.integration.serviceconfig.resolver.ServiceConfigResolverCounter;

@Configuration
@ComponentScan
@EnableConfigurationProperties({ PropertySourcesPlaceholderConfigurer.class })
@PropertySource(value = "classpath:config/discovery.properties")
public class DiscoveryServiceConfigure {
	@Bean
	public ServiceConfigResolverCounter serviceConfigResolverCounter(
			@Value("${zookeeper.connection}") String zookeeper_connection) {
		ServiceConfigResolverCounter.zookeeper_connection = zookeeper_connection;
		ServiceConfigResolverCounter serviceConfigResolverCounter = new ServiceConfigResolverCounter();
		return serviceConfigResolverCounter;
	}
}
