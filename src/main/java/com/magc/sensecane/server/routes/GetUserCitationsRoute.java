package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

import spark.Request;
import spark.Response;

public class GetUserCitationsRoute extends AbstractGetRoute<Void> {

	public GetUserCitationsRoute(Container container) {
		super(container);
	}

	@Override
	public String handle(Request request, Response response) throws Exception {
		Integer id;
		List<PreSerializedJson<CitationTable>> results = null;
		
		try {
			if (super.isValidRequest(request, response) && ( id = Integer.parseInt(request.params(":user"))) != null) {
				
				results = DaoFacade.getUserCitations(id, CitationFilter.ANY).stream()
						.map(e -> new PreSerializedJson<CitationTable>(e))
						.collect(Collectors.toList());
				response.status(200);
				response.type("application/json");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.toJson(results);
	}

}
