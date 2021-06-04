/**
 * 
 */
package model.service;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.dto.Center;
import util.UI;

/**
 * <pre>
 * 센터 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @Since jdk1.8
 */
public class CenterService {
	
	/** 센터들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Center> cenList = new ArrayList<Center>();
	
	/** 센터들이 위치한 지역을 임시로 저장하기 위한 자료 저장구조*/
	private ArrayList<String> tempArrayList = new ArrayList<String>();
	
	/** tempArrayList에서 중복을 제거한 지역만을 저장하기 위한 자료 저장구조 */
	private ArrayList<String> districts = new ArrayList<String>();
	
	/** 기본생성자 : 초기 센터 등록 수행 */
	public CenterService() {
		System.out.println("초기 센터 등록작업이 완료되었습니다");
		readFile();
	}
	
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
	
	public int exist(String centerName, String postCode) {
		Center tmp = new Center();
		for(int i = 0; i < getCount(); i++) {
			tmp = cenList.get(i);
			if(tmp.getCenterName().equals(centerName) && tmp.getPostCode().equals(postCode)) {
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
				////////////////////////////////////////////////////////////////////////////////
				String contact = null;
				if(temp.length > 5) {
					contact = temp[5];
				}
				Center dto = new Center(temp[0], temp[1], temp[2], temp[3], temp[4], contact);
				if(exist(dto) == -1) cenList.add(dto);				
				//////////////////////////////////////////////////////////////////////////////////
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		districts = (ArrayList)tempArrayList.parallelStream().distinct().collect(Collectors.toList());
	}
	
	/**
	 * <pre>
	 * 센터 등록 메서드
	 * </pre>
	 * @param dto 등록 센터
	 */
	public void addCenter(Center dto) {
		if(exist(dto) == -1) cenList.add(dto);
		else System.out.println("[오류] " + dto.getCenterName() + "는 이미 등록된 센터입니다");
	}

	/**
	 * <pre>
	 * 센터 등록 메서드
	 * -- 관리자 입력
	 * </pre>
	 */
	public void addCenter() {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		String centerName, facName, postCode, district, address, contact;
		int num;
		
		ui.printSubMenu("센터 등록");
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
		if(exist(dto) == -1 )addCenter(dto);
		else System.out.println("\n이미 등록된 센터입니다.");
	}
	
	/**
	 * <pre>
	 * 센터 삭제 메서드
	 * -- 관리자 입력
	 * </pre>
	 */
	public void delCenter() {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		String centerName, postCode, yn;
		
		ui.printSubMenu("센터 삭제");
		System.out.print("센터명 : "); centerName = sc.next();
		System.out.print("\n우편번호 : "); postCode = sc.next();
		
		int idx = exist(centerName, postCode);
		if(idx >= 0) {
			System.out.println(cenList.get(idx));
			boolean close = false;
			while(!close) {
				System.out.print("위 정보를 삭제하시겠습니까?(Y/N) :"); yn = sc.next();
				if(yn.equals("Y")) {
					cenList.remove(idx);
					System.out.println("정상적으로 삭제되었습니다.");
					close = true;
				} else if(yn.equals("N")) {
					System.out.println("관리자 메뉴로 되돌아갑니다.");
					close = true;
				} else {
					System.out.println("잘못 입력했습니다. Y 또는 N을 입력해주세요");
				}
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
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		String yn;
		
		ui.printSubMenu("센터 전체 삭제");

		boolean close = false;
		while(!close) {
			System.out.print("전체 센터 정보를 삭제하시겠습니까?(Y/N) :"); yn = sc.next();
			if(yn.equals("Y")) {
				cenList.clear();
				System.out.println("정상적으로 삭제되었습니다.");
				close = true;
			} else if(yn.equals("N")) {
				System.out.println("관리자 메뉴로 되돌아갑니다.");
				close = true;
			} else {
				System.out.println("잘못 입력했습니다. Y 또는 N을 입력해주세요");
			}
		}
	}
	
	/**
	 * <pre>
	 * 센터 정보 수정
	 * -- 관리자 메뉴
	 * </pre>
	 */
	public void reviseCenter() {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		String centerName, postCode, yn;
		int num;
		boolean close = false;
		
		ui.printSubMenu("센터 수정");
		System.out.print("센터명 : "); centerName = sc.next();
		System.out.print("\n우편번호 : "); postCode = sc.next();
		
		int idx = exist(centerName, postCode);
		if(idx == -1) {
			System.out.println("[오류] 등록되지 않은 센터입니다. 입력 정보를 확인해주세요.");
			return;
		}
		
		Center dto = new Center();
		System.out.println(dto);
		
		while(!close) {
			System.out.println("위 센터의 정보를 수정하시겠습니까?(Y/N) : "); yn = sc.next();
			if(yn.equals("Y")) {
				System.out.print("센터명 : "); dto.setCenterName(sc.next());
				System.out.print("\n시설명 : "); dto.setFacName(sc.next());
				System.out.print("\n우편번호 : "); dto.setPostCode(sc.next());
				
				System.out.print("[ ");
				for(int i = 0; i < districts.size(); i++) {
					System.out.print((i+1) + ". " + districts.get(i) + "\t");
				}
				System.out.println("]");
				System.out.print("지역 번호를 입력하세요 : "); num = sc.nextInt();
				dto.setDistrict(districts.get(num - 1));
				
				System.out.print("\n주소 : "); dto.setAddress(sc.next());
				System.out.print("\n연락처 : "); dto.setContact(sc.next());
				
				if(exist(dto) == -1 ) {
					addCenter(dto);
					System.out.println("\n정상적으로 수정되었습니다.");
				} else {
					System.out.println("\n[오류] 이미 등록된 센터입니다.");
					close = true;
				}
			} else if(yn.equals("N")) {
				System.out.println("관리자 메뉴로 되돌아갑니다.");
				close = true;
			} else {
				System.out.println("잘못 입력했습니다. Y 또는 N을 입력해주세요");
			}
		}
	}
	
	/**
	 * <pre>
	 * 전체 센터 조회
	 * </pre>
	 */
	public void printAllCenter() {
		UI ui = new UI();
		ui.printSubMenu("전체 센터 조회");
		
		for(int i = 0; i < cenList.size(); i++) {
			System.out.println(cenList.get(i));
		}
	}
	
	/**
	 * <pre>
	 * 지역별 센터 조회
	 * </pre>
	 */
	public void printCenterByDistrict() {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		int num;
		String district;
		
		ui.printSubMenu("지역별 센터 조회");
		
		System.out.print("[ ");
		for(int i = 0; i < districts.size(); i++) {
			System.out.print((i+1) + ". " + districts.get(i) + "\t");
		}
		System.out.println("0. 종료 ]");
		
		boolean close = false;
		while(!close) {
			System.out.print("조회하실 지역 번호를 입력하세요 : "); num = sc.nextInt();
			if(num > 0 && num < districts.size()) {
				district = districts.get(num-1);
				Center dto = new Center();
				for(int i = 0; i < cenList.size(); i++) {
					dto = cenList.get(i);
					if(dto.getDistrict().equals(district)) {
						System.out.println(dto);
					}
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
	 * 키워드로 센터 조회
	 * </pre>
	 */
	public void printCenterByKeywords() {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		String keyword;
		int cnt = 0;
		
		ui.printSubMenu("키워드로 센터 조회");
		System.out.print("조회하실 키워드를 입력하세요 : "); keyword = sc.next();
		
		Center dto = new Center();
		for(int i = 0; i < cenList.size(); i++) {
			dto = cenList.get(i);
			if(dto.getCenterName().contains(keyword) || dto.getFacName().contains(keyword) || dto.getAddress().contains(keyword)) {
				System.out.println(dto);
				cnt++;
			}
		}
		if(cnt == 0) {
			System.out.println("입력하신 키워드를 포함하는 센터 정보가 존재하지 않습니다.");
		}
	}
	
}





