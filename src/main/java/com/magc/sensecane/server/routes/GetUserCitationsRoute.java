package com.magc.sensecane.server.routes;

import java.util.List;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.model.json.PreSerializedJson;
import com.magc.sensecane.framework.spark.AbstractGetRoute;
import com.magc.sensecane.framework.spark.Authenticable;
import com.magc.sensecane.server.facade.DaoFacade;
import com.magc.sensecane.server.model.database.CitationTable;
import com.magc.sensecane.server.model.filter.CitationFilter;

import spark.Request;
import spark.Response;

public class GetUserCitationsRoute extends AbstractGetRoute<CitationTable> implements Authenticable {

	public GetUserCitationsRoute(Container container) {
		super(container);
	}

	@Override
	public PreSerializedJson<CitationTable> serve(Request request, Response response) throws Exception {
		Integer id;
		List<CitationTable> results = null;
		
		if (( id = Integer.parseInt(request.params(":user"))) != null) {
			results = DaoFacade.getUserCitations(id, CitationFilter.ANY);
		}
		
		return new PreSerializedJson<CitationTable>(results, "*");
	}

}
