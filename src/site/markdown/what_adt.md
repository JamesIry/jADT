What is an Algebraic DataType?
==============================

This page explains what an ADT is, but it doesn't cover [why to use them](why_adt.html) or [how to use them](how_adt.html).

An algebraic datatype (abbreviated ADT, but not to be confused with abstract datatypes) is something easy to express in many statically typed functional languages, but that has poor support in Java. An ADT is a very simple data structure type that consists of a choice of some finite number of alternative "constructors" each of which requires a finite number of fields.  Some concrete examples should help clarify.

One of the simplest ADTs in JADT syntax would look like

    Boolean = True | False
    
A Boolean type can be constructed using either True or False. Which looks a lot like the built in Java boolean.  In fact, all the Java primitives are ADTs the same way.  Effectively, Java primitives behave as if there were ADT declarations like

    boolean = true | false    
    int = 0 | 1 | -1 | 2 | -2 ... MAX_INT | MIN_INT
    char = 'a' | 'b' | 'c' | ...
    // etc
    
If that's all there was to ADTs then we'd just use primitive or Enums and be done with it.  But with an ADT, each constructor can have fields as well, something that Java primitives and Enums can't.

    OptionalInt = Some(int value) | None
    
That says that an OptionalInt is always either None (i.e. no int) or it is Some with an int value.  In effect that replicates using an java.lang.Integer which is allowed to be either null or a valid int value. But we haven't had to use null.  A more complicated example from an Enterprise style app might be

   TPSReportStatus = Pending | Approved(Manager approver) | Denied(Manager rejector)
   
So a TPSReport can be in one of 3 statuses.  Pending, Approved, or Denied.  If Approved or Denied it must have a an associated manager that did the approval or rejection.

Finally, nothing prevents an ADT from being recursive.  A binary tree of integers can be described as

    IntBinaryTree = Node(int value, IntBinaryTree left, IntBinaryTree right) | EmptyNode
    
Which says that an IntBinaryTree is either empty or it is a Node with an integer value, a left subtree, and a right subtree.

An ADT can also be generic

    BinaryTree<T> = Node(T value, BinaryTree<T> left, BinaryTree<T> right) | EmptyNode

Which says that a BinaryTree<T> is either a Node<T> or an EmptyNode<T>.

Now that you know what an ADT is, you might want to check out [why to use them](why_adt.html) or [how to use them](how_adt.html).
