package com.magc.sensecane.server.routes;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

import spark.Request;
import spark.Response;

public class GetUserCitationRoute extends AbstractGetRoute<String> {

	public GetUserCitationRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Integer userId = null, citationId = null;
		PreSerializedJson<CitationTable> result = null;
		
		try {
			if (super.isValidRequest(request, response) 
				&& ( userId = Integer.parseInt(request.params(":user"))) != null
				&& ( citationId = Integer.parseInt(request.params(":citation"))) != null
			) {
				
				result = new PreSerializedJson<CitationTable>(DaoFacade.getUserCitation(userId, citationId, CitationFilter.ANY));
				
				response.status(200);
				response.type("application/json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(result);
	}

}
