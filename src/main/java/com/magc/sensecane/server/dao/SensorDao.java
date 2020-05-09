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
import com.magc.sensecane.model.database.DoctorPatientCitationTable;
import com.magc.sensecane.model.database.SensorTable;

public class SensorDao extends CachedDao<SensorTable> {

	public SensorDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public SensorTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from sensor where sensor.id = ?";
//		SensorTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, SensorTable.class);
//			result = DaoUtils.parseResultSetAs(rs, SensorTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public List<SensorTable> findAll() {
		return super.findAll();
	}

	@Override
	public SensorTable insertOrUpdate(SensorTable sensor) {
		SensorTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (sensor.getId() != null) {
				sql = "update sensor set sensor.name = ? where sensor.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setString(1, sensor.getName());
					ps.setInt(2, sensor.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(sensor.getId(), SensorTable.class);
					result = sensor;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into sensor(name) values (?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, sensor.getName());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new SensorTable(rs.getInt(1), sensor.getName());
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
	public SensorTable remove(Integer id) {
		SensorTable result = null;
		String sql = "delete from sensor where sensor.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, SensorTable.class);
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
	public List<SensorTable> removeAll() {
		List<SensorTable> result = null;
		List<SensorTable> sensorTable = super.findAll();
		if (sensorTable.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from sensor where sensor.id in (");
			StringJoiner sj = new StringJoiner(", ");
			sensorTable.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < sensorTable.size()) {
					ps.setInt(i + 1, sensorTable.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == sensorTable.size()) {
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
		String sql = "truncate sensor";
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
		String sql = "select * from sensor";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, SensorTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
