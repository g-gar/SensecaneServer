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
import com.magc.sensecane.model.database.SensorPatientTable;
import com.magc.sensecane.model.database.TicketTable;

public class SensorPatientDao extends CachedDao<SensorPatientTable> {

	public SensorPatientDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public SensorPatientTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from sensorPatient where sensorPatient.id = ?";
//		SensorPatientTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, SensorPatientTable.class);
//			result = DaoUtils.parseResultSetAs(rs, SensorPatientTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public List<SensorPatientTable> findAll() {
		return super.findAll();
	}

	@Override
	public SensorPatientTable insertOrUpdate(SensorPatientTable sensorPatient) {
		SensorPatientTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (sensorPatient.getId() != null) {
				sql = "update sensor_patient set sensor_patient.sensor_id = ?, sensor_patient.user_id = ?, sensor_patient.timestamp = ?, sensor_patient.measurement = ? where sensor_patient.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setInt(1, sensorPatient.getSensorId());
					ps.setInt(2, sensorPatient.getPatientId());
					ps.setLong(3, sensorPatient.getTimestamp());
					ps.setDouble(4, sensorPatient.getMeasurement());
					ps.setInt(5, sensorPatient.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(sensorPatient.getId(), SensorPatientTable.class);
					result = sensorPatient;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into sensor_patient(sensor_id, patient_id, timestamp, measurement) values (?,?,?,?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, sensorPatient.getSensorId());
					ps.setInt(2, sensorPatient.getPatientId());
					ps.setLong(3, sensorPatient.getTimestamp());
					ps.setDouble(4, sensorPatient.getMeasurement());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new SensorPatientTable(rs.getInt(1), sensorPatient.getSensorId(),
							sensorPatient.getPatientId(), sensorPatient.getMeasurement(), sensorPatient.getTimestamp());
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			}
			result = super.insertOrUpdate(result);
		}catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
		return result;
	}

	@Override
	public SensorPatientTable remove(Integer id) {
		SensorPatientTable result = null;
		String sql = "delete from sensor_patient where sensor_patient.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, SensorPatientTable.class);
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
	public List<SensorPatientTable> removeAll() {
		List<SensorPatientTable> result = null;
		List<SensorPatientTable> sensorPatientTable = super.findAll();
		if (sensorPatientTable.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from sensor_patient where sensor_patient.id in (");
			StringJoiner sj = new StringJoiner(", ");
			sensorPatientTable.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < sensorPatientTable.size()) {
					ps.setInt(i + 1, sensorPatientTable.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == sensorPatientTable.size()) {
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
		String sql = "truncate sensor_patient";
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
		String sql = "select * from sensor_patient";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, SensorPatientTable.class).stream().forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
