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

public class GetUserTicketsRoute<T> extends AbstractGetRoute<T> {

	public GetUserTicketsRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		List<PreSerializedJson<MessageTable>> messages = null;
		try {
			if (super.isValidRequest(request, response)) {
				Integer id = Integer.valueOf(request.params(":user"));
				messages = DaoFacade.getUserMessages(id, MessageFilter.ANY).stream()
						.map(e -> new PreSerializedJson<MessageTable>(e))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.toJson(messages);
	}

}
