
package model.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.dao.CenterDao;
import model.dto.Center;
import util.UI;
import util.Utility;

/**
 * <pre>
 * 센터 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @Since jdk1.8
 */
public class CenterService {
	
	/** CenterDao 객체 */
	private CenterDao dao = CenterDao.getInstance();
	
	/** 센터들을 저장/관리하기 위한 자료 저장구조 */
	//private ArrayList<Center> cenList = new ArrayList<Center>();
	
	/** 중복을 제거한 지역을 저장하기 위한 자료 저장구조 */
	private ArrayList<String> districts = new ArrayList<String>();
	
	
	/** 기본생성자 */
	public CenterService() {
		setDistricts();
	}
	
	/**
	 * <pre>
	 * districts 리스트 생성
	 * </pre>
	 */
	public void setDistricts() {
		districts = dao.selectDistrict();
	}
	
	/**
	 * <pre>
	 * districts 리스트 반환
	 * <pre>
	 * @return ArrayList districts
	 */
	public ArrayList<String> getDistricts(){
		return districts;
	}
	
	/** 
	 * <pre>
	 * 현재 등록 센터수 조회
	 * </pre>
	 * @return 현재 등록 센터수
	 */
	public int getCount() {
		return dao.selectCount();
	}
	
	/**
	 * <pre>
	 * 센터 중복 체크
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @return 이미 등록된 센터가 있으면 true, 없으면 false
	 */
	public boolean exist(String centerName, String facName) {
		return dao.selectCenterInfo(centerName, facName);
	}
	
	/**
	 * <pre>
	 * 센터 중복 체크
	 * </pre>
	 * @param dto 중복 검사할 센터 객체
	 * @return 이미 등록된 센터가 있으면 true, 없으면 false
	 */
	public boolean exist(Center dto) {
		return exist(dto.getCenterName(), dto.getFacName());
	}
	
	/**
	 * <pre>
	 * 센터 등록 메서드
	 * </pre>
	 * @param dto 등록 센터
	 * @return 성공시 true, 실패시 false
	 */
	public boolean addCenter(Center dto) {
		if(!exist(dto)) {
			return dao.insertCenterInfo(dto);
		}
		else {
			System.out.println("[오류] " + dto.getCenterName() + "는 이미 등록된 센터입니다");
			return false;
		}
	}

	/**
	 * <pre>
	 * 센터 등록 메서드
	 * -- 관리자 입력
	 * </pre>
	 * @return 성공시 true, 실패시 false
	 */
	public boolean addCenter() {
		Scanner sc = new Scanner(System.in);
		String centerName, facName, postCode, district, address, contact;
		int num;
		
		System.out.print("센터명 : "); centerName = sc.next();
		System.out.print("\n시설명 : "); facName = sc.next();
		System.out.print("\n우편번호 : "); postCode = sc.next();
		
		System.out.print("[ ");
		for(int i = 0; i < districts.size(); i++) {
			System.out.print((i+1) + ". " + districts.get(i) + "\t");
		}
		System.out.println("]");
		System.out.print("지역 번호를 입력하세요 : "); num = sc.nextInt();
		district = districts.get(num - 1);
		
		System.out.print("\n주소 : "); address = sc.next();
		System.out.print("\n연락처 : "); contact = sc.next();
		
		Center dto = new Center(centerName, facName, postCode, district, address, contact);
		
		return dao.insertCenterInfo(dto);
	}
	
	/**
	 * <pre>
	 * 센터 삭제 메서드
	 * -- 관리자 입력
	 * </pre>
	 */
	public void delCenter() {
		Utility util = new Utility();
		Scanner sc = new Scanner(System.in);
		String centerName, facName, yn;
		
		System.out.print("센터명 : "); centerName = sc.next();
		System.out.print("\n시설명 : "); facName = sc.next();
		
		if(exist(centerName, facName)) {
			System.out.println(dao.selectOne(centerName, facName));
			if(util.getAnswer("위 정보를 삭제하시겠습니까?")) {
				if(dao.deleteOne(centerName, facName)) {
					System.out.println("정상적으로 삭제되었습니다.");
				}
			} else {
				System.out.println("관리자 메뉴로 되돌아갑니다.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 전체 센터 삭제
	 * -- 관리자 메뉴
	 * </pre>
	 */
	public void delAllCenter() {
		Utility util = new Utility();
		
		if(util.getAnswer("전체 센터 정보를 삭제하시겠습니까?")) {
			if(dao.deleteAll())
				System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("관리자 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 센터 정보 수정
	 * -- 관리자 메뉴
	 * </pre>
	 */
	public void reviseCenter() {
		Utility util = new Utility();
		Scanner sc = new Scanner(System.in);
		String centerName, facName, yn;
		
		System.out.print("센터명 : "); centerName = sc.next();
		System.out.print("\n시설명 : "); facName = sc.next();
		
		if(!exist(centerName, facName)) {
			System.out.println("[오류] 등록되지 않은 센터입니다. 입력 정보를 확인해주세요.");
			return;
		}
		
		Center dto = dao.selectOne(centerName, facName);
		System.out.println(dto);
		
		if(util.getAnswer("위 센터의 정보를 수정하시겠습니까?")) {
			dao.deleteOne(centerName, facName);
			if(addCenter()) {
				System.out.println("\n정상적으로 수정되었습니다.");
			} else {
				System.out.println("\n[오류] 이미 등록된 센터입니다.");
			}
		} else {
			System.out.println("관리자 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 전체 센터 조회
	 * </pre>
	 */
	public void printAllCenter() {
		ArrayList<Center> cenList = dao.selectAll();
		System.out.println("등록 센터 수 : " + cenList.size());
		for(int i = 0; i < cenList.size(); i++) {
			System.out.println("[" + (i+1) + "] " + cenList.get(i));
		}
	}
	
	/**
	 * <pre>
	 * 지역별 센터 조회
	 * </pre>
	 */
	public void printCenterByDistrict() {
		Scanner sc = new Scanner(System.in);
		ArrayList<Center> cenList = new ArrayList<Center>();
		int num;
		String district;
		
		System.out.println("-----------------------");
		for(int i = 0; i < districts.size(); i++) {
			System.out.print((i+1) + ". " + districts.get(i) + "\n");
		}
		System.out.println("0. 종료");
		System.out.println("-----------------------");
		
		boolean close = false;
		while(!close) {
			System.out.print("조회하실 지역 번호를 입력하세요 : "); num = sc.nextInt();
			if(num > 0 && num <= districts.size()) {
				district = districts.get(num-1);
				cenList = dao.selectByDistrict(district);
				
				System.out.println("조회 결과 : ");
				
				int idx = 1;
				for(int i = 0; i < cenList.size(); i++) {
					System.out.println("[" + idx++ + "] " + cenList.get(i));
				}
				close = true;
			} else if (num == 0) {
				System.out.println("이전 메뉴로 되돌아갑니다.");
				close = true;
			} else {
				System.out.println("[오류] 잘못된 번호를 입력했습니다.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 지역별 센터 조회
	 * </pre>
	 * @param district 조회할 지역명
	 */
	public void printCenterByDistrict(String district) {
		ArrayList<Center> cenList = dao.selectByDistrict(district);
		int idx = 1;
		
		for(int i = 0; i < cenList.size(); i++) {
			System.out.println("[" + idx++ + "] " + cenList.get(i));
		}
	}
	
	/**
	 * <pre>
	 * 키워드로 센터 조회
	 * </pre>
	 */
	public void printCenterByKeywords() {
		Scanner sc = new Scanner(System.in);
		ArrayList<Center> cenList = new ArrayList<Center>();
		String keyword;
		int cnt = 1;
		
		System.out.print("조회하실 키워드를 입력하세요 : "); keyword = sc.next();
		cenList = dao.selectByKeyword(keyword);
		
		for(int i = 0; i < cenList.size(); i++) {
			System.out.println("[" + cnt++ + "] " + cenList.get(i));
		}
		
		if(cnt == 1) {
			System.out.println("입력하신 키워드를 포함하는 센터 정보가 존재하지 않습니다.");
		}
	}
}





