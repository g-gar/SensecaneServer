package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.domain.User;

public class UserConversor extends AbstractConversor<User, UserTable>{

	public UserConversor(Container container) {
		super(container);
	}

	@Override
	public UserTable convert(User user) {
		return new UserTable(user.getId(), user.getUsername(), user.getPassword());
	}
	
	@Override
	public Boolean canProcess(User param) {
		return param != null && param.getClass().equals(User.class) ;
	}

}