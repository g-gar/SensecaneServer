package com.magc.sensecane.server.facade.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.ZeroParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.database.UserTable;

public class GetAllUsersUtil extends AbstractDaoUtil implements ZeroParameterizedFunction<List<UserTable>> {

	public GetAllUsersUtil(Container container) {
		super(container);
	}

	@Override
	public List<UserTable> apply() {
		return super.get(UserTable.class).findAll().stream()
				.sorted((a,b)->a.getId() >= b.getId() ? 1 : -1)
				.collect(Collectors.toList());
	}

}
