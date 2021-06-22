package util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * <pre>
 * 공통 메서드 
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */

public class Utility {
	
	/**
     * <pre>
     * 질문에 대한 형식에 맞는 답을 받는 메서드
     * </pre>
     * @param question 질문
     * @return 긍정하는 답이면 true, 그렇지 않으면 false
     */
    public boolean getAnswer(String question) {
    	Scanner sc = new Scanner(System.in);
    	String answer = null;
    	boolean close = false;
    	
    	while(!close) {
    		System.out.print(question + " (Y/N) : "); answer = sc.next();
    		if(answer.equals("Y") || answer.equals("y")) {
    			return true;
    		} else if (answer.equals("N") || answer.equals("n")) {
    			return false;
    		} else {
    			System.out.println("입력 형식이 올바르지 않습니다.");
    		}
    	}
    	return false;
    }
    
    /**
	 * <pre>
	 * 날짜 계산 메서드
	 * </pre>
	 * @param strDate 기준일
	 * @param day 더할 일 수
	 * @return 기준일이 day만큼 더해진 날짜
	 * @throws ParseException
	 */
	public String addDate(String strDate, int day) throws ParseException { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd"); 
		Calendar cal = Calendar.getInstance(); 
		Date dt = dtFormat.parse(strDate); 
		cal.setTime(dt); 

		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}
	
	/**
	 * <pre>
	 * 날짜 입력 메서드
	 * </pre>
	 * @return 입력받은 날짜
	 */
	public String inputDate(String message) {
		Scanner sc = new Scanner(System.in);
		int flag;
		String tmp;
		
		while(true) {
			flag = 0;
			System.out.print(message + " : "); 
			tmp = sc.next();
			
			if(tmp.length() == 8) {
				for(int i = 0; i < tmp.length(); i++) {
					if(tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
						continue;
					}
					else{
						flag = 1;
						break;
					}
				}
			} else {
				flag = 1;
			}
			if(flag == 0) {
				if(isValidDate(tmp)) {
					return tmp;
				}
			}
			else{
				System.out.println("[오류] 입력 형식을 확인해주세요");
			}
		}
	}
	
	/**
	 * <pre>
	 * 유효한 날짜인지 검사
	 * </pre>
	 * @param date 검사할 날짜
	 * @return 유효한 날짜이면 true, 그렇지 않으면 false
	 */
	public boolean isValidDate(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			System.out.println("[오류] 유효하지 않은 날짜입니다.");
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 입력 날짜가 유효 범위 내에 있는지 검사
	 * </pre>
	 * @param date 검사할 날짜
	 * @return 유효 범위 내에 있으면 true, 그렇지 않으면 false
	 */
	public boolean isDateInRange(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date dt1 = df.parse("20210311");
			Date dt2 = df.parse(df.format(new Date()));
			
			Date dt = df.parse(date);
			if(dt.before(dt1)) {
				System.out.println("2021년 3월 11일 이후 날짜만 입력 가능합니다.");
				return false;
			} else if(dt.after(dt2)) {
				System.out.println("오늘 이전 날짜만 입력 가능합니다.");
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 오늘 날짜를 입력했을 경우 현재 시간이 10시 이전인지 이후인지 검사
	 * -- REST API 문서가 매일 오전 9시 30분쯤 업데이트됨
	 * -- 오차를 고려하여 10시 이전에는 어제 날짜, 10시 이후에는 오늘 날짜 반환
	 * </pre>
	 * @param date 입력 날짜
	 * @return 10시 이전에는 어제 날짜, 10시 이후에는 오늘 날짜 반환
	 * @throws ParseException
	 */
	public String todayBeforeTen(String date) throws ParseException {
		SimpleDateFormat dfDay = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dfHour = new SimpleDateFormat("HH");
		
		String currentDay = dfDay.format(new Date());
		if(!date.equals(currentDay)) return date;
		
		String currentHour = dfHour.format(new Date());
		if(Integer.parseInt(currentHour) < 10) {
			System.out.println("오늘의 백신 접종 현황이 업데이트되지 않았습니다.\n어제 백신 접종 현황을 조회합니다.");
			return addDate(date, -1);
		} else {
			return date;
		}
	}
	
	/**
	 * <pre>
	 * 천단위마다 ,표시한 문자열 반환하는 메서드
	 * </pre>
	 * @param num 기존 숫자
	 * @return 천단위마다 ,표시한 문자열
	 */
	public String putComma(long num) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		return numberFormat.format(num);
	}
}
