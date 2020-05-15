package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.MessageTable;
import com.magc.sensecane.server.model.filter.MessageFilter;

import spark.Request;
import spark.Response;

public class GetUserTicketsRoute extends AbstractGetRoute<MessageTable> {

	public GetUserTicketsRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<MessageTable> serve(Request request, Response response) throws Exception {
		List<MessageTable> messages = null;
		
		Integer id = Integer.valueOf(request.params(":user"));
		messages = DaoFacade.getUserMessages(id, MessageFilter.ANY).stream()
				.collect(Collectors.toList());
		
		return new PreSerializedJson<MessageTable>(messages, "*");
	}

}
