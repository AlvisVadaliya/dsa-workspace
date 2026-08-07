package org.example.dsa.snakesandladders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * JUnit tests for {@link SnakesAndLadders#minMoves(java.util.Map, java.util.Map, int, int)}.
 *
 * A player starts on a square 0 and rolls a die (1..6) per turn. Landing on the
 * head of a snake slides you down to its tail; landing at the bottom of a ladder
 * lifts you to its top. The answer is the minimum number of dice rolls needed to
 * land exactly on the goal square.
 *
 * Each test draws its own miniature board in a comment and explains why the
 * expected number of rolls is what it is, then verifies the BFS result.
 */
class SnakesAndLaddersTest {

    /**
     * 1) EMPTY BOARD - no snakes, no ladders.
     *
     * Board (goal 100), nothing else on it:
     *
     *   0 --[(6)]--> 6 --[(6)]--> 12 --[(6)]--> ... --> 96 --[(4)]--> 100
     *
     * Every roll adds at most 6 squares, so after 16 rolls you can cover at most
     * 16 x 6 = 96 squares, still short of 100. The lower bound is therefore
     * ceil(100 / 6) = 17 rolls, and it is achievable: roll 6 fifteen times to
     * reach 96, then roll 4 to land exactly on 100.
     */
    @Test
    void emptyBoardNeedsLowerBoundOf17Rolls() {
        assertEquals(17, SnakesAndLadders.minMoves(new TreeMap<>(), new TreeMap<>(), 0, 100));
    }

    /**
     * 2) ONE LADDER REACHES THE GOAL - a single roll wins.
     *
     * Board (goal 12), ladder 6 -> 12:
     *
     *   0   1   2   3   4   5   6   7   8   9  10  11  12
     *   ^                        |     |
     *   └───────────────────────┘     |
     *   ladder bottom 6 -> top 12     goal
     *
     * Roll a single 6: land on 6 (the ladder bottom) and lift straight to 12,
     * which is the goal, so the game ends on that one roll. Without the ladder
     * the same board needs 2 rolls (6 then 6).
     */
    @Test
    void ladderToGoalWinsInOneRoll() {
        TreeMap<Integer, Integer> ladders = new TreeMap<>();
        ladders.put(6, 12);
        assertEquals(1, SnakesAndLadders.minMoves(new TreeMap<>(), ladders, 0, 12));
    }

    /**
     * 3) ONE LONG LADDER SHORTCUTS THE WHOLE MIDDLE.
     *
     * Board (goal 100), ladder 4 -> 80:
     *
     *   0 --[(4)]--> 4 --ladder--> 80 --[(6)]--> 86 --[(6)]--> 92 --[(6)]--> 98 --[(2)]--> 100
     *   roll:       1                 2         3         4         5         6
     *
     * Roll 4 to land on the ladder bottom 4 and jump to 80. From 80 three max
     * rolls take you to 98 (80 -> 86 -> 92 -> 98) and a final roll of 2 lands
     * exactly on 100. Total = 1 + 4 = 5 rolls, vs 17 without any ladder.
     */
    @Test
    void longLadderCutsRollsDownToFive() {
        TreeMap<Integer, Integer> ladders = new TreeMap<>();
        ladders.put(4, 80);
        assertEquals(5, SnakesAndLadders.minMoves(new TreeMap<>(), ladders, 0, 100));
    }

    /**
     * 4) A SNAKE DENIES THE OBVIOUS SHORT PATH.
     *
     * Board (goal 12), snake 6 -> 1:
     *
     *   0   1   2   3   4   5   6   7   8   9  10  11  12
     *       ^      ^           │ 6 ──snake──▶ 1
     *       └──────┴───────────┘
     *
     * The natural fastest win is 2 rolls: 0 --(6)--> 6 --(6)--> 12. But 6 is the
     * snake's head, landing there bounces you back to 1, so that route never
     * finishes. Avoiding it entirely gets there in 3:
     *
     *   0 --(6)--> 5 --(6)--> 11 --(1)--> 12
     */
    @Test
    void snakeHeadOnFastPathForcesDetour() {
        TreeMap<Integer, Integer> snakes = new TreeMap<>();
        snakes.put(6, 1);
        assertEquals(3, SnakesAndLadders.minMoves(snakes, new TreeMap<>(), 0, 12));
    }

    /**
     * 5) SNAKE NEXT TO A LADDER - you must skip the snake head to climb.
     *
     * Board (goal 100), snake 5 -> 1 and ladder 6 -> 25:
     *
     *   0   1   2   3   4   5   6   ...  25   ...  97  100
     *   ^      ^        ^  ▲    ^
     *   start  snake head 5→1  ladder bottom 6 → 25  (25 -> 97 + 3 -> 100)
     *
     * Rolling 6 from 0 lands on ladder bottom 6 -> lift to 25. Rolling 5 would
     * land on snake head 5 and slide back to 1, so we roll the 6 exactly. From
     * 25 the fastest run is 12 rolls of 6 (25 -> 97) and one roll of 3 to 100:
     *
     *   0 --(6)--> 25 --> 97 --(3)--> 100   (1 + 12 + 1 = 14 rolls)
     */
    @Test
    void snakeAndLadderComboTakesFourteenRolls() {
        TreeMap<Integer, Integer> snakes = new TreeMap<>();
        snakes.put(5, 1);
        TreeMap<Integer, Integer> ladders = new TreeMap<>();
        ladders.put(6, 25);
        assertEquals(14, SnakesAndLadders.minMoves(snakes, ladders, 0, 100));
    }

    /**
     * 6) A SNAKE THAT CHANGES NOTHING.
     *
     * Board (goal 100), snake 96 -> 40:
     *
     *   0 --(6)--> ... --(6)--> 84 --(6)--> 90 --(4)--> 94 --(6)--> 100
     *                                            (96 would slide to 40)
     *
     * The naive 17-roll plan 0, 6, 12, ..., 90, 96, 100 lands on 96 and slides
     * to 40. But there is a 17-roll route that skips 96: 0, 6, 12, ..., 84, 90,
     * 94, 100 (last steps +6, +4, +6). The 17-roll lower bound stays achievable,
     * so the snake is irrelevant to the answer.
     */
    @Test
    void snakeOnAlternativePathStillKeepsSeventeenRolls() {
        TreeMap<Integer, Integer> snakes = new TreeMap<>();
        snakes.put(96, 40);
        assertEquals(17, SnakesAndLadders.minMoves(snakes, new TreeMap<>(), 0, 100));
    }

    /**
     * 7) THE ORIGINAL DEMO BOARD from {@link SnakesAndLadders#main(String[])}:
     *
     *   snakes:  99->54  95->72  70->55  52->42  62->18  25->2  88->36
     *   ladders:  6->25  11->40  17->69  60->85  46->90   2->23   8->34
     *
* One concrete shortest 5-roll route the BFS finds:
     *
     *   0 --(5)--> 5 --(6)--> 11 --(6)--> 46 --(4)--> 94 --(6)--> 100
     *                           ▲ jump 11->40   ▲ jump 46->90
     *
     *   roll 5 to land on 5, roll 6 to hit ladder bottom 11 (lift to 40),
     *   roll 6 to hit ladder bottom 46 (lift to 90), roll 4 to 94, roll 6
     *   to land exactly on 100 = 5 rolls, far better than the ladderless 17.
     */
    @Test
    void originalDemoBoardNeedsFiveRolls() {
        TreeMap<Integer, Integer> snakes = new TreeMap<>();
        snakes.put(99, 54);
        snakes.put(95, 72);
        snakes.put(70, 55);
        snakes.put(52, 42);
        snakes.put(62, 18);
        snakes.put(25, 2);
        snakes.put(88, 36);

        TreeMap<Integer, Integer> ladders = new TreeMap<>();
        ladders.put(6, 25);
        ladders.put(11, 40);
        ladders.put(60, 85);
        ladders.put(46, 90);
        ladders.put(17, 69);
        ladders.put(2, 23);
        ladders.put(8, 34);

        assertEquals(5, SnakesAndLadders.minMoves(snakes, ladders, 0, 100));
    }
}