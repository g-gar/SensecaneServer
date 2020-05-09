package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.model.database.CitationTable;
import com.magc.sensecane.model.domain.Citation;

public class CitationConversor extends AbstractConversor<Citation, CitationTable> {

	public CitationConversor(Container container) {
		super(container);
	}

	@Override
	public CitationTable convert(Citation citation) {
		return new CitationTable(citation.getId(), citation.getLocation(), citation.getTimestamp(), citation.getMessage());
	}

	@Override
	public Boolean canProcess(Citation param) {
		return param != null && param.getClass().equals(Citation.class);
	}

}
