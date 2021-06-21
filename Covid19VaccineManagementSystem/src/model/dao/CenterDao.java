package model.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
	
	public static void main(String[] args) {
		CenterDao dao = new CenterDao();
		dao.readFile();
	}
	// FactoryDao 객체 멤버변수 선언 및 할당
	private FactoryDao factory = FactoryDao.getInstance();

	private CenterDao() {
		//readFile();
	}
	
	private static CenterDao instance = new CenterDao();
	
	public static CenterDao getInstance() {
		return instance;
	}
	
	/** 센터들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Center> cenList = new ArrayList<Center>();
	
	/** 센터들이 위치한 지역을 임시로 저장하기 위한 자료 저장구조*/
	private ArrayList<String> tempArrayList = new ArrayList<String>();
	
	/** tempArrayList에서 중복을 제거한 지역만을 저장하기 위한 자료 저장구조 */
	private ArrayList<String> districts = new ArrayList<String>();
	
	/** 
	 * <pre>
	 * 현재 등록 센터수 조회
	 * </pre>
	 * @return 현재 등록 센터수
	 */
	public int getCount() {
		return cenList.size();
	}
	
	
	/**
	 * <pre>
	 * 센터 중복 체크
	 * <pre>
	 * @param dto 중복 검사 대상 센터 객체
	 * @return 존재시에 저장위치 번호, 존재하지 않으면 -1
	 */
	public int exist(Center dto) {
		for(int i = 0; i < getCount(); i++) {
			if(cenList.get(i).equals(dto)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * <pre>
	 * csv 파일로부터 자료를 읽어와 저장하기 위한 메서드
	 * </pre>
	 */
	public void readFile() {
		BufferedReader br = null;
		String line;
		String path = "./docs/centerInfo.csv";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			while((line = br.readLine()) != null) {
				String[] temp = line.split(","); 
				
				String contact = null;
				if(temp.length > 5) {
					contact = temp[5];
				}
				
				tempArrayList.add(temp[3]);
				Center dto = new Center(temp[0], temp[1], temp[2], temp[3], temp[4], contact);
				if(exist(dto) == -1) insertCenterInfo(dto);			
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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
		
}
