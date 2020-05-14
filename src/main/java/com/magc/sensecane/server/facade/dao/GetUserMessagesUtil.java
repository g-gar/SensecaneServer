package com.magc.sensecane.server.facade.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.MessageTable;
import com.magc.sensecane.server.model.database.UserTable;
import com.magc.sensecane.server.model.filter.MessageFilter;

public class GetUserMessagesUtil<T> extends AbstractDaoUtil implements BiParameterizedFunction<T, MessageFilter, List<MessageTable>> {

	public GetUserMessagesUtil(Container container) {
		super(container);
	}

	@Override
	public List<MessageTable> apply(T param1, MessageFilter param2) {
		return this.<List<MessageTable>>tryOr(
			() -> execute((Integer) param1, param2), 
			() -> execute((UserTable) param1, param2),
			() -> execute((User) param1, param2)
		);
	}
	
	private List<MessageTable> execute(UserTable usertable, MessageFilter filter) {
		return execute(usertable.getId(), filter);
	}
	
	private List<MessageTable> execute(User user, MessageFilter filter) {
		return execute(user.getId(), filter);
	}
	
	private List<MessageTable> execute(Integer id, MessageFilter filter) {
		return get(MessageTable.class).findAll().stream()
			.filter(m -> filter.apply(id, m))
			.collect(Collectors.toList());
	}

}
