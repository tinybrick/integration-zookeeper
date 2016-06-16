package net.tinybrick.integration.serviceconfig.resolver;

public interface IServiceConfigResolver<T> {
	public abstract T getServiceConfig();
}