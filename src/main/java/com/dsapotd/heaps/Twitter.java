package com.dsapotd.heaps;

import java.util.*;

/**
 * <h1>355. Design Twitter</h1>
 *
 * <p>Design a simplified version of Twitter where users can post tweets, follow/unfollow
 * each other, and see the 10 most recent tweets in their news feed.</p>
 *
 * <p>Implement the {@code Twitter} class:</p>
 * <ul>
 *   <li>{@code Twitter()} — initializes the Twitter object.</li>
 *   <li>{@code void postTweet(int userId, int tweetId)} — composes a new tweet with id
 *       {@code tweetId} by the user {@code userId}. Each call is guaranteed a unique tweetId.</li>
 *   <li>{@code List<Integer> getNewsFeed(int userId)} — retrieves the 10 most recent tweet ids
 *       in the user's news feed. Each item in the news feed must be posted by users who the user
 *       followed or by the user themself. Tweets must be ordered from most recent to least recent.</li>
 *   <li>{@code void follow(int followerId, int followeeId)} — the user with id {@code followerId}
 *       starts following the user with id {@code followeeId}.</li>
 *   <li>{@code void unfollow(int followerId, int followeeId)} — the user with id {@code followerId}
 *       starts unfollowing the user with id {@code followeeId}.</li>
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>
 * Twitter twitter = new Twitter();
 * twitter.postTweet(1, 5);        // User 1 posts tweet 5
 * twitter.getNewsFeed(1);         // → [5]
 * twitter.follow(1, 2);           // User 1 follows user 2
 * twitter.postTweet(2, 6);        // User 2 posts tweet 6
 * twitter.getNewsFeed(1);         // → [6, 5] (tweet 6 is more recent)
 * twitter.unfollow(1, 2);         // User 1 unfollows user 2
 * twitter.getNewsFeed(1);         // → [5]  (tweet 6 no longer in feed)
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= userId, followerId, followeeId, tweetId <= 500</li>
 *   <li>All tweetIds are unique.</li>
 *   <li>At most 3 * 10^4 calls will be made in total.</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Hash Table, Linked List, Design
 */
public class Twitter {

    // -------------------------------------------------------------------------
    // Tier 2: Senior — Heap-based K-way merge (merge K sorted tweet lists)
    // -------------------------------------------------------------------------
    // Each user's tweets are stored newest-last (appended in order).
    // getNewsFeed uses a max-heap to merge the latest tweets from each feed
    // source, pulling the next tweet from the same user when needed.
    // This is the "Merge K Sorted Lists" pattern applied to feeds.
    //
    // Heap entry: int[] { timestamp, tweetId, userId, nextIndexToVisit }

    private int timestamp;
    private final Map<Integer, List<int[]>> tweets;   // userId -> [[ts, tweetId], ...]
    private final Map<Integer, Set<Integer>> following; // userId -> set of followeeIds

    public Twitter() {
        timestamp = 0;
        tweets = new HashMap<>();
        following = new HashMap<>();
    }

    // Time Complexity: O(1) amortized
    // Space Complexity: O(1) per call
    public void postTweet(int userId, int tweetId) {
        tweets.computeIfAbsent(userId, k -> new ArrayList<>())
              .add(new int[]{timestamp++, tweetId});
    }

    // Time Complexity: O(F log F + 10 log F) where F = number of feed sources
    // Space Complexity: O(F) for the heap
    public List<Integer> getNewsFeed(int userId) {
        // Seed the heap with each feed source's latest tweet
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        Set<Integer> sources = new HashSet<>();
        sources.add(userId); // always include self
        if (following.containsKey(userId)) {
            sources.addAll(following.get(userId));
        }

        for (int sourceId : sources) {
            List<int[]> userTweets = tweets.get(sourceId);
            if (userTweets != null && !userTweets.isEmpty()) {
                int lastIdx = userTweets.size() - 1;
                int[] latest = userTweets.get(lastIdx);
                // [timestamp, tweetId, sourceId, nextOlderIndex]
                maxHeap.offer(new int[]{latest[0], latest[1], sourceId, lastIdx - 1});
            }
        }

        List<Integer> feed = new ArrayList<>();
        while (!maxHeap.isEmpty() && feed.size() < 10) {
            int[] top = maxHeap.poll();
            feed.add(top[1]); // tweetId
            // If this user has older tweets, push the next one into the heap
            if (top[3] >= 0) {
                List<int[]> userTweets = tweets.get(top[2]);
                int[] older = userTweets.get(top[3]);
                maxHeap.offer(new int[]{older[0], older[1], top[2], top[3] - 1});
            }
        }

        return feed;
    }

    // Time Complexity: O(1)
    // Space Complexity: O(1)
    public void follow(int followerId, int followeeId) {
        following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
    }

    // Time Complexity: O(1)
    // Space Complexity: O(1)
    public void unfollow(int followerId, int followeeId) {
        if (following.containsKey(followerId)) {
            following.get(followerId).remove(followeeId);
        }
    }

    // -------------------------------------------------------------------------
    // Tier 1: Intermediate — Collect all tweets, sort, return top 10
    // -------------------------------------------------------------------------
    static class Intermediate {

        private int timestamp;
        private final Map<Integer, List<int[]>> tweets;
        private final Map<Integer, Set<Integer>> following;

        Intermediate() {
            timestamp = 0;
            tweets = new HashMap<>();
            following = new HashMap<>();
        }

        // Time Complexity: O(1)
        // Space Complexity: O(1)
        public void postTweet(int userId, int tweetId) {
            tweets.computeIfAbsent(userId, k -> new ArrayList<>())
                  .add(new int[]{timestamp++, tweetId});
        }

        // Time Complexity: O(T log T) where T = total tweets from all feed sources
        // Space Complexity: O(T)
        public List<Integer> getNewsFeed(int userId) {
            List<int[]> allTweets = new ArrayList<>();

            allTweets.addAll(tweets.getOrDefault(userId, Collections.emptyList()));
            for (int followeeId : following.getOrDefault(userId, Collections.emptySet())) {
                allTweets.addAll(tweets.getOrDefault(followeeId, Collections.emptyList()));
            }

            // Sort all collected tweets by timestamp descending
            allTweets.sort((a, b) -> b[0] - a[0]);

            List<Integer> feed = new ArrayList<>();
            for (int i = 0; i < Math.min(10, allTweets.size()); i++) {
                feed.add(allTweets.get(i)[1]);
            }
            return feed;
        }

        // Time Complexity: O(1)
        // Space Complexity: O(1)
        public void follow(int followerId, int followeeId) {
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        // Time Complexity: O(1)
        // Space Complexity: O(1)
        public void unfollow(int followerId, int followeeId) {
            if (following.containsKey(followerId)) {
                following.get(followerId).remove(followeeId);
            }
        }
    }

    /*
     * Tier 3: Staff Discussion
     * -------------------------
     * 1. SCALABILITY — At Twitter's scale, a single machine can't hold all tweets.
     *    Fan-out-on-write: pre-compute and cache each user's feed in Redis on every postTweet.
     *    Fan-out-on-read (used here): merge at read time — better for users with massive follower counts.
     *    Twitter uses a hybrid: fan-out-on-write for regular users, fan-out-on-read for celebrities.
     *
     * 2. HEAP COMPLEXITY ADVANTAGE — Tier 2 is O(F log F) vs Tier 1's O(T log T).
     *    If a user follows 500 people each with 10,000 tweets, Tier 1 sorts 5M entries;
     *    Tier 2 only processes at most F + 10 heap operations to retrieve 10 tweets.
     *
     * 3. STORAGE — In production, tweets are stored in a distributed columnar store (e.g., Manhattan at Twitter).
     *    The in-memory list here simulates a per-user tweet timeline shard.
     *
     * 4. UNFOLLOW CONSISTENCY — In a distributed system, unfollow doesn't immediately
     *    purge pre-computed feeds. Eventual consistency is acceptable for social feeds.
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        Twitter twitter = new Twitter();

        twitter.postTweet(1, 5);
        System.out.println(twitter.getNewsFeed(1));   // [5]

        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        System.out.println(twitter.getNewsFeed(1));   // [6, 5]

        twitter.unfollow(1, 2);
        System.out.println(twitter.getNewsFeed(1));   // [5]

        // Test: feed limited to 10 most recent
        Twitter twitter2 = new Twitter();
        twitter2.follow(1, 2);
        for (int i = 1; i <= 12; i++) twitter2.postTweet(i % 2 == 0 ? 2 : 1, i * 10);
        System.out.println(twitter2.getNewsFeed(1));  // 10 most recent tweet ids
    }

    private static void runIntermediate() {
        Intermediate twitter = new Intermediate();

        twitter.postTweet(1, 5);
        System.out.println(twitter.getNewsFeed(1));   // [5]

        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        System.out.println(twitter.getNewsFeed(1));   // [6, 5]

        twitter.unfollow(1, 2);
        System.out.println(twitter.getNewsFeed(1));   // [5]
    }
}
