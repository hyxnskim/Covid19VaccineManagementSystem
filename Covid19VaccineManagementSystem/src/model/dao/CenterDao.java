package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dto.Center;

/**
 * <pre>
 * CenterDao class
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */

public class CenterDao {
	
	// FactoryDao 객체 멤버변수 선언 및 할당
	private FactoryDao factory = FactoryDao.getInstance();

	private CenterDao() {
		//readFile();
	}
	
	private static CenterDao instance = new CenterDao();
	
	public static CenterDao getInstance() {
		return instance;
	}
	
	public int selectCount() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT COUNT(*) FROM CENTER";
			stmt = conn.prepareStatement(sql);

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("COUNT(*)");
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectCount");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		
		return 0;
	}
	

	public boolean insertCenterInfo(Center dto) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = factory.getConnection();
			
			String sql = "INSERT INTO CENTER VALUES(?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, dto.getCenterName());
			stmt.setString(2, dto.getFacName());
			stmt.setString(3, dto.getPostCode());
			stmt.setString(4, dto.getDistrict());
			stmt.setString(5, dto.getAddress());
			stmt.setString(6, dto.getContact());

			int rows = stmt.executeUpdate();
			
			// 5.
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] 센터 정보 추가");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	public ArrayList<String> selectDistrict() {
		ArrayList<String> districts = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT DISTINCT DISTRICT D FROM CENTER";
			stmt = conn.prepareStatement(sql);

			rs = stmt.executeQuery();
			
			while(rs.next()) {
				districts.add(rs.getString("D"));
			}
			return districts;
			
		} catch (SQLException e) {
			System.out.println("[오류] selectDistrict");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		
		return null;
	}
	
	public boolean selectCenterInfo(Center dto) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE CNAME = ? AND FNAME = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, dto.getCenterName());
			stmt.setString(2, dto.getFacName());

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectCenterInfo");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return false;
	}
	
	public boolean selectCenterInfo(String centerName, String facName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE CNAME = ? AND FNAME = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, centerName);
			stmt.setString(2, facName);

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectCenterInfo");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return false;
	}
	
	public Center selectOne(String centerName, String facName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Center dto = new Center();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE CNAME = ? AND FNAME = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, centerName);
			stmt.setString(2, facName);

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				dto.setCenterName(centerName);
				dto.setFacName(facName);
				dto.setPostCode(rs.getString("POSTCODE"));
				dto.setDistrict(rs.getString("DISTRICT"));
				dto.setAddress(rs.getString("ADDRESS"));
				dto.setContact(rs.getString("CONTACT"));
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return dto;
	}
	
	public ArrayList<Center> selectAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Center> cenList = new ArrayList<Center>();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER";
			stmt = conn.prepareStatement(sql);

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String centerName = rs.getString("CNAME");
				String facName = rs.getString("FNAME");
				String postCode = rs.getString("POSTCODE");
				String district = rs.getString("DISTRICT");
				String address = rs.getString("ADDRESS");
				String contact = rs.getString("CONTACT");
				
				Center dto = new Center(centerName, facName, postCode, district, address, contact);
				cenList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return cenList;
	}
	
	public ArrayList<Center> selectByDistrict(String district) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Center> cenList = new ArrayList<Center>();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE DISTRICT = ?";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, district);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String centerName = rs.getString("CNAME");
				String facName = rs.getString("FNAME");
				String postCode = rs.getString("POSTCODE");
				String address = rs.getString("ADDRESS");
				String contact = rs.getString("CONTACT");
				
				Center dto = new Center(centerName, facName, postCode, district, address, contact);
				cenList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectByDistrict");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return cenList;
	}
	
	public ArrayList<Center> selectByKeyword(String keyword) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Center> cenList = new ArrayList<Center>();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE CNAME LIKE ? OR FNAME LIKE ? OR ADDRESS LIKE ?;";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			stmt.setString(3, "%" + keyword + "%");
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String centerName = rs.getString("CNAME");
				String facName = rs.getString("FNAME");
				String postCode = rs.getString("POSTCODE");
				String district = rs.getString("DISTRICT");
				String address = rs.getString("ADDRESS");
				String contact = rs.getString("CONTACT");
				
				Center dto = new Center(centerName, facName, postCode, district, address, contact);
				cenList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("[오류] selectByKeyword");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return cenList;
	}
	
	public boolean deleteOne(String centerName, String facName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "DELETE FROM CENTER WHERE CNAME = ? AND FNAME = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, centerName);
			stmt.setString(2, facName);

			int rows = stmt.executeUpdate();
			
			// 5.
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] deletOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	public boolean deleteAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "DELETE FROM CENTER";
			stmt = conn.prepareStatement(sql);

			int rows = stmt.executeUpdate();
			
			// 5.
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] deleteAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
		
}
