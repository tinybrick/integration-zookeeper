package net.tinybrick.integration.zookeeper;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.tinybrick.integration.configure.DiscoveryServiceConfigure;
import net.tinybrick.integration.serviceconfig.RestServiceConfig;
import net.tinybrick.integration.serviceconfig.resolver.IServiceConfigResolver;
import net.tinybrick.integration.serviceconfig.resolver.ServiceConfigResolverCounter;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan
@SpringApplicationConfiguration(classes = DiscoveryServiceConfigure.class)
public class ZKClientTest {
	Logger logger = Logger.getLogger(this.getClass());

	static final String node = "/com/wang/goods/brands";
	static final String newAddress = "www.wang.com";

	@Autowired ServiceConfigResolverCounter serviceConfigResolverCounter;

	IServiceConfigResolver<RestServiceConfig> serviceConfigResolver;
	RestServiceConfig serviceConfig;

	@Before
	public void before() throws IOException, InterruptedException, KeeperException {
		serviceConfigResolver = serviceConfigResolverCounter.getServiceConfigListener("test_listener", node,
				RestServiceConfig.class);
	}

	@Test
	public void zookeeperServiceConfigResolverTest() throws KeeperException, InterruptedException, IOException {
		RestServiceConfig config = serviceConfigResolver.getServiceConfig();

		if (null == config) {
			config = new RestServiceConfig();
		}
		config.setAddress(newAddress);
		ServiceConfigResolverCounter.updateServiceConfig(node, config);

		Thread.sleep(1000);
		Assert.assertTrue(serviceConfigResolver.getServiceConfig().getAddress().equals(newAddress));
	}
}
