package com.wang.integration.zookeeper;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

public class NotifiableZookeeper extends ZooKeeper {
	Logger logger = Logger.getLogger(this.getClass());

	public NotifiableZookeeper(String connectString, int sessionTimeout, IZookeeperWatcher watcher) throws IOException,
			InterruptedException {
		super(connectString, sessionTimeout, watcher);
		synchronized (watcher) {
			logger.debug("Waiting for connection establish");
			watcher.wait(1000);
		}
	}

}
