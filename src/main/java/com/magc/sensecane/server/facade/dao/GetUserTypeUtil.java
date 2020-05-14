package com.magc.sensecane.server.facade.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.generics.MonoParameterizedFunction;
import com.magc.sensecane.server.App;
import com.magc.sensecane.server.facade.AbstractDaoUtil;
import com.magc.sensecane.server.model.Type;
import com.magc.sensecane.server.model.User;
import com.magc.sensecane.server.model.database.CarerTable;
import com.magc.sensecane.server.model.database.DoctorTable;
import com.magc.sensecane.server.model.database.PatientTable;

public class GetUserTypeUtil<T> extends AbstractDaoUtil implements MonoParameterizedFunction<T, Type> {

	public GetUserTypeUtil(Container container) {
		super(container);
	}

	@Override
	public Type apply(T param) {
		return this.<Type>tryOr(
			() -> execute((Integer) param), 
			() -> execute((User) param)
		);
	}
	
	private Type execute(Integer id) {
		Map<Type, Dao> daos = new HashMap<Type, Dao>() {
			{
				put(Type.CARER, App.getInstance().get(DaoContainer.class).get(CarerTable.class));
				put(Type.PATIENT, App.getInstance().get(DaoContainer.class).get(PatientTable.class));
				put(Type.DOCTOR, App.getInstance().get(DaoContainer.class).get(DoctorTable.class));
			}
		};
		
		Iterator<Map.Entry<Type, Dao>> it = daos.entrySet().iterator(); 
		Type type = null;
		while (type == null && it.hasNext()) {
			Map.Entry<Type, Dao> entry = it.next();
			try {
				if ( entry.getValue().find(id) != null ) {
					type = entry.getKey();
				}
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			}
		}
		return type;
	}

	private Type execute(User user) {
		return this.execute(user.getId());
	}
}
