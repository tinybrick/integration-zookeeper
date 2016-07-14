package net.tinybrick.integration.serviceconfig;

public class RestServiceConfig extends ServerConfig {
	public enum METHOD {
		GET, POST, DELETE, UPDATE
	}

	METHOD method;
	String path;
	String context;

	public METHOD getMethod() {
		return method;
	}

	public void setMethod(METHOD method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUriBase() {
		return (tlsEnabled ? "https" : "http") + "://" + address + ":" + (0 == port ? (tlsEnabled ? 443 : 80) : port);
	}

	public String getUri() {
		return getUriBase() + "/" + (context == null || "".equals(context) ? "" : context + "/") + getPath();
	}

	public int getPort() {
		return super.getPort() == 0 ? 80 : super.getPort();
	}
}