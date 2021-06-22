
package model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import model.dao.MemberDao;
import model.dto.Member;
import util.Utility;

/**
 * <pre>
 * 등록 사용자 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */
public class MemberService {
	
	/** MemberDao 객체 */
	private MemberDao dao = MemberDao.getInstance();
	
	Scanner sc = new Scanner(System.in);
	Utility util = new Utility();
	
	/** 
	 * <pre>
	 * 기본생성자
	 * <pre>
	 * @throws ParseException 
	 */
	public MemberService() {}
	
	/** 
	 * <pre>
	 * 현재 등록자수 조회
	 * </pre>
	 * @return 현재 등록자수
	 */
	public int getCount() {
		return dao.selectCount();
	}
	
	/**
	 * <pre>
	 * 회원 중복 체크
	 * </pre>
	 * @param regiNum 주민등록번호
	 * @return 존재시에 true, 존재하지 않으면 false
	 */
	public boolean exist(String regiNum) {
		return dao.selectRegiNum(regiNum);
	}
	
	/**
	 * <pre>
	 * 전체 회원 출력 메서드
	 * </pre>
	 */
	public void printAllMember() {
		ArrayList<Member> memList = dao.selectAll();
		System.out.println("전체 회원 수 : " + memList.size());
		for(int i = 0; i < memList.size(); i++) {
			System.out.println("[" + (i+1) + "] " + memList.get(i));
		}
	}
	
	/**
	 * <pre>
	 * 1차 접종받은 백신 종류에 따라 다음 접종까지의 대기 기간을 알려주는 메서드
	 * </pre>
	 * @param vacType 접종받은 백신 종류
	 * @return 다음 접종까지의 대기 기간
	 */
	public int findPeriod(String vacType) {
		if(vacType.equals("화이자")) return 21;
		else if(vacType.equals("모더나")) return 28;
		else if(vacType.equals("AZ")) return 84;
		else return 0;
	}
	
	/**
	 * <pre>
	 * 접종 백신 이름을 입력받아 유효한 이름인지 검사
	 * </pre>
	 * @return 성공시 백신 이름, 실패시 null
	 */
	public String inputVacType() {
		String tmp;
		boolean close = false;

		while(!close) {
			System.out.print("접종 백신 종류 (AZ/화이자/모더나): "); tmp = sc.next(); 
			if(tmp.equals("AZ") || tmp.equals("화이자") || tmp.equals("모더나")) {
				return tmp;
			} else if(tmp.equals("얀센")) {
				System.out.println("[알림] 얀센은 2차접종이 필요하지 않으므로 등록하지 않으셔도 됩니다.");
				if(!util.getAnswer("다른 백신으로 등록하시겠습니까?")) {
					return null;
				}
			} else {
				System.out.println("[오류] 입력하신 백신 이름이 올바르지 않습니다.");
			}
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * </pre>
	 * @param dto 등록 회원
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException 
	 */
	public boolean addMember(Member dto) throws ParseException {
		
		if(!exist(dto.getRegiNum())) {
			int period = findPeriod(dto.getVacType());
			if(period > 0) {
				Utility util = new Utility();
				String date = util.addDate(dto.getDateFirst(), period);
				dto.setDateSecond(date);
				dto.setNotiDate(util.addDate(date, -3));
				dao.insertMemberInfo(dto);
				return true;
			} else {
				System.out.println("[오류] 입력한 백신 이름이 올바르지 않습니다");
				return false;
			}
		}
		else {
			System.out.println("[오류] " + dto.getName() + "님은 이미 등록되었습니다.");
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 회원 둥록 메서드
	 * -- 사용자 입력
	 * </pre>
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException 
	 */
	public boolean addMember() throws ParseException {
		Utility util = new Utility();
		String name, vacType, dateFirst = null;
		
		System.out.print("이름 : "); name = sc.next();
		vacType = inputVacType();
		if(vacType == null) {
			return false;
		}
		dateFirst = util.inputDate("1차 접종일 (형식 : 20210605)");
		
		return addMember(name, vacType, dateFirst);
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * -- 다른 메서드로부터 호출됨
	 * </pre>
	 * @param name 아룸
	 * @param vacType 접종 백신 이름
	 * @param dateFirst 1차 접종일
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException
	 */
	public boolean addMember(String name, String vacType, String dateFirst) throws ParseException {
		ArrayList<String> districts = new CenterService().getDistricts();
		int num = 0;
		Member dto = new Member();
		
		dto.setName(name);
		dto.setVacType(vacType);
		dto.setDateFirst(dateFirst);
		
		String regiNum = inputRegiNum();
		if(regiNum != null) dto.setRegiNum(regiNum);
		else return false;
		
		dto.setContact(inputContact());
		System.out.println("----------------------------"); 
		for(int i = 0; i < districts.size(); i++) {
			System.out.print((i+1) + ". " + districts.get(i) + "\t");
			if(districts.get(i).equals("강원도")) System.out.print("\t");
			if(i%3==2) System.out.println();
		}
		System.out.println("\n---------------------------");
		
		while(true) {
			num = util.inputNum("거주지역을 선택하세요");
			if(num > 0 && num <= districts.size()) {
				dto.setDistrict(districts.get(num - 1));
				break;
			} else {
				System.out.println("[오류] 1 ~ " + districts.size() + " 사이의 숫자를 입력하세요");
			}
		}
		
		return addMember(dto);
	}
	
	/**
	 * <pre>
	 * 주민번호 입력 메서드
	 * </pre>
	 * @return 입력받은 주민번호
	 */
	public String inputRegiNum() {
		int flag;
		String tmp;
		
		while(true) {
			flag = 0;
			System.out.print("주민번호 : "); 
			tmp = sc.next();
			
			if(tmp.length() == 14) {
				for(int i = 0; i < tmp.length(); i++) {
					if(i == 6) {
						if(tmp.charAt(i) == '-') {
							continue;
						} else {
							flag = 1;
							break;
						}
					} 
					if(tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
						continue;
					} else {
						flag = 1;
						break;
					}
				}
			} else {
				flag = 1;
			}
			if(flag == 0) {
				if(dao.selectRegiNum(tmp)) {
					System.out.println("[오류] 이미 등록된 주민등록번호입니다.");
					if(!util.getAnswer("계속 등록하시겠습니까?")) return null;
				} 
				else break;
			}
			else{
				System.out.println("[오류] 주민등록번호 입력 형식은 앞6자리-뒷7자리 입니다.");
			}
		}
		return tmp;
	}
	
	/**
	 * 전화번호 입력 메서드
	 * </pre>
	 * @return 입력받은 전화번호
	 */
	public String inputContact() {
		int flag;
		String tmp;
		
		while(true) {
			flag = 0;
			System.out.print("연락처 (형식 : 01012341234) : "); 
			tmp = sc.next();
			
			if(tmp.length() == 11) {
				for(int i = 0; i < tmp.length(); i++) {
					if(tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
						continue;
					} else{
						flag = 1;
						break;
					}
				}
			} else {
				flag = 1;
			}
			if(flag == 0) {
				break;
			} else {
				System.out.println("[오류] 입력 형식을 확인해주세요");
			}
		}
		return tmp;
	}
	
	/**
	 * <pre>
	 * 회원 검증 메서드
	 * </pre>
	 * @param name 이름
	 * @param regiNum 주민번호
	 * @return 성공시 해당 회원 객체, 실패시 null
	 */
	public Member verifyMember(String name, String regiNum) {
		Member dto = dao.selectOne(name, regiNum);
		if(dto != null) {
			System.out.println("회원 정보가 확인되었습니다.");
			return dto;
		}
		System.out.println("[오류] 입력하신 정보로 등록된 회원이 존재하지 않습니다.");
		return null;
	}
	
	/**
	 * <pre>
	 * 사용자 정보 삭제 메서드
	 * -- 관리자용
	 * </pre>
	 */
	public void delMember() {
		Utility util = new Utility();
		String name, regiNum;
		
		System.out.print("이름 : "); name = sc.next();
		System.out.print("주민번호 : "); regiNum = sc.next();
		
		Member dto = null;
		dto = verifyMember(name, regiNum);
		
		if(dto == null) {
			System.out.println("이전 메뉴로 되돌아갑니다.");
			return;
		}
		
		System.out.println(dto);
		
		if(util.getAnswer("위 회원정보를 삭제하겠습니까?")){
			dao.deleteOne(dto);
			System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 사용자 정보 삭제 메서드
	 * -- 사용자용
	 * </pre>
	 * @param dto 삭제할 멤버 객체
	 */
	public void delMember(Member dto) {
		Utility util = new Utility();
		
		if(util.getAnswer("코로나 백신 2차 접종 알림 서비스를 해지하시겠습니까?")) {
			if(dao.deleteOne(dto))
				System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 전체 회원 정보 삭제 메서드
	 * -- 관리자용
	 * </pre>
	 */
	public void delAllMember() {
		Utility util = new Utility();
		
		if(util.getAnswer("전체 회원 정보를 삭제하시겠습니까?")) {
			if(dao.deleteAll())
				System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 사용자 정보 수정 메서드
	 * -- 관리자용
	 * </pre>
	 * @throws ParseException
	 */
	public void reviseMember() throws ParseException {
		String name, regiNum;
		
		System.out.print("이름 : "); name = sc.next();
		System.out.print("주민번호 : "); regiNum = sc.next();
		
		Member dto = null;
		dto = verifyMember(name, regiNum);
		
		if(dto == null) {
			System.out.println("이전 메뉴로 되돌아갑니다.");
			return;
		}
		
		reviseMember(dto);
	}
	
	/**
	 * <pre>
	 * 회원 정보 수정 메서드
	 * -- 사용자용
	 * </pre>
	 * @param dto 수정할 회원 객체
	 * @throws ParseException
	 */
	public void reviseMember(Member dto) throws ParseException {
		Utility util = new Utility();
		ArrayList<String> districts = new CenterService().getDistricts();
		int num;
		
		System.out.println(dto);
		
		if(!util.getAnswer("위 회원정보를 수정하겠습니까?")) {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
		
		System.out.println("-------------------");
		System.out.println("1. 전화번호");
		System.out.println("2. 거주 지역");
		System.out.println("3. 접종 백신 종류");
		System.out.println("4. 1차 접종일");
		System.out.println("0. 돌아가기");
		System.out.println("-------------------");
		num = util.inputNum("수정할 항목을 선택하세요");
		
		while(true) {
			switch(num){
				case 1:
					System.out.print("수정할 전화번호 : ");
					dto.setContact(inputContact());
					if(dao.updateMember(dto))
						System.out.println("수정되었습니다.");
					return;
				case 2:
					System.out.println("-------- "); 
					for(int i = 0; i < districts.size(); i++) {
						System.out.printf("%d. %s\n", i+1, districts.get(i));
					}
					System.out.println("-------");
					
					boolean close = false;
					while(!close) {
						num = util.inputNum("거주지역을 선택하세요");
						if(num > 0 && num <= districts.size()) {
							dto.setDistrict(districts.get(num - 1));
							close = true;
						} else {
							System.out.println("[오류] 1 ~ " + districts.size() + " 사이의 숫자를 입력하세요");
						}
					}
					dto.setDistrict(districts.get(num - 1));
					if(dao.updateMember(dto))
						System.out.println("수정되었습니다.");
					return;
				case 3:
					dto.setVacType(inputVacType());
					if(dao.updateMember(dto))
						System.out.println("수정되었습니다.");
					return;
				case 4:
					dto.setDateFirst(util.inputDate("1차 접종일 (형식 : 20210608)"));
					
					int period = findPeriod(dto.getVacType());
					String date = util.addDate(dto.getDateFirst(), period);
					
					dto.setDateSecond(date);
					dto.setNotiDate(util.addDate(date, -3));
					if(dao.updateMember(dto))
						System.out.println("수정되었습니다.");
					return;
				case 0:
					System.out.println("이전 메뉴로 돌아갑니다.");
					return;
				default:
					System.out.println("0~4 사이의 숫자를 입력하세요");
					
			}
		}
	}
	
	/**
	 * <pre>
	 * 2차 접종 예정일 알림 메서드
	 * </pre>
	 * @param 대상 멤버 객체
	 * @throws ParseException
	 */
	public void notification(Member dto) throws ParseException {
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String second = dto.getDateSecond();
		
		int td = Integer.parseInt(today);
		int sd = Integer.parseInt(second);
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		Date dt = dtFormat.parse(second); 
		String tmp = new SimpleDateFormat("yyyy년 MM월 dd일").format(dt);
		
		if(sd - td > 3) {
			System.out.println("알림 기간이 아닙니다.\n");
			System.out.println(dto.getName() + "님의 2차 접종 예정일은 " + tmp + " 입니다.");
			
		} else if(sd - td <= 3 && sd - td >= 0) {
			System.out.println(dto.getName() + "님의 " + dto.getVacType() + " 백신 2차 접종 예정일은 " + tmp + " 입니다.");
			System.out.println("거주지역 내의 예방접종 센터는 다음과 같습니다 : ");
			CenterService cs = new CenterService();
			cs.printCenterByDistrict(dto.getDistrict());
			System.out.println("본 내용은 휴대폰 " + dto.getContact() + " 으로 전송되었습니다.");
			
		} else {
			System.out.println("2차 접종 예정일이 지났습니다.");
			System.out.println(dto.getName() + "님의 2차 접종 예정일은 " + tmp + " 이었습니다.");
		}
	}
}
