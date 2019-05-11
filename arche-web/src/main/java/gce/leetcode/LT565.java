package gce.leetcode;

import edu.princeton.cs.algs4.LinkedQueue;

import java.util.HashMap;

/**
 * 数组嵌套
 * 索引从0开始长度为N的数组A，包含0到N - 1的所有整数。找到并返回最大的集合S，
 * S[i] = {A[i], A[A[i]], A[A[A[i]]], ... }且遵守以下的规则。
 假设选择索引为i的元素A[i]为S的第一个元素，S的下一个元素应该是A[A[i]]，
 之后是A[A[A[i]]]... 以此类推，不断添加直到S出现重复的元素。

 // 类似于找最大环的长度

 认真观察题目中的case可以发现，如果当前的元素已经在之前的环中出现过了，
 那么以该元素开头必定不会出现最大环。所以我们只需维护一个visited数组就可以
 将程序优化为O（N）的时间效率。
 */
public class LT565 {
    /**
     * 注意:

     N是[1, 20,000]之间的整数。
     A中不含有重复的元素。
     A中的元素大小在[0, N-1]之间。

     输入: A = [5,4,0,3,1,6,2]
     输出: 4
     解释:
     A[0] = 5, A[1] = 4, A[2] = 0, A[3] = 3, A[4] = 1, A[5] = 6, A[6] = 2.

     其中一种最长的 S[K]:
     S[0] = {A[0], A[5], A[6], A[2]} = {5, 6, 2, 0}
     * @param nums
     * @return
     */
    public static int arrayNesting(int[] nums) {
        int max = 1;
        // 访问标记
        boolean[] visited = new boolean[nums.length];

        HashMap<Integer,LinkedQueue> map = new HashMap<Integer, LinkedQueue>();
        for(int i = 0; i < nums.length; i++){
            LinkedQueue list = new LinkedQueue();
            //如果一个环的长度超过了数组的一半大小，那么这个长度就是答案直接返回即可。
            if(max > nums.length/2)
                return max;
            if(visited[nums[i]])
                continue;
            visited[nums[i]] = true;
            int curMax = 1;
            int cur = nums[nums[i]];
            map.put(i,list);
            list.enqueue(nums[i]);
            while(nums[i] != cur){
                list.enqueue(cur);
                visited[cur] = true;
                curMax++;
                cur = nums[cur];
            }
            max = curMax > max? curMax : max;
        }
        return max;
    }


    public static void main(String [] args){
        int[] nums = new int[200];
//         int[] nums = new int[]{ 5,4,0,3,1,6,2};
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }
        LT384 lt = new LT384(nums);

        // A中不含有重复的元素。
        arrayNesting(lt.shuffle());
    }

}
