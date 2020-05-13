package com.magc.sensecane.server.model.filter;

public interface Filter<A,B> {
	
	Boolean apply(A a, B b);
	
}
