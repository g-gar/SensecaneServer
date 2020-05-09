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
import com.magc.sensecane.model.database.UserTokenTable;

public class UserTokenDao extends CachedDao<UserTokenTable> {

	public UserTokenDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public UserTokenTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from user_token where user_token.id = ?";
//		UserTokenTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, UserTokenTable.class);
//			result = DaoUtils.parseResultSetAs(rs, UserTokenTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public UserTokenTable insertOrUpdate(UserTokenTable userToken) {
		UserTokenTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (userToken.getId() != null) {
				sql = "update user_token set user_token.user_id = ?, user_token.token = ? where user_token.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setInt(1, userToken.getUserId());
					ps.setString(2, userToken.getToken());
					ps.setInt(3, userToken.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(userToken.getId(), UserTokenTable.class);
					result = userToken;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into user_token( user_id, token) values (?, ?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, userToken.getUserId());
					ps.setString(2, userToken.getToken());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new UserTokenTable(rs.getInt(1), userToken.getUserId(), userToken.getToken());
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
	public UserTokenTable remove(Integer id) {
		UserTokenTable result = null;
		String sql = "delete from user_token where user_token.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, UserTokenTable.class);
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
	public List<UserTokenTable> removeAll() {
		List<UserTokenTable> result = null;
		List<UserTokenTable> userTokens = super.findAll();
		if (userTokens.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from user_token where user_token.id in (");
			StringJoiner sj = new StringJoiner(", ");
			userTokens.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < userTokens.size()) {
					ps.setInt(i + 1, userTokens.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == userTokens.size()) {
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
		String sql = "truncate user_token";
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
		String sql = "select * from user_token";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, UserTokenTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
