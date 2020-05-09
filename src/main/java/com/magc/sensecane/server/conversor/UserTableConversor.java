
package com.magc.sensecane.server.conversor;

import com.magc.sensecane.framework.container.Container;
import com.magc.sensecane.framework.conversor.ConversorContainer;
import com.magc.sensecane.framework.dao.Dao;
import com.magc.sensecane.framework.dao.DaoContainer;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.model.database.TypeTable;
import com.magc.sensecane.model.database.UserTable;
import com.magc.sensecane.model.database.UserTokenTable;
import com.magc.sensecane.model.database.UserTypeTable;
import com.magc.sensecane.model.domain.User;

public class UserTableConversor extends AbstractConversor<UserTable, User> {

	public UserTableConversor(Container container) {
		super(container);
	}

	@Override
	public User convert(UserTable usertable) {
		DaoContainer daocontainer = container.get(DaoContainer.class);
		Dao<UserTable> udao = daocontainer.get(UserTable.class);
		Dao<UserTypeTable> utydao = daocontainer.get(UserTypeTable.class);
		Dao<TypeTable> tydao = daocontainer.get(TypeTable.class);
		Dao<UserTokenTable> utodao = daocontainer.get(UserTokenTable.class);
		
		UserTypeTable usertypetable = utydao.findAll().stream().filter(utt -> utt.getUserId().equals(usertable.getId())).findFirst().orElse(null);
		TypeTable typetable = null;
		if (usertypetable != null) {
			try {
				typetable = tydao.find(usertypetable.getTypeId());
			} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			}
		}
		UserTokenTable tokentable = utodao.findAll().stream().filter(utt -> utt.getUserId().equals(usertable.getId())).findFirst().orElse(null);
		return new User(usertable.getId(), usertable.getUsername(), usertable.getPassword(), container.get(ConversorContainer.class).convert(typetable), tokentable.getToken());
	}

	@Override
	public Boolean canProcess(UserTable param) {
		return param != null && param.getClass().equals(UserTable.class) && param.getId() != null;
	}
}
