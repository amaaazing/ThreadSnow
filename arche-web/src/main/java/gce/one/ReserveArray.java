package gce.one;

public class ReserveArray {

	private int[] a={1,2,3,4,5,6,7,8,9,10};
	
	public int[] reserve(int[] a){
		int N = a.length;
		
		//数组中的元素倒序排列，只需交换一半的元素
		// 
		for(int i=0;i<N/2;i++){
			int temp = a[i];
			a[i]=a[N-i-1];
			a[N-i-1]=temp;
		}
		
		return a;
	}
	public static void main(String[] args) {
		ReserveArray r = new ReserveArray();
		int[] c = r.reserve(r.a);
		for(int i=0;i<c.length;i++){
			System.out.print(c[i]+" ");
		}
	}
}
