package com.dsapotd.linkedlists;

/**
 * <h1>1472. Design Browser History</h1>
 *
 * <p>You have a browser of one tab where you start on the <code>homepage</code> and you can visit another <code>url</code>, get back in the history number of <code>steps</code> or move forward in the history number of <code>steps</code>.</p>
 *
 * <p>Implement the <code>BrowserHistory</code> class:</p>
 * <ul>
 *   <li><code>BrowserHistory(string homepage)</code> Initializes the object with the <code>homepage</code> of the browser.</li>
 *   <li><code>void visit(string url)</code> Visits <code>url</code> from the current page. It clears up all the forward history.</li>
 *   <li><code>string back(int steps)</code> Move <code>steps</code> back in history. If you can only return <code>x</code> steps in the history and <code>steps > x</code>, you will return only <code>x</code> steps. Return the current <code>url</code> after moving back in history at most <code>steps</code>.</li>
 *   <li><code>string forward(int steps)</code> Move <code>steps</code> forward in history. If you can only forward <code>x</code> steps in the history and <code>steps > x</code>, you will forward only <code>x</code> steps. Return the current <code>url</code> after moving forward in history at most <code>steps</code>.</li>
 * </ul>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= homepage.length <= 20</li>
 *   <li>1 <= url.length <= 20</li>
 *   <li>1 <= steps <= 100</li>
 *   <li>homepage and url consist of '.' or lower case English letters.</li>
 *   <li>At most 5000 calls will be made to visit, back, and forward.</li>
 * </ul>
 *
 * @tags Array, Linked List, Stack, Design, Doubly-Linked List, Data Stream
 */
public class BrowserHistory {

    // Tier 2: Senior (Doubly Linked List)
    // Time Complexity: O(1) for visit, O(steps) for back/forward
    // Space Complexity: O(N) where N is number of unique visits
    private static class Node {
        String url;
        Node prev, next;
        Node(String url) {
            this.url = url;
        }
    }

    private Node curr;

    public BrowserHistory(String homepage) {
        this.curr = new Node(homepage);
    }
    
    public void visit(String url) {
        Node newNode = new Node(url);
        curr.next = newNode;
        newNode.prev = curr;
        curr = newNode;
    }
    
    public String back(int steps) {
        while (steps > 0 && curr.prev != null) {
            curr = curr.prev;
            steps--;
        }
        return curr.url;
    }
    
    public String forward(int steps) {
        while (steps > 0 && curr.next != null) {
            curr = curr.next;
            steps--;
        }
        return curr.url;
    }

    /**
     * Tier 1: Mid-Level (Dynamic Array / ArrayList)
     * Time Complexity: O(1) for all operations
     * Space Complexity: O(N)
     */
    static class Intermediate {
        private final java.util.List<String> history;
        private int currIdx;
        private int maxIdx;

        public Intermediate(String homepage) {
            history = new java.util.ArrayList<>();
            history.add(homepage);
            currIdx = 0;
            maxIdx = 0;
        }

        public void visit(String url) {
            currIdx++;
            if (currIdx < history.size()) {
                history.set(currIdx, url);
            } else {
                history.add(url);
            }
            maxIdx = currIdx; // Overwrite forward history
        }

        public String back(int steps) {
            currIdx = Math.max(0, currIdx - steps);
            return history.get(currIdx);
        }

        public String forward(int steps) {
            currIdx = Math.min(maxIdx, currIdx + steps);
            return history.get(currIdx);
        }
    }

    public static void main(String[] args) {
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Doubly Linked List):");
        BrowserHistory browserHistory = new BrowserHistory("leetcode.com");
        browserHistory.visit("google.com");
        browserHistory.visit("facebook.com");
        browserHistory.visit("youtube.com");
        System.out.println(browserHistory.back(1)); // facebook.com
        System.out.println(browserHistory.back(1)); // google.com
        System.out.println(browserHistory.forward(1)); // facebook.com
        browserHistory.visit("linkedin.com"); // clears forward history
        System.out.println(browserHistory.forward(2)); // linkedin.com
        System.out.println(browserHistory.back(2)); // google.com
        System.out.println(browserHistory.back(7)); // leetcode.com
    }
}
