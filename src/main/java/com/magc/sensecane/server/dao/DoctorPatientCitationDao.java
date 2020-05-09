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
import com.magc.sensecane.model.database.TicketTable;

public class DoctorPatientCitationDao extends CachedDao<DoctorPatientCitationTable> {

	public DoctorPatientCitationDao(ConnectionPool pool) {
		super(pool);
	}

	@Override
	public Boolean contains(Integer id) throws InstanceNotFoundException {
		return this.find(id) != null;
	}

//	@Override
//	public DoctorPatientCitationTable find(Integer id) throws InstanceNotFoundException {
//		String sql = "select * from doctor_patient_citation where doctor_patient_citation.id = ?";
//		DoctorPatientCitationTable result = null;
//		Connection connection = pool.get();
//		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//			preparedStatement.setInt(1, id);
//			ResultSet rs = preparedStatement.executeQuery();
//			if (!rs.next())
//				throw new InstanceNotFoundException(id, DoctorPatientCitationTable.class);
//			result = DaoUtils.parseResultSetAs(rs, DoctorPatientCitationTable.class).stream().findFirst().orElse(null);
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			pool.release(connection);
//		}
//
//		return result.equals(super.find(id)) ? result : null;
//	}

	@Override
	public DoctorPatientCitationTable insertOrUpdate(DoctorPatientCitationTable doctorPatientCitation) {
		DoctorPatientCitationTable result = null;
		String sql;
		Connection connection = pool.get();
		try {
			if (doctorPatientCitation.getId() != null) {
				sql = "update citation set doctor_patient_citation.doctor_id = ?, doctor_patient_citation.patient_id = ?, doctor_patient_citation.citation_id = ? where doctor_patient_citation.id = ?";
				try (PreparedStatement ps = connection.prepareStatement(sql);) {
					ps.setInt(1, doctorPatientCitation.getDoctorId());
					ps.setInt(2, doctorPatientCitation.getPatientId());
					ps.setInt(3, doctorPatientCitation.getCitationId());
					ps.setInt(4, doctorPatientCitation.getId());
					int updatedRows = ps.executeUpdate();
					if (updatedRows == 0)
						throw new InstanceNotFoundException(doctorPatientCitation.getId(),
								DoctorPatientCitationTable.class);
					result = doctorPatientCitation;
				} catch (SQLException e) {
					connection.rollback();
					throw new RuntimeException(e);
				}
			} else {
				sql = "insert into doctor_patient_citation(doctor_id, patient_id, citation_id) values (?,?,?)";
				try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					ps.setInt(1, doctorPatientCitation.getDoctorId());
					ps.setInt(2, doctorPatientCitation.getPatientId());
					ps.setInt(3, doctorPatientCitation.getCitationId());
					ps.executeUpdate();
					ResultSet rs = ps.getGeneratedKeys();
					if (!rs.next())
						throw new SQLException("JDBC driver did not return generated key.");
					result = new DoctorPatientCitationTable(rs.getInt(1), doctorPatientCitation.getDoctorId(),
							doctorPatientCitation.getPatientId(), doctorPatientCitation.getCitationId());
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
	public DoctorPatientCitationTable remove(Integer id) {
		DoctorPatientCitationTable result = null;
		String sql = "delete from doctor_patient_citation where doctor_patient_citation.id = ?";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			int removedRows = ps.executeUpdate();
			if (removedRows == 0)
				throw new InstanceNotFoundException(id, DoctorPatientCitationTable.class);
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
	public List<DoctorPatientCitationTable> removeAll() {
		List<DoctorPatientCitationTable> result = null;
		List<DoctorPatientCitationTable> doctorPatientCitationTable = super.findAll();
		if (doctorPatientCitationTable.size() > 0) {
			StringBuilder sql = new StringBuilder("delete from doctor_patient_citation where doctor_patient_citation.id in (");
			StringJoiner sj = new StringJoiner(", ");
			doctorPatientCitationTable.forEach(e -> sj.add("?"));
			sql.append(sj.toString()).append(")");
			Connection connection = pool.get();
			try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
				int i = 0;
				while (i < doctorPatientCitationTable.size()) {
					ps.setInt(i + 1, doctorPatientCitationTable.get(i).getId());
					i++;
				}
				int rows = ps.executeUpdate();
				if (rows == doctorPatientCitationTable.size()) {
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
		String sql = "truncate doctor_patient_citation";
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
		String sql = "select * from doctor_patient_citation";
		Connection connection = pool.get();
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			DaoUtils.parseResultSetAs(rs, DoctorPatientCitationTable.class).stream()
					.forEach(entity -> super.insertOrUpdate(entity));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			pool.release(connection);
		}
	}

}
