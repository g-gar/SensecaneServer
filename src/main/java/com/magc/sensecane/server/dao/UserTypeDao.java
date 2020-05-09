package com.magc.sensecane.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.StringJoiner;

import com.magc.sensecane.framework.database.connection.pool.ConnectionPool;
import com.magc.sensecane.framework.exception.InstanceNotFoundException;
import com.magc.sensecane.framework.utils.DaoUtils;
import com.magc.sensecane.model.database.UserTypeTable;

public class UserTypeDao extends CachedDao<UserTypeTable> {

	public UserTypeDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}
//
//	@Override
//	public UserTypeTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from user_type where user_type.id = ?";
//		UserTypeTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, UserTypeTable.class);
//			result = DaoUtils.parseResultSetAs(rs, UserTypeTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public UserTypeTable insertOrUpdate(UserTypeTable userType) {
		UserTypeTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (userType.getId() != null) {
				sql = "update user_type set user_type.user_id = ?, user_type.type_id = ? where user_type.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setInt(1, userType.getUserId());
					ps.setInt(2, userType.getTypeId());
					ps.setInt(3, userType.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(userType.getId(), UserTypeTable.class);
					result = userType;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into user_type( user_id, type_id) values (?, ?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, userType.getUserId());
					ps.setInt(2, userType.getTypeId());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new UserTypeTable(rs.getInt(1), userType.getUserId(), userType.getTypeId());
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			}
			result = super.insertOrUpdate(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
		return result;
	}

	@Override
	public UserTypeTable remove(Integer id) {
		UserTypeTable result = null;
		String sql = "delete from user_type where user_type.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, UserTypeTable.class);
			else {
				result = super.remove(id);
			}
		} catch (SQLException | InstanceNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
		return result;
	}

	@Override
	public List<UserTypeTable> removeAll() {
		List<UserTypeTable> result = null;
		List<UserTypeTable> userTypes = super.findAll();
		if (userTypes.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from user_type where user_type.id in (");
			StringJoiner sj = new StringJoiner(", ");
			userTypes.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < userTypes.size()) {
					ps.setInt(i + 1, userTypes.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == userTypes.size()) {
					result = super.removeAll();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				pool.release(connection);
			}
		}
		return result;
	}

	@Override
	public void truncate() {
		String sql = "truncate user_type";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

	@Override
	public void reloadCache() {
		// Loading data from table
		String sql = "select * from user_type";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, UserTypeTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
