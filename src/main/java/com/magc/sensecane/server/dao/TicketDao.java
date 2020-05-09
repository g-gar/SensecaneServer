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
import com.magc.sensecane.model.database.TicketTable;

public class TicketDao extends CachedDao<TicketTable> {

	public TicketDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public TicketTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from ticket where ticket.id = ?";
//		TicketTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, UserTable.class);
//			result = DaoUtils.parseResultSetAs(rs, TicketTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public List<TicketTable> findAll() {
		return super.findAll();
	}

	@Override
	public TicketTable insertOrUpdate(TicketTable ticket) {
		TicketTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (ticket.getId() != null) {
				sql = "update ticket set ticket.message = ? where ticket.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setString(1, ticket.getMessage());
					ps.setInt(2, ticket.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(ticket.getId(), TicketTable.class);
					result = ticket;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into ticket(message) values (?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, ticket.getMessage());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new TicketTable(rs.getInt(1), ticket.getMessage());
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
	public TicketTable remove(Integer id) {
		TicketTable result = null;
		String sql = "delete from ticket where ticket.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, TicketTable.class);
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
	public List<TicketTable> removeAll() {
		List<TicketTable> result = null;
		List<TicketTable> ticket = super.findAll();
		if (ticket.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from ticket where ticket.id in (");
			StringJoiner sj = new StringJoiner(", ");
			ticket.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < ticket.size()) {
					ps.setInt(i + 1, ticket.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == ticket.size()) {
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
		String sql = "truncate ticket";
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
		String sql = "select * from ticket";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, TicketTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
