package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <pre>
 * 모든 DAO 클래스에사 사용하기 위한 기능 분리 설계
 * -- Connection 반환
 * -- close() 자원해제
 * -- Singleton pattern 적용
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */


public class FactoryDao {
	
	// jdbc resource property
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "vaccine";
	private String password = "tiger";
	
	private FactoryDao(){
		try {
			Class.forName(driver);
			System.out.println("[성공] 드라이버 로딩");
		} catch (ClassNotFoundException e) {
			System.out.println("[오류] 드라이버 로딩 오류");
			e.printStackTrace();
		}
	}
	
	private static FactoryDao instance = new FactoryDao();
	public static FactoryDao getInstance() {
		return instance;
	}
	
	/**
	 * <pre>
	 * DB 연결 Connection 반환 메서드
	 * </pre>
	 * @return Connection
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * <pre>
	 * 자원 해제 : SELECT 수행에 대한 자원
	 * </pre>
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(stmt != null) stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null)
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <pre>
	 * 자원해제 : CUD 수행에 대한 자원
	 * </pre>
	 * @param conn
	 * @param stmt
	 */
	public void close(Connection conn, Statement stmt) {
		close(conn, stmt, null);
	}
}
