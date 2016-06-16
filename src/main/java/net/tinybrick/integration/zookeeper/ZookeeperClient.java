package net.tinybrick.integration.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZookeeperClient {
	Logger logger = Logger.getLogger(this.getClass());

	final static int SESSION_TIME = 30000;
	public String zookeeper_connection;
	ZooKeeper zookeeper;

	IZookeeperWatcher watcher;

	public ZookeeperClient(String zookeeper_connection, IZookeeperWatcher watcher, String userName, String password)
			throws IOException, InterruptedException {
		this.zookeeper_connection = zookeeper_connection;
		this.watcher = watcher;
		this.watcher.setOwner(this);
		connect();
	}

	public ZookeeperClient(String zookeeper_connection, IZookeeperWatcher watcher) throws IOException,
			InterruptedException {
		this(zookeeper_connection, watcher, null, null);
	}

	protected void connect() throws IOException, InterruptedException {
		zookeeper = new NotifiableZookeeper(zookeeper_connection, SESSION_TIME, watcher);
	}

	public void getNodeData(String path) throws KeeperException, InterruptedException {
		Stat nodeStatus = new Stat();
		watcher.onDataChange(zookeeper.getData(path, (Watcher) watcher, nodeStatus));
	}

	private static IZookeeperWatcher getConnectionWatcher() {
		IZookeeperWatcher connectionWatcher = new AbstractZookeeperWatcher() {
			@Override
			public void onDataChange(byte[] newData) {
				return;
			}
		};

		return connectionWatcher;
	}

	/***
	 * 创建一个note
	 * 
	 * @param path
	 * @param data
	 * @param acl
	 * @param mode
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void creatNode(String zookeeper_connection, String path, String data, List<ACL> acl, CreateMode mode)
			throws KeeperException, InterruptedException, IOException {
		IZookeeperWatcher connectionWatcher = getConnectionWatcher();
		ZooKeeper zk = new NotifiableZookeeper(zookeeper_connection, SESSION_TIME, connectionWatcher);

		StringTokenizer st = new StringTokenizer(path, "/");
		StringBuffer newPath = new StringBuffer();

		while (st.hasMoreTokens()) {
			newPath.append("/" + st.nextToken());
			if (null == zk.exists(newPath.toString(), false)) {
				if (newPath.toString().equals(path)) {
					zk.create(newPath.toString(), null != data ? data.getBytes() : null, acl, mode);
					zk.exists(newPath.toString(), true);
				}
				else {
					zk.create(newPath.toString(), null, acl, mode);
				}
			}
		}

		zk.close();
	}

	public static void creatNode(String zookeeper_connection, String path, String data) throws KeeperException,
			InterruptedException, IOException {
		creatNode(zookeeper_connection, path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	/**
	 * @param zookeeper_connection
	 * @param path
	 * @param data
	 * @param acl
	 * @param mode
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void updateNode(String zookeeper_connection, String path, String data, List<ACL> acl, CreateMode mode)
			throws KeeperException, InterruptedException, IOException {
		IZookeeperWatcher connectionWatcher = getConnectionWatcher();
		ZooKeeper zk = new NotifiableZookeeper(zookeeper_connection, SESSION_TIME, connectionWatcher);

		zk.setData(path, null != data ? data.getBytes() : null, -1);
		zk.close();
	}

	public static void updateNode(String zookeeper_connection, String path, String data) throws InterruptedException,
			KeeperException, IOException {
		updateNode(zookeeper_connection, path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	/**
	 * @param zookeeper_connection
	 * @param path
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws IOException
	 */
	public static void deleteNode(String zookeeper_connection, String path) throws InterruptedException,
			KeeperException, IOException {
		IZookeeperWatcher connectionWatcher = getConnectionWatcher();
		ZooKeeper zk = new NotifiableZookeeper(zookeeper_connection, SESSION_TIME, connectionWatcher);

		zk.delete(path, 0);
		zk.close();
	}

	/***
	 * 判断一个节点是否存在
	 * 
	 * @param path
	 * @param watch
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static Boolean isNodeExists(String zookeeper_connection, String path, Boolean watch) throws KeeperException,
			InterruptedException, IOException {
		IZookeeperWatcher connectionWatcher = getConnectionWatcher();
		ZooKeeper zk = new NotifiableZookeeper(zookeeper_connection, SESSION_TIME, connectionWatcher);

		Stat stat = zk.exists(path, null == watch ? false : watch);
		zk.close();
		if (stat == null) {
			return false;
		}
		else {
			return true;
		}
	}
}
