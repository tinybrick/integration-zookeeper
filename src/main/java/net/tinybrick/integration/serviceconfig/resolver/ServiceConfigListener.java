package net.tinybrick.integration.serviceconfig.resolver;

import java.io.IOException;

import net.tinybrick.integration.zookeeper.AbstractZookeeperWatcher;
import net.tinybrick.integration.zookeeper.ZookeeperClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;

import com.wang.utils.crypto.Codec;

public class ServiceConfigListener<T> extends AbstractZookeeperWatcher implements IServiceConfigResolver<T> {
	Logger logger = Logger.getLogger(this.getClass());

	String zkNoteName = null;
	T serviceConfig = null;
	Class<T> configClazz;

	ZookeeperClient zkClient = null;

	public ServiceConfigListener(String zookeeper_connection, String node, Class<T> clazz) throws IOException,
			KeeperException, InterruptedException {
		this(zookeeper_connection, node, clazz, false);
	}

	public ServiceConfigListener(String zookeeper_connection, String node, Class<T> clazz, boolean forceCreate)
			throws IOException, KeeperException, InterruptedException {
		if (!ZookeeperClient.isNodeExists(zookeeper_connection, node, null)) {
			if (forceCreate) {
				ZookeeperClient.creatNode(zookeeper_connection, node, null);
			}
			else {
				throw new NoNodeException(node);
			}
		}

		configClazz = clazz;
		zkNoteName = node;
		zkClient = new ZookeeperClient(zookeeper_connection, this);
		zkClient.getNodeData(zkNoteName);
	}

	@Override
	public void onDataChange(byte[] newData) {
		if (null != newData) {
			try {
				String jsonString = new String(newData, "UTF-8");
				serviceConfig = (T) Codec.toObject(jsonString, configClazz);
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public T getServiceConfig() {
		return serviceConfig;
	}

}
