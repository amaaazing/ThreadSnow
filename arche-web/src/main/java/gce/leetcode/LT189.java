package gce.leetcode;

import edu.princeton.cs.algs4.StdOut;

/**
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 * 输入: [1,2,3,4,5,6,7] 和 k = 3
   输出: [5,6,7,1,2,3,4]
 解释:
 向右旋转 1 步: [7,1,2,3,4,5,6]
 向右旋转 2 步: [6,7,1,2,3,4,5]
 向右旋转 3 步: [5,6,7,1,2,3,4]

 简单解法： 保存最后一个元素，然后从数组最后面开始，依次把a[N-2] 赋值给a[N-1] ，最后令 a[0] = 保存的值
 */
public class LT189 {
    public static int[] rotate(int[] nums, int k) {
        int N = nums.length;
        k %= N;// 取余

        /**
         * 双重循环
         * 时间复杂度：O(kn)
         * 空间复杂度：O(1)
         */
        for (int i = 0; i < k; i++) {
            // 交换a[0]与a[N-1]
            LeetcodeUtils.exch(nums, N - 1, 0);
            for (int j = N; j > 2; j--) {
                LeetcodeUtils.exch(nums, j - 2, j - 1); // j-2 >0 ===>j>2
                for (int s : nums)
                    StdOut.print(s + " ");
                StdOut.print(" j= "+j);
                StdOut.println();
            }
            StdOut.println("==========================");
        }
        return nums;
    }
    /**
     * 翻转
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * arr = [1,2,3,4,5] --右移两位--> [4,5,1,2,3]
     假设 n = arr.length，k = 右移位数，可得：
     [1,2,3,4,5]
                --翻转索引为[0,n-1]之间的元素--> [5,4,3,2,1]
                --翻转索引为[0,k-1]之间的元素--> [4,5,3,2,1]
                --翻转索引为[k,n-1]之间的元素--> [4,5,1,2,3]

     旋转数组其实就是把数组分成了两部分，解题关键就是在保证原有顺序的情况下
     把后面一部分移到前面去。数组整体翻转满足了第二个要素，但是打乱了数组的
     原有顺序，所以此时再次对两部分进行翻转，让他们恢复到原有顺序（翻转之后
     再翻转，就恢复成原有顺序了）。没有什么太复杂的思想，我也不清楚算法作者
     的灵感来自哪。不过我看到这个算法的时候脑袋里想的是“汉诺塔”，也许有渊源
     也说不定~
     */
    public static int[] rotate_2(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        LeetcodeUtils.reverse(nums, 0, n - 1);
        LeetcodeUtils.reverse(nums, 0, k - 1);
        LeetcodeUtils.reverse(nums, k, n - 1);
        return nums;
    }

    public static void main(String [] args){
        int[] nums = new int[]{1,2,3,4,5,6,7};
        int k = 3;
        for(int i:nums)
            StdOut.print(i+" ");

        StdOut.println("\nrotate:  "+k);

        for(int i:rotate_2(nums,k)) {
            StdOut.print(i + " ");
        }
    }
}
