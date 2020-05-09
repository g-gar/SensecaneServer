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
import com.magc.sensecane.model.database.UserTable;

public class UserDao extends CachedDao<UserTable> {

	public UserDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

	@Override
	public UserTable insertOrUpdate(UserTable user) {
		UserTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (user.getId() != null) {
				sql = "update user set user.username = ?, user.password = ? where user.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql)) {
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getPassword());
					ps.setInt(3, user.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0) throw new InstanceNotFoundException(user.getId(), UserTable.class);
					result = user;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into user( username, password) values (?, ?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, user.getUsername());
					ps.setString(2, user.getPassword());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys(); 
					if (!rs.next()) throw new SQLException("JDBC driver did not return generated key.");
					result = new UserTable(rs.getInt(1), user.getUsername(), user.getPassword());
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
	public UserTable remove(Integer id) {
		UserTable result = null;
		String sql = "delete from user where user.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0) throw new InstanceNotFoundException(id, UserTable.class);
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
	public List<UserTable> removeAll() {
		List<UserTable> result = null;
		List<UserTable> users = super.findAll();
		if (users.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from user where user.id in (");
			StringJoiner sj = new StringJoiner(", ");
			users.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < users.size()) {
					ps.setInt(i+1, users.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == users.size()) {
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
		String sql = "truncate user";
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
		String sql = "select * from user";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, UserTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
