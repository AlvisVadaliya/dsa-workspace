package org.example.dsa.snakesandladders;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Snakes and Ladders - minimum number of dice rolls to reach the goal.
 *
 * <p><b>Problem.</b> A player starts on square {@code start} and rolls a single
 * die (1..6) per turn. Landing on a snake's head slides you down to its tail;
 * landing at the bottom of a ladder lifts you to its top. Find the fewest rolls
 * to land exactly on {@code goal} (rolling past the goal is not allowed).
 *
 * <p><b>Approach.</b> The board is an unweighted graph where an edge goes from
 * square {@code s} to {@code s + 1 .. s + 6} followed by any snake/ladder
 * resolution on the landing square. BFS from the start gives the shortest path
 * in that graph, i.e. the minimum number of rolls. {@code moveCount[i]} stores
 * the first (minimum) number of rolls at which square {@code i} is reached.
 *
 * <p><b>Time.</b> O(goal * 6), <b>space</b> O(goal).
 *
 * <p><b>LeetCode.</b> Related to {@code "909. Snakes and Ladders"} - our 1-D
 * linear board with a start square of 0 differs from LC's n x n board ordered
 * boustrophedon, but the BFS approach is identical.
 * @see <a href="https://leetcode.com/problems/snakes-and-ladders/">909. Snakes and Ladders</a>
 */
public final class SnakesAndLadders {

    private SnakesAndLadders() {
    }

    /**
     * Minimum dice rolls to go from {@code start} to {@code goal} or -1 if the
     * goal can never be reached.
     *
     * @param snakes  head square ({@code < goal}) -> tail square
     * @param ladders bottom square -> top square
     */
    public static int minMoves(Map<Integer, Integer> snakes, Map<Integer, Integer> ladders, int start, int goal) {
        boolean[] visited = new boolean[goal + 1];
        int[] rolls = new int[goal + 1];
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int square = queue.poll();
            if (square == goal) {
                return rolls[square];
            }
            for (int die = 1; die <= 6; die++) {
                int next = square + die;
                if (next > goal) {
                    continue;
                }
                int landed = ladders.getOrDefault(next, snakes.getOrDefault(next, next));
                if (landed > goal) {
                    continue;
                }
                if (!visited[landed]) {
                    visited[landed] = true;
                    rolls[landed] = rolls[square] + 1;
                    queue.add(landed);
                }
            }
        }
        return -1;
    }

    /** Demo: prints the minimum rolls for the hard-coded board below. */
    public static void main(String[] args) {
        TreeMap<Integer, Integer> snakes = new TreeMap<>();
        snakes.put(99, 54);
        snakes.put(70, 55);
        snakes.put(52, 42);
        snakes.put(25, 2);
        snakes.put(95, 72);
        snakes.put(62, 18);
        snakes.put(88, 36);

        TreeMap<Integer, Integer> ladders = new TreeMap<>();
        ladders.put(6, 25);
        ladders.put(11, 40);
        ladders.put(60, 85);
        ladders.put(46, 90);
        ladders.put(17, 69);
        ladders.put(2, 23);
        ladders.put(8, 34);

        System.out.println(minMoves(snakes, ladders, 0, 100));
    }
}