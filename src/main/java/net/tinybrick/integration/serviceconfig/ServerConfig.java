package net.tinybrick.integration.serviceconfig;

public abstract class ServerConfig {
	String address;
	int port = 0;
	boolean tlsEnabled = false;
	String certification = null;
	String username = null;
	String password = null;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isTlsEnabled() {
		return tlsEnabled;
	}

	public void setTlsEnabled(boolean tlsEnabled) {
		this.tlsEnabled = tlsEnabled;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
