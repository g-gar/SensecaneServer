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
import com.magc.sensecane.model.database.CitationTable;
import com.magc.sensecane.model.database.SensorTable;

public class CitationDao extends CachedDao<CitationTable> {

	public CitationDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public CitationTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from citation where citation.id = ?";
//		CitationTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, CitationTable.class);
//			result = DaoUtils.parseResultSetAs(rs, CitationTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public List<CitationTable> findAll() {
		return super.findAll();
	}

	@Override
	public CitationTable insertOrUpdate(CitationTable citation) {
		CitationTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (citation.getId() != null) {
				sql = "update citation set citation.timestamp = ?, citation.message = ?, citation.location = ? where citation.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setLong(1, citation.getTimestamp());
					ps.setString(2, citation.getMessage());
					ps.setString(3, citation.getLocation());
					ps.setInt(4, citation.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(citation.getId(), CitationTable.class);
					result = citation;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into citation(timestamp, message, location) values (?,?,?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setLong(1, citation.getTimestamp());
					ps.setString(2, citation.getMessage());
					ps.setString(3, citation.getLocation());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new CitationTable(rs.getInt(1), citation.getLocation(), citation.getTimestamp(), citation.getMessage());
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
	public CitationTable remove(Integer id) {
		CitationTable result = null;
		String sql = "delete from citation where citation.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, CitationTable.class);
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
	public List<CitationTable> removeAll() {
		List<CitationTable> result = null;
		List<CitationTable> citationTable = super.findAll();
		if (citationTable.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from citation where citation.id in (");
			StringJoiner sj = new StringJoiner(", ");
			citationTable.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < citationTable.size()) {
					ps.setInt(i + 1, citationTable.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == citationTable.size()) {
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
		String sql = "truncate citation";
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
		String sql = "select * from citation";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, CitationTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
