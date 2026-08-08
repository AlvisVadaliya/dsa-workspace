package org.example.dsa.twosum;

import java.util.HashMap;
import java.util.Map;

/**
 * Two Sum - find the two indices whose values add up to the target.
 *
 * <p><b>Problem.</b> Given an array of integers {@code nums} and an integer
 * {@code target}, return the two indices such that {@code nums[i] + nums[j]}
 * equals the target. Exactly one solution exists and the same element cannot
 * be used twice.
 *
 * <p><b>Approach.</b> Single pass with a hash map from value to its latest
 * index. For each element, check whether its complement ({@code target - value})
 * was already seen; if yes, that pair is the answer. Otherwise record the
 * current value and keep scanning.
 *
 * <p><b>Time.</b> O(n), <b>space</b> O(n).
 *
 * <p><b>LeetCode.</b> <a href="https://leetcode.com/problems/two-sum/">1. Two Sum</a>
 */
public final class TwoSum {

    private TwoSum() {
    }

    /**
     * Indices of the two values in {@code nums} that add up to {@code target}.
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            Integer index = seen.get(complement);
            if (index != null) {
                return new int[] {index, i};
            }
            seen.put(nums[i], i);
        }
        throw new IllegalArgumentException("no two values sum to the target");
    }

    /** Demo: prints the indices for the textbook example. */
    public static void main(String[] args) {
        int[] indices = twoSum(new int[] {2, 7, 11, 15}, 9);
        System.out.println("[" + indices[0] + ", " + indices[1] + "]");
    }
}