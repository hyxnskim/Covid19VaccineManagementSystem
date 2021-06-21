package model.dto;

/**
 * <pre>
 * 예방접종 센터 도메인 클래스
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */

public class Center {

	/** 센터명, 필수 */
	private String centerName;
	
	/** 시설명, 필수 */
	private String facName;
	
	/** 우편번호, 필수 */
	private String postCode;
	
	/** 지역, 필수 */
	private String district;
	
	/** 주소, 필수 */
	private String address;
	
	/** 연락처, 선택 */
	private String contact;

	
	/** 기본 생성자	 */
	public Center() {}

	/**
	 * <pre>
	 * 필수 입력정보 생성자
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @param postCode 우편번호
	 * @param district 지역
	 * @param address 주소
	 */
	public Center(String centerName, String facName, String postCode, String district, String address) {
		this.centerName = centerName;
		this.facName = facName;
		this.postCode = postCode;
		this.district = district;
		this.address = address;
	}

	/**
	 * <pre>
	 * 필수 입력정보 생성자
	 * </pre>
	 * @param centerName 센터명
	 * @param facName 시설명
	 * @param postCode 우편번호
	 * @param district 지역
	 * @param address 주소
	 * @param contact 연락처
	 */
	public Center(String centerName, String facName, String postCode, String district, String address, String contact) {
		this(centerName, facName, postCode, district, address);
		this.contact = contact;
	}

	/**
	 * @return the centerName
	 */
	public String getCenterName() {
		return centerName;
	}

	/**
	 * @param centerName the centerName to set
	 */
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	/**
	 * @return the facName
	 */
	public String getFacName() {
		return facName;
	}

	/**
	 * @param facName the facName to set
	 */
	public void setFacName(String facName) {
		this.facName = facName;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centerName == null) ? 0 : centerName.hashCode());
		result = prime * result + ((postCode == null) ? 0 : postCode.hashCode());
		return result;
	}

	/**
	 * <pre>
	 * equal 메서드 재정의
	 * -- 센터명과 우편번호가 동시에 일치하면 동일한 객체로 처리
	 * @param obj 검사 대상 객체
	 * </pre>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Center other = (Center) obj;
		if (centerName == null) {
			if (other.centerName != null)
				return false;
		} else if (!centerName.equals(other.centerName))
			return false;
		if (postCode == null) {
			if (other.postCode != null)
				return false;
		} else if (!postCode.equals(other.postCode))
			return false;
		return true;
	}

	/**
	 * <pre>
	 * toString 메서드 재정의
	 * </pre>
	 * @return 센터 객체 출력 문자열
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(centerName);
		builder.append(", ");
		builder.append(facName);
		builder.append(", ");
		builder.append(postCode);
		builder.append(", ");
		builder.append(address);
		builder.append(", ");
		builder.append(contact);
		return builder.toString();
	}
}
