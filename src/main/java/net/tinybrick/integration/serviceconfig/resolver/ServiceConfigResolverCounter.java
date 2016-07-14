package net.tinybrick.integration.serviceconfig.resolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.tinybrick.integration.zookeeper.ZookeeperClient;
import org.apache.zookeeper.KeeperException;

import com.wang.utils.crypto.Codec;

public class ServiceConfigResolverCounter {
	public static String zookeeper_connection;

	Map<String, IServiceConfigResolver<?>> resolverMap = new HashMap<String, IServiceConfigResolver<?>>();

	public Map<String, IServiceConfigResolver<?>> getResolverMap() {
		return resolverMap;
	}

	public <T> ServiceConfigListener<T> getServiceConfigListener(String name, String node, Class<T> clazz)
			throws IOException, KeeperException, InterruptedException {
		return getServiceConfigListener(name, node, clazz, true);
	}

	public synchronized <T> ServiceConfigListener<T> getServiceConfigListener(String name, String node, Class<T> clazz,
			boolean forceCreate) throws IOException, KeeperException, InterruptedException {
		if (resolverMap.get(name) != null) {
			throw new RuntimeException("Service " + name + " is existing.");
		}

		ServiceConfigListener<T> listener = new ServiceConfigListener<T>(zookeeper_connection, node, clazz, forceCreate);
		resolverMap.put(name, listener);
		return listener;
	}

	public IServiceConfigResolver<?> getResolver(String name) {
		return resolverMap.get(name);
	}

	public static void registerServiceConfig(String path, Object config) throws KeeperException, InterruptedException,
			IOException {
		ZookeeperClient.creatNode(zookeeper_connection, path, Codec.toJsonString(config));
	}

	public static void updateServiceConfig(String path, Object config) throws KeeperException, InterruptedException,
			IOException {
		ZookeeperClient.updateNode(zookeeper_connection, path, Codec.toJsonString(config));
	}

	public static void withdrawServiceConfig(String path) throws InterruptedException, KeeperException, IOException {
		ZookeeperClient.updateNode(zookeeper_connection, path, null);
	}
}
