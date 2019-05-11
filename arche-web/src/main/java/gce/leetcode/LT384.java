package gce.leetcode;

import edu.princeton.cs.algs4.StdOut;
import org.bouncycastle.util.Arrays;

import java.util.Random;

/**
 * 打乱一个没有重复元素的数组。
 * // 以数字集合 1, 2 和 3 初始化数组。
 int[] nums = {1,2,3};
 Solution solution = new Solution(nums);

 // 打乱数组 [1,2,3] 并返回结果。任何 [1,2,3]的排列返回的概率应该相同。
 solution.shuffle();

 // 重设数组到它的初始状态[1,2,3]。
 solution.reset();

 // 随机返回数组[1,2,3]打乱后的结果。
 solution.shuffle();
 */
public class LT384 {
    private int[] nums;

    public LT384(int[] nums) {
        this.nums = nums;
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        return nums;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        int N = nums.length;
        int[] result = Arrays.copyOf(nums,N);
        Random r = new Random(System.currentTimeMillis());

        for(int i=0;i<N;i++){
            // 开始的解决方案，想法正确，实现错误 result[i] = nums[r.nextInt(N)]; result里面有重复的元素出现
            int n = r.nextInt(N);
//            StdOut.print(n+" ");
            // 产生的n值有重复，但是数组下标i的值没有重复的
            // 随机值与数组小标交换
            exch(result,n,i);
        }
//        StdOut.print("\n");
        return result;
    }

    public static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    public static void main(String [] args){
        LT384 lt = new LT384(new int[]{1,2,3,4,5,6,7,8,9,0});
        for(int i:lt.nums)
            StdOut.print(i+" ");
        StdOut.println("\nshuffle: ");
        for(int i:lt.shuffle())
            StdOut.print(i+" ");
        StdOut.println("\nreset: ");
        for(int i:lt.reset())
            StdOut.print(i+" ");
    }
    /**
     * 如何写出随机生成一个整数，该整数以55%的几率生成1，以40%的几率生成2，以5%的几率生成3
     * int n5 = r.nextInt(100);
     int m; //结果数字
     if(n5 < 55){ //55个数字的区间，55%的几率
     m = 1;
     }else if(n5 < 95){//[55,95)，40个数字的区间，40%的几率
     m = 2;
     }else{
     m = 3;
     }
     ---------------------
     */



}
