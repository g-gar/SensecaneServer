package com.magc.sensecane.server.facade.dao;

import java.util.function.Supplier;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.model.database.TableEntity;

public abstract class AbstractDaoUtil {
	
	protected final Container container;
	
	public AbstractDaoUtil(Container container) {
		this.container = container;
	}

	public <A, T extends TableEntity<A>> Dao<T> get(Class<T> clazz) {
		return container.get(DaoContainer.class).get(clazz);
	}
	
	public <T> boolean isInstance(Class<T> clazz, Object param) {
		return param.getClass().isInstance(clazz);
	}
	
	public <R> R tryOr(Supplier<R>...fns) {
		R result = null;
		
		boolean success = false;
		int i = 0;
		while (!success && i < fns.length) {
			Supplier<R> fn = fns[i++];
			try {
				result = fn.get();
				success = true;
			} catch (Exception e) {
				
			}
		}
		
		if (!success) {
			throw new RuntimeException(new Exception(String.format("[%s] Couldn't find a successful method to apply\"", this.getClass())));
		}
		
		return result;
	}
}
