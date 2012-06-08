How are Algebaric DataTypes used?
==============================

This page explains how to use ADTs, but it doesn't cover exactly [what they are](what_adt.html) or [why to use them](why_adt.html).

If you have an ADT definition in JADT's [syntax](syntax.html) for a binary tree

    package my.package
    
    IntBinaryTree = 
        Node(int value, IntBinaryTree left, IntBinaryTree right)
      | EmptyTree

Using [ant](ant/index.html) or the [shell](core/index.html) JADT generates Java code such that with a couple of imports

    import my.package.IntBinaryTree.*
    import static my.package.IntBinaryTree.*
    
You can create a new Tree with code like

    IntBinaryTree tree = _Node(42, _Node(12, _EmptyTree(), _EmptyTree()), _Node(103, _EmptyTree(), _Node(110, _EmptyTree(), _EmptyTree())));
 
 JADT also allows ADTs to be generic. The IntBinaryTree above could be replaced with a more generic version
 
     BinaryTree<A> = 
        Node(int value, BinaryTree<A> left, BinaryTree<A> right)
      | EmptyTree
 
 With that you can create a Tree with code like
 
     BinaryTree<String> empty = BinaryTree.<String>_EmptyTree();

     return _Node("hello", _Node("goodbye", empty, empty), _Node("whatever", empty, _Node("foo", empty, empty)));        

You can also achieve the same thing with 'new BinaryTree.Node<String>("hello"...)' and 'new BinaryTree.EmptyTree<String>()' but that

* constructs new instances of EmptyTree, where _EmptyTree() always reuse the same one
* requires more typing
* occasionally creates the less-than-useful (though technically correct) types.  E.g. the type of java.util.Colletions.singleton(_EmptyTree) is Set<IntBinaryTree> whereas the type of Collections.singleton(new IntBinaryTree.EmptyTree()) is Set<EmptyTree> which isn't probably very useful.
* requires more type annotation because Java does slightly more type inference with static methods than it does with regular constructors

Matching on an ADT
=================

In Java you can use "switch" to decide what to do you based on specific values of primitives and Enums.  If the primitive happens to be a boolean then you can also use "if" or the ternary operation (x ? y : z).  But what about ADTs?  How do you decide what to do based on a specific ADT constructor?

Most statically typed functional programming languages have a pattern matching for making decisions based on ADTs.  Java doesn't.  So with JADT you have a couple of choices, you can use instanceof and casting or preferably you can use the [Visitor](http://en.wikipedia.org/wiki/Visitor_pattern) pattern interfaces that JADT generates.  

Here's using a Visitor to find the max of an IntBinaryTree where null indicates an empty tree.

    public Integer max(IntBinaryTree tree)  {
       return tree.accept(new IntBinaryTree.Visitor<Integer>() {
          @Override
          Integer visit(Node x) {
             final Integer maxLeft = max(x.left);
             final int left = maxLeft == null ? Integer.MIN_VALUE;
             final Integer maxRight = max(x.right);
             final int right = maxRight == null ? Integer.MIN_VALUE;
             
             return Math.max(x.value, Math.max(left, right));
          }
          
          @Override
          Integer visit(EmptyTree x) {
             return null;
          }
       });
    }
    
Which says that the max of an EmptyTree is null while the max of a Node is the max of the node's value compared with the max of the left tree compared with the max of the right tree.

Languages with pattern matching also generally allow a "don't care" case, a default for when no other case matches.  JADT emulates that using a small variation of the visitor pattern.  

Given the ADT

    TPSReportStatus=
        Pending 
      | Approved(Manager approver)
      | Denied(Manager rejector)
    
You can see if an TPS report was approved by

    public boolean isApproved(TPSReportStatus status) {
        return status.accept(new TPSReportStatus.VisitorWithDefault<Boolean>() {
            @Override
            public Boolean visit(Approved x) {
                return true;
            }

            @Override
            protected Boolean getDefault(TPSReportStatus x) {
                return false;
            }
        });
    }

So for Approved the result is true, but for all other statuses (Pending and Denied) the result is false.

In this particular case the same thing could be achieved with instance of pretty nicely

    public boolean isApproved(TPSReportStatus status) {
       return status instanceof TPSReportStatus.Approved;
    }

A few rules of thumb for when to use instanceof vs a Visitor.

1. If you find yourself casting, stop, use the Visitor
2. If you have a bunch of instanceofs in a row then odds are pretty good you'll make a mistake.  Seriously consider using Visitor.
