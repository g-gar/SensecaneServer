package com.magc.sensecane.server.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.model.BaseEntity;

public abstract class CachedDao<T extends BaseEntity> implements Dao<T> { 
	
	protected ConnectionPool pool;
	protected final Map<Integer, T> cache;
	
	public CachedDao(ConnectionPool pool) {
		this.pool = pool;
		this.cache = new ConcurrentHashMap<>();
	}
	
	public void empty() {
		for (Integer e : this.cache.keySet()) {
			this.cache.remove(e);
		}
	}
	public abstract void reloadCache();

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.cache.containsKey(id);
	}

	@Override
	public T find(Integer id) throws InstanceNotFoundException {
		return this.cache.get(id);
	}

	@Override
	public List<T> findAll() {
		return this.cache.values().stream().collect(Collectors.toList());
	}

	@Override
	public T insertOrUpdate(T entity) {
		T result = null;
		Map<String, Object> attrs = entity.getAttributes();
		Integer id;
		if (attrs.containsKey("id")) {
			if ((id = (Integer) attrs.get("id")) != null) {
				this.cache.put(id, entity);
			} else {
				id = this.cache.keySet().stream().max(Comparator.naturalOrder()).orElse(1);
				this.cache.put(id, entity);
			}
			result = this.cache.get(id);
		}
		return result;
	}

	@Override
	public T remove(Integer id) {
		T result = null;
		Integer temp = this.cache.keySet().stream().filter(e -> ((Integer) this.cache.get(e).getAttributes().get("id")).equals(id)).findFirst().orElse(null);
		if (temp != null) {
			result = this.cache.remove(temp);
		}
		return result;
	}

	@Override
	public List<T> removeAll() {
		List<T> result = new ArrayList<T>();
		Integer id;
		Entry<Integer, T> entry;
		for (Iterator<Entry<Integer, T>> it = this.cache.entrySet().iterator(); it.hasNext();) {
			entry = it.next();
			if (entry.getValue() != null) {
				id = (Integer) entry.getValue().getAttributes().get("id");
				result.add(this.cache.remove(entry.getKey()));
			}
		}
		return result;
	}

}
