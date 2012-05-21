package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;
import static pogofish.jadt.util.Util.list;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.emitter.DataTypeEmitter;
import pogofish.jadt.emitter.StandardDataTypeEmitter;
import pogofish.jadt.util.Util;


public class DataTypeEmitterTest {
    private static final String HEADER = "/*header*/\n";
    private static final String FOOBAR = 
    "public abstract class FooBar {\n" +
    "\n" +
    "   private FooBar() {\n" +
    "   }\n" +
    "\n" +
    "   public static final FooBar Foo(Integer yeah, String hmmm) { return new Foo(yeah, hmmm); }\n" +
    "   public static FooBar Bar = new Bar();\n" +
    "\n" +
    "   public static interface Visitor<A> {\n" +
    "      A visit(Foo x);\n" +
    "      A visit(Bar x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n" +
    "      @Override\n" +
    "      public A visit(Foo x) { return getDefault(x); }\n" +
    "\n" +
    "      @Override\n" +
    "      public A visit(Bar x) { return getDefault(x); }\n" +
    "\n" +
    "      public abstract A getDefault(FooBar x);\n" +
    "   }\n" +
    "\n" +
    "   public static final class Foo extends FooBar {\n" +
    "      public final Integer yeah;\n" +
    "      public final String hmmm;\n" +
    "\n" +
    "      public Foo(Integer yeah, String hmmm) {\n" +
    "         this.yeah = yeah;\n" +
    "         this.hmmm = hmmm;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          final int prime = 31;\n" +
    "          int result = 1;\n" +
    "          result = prime * result + ((yeah == null) ? 0 : yeah.hashCode());\n" +
    "          result = prime * result + ((hmmm == null) ? 0 : hmmm.hashCode());\n" +
    "          return result;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         Foo other = (Foo)obj;\n" +
    "         if (yeah == null) {\n" +
    "            if (other.yeah != null) return false;\n" +
    "         } else if (!yeah.equals(other.yeah)) return false;\n" +
    "         if (hmmm == null) {\n" +
    "            if (other.hmmm != null) return false;\n" +
    "         } else if (!hmmm.equals(other.hmmm)) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(yeah = \" + yeah + \", hmmm = \" + hmmm + \")\";\n" +
    "      }\n" +
    "\n" +
    "   }\n" +
    "\n" +
    "   public static final class Bar extends FooBar {\n" +
    "\n" +
    "      public Bar() {\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          return 0;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Bar\";\n" +
    "      }\n" +
    "\n" +
    "   }\n" +
    "\n" +
    "   public abstract <A> A accept(Visitor<A> visitor);\n" +
    "\n" +
    "}";
    
    private static final String PRIMITIVE_NON_INT = 
    "public abstract class PrimitiveNonInt {\n" +
    "\n" +
    "   private PrimitiveNonInt() {\n" +
    "   }\n" +
    "\n" +
    "   public static final PrimitiveNonInt Foo(long yeah) { return new Foo(yeah); }\n" +
    "\n" +
    "   public static interface Visitor<A> {\n" +
    "      A visit(Foo x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n" +
    "      @Override\n" +
    "      public A visit(Foo x) { return getDefault(x); }\n" +
    "\n" +
    "      public abstract A getDefault(PrimitiveNonInt x);\n" +
    "   }\n" +
    "\n" +
    "   public static final class Foo extends PrimitiveNonInt {\n" +
    "      public final long yeah;\n" +
    "\n" +
    "      public Foo(long yeah) {\n" +
    "         this.yeah = yeah;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          final int prime = 31;\n" +
    "          int result = 1;\n" +
    "          result = prime * result + (int)yeah;\n" +
    "          return result;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         Foo other = (Foo)obj;\n" +
    "         if (yeah != other.yeah) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(yeah = \" + yeah + \")\";\n" +
    "      }\n" +
    "\n" +
    "   }\n" +
    "\n" +
    "   public abstract <A> A accept(Visitor<A> visitor);\n" +
    "\n" +
    "}";
    
    private static final String PRIMITIVE_INT = 
    "public abstract class PrimitiveInt {\n" +
    "\n" +
    "   private PrimitiveInt() {\n" +
    "   }\n" +
    "\n" +
    "   public static final PrimitiveInt Foo(int yeah) { return new Foo(yeah); }\n" +
    "\n" +
    "   public static interface Visitor<A> {\n" +
    "      A visit(Foo x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n" +
    "      @Override\n" +
    "      public A visit(Foo x) { return getDefault(x); }\n" +
    "\n" +
    "      public abstract A getDefault(PrimitiveInt x);\n" +
    "   }\n" +
    "\n" +
    "   public static final class Foo extends PrimitiveInt {\n" +
    "      public final int yeah;\n" +
    "\n" +
    "      public Foo(int yeah) {\n" +
    "         this.yeah = yeah;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          final int prime = 31;\n" +
    "          int result = 1;\n" +
    "          result = prime * result + yeah;\n" +
    "          return result;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         Foo other = (Foo)obj;\n" +
    "         if (yeah != other.yeah) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(yeah = \" + yeah + \")\";\n" +
    "      }\n" +
    "\n" +
    "   }\n" +
    "\n" +
    "   public abstract <A> A accept(Visitor<A> visitor);\n" +
    "\n" +
    "}";
        
    private static final String WHATEVER =
    "public abstract class Whatever {\n" +
    "\n" +
    "   private Whatever() {\n" +
    "   }\n" +
    "\n" +
    "   public static Whatever Whatever = new Whatever();\n" +
    "\n" +
    "   public static interface Visitor<A> {\n" +
    "      A visit(Whatever x);\n" +
    "   }\n" +
    "\n" +
    "   public static abstract class VisitorWithDefault<A> implements Visitor<A> {\n" +
    "      @Override\n" +
    "      public A visit(Whatever x) { return getDefault(x); }\n" +
    "\n" +
    "      public abstract A getDefault(Whatever x);\n" +
    "   }\n" +
    "\n" +
    "   public static final class Whatever extends Whatever {\n" +
    "\n" +
    "      public Whatever() {\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          return 0;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Whatever\";\n" +
    "      }\n" +
    "\n" +
    "   }\n" +
    "\n" +
    "   public abstract <A> A accept(Visitor<A> visitor);\n" +
    "\n" +
    "}";

    @Test
    public void testNoArg() throws IOException {
        final DataType whatever = new DataType("Whatever", list(
                        new Constructor("Whatever", Util.<Arg>list())
                ));
        
        final Map<String, String> results = new HashMap<String, String>();
        final StringTarget target = new StringTarget("Whatever", results);
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter();
            
            emitter.emit(target,  whatever, HEADER);
        } finally {
            target.close();            
        }
        final String result = results.get("Whatever");
        assertEquals(HEADER+WHATEVER, result);
    }

    @Test
    public void testArgs() throws IOException {
        final DataType fooBar =
                new DataType("FooBar", list(
                        new Constructor("Foo", list(
                                new Arg("Integer", "yeah"),
                                new Arg("String", "hmmm")
                        )),
                        new Constructor("Bar", Util.<Arg>list())
                ));
        
        final Map<String, String> results = new HashMap<String, String>();
        final StringTarget target = new StringTarget("FooBar", results);
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter();
            
            emitter.emit(target,  fooBar, HEADER);
        } finally {
            target.close();            
        }
        final String result = results.get("FooBar");
        assertEquals(HEADER+FOOBAR, result);
    }
    
    @Test
    public void testPrimitiveNonInt() throws IOException {
        final DataType dataType =
                new DataType("PrimitiveNonInt", list(
                        new Constructor("Foo", list(
                                new Arg("long", "yeah")
                        ))
                ));
        
        final Map<String, String> results = new HashMap<String, String>();
        final StringTarget target = new StringTarget("PrimitiveNonInt", results);
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter();
            
            emitter.emit(target,  dataType, HEADER);
        } finally {
            target.close();            
        }
        final String result = results.get("PrimitiveNonInt");
        assertEquals(HEADER+PRIMITIVE_NON_INT, result);
    }
    
    @Test
    public void testPrimitiveInt() throws IOException {
        final DataType dataType =
                new DataType("PrimitiveInt", list(
                        new Constructor("Foo", list(
                                new Arg("int", "yeah")
                        ))
                ));
        
        final Map<String, String> results = new HashMap<String, String>();
        final StringTarget target = new StringTarget("PrimitiveInt", results);
        try {
            final DataTypeEmitter emitter = new StandardDataTypeEmitter();
            
            emitter.emit(target,  dataType, HEADER);
        } finally {
            target.close();            
        }
        final String result = results.get("PrimitiveInt");
        assertEquals(HEADER+PRIMITIVE_INT, result);
    }
    
}
