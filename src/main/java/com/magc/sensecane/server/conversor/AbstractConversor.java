package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.Conversor;

public abstract class AbstractConversor<T, R> implements Conversor<T, R> {

	protected final Container container;
	
	public AbstractConversor(Container container) {
		this.container = container;
	}

	public Boolean canProcess(T param) {
		return false;
	}
}
