/**
 * 
 */
package model.dto;

/**
 * <pre>
 * 등록 사용자 도메인 클래스
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @since jdk1.8
 */
public class Member {

	/** 이름, 필수 */
	private String name;

	/** 주민번호, 필수, 식별키 */
	private String regiNum;
	
	/** 연락처, 선택 */
	private String contact;
	
	/** 지역, 필수 */
	private String district;
	
	/** 백신 종류, 필수 */
	private String vacType;
	
	/** 1차 접종일, 필수 */
	private String dateFirst;
	
	/** 2차 접종 예정일 */
	private String dateSecond;
	
	/** 2차 접종 알림일 */
	private String notiDate;

	/** 기본 생성자	 */
	public Member() {}

	/**
	 * <pre>
	 * 필수 정보 입력 생성자
	 * </pre>
	 * @param name 이름
	 * @param regiNum 주민번호
	 * @param contact 연락처
	 * @param district 지역
	 * @param vacType 접종 백신 종료
	 * @param dateFirst 1차 접종일
	 */
	public Member(String name, String regiNum, String contact, String district, String vacType, String dateFirst) {
		this.name = name;
		this.regiNum = regiNum;
		this.contact = contact;
		this.district = district;
		this.vacType = vacType;
		this.dateFirst = dateFirst;
		
		// TODO : dateSecond, notiDate 계산하는 메서드 구현해서 추가하기
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the regiNum
	 */
	public String getRegiNum() {
		return regiNum;
	}

	/**
	 * @param regiNum the regiNum to set
	 */
	public void setRegiNum(String regiNum) {
		this.regiNum = regiNum;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the vacType
	 */
	public String getVacType() {
		return vacType;
	}

	/**
	 * @param vacType the vacType to set
	 */
	public void setVacType(String vacType) {
		this.vacType = vacType;
	}

	/**
	 * @return the dateFirst
	 */
	public String getDateFirst() {
		return dateFirst;
	}

	/**
	 * @param dateFirst the dateFirst to set
	 */
	public void setDateFirst(String dateFirst) {
		this.dateFirst = dateFirst;
	}

	/**
	 * @return the dateSecond
	 */
	public String getDateSecond() {
		return dateSecond;
	}

	/**
	 * @param dateSecond the dateSecond to set
	 */
	public void setDateSecond(String dateSecond) {
		this.dateSecond = dateSecond;
	}

	/**
	 * @return the notiDate
	 */
	public String getNotiDate() {
		return notiDate;
	}

	/**
	 * @param notiDate the notiDate to set
	 */
	public void setNotiDate(String notiDate) {
		this.notiDate = notiDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((regiNum == null) ? 0 : regiNum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (regiNum == null) {
			if (other.regiNum != null)
				return false;
		} else if (!regiNum.equals(other.regiNum))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Member [name=");
		builder.append(name);
		builder.append(", regiNum=");
		builder.append(regiNum);
		builder.append(", contact=");
		builder.append(contact);
		builder.append(", district=");
		builder.append(district);
		builder.append(", vacType=");
		builder.append(vacType);
		builder.append(", dateFirst=");
		builder.append(dateFirst);
		builder.append(", dateSecond=");
		builder.append(dateSecond);
		builder.append(", notiDate=");
		builder.append(notiDate);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
}
