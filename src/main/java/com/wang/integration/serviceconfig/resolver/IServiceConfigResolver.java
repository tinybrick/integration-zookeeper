package com.wang.integration.serviceconfig.resolver;

public interface IServiceConfigResolver<T> {
	public abstract T getServiceConfig();
}