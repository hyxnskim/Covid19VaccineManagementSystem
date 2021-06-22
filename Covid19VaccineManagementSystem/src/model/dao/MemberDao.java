package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dto.Member;

/**
 * <pre>
 * MemberDao class
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */

public class MemberDao {
	private FactoryDao factory = FactoryDao.getInstance();

	private MemberDao() {}
	
	private static MemberDao instance = new MemberDao();
	
	public static MemberDao getInstance() {
		return instance;
	}
	
	/**
	 * <pre>
	 * 멤버 정보 삽입 메서드
	 * </pre>
	 * @param dto 삽입할 맴버 객체
	 * @return 성공시 true, 실패시 false
	 */
	public boolean insertMemberInfo(Member dto) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = factory.getConnection();
			
			String sql = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, dto.getRegiNum());
			stmt.setString(2, dto.getName());
			stmt.setString(3, dto.getContact());
			stmt.setString(4, dto.getDistrict());
			stmt.setString(5, dto.getVacType());
			stmt.setString(6, dto.getDateFirst());
			stmt.setString(7, dto.getDateSecond());
			stmt.setString(8, dto.getNotiDate());

			int rows = stmt.executeUpdate();
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] 멤버 정보 추가");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 등록 회원 수 조회 메서드
	 * </pre>
	 * @return 등록 회원수
	 */
	public int selectCount() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT COUNT(*) FROM MEMBER";
			stmt = conn.prepareStatement(sql);

			rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("COUNT(*)");
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - selectCount");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return 0;
	}
	
	/**
	 * <pre>
	 * 주민번호 중복 검사 메서드
	 * </pre>
	 * @param regiNum 중복 검사할 주민번호
	 * @return 이미 존재하는 주민번호면 true, 그렇지 않으면 false
	 */
	public boolean selectRegiNum(String regiNum) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT COUNT(*) FROM MEMBER WHERE REGINUM = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, regiNum);

			rs = stmt.executeQuery();
			if(rs.next()) {
				int cnt = rs.getInt("COUNT(*)");
				if(cnt>0) return true;
				else return false;
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - selectCount");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 전체 회원정보 조회 메서드
	 * </pre>
	 * @return 전체 회원 객체가 저장된 ArrayList
	 */
	public ArrayList<Member> selectAll(){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Member> memList = new ArrayList<Member>();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM MEMBER";
			stmt = conn.prepareStatement(sql);

			rs = stmt.executeQuery();
			while(rs.next()) {
				String regiNum = rs.getString("REGINUM");
				String name = rs.getString("MNAME");
				String contact = rs.getString("CONTACT");
				String district = rs.getString("DISTRICT");
				String vacType = rs.getString("VACTYPE");
				String dateFirst = rs.getString("DATE_FIRST");
				String dateSecond = rs.getString("DATE_SECOND");
				String notiDate = rs.getString("NOTIDATE");
				
				Member dto = new Member(name, regiNum, contact, district, vacType, dateFirst, dateSecond, notiDate);
				memList.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - selectAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return memList;
	}
	
	/**
	 * <pre>
	 * 특정 회원 정보 조회 메서드
	 * </pre>
	 * @param name 이름
	 * @param regiNum 주민번호
	 * @return 조회한 회원 객체
	 */
	public Member selectOne(String name, String regiNum) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Member dto = new Member();
		
		try {
			conn = factory.getConnection();
			
			String sql = "SELECT * FROM MEMBER WHERE MNAME = ? AND REGINUM = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, name);
			stmt.setString(2, regiNum);

			rs = stmt.executeQuery();
			if(rs.next()) {
				dto.setRegiNum(rs.getString("REGINUM"));
				dto.setName(rs.getString("MNAME"));
				dto.setContact(rs.getString("CONTACT"));
				dto.setDistrict(rs.getString("DISTRICT"));
				dto.setVacType(rs.getString("VACTYPE"));
				dto.setDateFirst(rs.getString("DATE_FIRST"));
				dto.setDateSecond(rs.getString("DATE_SECOND"));
				dto.setNotiDate(rs.getString("NOTIDATE"));
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - selectOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt, rs);
		}
		return dto;
	}
	
	/**
	 * <pre>
	 * 회원 정보 변경
	 * </pre>
	 * @param dto 변경할 회원 객체
	 * @return 성공시 true, 실패시 false
	 */
	public boolean updateMember(Member dto) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = factory.getConnection();
			
			String sql = "UPDATE MEMBER SET CONTACT = ?, DISTRICT = ?, VACTYPE = ?, DATE_FIRST = ?, DATE_SECOND = ?, NOTIDATE = ? WHERE MNAME = ? AND REGINUM = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, dto.getContact());
			stmt.setString(2, dto.getDistrict());
			stmt.setString(3, dto.getVacType());
			stmt.setString(4, dto.getDateFirst());
			stmt.setString(5, dto.getDateSecond());
			stmt.setString(6, dto.getNotiDate());
			stmt.setString(7, dto.getName());
			stmt.setString(8, dto.getRegiNum());
			
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] 회원 정보 수정");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 특정 회원 정보 삭제
	 * </pre>
	 * @param dto 삭제할 회원 객체
	 * @return 성공시 true, 실패시 false
	 */
	public boolean deleteOne(Member dto) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "DELETE FROM MEMBER WHERE MNAME = ? AND REGINUM = ?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, dto.getName());
			stmt.setString(2, dto.getRegiNum());

			int rows = stmt.executeUpdate();
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - deleteOne");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
	
	/**
	 * <pre>
	 * 전체 회원정보 삭제
	 * </pre>
	 * @return 성공시 true, 실패시 false
	 */
	public boolean deleteAll() {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "DELETE FROM MEMBER";
			stmt = conn.prepareStatement(sql);

			int rows = stmt.executeUpdate();
			if(rows > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("[오류] Member - deleteAll");
			e.printStackTrace();
		} finally {
			factory.close(conn, stmt);
		}
		return false;
	}
}
