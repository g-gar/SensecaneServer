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
import com.magc.sensecane.model.database.TicketUserTable;

public class TicketUserDao extends CachedDao<TicketUserTable> {

	public TicketUserDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public TicketUserTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from ticket_user where ticket_user.id = ?";
//		TicketUserTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, SensorPatientTable.class);
//			result = DaoUtils.parseResultSetAs(rs, TicketUserTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public List<TicketUserTable> findAll() {
		return super.findAll();
	}

	@Override
	public TicketUserTable insertOrUpdate(TicketUserTable ticketUser) {
		TicketUserTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (ticketUser.getId() != null) {
				sql = "update ticket_user set ticket_user.user_to = ?, ticket_user.user_from = ?, ticket_user.ticket_id = ?, ticket_user.timestamp = ? where ticket_user.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setInt(1, ticketUser.getUserFrom());
					ps.setInt(2, ticketUser.getUserTo());
					ps.setLong(3, ticketUser.getTicketId());
					ps.setDouble(4, ticketUser.getTimestamp());
					ps.setInt(5, ticketUser.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(ticketUser.getId(), TicketUserTable.class);
					result = ticketUser;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into ticket_user(user_to, user_from, ticket_id, timestamp) values (?,?,?,?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, ticketUser.getUserFrom());
					ps.setInt(2, ticketUser.getUserTo());
					ps.setLong(3, ticketUser.getTicketId());
					ps.setDouble(4, ticketUser.getTimestamp());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new TicketUserTable(rs.getInt(1), ticketUser.getUserFrom(), ticketUser.getUserTo(),
							ticketUser.getTicketId(), ticketUser.getTimestamp());
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
	public TicketUserTable remove(Integer id) {
		TicketUserTable result = null;
		String sql = "delete from ticket_user where ticket_user.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, TicketUserTable.class);
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
	public List<TicketUserTable> removeAll() {
		List<TicketUserTable> result = null;
		List<TicketUserTable> ticketUser = super.findAll();
		if (ticketUser.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from ticket_user where ticket_user.id in (");
			StringJoiner sj = new StringJoiner(", ");
			ticketUser.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < ticketUser.size()) {
					ps.setInt(i + 1, ticketUser.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == ticketUser.size()) {
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
		String sql = "truncate ticket_user";
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
		String sql = "select * from ticket_user";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, TicketUserTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
