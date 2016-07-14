package net.tinybrick.integration.zookeeper;

import org.apache.zookeeper.Watcher;

public interface IZookeeperWatcher extends Watcher {
	void setOwner(ZookeeperClient zkClient);

	//void onFolderChange(List<String> newList);
	void onDataChange(byte[] newData);

	void onConnected();
}
