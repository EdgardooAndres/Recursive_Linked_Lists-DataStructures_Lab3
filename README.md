# Lab Exercises on Recursion and Linked Lists

In this activity, we will work with a recursive implementation of the IndexList ADT based on singly linked list. The implementation will be direct: not depending on another object of type LinkedList as was used in one of the previous lab sessions. We shall discuss some issues that are relevant in deriving a recursive solution for each operation to be implemented, as specified in the IndexList interface. Be aware that this way of thinking is useful for many other cases regarding operations on more complex data structures that shall be studied in the course. The class to implement is  SLRIndexList .

# Recursive Thinking with Lists
One needs to “think recursively” when designing recursive algorithms. One strategy in this case is to leverage on the recursive nature of the list. When working with a particular data structure, or data space, there might be different recursive ways to view (or define) that particular type of structure. Different recursive algorithms may be designed to traverse the structure, each based on those different recursive views that the structure may allow. 
