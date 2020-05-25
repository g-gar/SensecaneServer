package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

import spark.Request;
import spark.Response;

public class GetUserCitationRoute extends AbstractGetRoute<CitationTable> implements Authenticable {

	public GetUserCitationRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<CitationTable> serve(Request request, Response response) throws Exception {
		Integer userId = null, citationId = null;
		CitationTable result = null;
		
		if (( userId = Integer.parseInt(request.params(":user"))) != null && ( citationId = Integer.parseInt(request.params(":citation"))) != null) {
			result = DaoFacade.getUserCitation(userId, citationId, CitationFilter.ANY);
		}
		
		return new PreSerializedJson<CitationTable>(result);
	}

}
