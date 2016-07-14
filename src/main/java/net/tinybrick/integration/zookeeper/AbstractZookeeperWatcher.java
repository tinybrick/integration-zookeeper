package net.tinybrick.integration.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;

public abstract class AbstractZookeeperWatcher implements IZookeeperWatcher {
	Logger logger = Logger.getLogger(this.getClass());

	ZookeeperClient owner;

	public void setOwner(ZookeeperClient owner) {
		this.owner = owner;
	}

	public void onConnected() {
		synchronized (this) {
			this.notify();
			logger.debug("Connection is established");
		}
	}

	public void process(WatchedEvent event) {
		logger.debug("Event: " + event.toString());

		try {
			switch (event.getType()) {
				case NodeDataChanged:
				case NodeCreated:
					owner.getNodeData(event.getPath());
					break;

				case NodeDeleted:
					onDataChange(null);
					break;

				default:
					logger.warn("Event type " + event.getType() + " is not supported yet!");
					break;
			}

			switch (event.getState()) {
				case Disconnected:
				case Expired:
					owner.connect();
					owner.getNodeData(event.getPath());
					break;

				case SyncConnected:
					onConnected();
					break;

				default:
					logger.warn("Event state " + event.getState() + " is not supported yet!");
					break;
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
