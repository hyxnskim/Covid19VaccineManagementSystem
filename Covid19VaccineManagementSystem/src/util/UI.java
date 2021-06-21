package util;

/**
 * <pre>
 * 메뉴 구성
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */
public class UI {

	/**
	 * <pre>
	 * 서브메뉴 출력 메서드
	 * </pre>
	 * @param menuName 메뉴 이름
	 */
	public void printSubMenu(String menuName) {
		System.out.println();
		System.out.println("---------------------------------------");
		System.out.println("\t" + menuName + "\t");
		System.out.println("---------------------------------------");
	}
	
	/**
	 * <pre>
	 * 서브-서브 메뉴 출력 메서드
	 * </pre>
	 * @param menuName 메뉴 이름
	 */
	public void printSubSubMenu(String menuName) {
		System.out.println();
		System.out.println("***\t" + menuName + "\t***");
		System.out.println();
	}
}
