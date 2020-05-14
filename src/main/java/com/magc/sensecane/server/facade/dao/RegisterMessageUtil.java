package com.magc.sensecane.server.facade.dao;

import java.sql.Timestamp;
import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.TriParamerizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.MessageTable;
import com.magc.sensecane.server.model.database.UserTable;

public class RegisterMessageUtil<T> extends AbstractDaoUtil implements TriParamerizedFunction<T, T, Map<String,String>, MessageTable> {

	public RegisterMessageUtil(Container container) {
		super(container);
	}

	@Override
	public MessageTable apply(T from, T to, Map<String, String> param3) {
		return this.<MessageTable>tryOr(
			() -> execute((Integer) from, (Integer) to, param3), 
			() -> execute((User) from, (User) to, param3)
		);
	}
	
	private MessageTable execute(User from, User to, Map<String, String> params) {
		return execute(from.getId(), to.getId(), params);
	}

	private MessageTable execute(Integer fromId, Integer toId, Map<String, String> params) {
		MessageTable result = null;
		UserTable from = null, to = null;
		try {
			if (
				(from = get(UserTable.class).find(fromId)) != null
				&& (to = get(UserTable.class).find(toId)) != null
				&& params.containsKey("message")
			) {
				result = get(MessageTable.class).insertOrUpdate(new MessageTable(
					null, 
					from.getId(), 
					to.getId(), 
					new Timestamp(System.currentTimeMillis()).getTime(), 
					params.get("message")
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
}
