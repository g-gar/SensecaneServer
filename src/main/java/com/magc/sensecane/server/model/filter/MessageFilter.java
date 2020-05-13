package com.magc.sensecane.server.model.filter;

import java.util.function.BiFunction;

import com.magc.sensecane.framework.model.filter.Filter;
import com.magc.sensecane.server.model.database.MessageTable;

public enum MessageFilter implements Filter<Integer, MessageTable> {

	FROM((Integer id, MessageTable m) -> id != null && m != null && m.getUserFrom().equals(id)),
	TO((Integer id, MessageTable m) -> id != null && m != null && m.getUserTo().equals(id)),
	ANY((Integer id, MessageTable m) -> id != null && m != null && (FROM.apply(id, m) || TO.apply(id, m)));

	private final BiFunction<Integer, MessageTable, Boolean> fn;
	
	private MessageFilter(BiFunction<Integer, MessageTable, Boolean> fn) {
		this.fn = fn;
	}
	
	@Override
	public Boolean apply(Integer id, MessageTable message) {
		return fn.apply(id, message);
	}
	
}
