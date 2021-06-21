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
	
	private FactoryDao factory = FactoryDao.getInstance();

	private CenterDao() {}
	
	private static CenterDao instance = new CenterDao();
	
	public static CenterDao getInstance() {
		return instance;
	}
	
	/**
	 * <pre>
	 * 등록된 센터 개수 조회
	 * </pre>
	 * @return 등록된 센터 개수
	 */
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
			System.out.println("[오류] Center - selectCount");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return 0;
	}
	
	/**
	 * <pre>
	 * 센터 정보 삽입
	 * </pre>
	 * @param dto 삽입할 센터 객체
	 * @return 성공시 true, 실패시 false
	 */
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
	
	/**
	 * <pre>
	 * 지역 조회
	 * <pre>
	 * @return 지역 정보가 저장된 ArrayList
	 */
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
	
	/**
	 * <pre>
	 * 센터 정보 조회(중복체크)
	 * </pre>
	 * @param dto 대상 센터 객체
	 * @return 중복된 센터가 존재하면 true, 그렇지 않으면 false
	 */
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
	
	/**
	 * <pre>
	 * 센터 정보 조회(중복체크)
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @return 중복된 센터가 존재하면 true, 그렇지 않으면 false
	 */
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
	
	/**
	 * <pre>
	 * 특정 센터 정보 조회
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @return 조회한 센터 객체
	 */
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
			return dto;
		} catch (SQLException e) {
			System.out.println("[오류] Center - selectOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 전체 센터 조회
	 * </pre>
	 * @return 전체 센터 객체가 저장된 ArrayList
	 */
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
			while(rs.next()) {
				String centerName = rs.getString("CNAME");
				String facName = rs.getString("FNAME");
				String postCode = rs.getString("POSTCODE");
				String district = rs.getString("DISTRICT");
				String address = rs.getString("ADDRESS");
				String contact = rs.getString("CONTACT");
				
				Center dto = new Center(centerName, facName, postCode, district, address, contact);
				cenList.add(dto);
			}
			return cenList;
		} catch (SQLException e) {
			System.out.println("[오류] Center - selectAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 지역별 센터 조회
	 * </pre>
	 * @param district 지역
	 * @return 조회결과
	 */
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
			while(rs.next()) {
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
	
	/**
	 * <pre>
	 * 키워드로 센터 조회
	 * </pre>
	 * @param keyword 검색 키워드
	 * @return 조회 결과
	 */
	public ArrayList<Center> selectByKeyword(String keyword) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Center> cenList = new ArrayList<Center>();

		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM CENTER WHERE CNAME LIKE ? OR FNAME LIKE ? OR ADDRESS LIKE ?";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			stmt.setString(3, "%" + keyword + "%");
			
			rs = stmt.executeQuery();
			while(rs.next()) {
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
	
	/**
	 * <pre>
	 * 특정 센터 정보 삭제
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @return 성공시 true, 실패시 false
	 */
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
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] Center - deleteOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 전체 센터 정보 삭제
	 * </pre>
	 * @return 성공시 true, 실패시 false
	 */
	public boolean deleteAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "DELETE FROM CENTER";
			stmt = conn.prepareStatement(sql);

			int rows = stmt.executeUpdate();
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] Center - deleteAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
		
}
