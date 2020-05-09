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
import com.magc.sensecane.model.database.TypeTable;

public class TypeDao extends CachedDao<TypeTable> {

	public TypeDao(ConnectionPool pool) {
		super(pool);
	}

//	@Override
//	public TypeTable find(Integer id) throws InstanceNotFoundException {
//		TODO: mirar
//		String sql = "select * from type where type.id = ?";
//		TypeTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, TypeTable.class);
//			System.out.println(1);
//			System.out.println(DaoUtils.parseResultSetAs(rs, TypeTable.class).stream().count());
//			result = new ArrayList<TypeTable>(DaoUtils.parseResultSetAs(rs, TypeTable.class)).get(0);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//		
//		System.out.println("TypeDao find:" + result);
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public TypeTable insertOrUpdate(TypeTable type) {
		TypeTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (type.getId() != null) {
				sql = "update type set type.type = ? where type.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setString(1, type.getType());
					ps.setInt(2, type.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(type.getId(), TypeTable.class);
					result = type;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into type(type) values (?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, type.getType());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new TypeTable(rs.getInt(1), type.getType());
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
	public TypeTable remove(Integer id) {
		TypeTable result = null;
		String sql = "delete from type where type.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, TypeTable.class);
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
	public List<TypeTable> removeAll() {
		List<TypeTable> result = null;
		List<TypeTable> types = super.findAll();
		if (types.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from type where type.id in (");
			StringJoiner sj = new StringJoiner(", ");
			types.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < types.size()) {
					ps.setInt(i + 1, types.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == types.size()) {
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
		String sql = "truncate type";
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
		String sql = "select * from type";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, TypeTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
