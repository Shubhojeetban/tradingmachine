package com.cryptotradder.tradingmachine.datastructure;

import java.util.ArrayList;
import java.util.List;

import com.cryptotradder.tradingmachine.models.Limit;

public class LimitTree {
	private int size;
    private int capacity;
    private final int removeLoad = 5; // const to check how many node to be removed
    private Limit root;
    private int count;
    private Limit smallestLimit;
    private Limit highestLimit;

    public LimitTree() {
        root = null;
    }

    public void insert(Limit limit) {
        if (smallestLimit == null || limit.limitPrice < smallestLimit.limitPrice) {
            smallestLimit = limit;
        }
        if (highestLimit == null || limit.limitPrice > highestLimit.limitPrice) {
            highestLimit = limit;
        }
        if (size == capacity) {
//            if(limit.limitPrice > getHighest().limitPrice) return null;
//            else return removeLimits();
        }
        if (root != null && limit != null) {
            if (smallestLimit == null || limit.limitPrice < smallestLimit.limitPrice) {
                smallestLimit = limit;
            }
            if (highestLimit == null || limit.limitPrice > highestLimit.limitPrice) {
                highestLimit = limit;
            }
        }
        root = insertRec(root, limit);
    }

    public Limit searchByLimitPrice(int limitPrice) {
        return search(root, limitPrice);
    }

    private Limit search(Limit root, int limitPrice) {
        // Base Cases: root is null or key is present at root
        if (root == null || root.limitPrice == limitPrice) {
            return root;
        }

        // Key is greater than root's key
        if (root.limitPrice < limitPrice) {
            return search(root.rightChild, limitPrice);
        }

        // Key is smaller than root's key
        return search(root.leftChild, limitPrice);
    }

    public Limit getSmallestLimit() {
        return smallestLimit;
    }

    public Limit getHighestLimit() {
        return highestLimit;
    }

    private Limit insertRec(Limit root, Limit limit) {
        if (root == null) {
            root = limit;
            return root;
        } else if (limit.limitPrice < root.limitPrice) {
            limit.parent = root;
            root.leftChild = insertRec(root.leftChild, limit);
        } else if (limit.limitPrice > root.limitPrice) {
            limit.parent = root;
            root.rightChild = insertRec(root.rightChild, limit);
        }
        
        return root;
    }

    public Limit deleteLimit(Limit limit) {
        root = deleteRec(root, limit.limitPrice);
        if (limit == smallestLimit) {
            smallestLimit = findSmallestLimit(root);
        }
        if(limit == highestLimit) {
            highestLimit = findHighestLimit(root);
        }
        return root;
    }

    private Limit deleteRec(Limit root, double limitPrice) {
        /* Base Case: If the tree is empty */
        if (root == null) {
            return root;
        }

        /* Otherwise, recur down the tree */
        if (limitPrice < root.limitPrice) {
            root.leftChild = deleteRec(root.leftChild, limitPrice);
        } else if (limitPrice > root.limitPrice) {
            root.rightChild = deleteRec(root.rightChild, limitPrice);
        } // if key is same as root's 
        // key, then This is the
        // node to be deleted
        else {
            if (root.leftChild == null && root.rightChild == null) {
                return null;
            }
            // node with only one child or no child
            if (root.leftChild == null) {
                return root.rightChild;
            } else if (root.rightChild == null) {
                return root.leftChild;
            }

            // node with two children: Get the inorder
            // successor (smallest in the right subtree)
            Limit inOrderSuccessor = minValue(root.rightChild);
            root.limitPrice = inOrderSuccessor.limitPrice;
            root.totalVolume = inOrderSuccessor.totalVolume;
            root.order = inOrderSuccessor.order;
            root.rightChild = deleteRec(root.rightChild, root.limitPrice);
        }

        return root;
    }

    private Limit minValue(Limit root) {
        while (root.leftChild != null) {
            root = root.leftChild;
        }
        return root;
    }

    public List<Limit> getAllLimitsInAsc() {
        List<Limit> list = new ArrayList<>();
        ascInorderTraversal(root, list);
        return list;
    }

    // need modification then make it public
    public List<Limit> getkSmallestLimit(int k) {
        count = 0;
        Limit limit = getkthSmallest(root, k);
        List<Limit> list = new ArrayList<>();
        descInorderTraversal(limit, list);
        return list;
    }

    public List<Limit> getkLargestLimit() {
        return null;
    }

    private List<Limit> removeLimits() {
        return null;
    }

    private Limit getkthSmallest(Limit root, int k) {
        // base case
        if (root == null) {
            return null;
        }

        // search in left subtree
        Limit left = getkthSmallest(root.leftChild, k);

        // if k'th smallest is found in left subtree, return it
        if (left != null) {
            return left;
        }

        // if current element is k'th smallest, return it
        count++;
        if (count == k) {
            return root;
        }

        // else search in right subtree
        return getkthSmallest(root.rightChild, k);
    }

    private void ascInorderTraversal(Limit root, List<Limit> list) {
        if (root != null) {
            ascInorderTraversal(root.leftChild, list);
            list.add(root);
            ascInorderTraversal(root.rightChild, list);
        }
    }

    private void descInorderTraversal(Limit root, List<Limit> list) {
        if (root != null) {
            descInorderTraversal(root.rightChild, list);
            list.add(root);
            descInorderTraversal(root.leftChild, list);
        }
    }
    
    private Limit findSmallestLimit(Limit root) {
        Limit temp = root;
        if(temp == null) return temp;
        while(temp.leftChild != null) {
            temp = temp.leftChild;
        }
        return temp;
    }
    
    private Limit findHighestLimit(Limit root) {
        Limit temp = root;
        if(temp == null) return temp;
        while(temp.rightChild != null) {
            temp = temp.rightChild;
        }
        return temp;
    }
}
