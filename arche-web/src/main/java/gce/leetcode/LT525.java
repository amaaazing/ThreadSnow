package gce.leetcode;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 给定一个二进制数组, 找到含有相同数量的 0 和 1 的最长连续子数组（的长度）
 *
 * 首先对原数组做处理，将所有的0都变为-1；这样 “含有相同数量的 0 和 1 的连续数组” 就
 * 等价为 “元素值总和为0的连续数组”。
 */
public class LT525 {

    public static int findMaxLength(int[] nums) {
        int res = 0, sum = 0;
        int lastIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                nums[i] = -1;
            }
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum == 0 && i > res) {
                res = i + 1;
                lastIndex=i;
            }
            //若该前缀和的值已出现过（即标记数组或哈希中已存在），
            // 则说明标记中的下标到当前扫描的下标的这段数组的总和值是为0的。
            if (map.containsKey(sum)) {
                res = Math.max(i - map.get(sum), res);
                // 当i - map.get(sum)== res时，也要变更lastIdex
                if(res <= i - map.get(sum)) lastIndex = i; // 可能res > i - map.get(sum) 不需要修改指针
            } else {
                map.put(sum, i);
            }
        }
         int firstIndex = (lastIndex - res) < 0 ? 0 : lastIndex - res;
        StdOut.println("firstIndex: "+firstIndex);
        StdOut.println("lastIndex: "+lastIndex);

        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[20];
       // int[] nums = new int[]{ 1 ,0 ,1 ,0 ,1 ,0 ,0 ,0 ,1, 0 ,1 ,1 ,1, 0 ,0 ,1, 1 ,1 ,0, 1};

        Random r = new Random();

        for (int i = 0; i < nums.length; i++) {
            nums[i] = r.nextInt(2);
        }

        StdOut.println(Arrays.toString(nums));

        StdOut.println(findMaxLength(nums));

    }
}
