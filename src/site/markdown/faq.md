FAQ: Frequently Anticipated Questions
=====================================

Q: Why not use Scala or another language with ADTs and pattern matching instead of JADT?

A: If you are in a position where you can then by all means do!  JADT can't hope to compete.  But JADT is meant to be used where other languages can't be used.  Also, unlike Scala's ADTs, JADT's ADTs are meant to be easily used from within Java via the Visitor pattern.

***

Q: Why isn't JADT written in Scala or language X

A: That was a tough call.  I would have preferred to write JADT in Scala since it excels at language processing.  However, I finally landed on using Java because the target audience would be Java users and in open source your contributor pool comes from your user pool. Plus, not using Scala means one less runtime dependency.
