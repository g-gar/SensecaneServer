package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.facade.ResponseModelFacade;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.MessageTable;
import com.magc.sensecane.server.model.filter.MessageFilter;

import spark.Request;
import spark.Response;

public class GetUserTicketRoute extends AbstractGetRoute<Object> implements Authenticable {

	public GetUserTicketRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<Object> serve(Request request, Response response) throws Exception {
		Integer userId, messageId;
		User user = null;
		Object message = null;
		
		userId = Integer.valueOf(request.params(":user"));
		messageId = Integer.valueOf(request.params(":message"));
		
		if ( (user = DaoFacade.find(userId)) != null ) {
			message = DaoFacade.getUserMessages(user.getId(), MessageFilter.ANY).stream()
				.filter(m -> m.getId().equals(messageId))
				.map(ResponseModelFacade::createTicket)
				.findFirst().orElse(null);
		}
		
		return new PreSerializedJson<Object>(message, "*");
	}

}
