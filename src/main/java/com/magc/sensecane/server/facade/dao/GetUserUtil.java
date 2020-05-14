package com.magc.sensecane.server.facade.dao;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.UserTable;

public class GetUserUtil extends AbstractDaoUtil implements MonoParameterizedFunction<Integer, User> {

	public GetUserUtil(Container container) {
		super(container);
	}

	@Override
	public User apply(Integer param) {
		User user = null;
		UserTable usertable = null;
		try {
			if (( usertable = get(UserTable.class).find(param) ) != null) {
				user = DaoFacade.getUserInfo(usertable);
			}
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}

}
