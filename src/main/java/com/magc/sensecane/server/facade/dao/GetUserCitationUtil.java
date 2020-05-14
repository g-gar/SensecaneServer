package com.magc.sensecane.server.facade.dao;

import java.util.List;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.generics.TriParamerizedFunction;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

public class GetUserCitationUtil<T, S> extends AbstractDaoUtil implements TriParamerizedFunction<T, S, CitationFilter, CitationTable> {

	public GetUserCitationUtil(Container container) {
		super(container);
	}

	@Override
	public CitationTable apply(T param1, S param2, CitationFilter filter) {
		return this.<CitationTable>tryOr(
			() -> execute((Integer) param1, (Integer) param2, filter), 
			() -> execute((User) param1, (CitationTable) param2, filter)
		);
	}

	private CitationTable execute(User param1, CitationTable param2, CitationFilter filter) {
		return execute(param1.getId(), param2.getId(), filter);
	}
	
	private CitationTable execute(Integer userId, Integer citationId, CitationFilter filter) {
		return new GetUserUtil(container).apply(userId) != null 
				? new GetUserCitationsUtil<Integer>(container).apply(userId, filter).stream()
					.filter(e -> e.getId().equals(citationId))
					.findFirst().orElse(null)
				: null;
	}
}
