/**
 * 
 */
package model.service;

import java.util.ArrayList;

import model.dto.Member;

/**
 * <pre>
 * 등록 사용자 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @ version ver.1.0
 * @ since jdk1.8
 */
public class MemberService {
	
	/** 회원들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Member> memList = new ArrayList<Member>();
	
	/** 기본생성자 : 초기화 회원 등록 수행 */
	public MemberService() {
		System.out.println("초기 회원 등록작업이 완료되었습니다 : " + initMember());
	}
	
	/** 
	 * <pre>
	 * 현재 등록자수 조회
	 * </pre>
	 * @return 현재 등록자수
	 */
	public int getCount() {
		return memList.size();
	}
	
	/**
	 * <pre>
	 * 회원 중복 체크
	 * </pre>
	 * @param regiNum 주민등록번호
	 * @return 존재시에 저장위치 번호, 존재하지 않으면 -1
	 */
	public int exist(String regiNum) {
		for(int i = 0; i < getCount(); i++) {
			if(regiNum.equals((memList.get(i)).getRegiNum())) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * <pre>
	 * 테스트를 위한 회원 초기화 등록 메서드
	 * </pre>
	 * @return 초기화 회원등록 인원수
	 */
	public int initMember() {
		
		Member dto1 = new Member("김현수", "970620-2000000", "01063886503", "서울특별시", "화이자", "2021-05-31");
		Member dto2 = new Member("김진홍", "980828-1000000", "01089971463", "강원도", "화이자", "2021-06-01");
		Member dto3 = new Member("조춘웅", "420519-1000000", "01052535388", "경기도", "AZ", "2021-06-04");
		Member dto4 = new Member("김태재", "970530-1000000", "01093433384", "경상남도", "모더나", "2021-06-02");
		Member dto5 = new Member("박승현", "971230-2000000", "01091639252", "충청북도", "AZ", "2021-06-03");
		addMember(dto1);
		addMember(dto2);
		addMember(dto3);
		addMember(dto4);
		addMember(dto5);
		
		return getCount();
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * </pre>
	 * @param dto 등록 회원
	 */
	public void addMember(Member dto) {
		if(exist(dto.getRegiNum()) == -1) {
			memList.add(dto);
			// TODO : dateSecond, notiDate 계산하는 메서드 구현해서 추가하기
		}
		else System.out.println("[오류] " + dto.getName() + "님은 이미 등록되었습니다.");
	}
	
	/**
	 * <pre>
	 * 회원 둥록 메서드
	 * -- 사용자 입력
	 * </pre>
	 */
	public void addMember() {
	
		Member dto = new Member();
		
		// TODO : 사용자 정보 입력 구현
		
		addMember(dto);
	}
}
