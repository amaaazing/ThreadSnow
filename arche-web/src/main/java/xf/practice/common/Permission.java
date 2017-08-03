package xf.practice.common;

/**
 * 位运算的运用
 * 
 * 利用位的异或（^）运算使用一个数字记录多个信息：
 * 有几个状态值分别是 1、2、8、16 .....
 * 这些值的规律是，他们的二进制只有一位是 1 ，其余都是 0， 因此， 
 * 他们中的任意几个的按位异或运算的结果都不会出现 两个数的某一位都是 1 的情况，并且运算的值都是唯一确定的，
 * 也就是，知道运算的结果，就知道是哪几个数的组合，这样可以用一个数字记录多个信息。 
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class Permission {
	// 允许查询，二进制第1位，0表示否，1表示是
	public static final int ALLOW_SELECT = 1 << 0; // 0001 = 1
	
	// 允许新增，二进制第2位，0表示否，1表示是
	public static final int ALLOW_INSERT = 1 << 1; // 0010 = 2
	
	// 允许修改，二进制第3位，0表示否，1表示是
	public static final int ALLOW_UPDATE = 1 << 2; // 0100 = 4
	
	// 允许删除，二进制第4位，0表示否，1表示是
	public static final int ALLOW_DELETE = 1 << 3; // 1000 = 8
	
	// 存储目前的权限状态，flag 的四个二进制位存储了四种权限的状态，每一位的0和1代表一项权限的启用和停用
	private int flag;

	/**
	 *  重新设置权限
	 *  
	 */
	// 1^2^4 = 7 // "00000111"
	// 因此，如果我们知道结果是 7 ，就知道他们是由 1 、2、4 组合而成。
	// 如果我们要设置一个参数，使其包含几个状态值，就可以用 按位或运算，
	// 如：permission.setPermission(ALLOW_SELECT ^ ALLOW_INSERT ^ ALLOW_UPDATE)
	// permission.setPermission(ALLOW_SELECT | ALLOW_INSERT | ALLOW_UPDATE)
	public void setPermission(int permission) {
		flag = permission; 
	}

	/**
	 *  添加一项或多项权限,使用或运算（|）
	 */
	public void enable(int permission) {
		flag |= permission; // 相当于flag = flag | permission;
	}
	
	/**
	 *  删除一项或多项权限
	 */
	public void disable(int permission) {
		flag &= ~permission; // flag = flag & (~permission) 
	}
	
	/**
	 *  是否拥某些权限，与运算（&）
	 */
	public boolean isAllow(int permission) {
		return (flag & permission) == permission;
	}
	
	/**
	 *  是否禁用了某些权限
	 *  
	 *  如： flag & ALLOW_SELECT = 0 ，表示禁用了查询权限
	 */
	public boolean isNotAllow(int permission) {
		return (flag & permission) == 0;
	}
	
	/**
	 *  是否仅仅拥有某些权限
	 *  
	 *  
	 *  如：permission.isOnlyAllow(ALLOW_SELECT | ALLOW_INSERT);
	 */
	public boolean isOnlyAllow(int permission) {
		return flag == permission;
	}
	
	public void toggle(int permission){
		 flag ^= permission;
	}

}
