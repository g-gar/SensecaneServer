package com.magc.sensecane.server.facade.dao;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.BiParameterizedFunction;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

public class GetUserCitationsUtil<T> extends AbstractDaoUtil implements BiParameterizedFunction<T, CitationFilter, List<CitationTable>> {

	public GetUserCitationsUtil(Container container) {
		super(container);
	}

	@Override
	public List<CitationTable> apply(T param1, CitationFilter param2) {
		return this.<List<CitationTable>>tryOr(
			() -> execute((Integer) param1, param2), 
			() -> execute((User) param1, param2)
		);
	}

	private List<CitationTable> execute(User user, CitationFilter filter) {
		return execute(user.getId(), filter);
	}
	
	private List<CitationTable> execute(Integer userId, CitationFilter filter) {
		return get(CitationTable.class).findAll().stream()
				.filter(e -> filter.apply(userId, e))
				.collect(Collectors.toList());
	}
}
