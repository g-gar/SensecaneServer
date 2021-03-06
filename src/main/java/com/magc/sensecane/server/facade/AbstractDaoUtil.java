package com.magc.sensecane.server.facade;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.facade.util.AbstractFacadeUtil;
import com.magc.sensecane.framework.model.database.TableEntity;

public abstract class AbstractDaoUtil extends AbstractFacadeUtil {
	
	protected final Container container;
	
	public AbstractDaoUtil(Container container) {
		this.container = container;
	}

	public <T extends TableEntity> Dao<T> get(Class<T> clazz) {
		return container.get(DaoContainer.class).get(clazz);
	}
	
	
}
