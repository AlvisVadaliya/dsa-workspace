package org.example.dsa.twosum;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * JUnit tests for {@link TwoSum#twoSum(int[], int)}.
 *
 * <p>Given an array of integers and a target, return the indices of the two
 * numbers that add up to the target. The problem guarantees exactly one
 * solution and forbids using the same element twice.
 *
 * <p>Each test documents the input array, why the expected pair is the (unique)
 * answer, and how the hash map finds it in a single pass.
 *
 * <p><b>LeetCode.</b> <a href="https://leetcode.com/problems/two-sum/">1. Two Sum</a>
 */
class TwoSumTest {

    /**
     * 1) THE TEXTBOOK EXAMPLE from the LeetCode description.
     *
     *   nums = [2, 7, 11, 15], target = 9
     *
     *   indices [0, 1]. Walk through the single pass:
     *   - index 0: complement = 9 - 2 = 7, not seen yet -> remember 2 -> 0
     *   - index 1: complement = 9 - 7 = 2, seen at index 0 -> answer [0, 1]
     */
    @Test
    void textbookExample() {
        assertArrayEquals(new int[] {0, 1}, TwoSum.twoSum(new int[] {2, 7, 11, 15}, 9));
    }

    /**
     * 2) FIRST AND LAST ELEMENTS - the answer spans the whole array.
     *
     *   nums = [3, 2, 4], target = 6
     *
     *   The pair is 2 + 4 = 6 at indices [1, 2]. Beware: 3 + 3 = 6 is invalid
     *   because index 0 may only be used once, so the answer can never be [0, 0].
     */
    @Test
    void interiorPairNotDuplicatedElement() {
        assertArrayEquals(new int[] {1, 2}, TwoSum.twoSum(new int[] {3, 2, 4}, 6));
    }

    /**
     * 3) NEGATIVE NUMBERS - complements can be larger than the value seen.
     *
     *   nums = [-1, -2, -3, -4, -5], target = -8
     *
     *   -3 + -5 = -8 at indices [2, 4]. The map keyed by value handles negatives
     *   exactly like positives.
     */
    @Test
    void negativeNumbers() {
        assertArrayEquals(new int[] {2, 4}, TwoSum.twoSum(new int[] {-1, -2, -3, -4, -5}, -8));
    }

    /**
     * 4) ZERO TARGET - two zeros can pair with each other.
     *
     *   nums = [0, 4, 3, 0], target = 0
     *
     *   0 + 0 = 0 at indices [0, 3]. Both values are stored while scanning until
     *   the second zero is encountered, and the complement 0 - 0 = 0 is found in
     *   the map.
     */
    @Test
    void zeroTargetWithDuplicateZeros() {
        assertArrayEquals(new int[] {0, 3}, TwoSum.twoSum(new int[] {0, 4, 3, 0}, 0));
    }

    /**
     * 5) DUPLICATE VALUES - the second occurrence wins.
     *
     *   nums = [3, 3], target = 6
     *
     *   The two 3s at indices [0, 1] are a legal pair. The map must be updated to
     *   the latest index only if a stale one were kept the answer would collide.
     *   Here we add index 0, then at index 1 find complement 3 -> answer [0, 1].
     */
    @Test
    void duplicateValueUsesBothOccurrences() {
        assertArrayEquals(new int[] {0, 1}, TwoSum.twoSum(new int[] {3, 3}, 6));
    }

    /**
     * 6) LARGE VALUES - near int limits without overflow.
     *
     *   nums = [1_000_000_000, 651, 998_999_999], target = 1_998_999_999
     *
     *   Complement math stays well within the int range since every value and
     *   the target itself fit in 31 bits: 1_000_000_000 + 998_999_999 =
     *   1_998_999_999 at indices [0, 2].
     */
    @Test
    void largeValuesFitInIntRange() {
        assertArrayEquals(new int[] {0, 2}, TwoSum.twoSum(new int[] {1_000_000_000, 651, 998_999_999}, 1_998_999_999));
    }
}