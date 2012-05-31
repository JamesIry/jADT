/*
Copyright 2012 James Iry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package pogofish.jadt.emitter;

import static org.junit.Assert.assertEquals;
import static pogofish.jadt.ast.PrimitiveType._BooleanType;
import static pogofish.jadt.ast.PrimitiveType._IntType;
import static pogofish.jadt.ast.PrimitiveType._LongType;
import static pogofish.jadt.ast.RefType._ArrayType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.Arg;
import pogofish.jadt.ast.Constructor;
import pogofish.jadt.ast.RefType;
import pogofish.jadt.target.StringTarget;
import pogofish.jadt.util.Util;

/**
 * Test the StandardClassBodyEmitter
 *
 * @author jiry
 */
public class ClassBodyEmitterTest {
          
    private static final String NO_ARG_NO_TYPES_FACTORY =
    "   private static final SomeDataType _SomeFactory = new Whatever();\n" +
    "   public static final  SomeDataType _SomeFactory() { return _SomeFactory; }";
    
    private static final String NO_ARG_TYPES_FACTORY =
    "   @SuppressWarnings(\"rawtypes\")\n" +
    "   private static final SomeDataType _SomeFactory = new Whatever();\n" +
    "   @SuppressWarnings(\"unchecked\")\n" +
    "   public static final <A, B> SomeDataType<A, B> _SomeFactory() { return _SomeFactory; }";
    
    
    private static final String ARGS_NO_TYPES_FACTORY = 
    "   public static final  SomeDataType _SomeFactory(Integer yeah, String hmmm) { return new Foo(yeah, hmmm); }";    
    
    private static final String ARGS_TYPES_FACTORY = 
    "   public static final <A, B> SomeDataType<A, B> _SomeFactory(Integer yeah, String hmmm) { return new Foo<A, B>(yeah, hmmm); }";    
    
    private static final String CONSTRUCTOR_METHOD = 
    "      public final String um;\n" +
    "      public final int yeah;\n" +
    "\n" +
    "      public Foo(String um, int yeah) {\n" +
    "         this.um = um;\n" +
    "         this.yeah = yeah;\n" +
    "      }";

    private static final String NO_ARG_TO_STRING =
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Whatever\";\n" +
    "      }";

    private static final String ONE_ARG_TO_STRING =
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(um = \" + um + \")\";\n" +
    "      }";

    private static final String ARGS_TO_STRING =
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(um = \" + um + \", yeah = \" + yeah + \")\";\n" +
    "      }";

    private static final String NO_ARG_EQUALS =
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         return true;\n" +
    "      }";

    private static final String ARGS_NO_TYPES_EQUALS =
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         Foo other = (Foo)obj;\n" +
    "         if (um != other.um) return false;\n" +
    "         if (yeah == null) {\n" +
    "            if (other.yeah != null) return false;\n" +
    "         } else if (!yeah.equals(other.yeah)) return false;\n" +
    "         if (!java.util.Arrays.equals(oh, other.oh)) return false;\n" +
    "         return true;\n" +
    "      }";

    private static final String ARGS_TYPES_EQUALS =
    "      @Override\n" +
    "      public boolean equals(Object obj) {\n" +
    "         if (this == obj) return true;\n" +
    "         if (obj == null) return false;\n" +
    "         if (getClass() != obj.getClass()) return false;\n" +
    "         @SuppressWarnings(\"rawtypes\")" +
    "         Foo other = (Foo)obj;\n" +
    "         if (um != other.um) return false;\n" +
    "         if (yeah == null) {\n" +
    "            if (other.yeah != null) return false;\n" +
    "         } else if (!yeah.equals(other.yeah)) return false;\n" +
    "         if (!java.util.Arrays.equals(oh, other.oh)) return false;\n" +
    "         return true;\n" +
    "      }";

    private static final String NO_ARG_HASHCODE =
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          return 0;\n" +
    "      }";

    private static final String ARGS_HASHCODE =
    "      @Override\n" +
    "      public int hashCode() {\n" +
    "          final int prime = 31;\n" +
    "          int result = 1;\n" +
    "          result = prime * result + um;\n" +
    "          result = prime * result + (bool ? 1 : 0);\n" +
    "          result = prime * result + (int)shorty;\n" +
    "          result = prime * result + ((yeah == null) ? 0 : yeah.hashCode());\n" +
    "          result = prime * result + java.util.Arrays.hashCode(oh);\n" +
    "          return result;\n" +
    "      }";
    
    private final ClassBodyEmitter emitter = new StandardClassBodyEmitter();
    

    /**
     * Make sure non parameterized type names are handled properly
     */
    public void testNonParameterizedTypeName() {
        final StringTarget target = new StringTarget();
        try {
            emitter.emitParameterizedTypeName(target, Util.<String>list());
        } finally {
            target.close();            
        }
        assertEquals("", target.result());
    }
    
    /**
     * Make sure parameterized type names with one type parameter are handled properly
     */
    public void testOneParameterizedTypeName() {
        final StringTarget target = new StringTarget();
        try {
            emitter.emitParameterizedTypeName(target, list("A"));
        } finally {
            target.close();            
        }
        assertEquals("<A>", target.result());
	
	}
    
    /**
     * Make sure parameterized type names with multiple type parameters are handled properly
     */
    public void testMultiParameterizedTypeName() {
        final StringTarget target = new StringTarget();
        try {
            emitter.emitParameterizedTypeName(target, list("A", "B", "C"));
        } finally {
            target.close();            
        }
        assertEquals("<A, B, C>", target.result());
	
	}
    
    /**
     * If a factory has no args and no type parameters it should be a simple constant
     */
    @Test
    public void testNoArgNoTypesFactory() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        final StringTarget target = new StringTarget();
        try {
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", Util.<String>list(), constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_NO_TYPES_FACTORY, target.result());
    }

    /**
     * If a factory has no args but does have type parameters it should be a constant with some warning suppression
     */
    @Test
    public void testNoArgTypesFactory() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        final StringTarget target = new StringTarget();
        try {
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", list("A", "B"), constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_TYPES_FACTORY, target.result());
    }

    /**
     * If a factory has args but not types then it must be a simple method
     */
    @Test
    public void testArgsNoTypesFactory() {
        final Constructor constructor = new Constructor("Foo", list(
                                new Arg(_Ref(_ClassType("Integer", Util.<RefType>list())), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", Util.<String>list(), constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_NO_TYPES_FACTORY, target.result());
    }

    /**
     * If a factory has args and types then it must be a slightly more complicated method
     */
    @Test
    public void testArgsTypesFactory() {
        final Constructor constructor = new Constructor("Foo", list(
                                new Arg(_Ref(_ClassType("Integer", Util.<RefType>list())), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", list("A", "B"), constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_TYPES_FACTORY, target.result());
    }    
    /**
     * How does the Java class constructor look?
     */
    @Test
    public void testConstructorMethod() {
        final Constructor constructor = new Constructor("Foo",
                list(new Arg(_Ref(_ClassType("String", Util.<RefType> list())), "um"), new Arg(_Primitive(_IntType()),
                        "yeah")));

        final StringTarget target = new StringTarget();
        try {
            emitter.emitConstructorMethod(target, constructor);
        } finally {
            target.close();
        }
        assertEquals(CONSTRUCTOR_METHOD, target.result());
    }
        
    /**
     * toString with no args
     */
    @Test
    public void testNoArgToString() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitToString(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_TO_STRING, target.result());         
    }
    
    /**
     * toString with one arg
     */
    @Test
    public void testOneArgToString() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Ref(_ClassType("Integer", Util.<RefType>list())), "um")
        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitToString(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(ONE_ARG_TO_STRING, target.result());         
    }
    
    /**
     * toString with multiple args
     */
    @Test
    public void testArgsToString() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Ref(_ClassType("Integer", Util.<RefType>list())), "um"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah")
        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitToString(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_TO_STRING, target.result());         
    }
    
    /**
     * equals with no args
     */
    @Test
    public void testNoArgsNoTypesEquals() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitEquals(target, constructor, Util.<String>list());
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_EQUALS, target.result());                 
    }
    
    
    /**
     * equals with args and no types
     */
    @Test
    public void testArgsNoTypesEquals() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Primitive(_IntType()), "um"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(_Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitEquals(target, constructor, Util.<String>list());
        } finally {
            target.close();            
        }
        assertEquals(ARGS_NO_TYPES_EQUALS, target.result());                 
    }
    
    /**
     * equals with args and types
     */
    @Test
    public void testArgsTypesEquals() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Primitive(_IntType()), "um"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(_Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitEquals(target, constructor, list("A", "B"));
        } finally {
            target.close();            
        }
        assertEquals(ARGS_TYPES_EQUALS, target.result());                 
    }
    
    /**
     * hashCode with no args
     */
    @Test
    public void testNoArgHashCode() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitHashCode(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_HASHCODE, target.result());                 
        
    }
    
    /**
     * hashCode with args
     */
    @Test
    public void testArgHashCode() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Primitive(_IntType()), "um"),
                new Arg(_Primitive(_BooleanType()), "bool"),
                new Arg(_Primitive(_LongType()), "shorty"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(_Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringTarget target = new StringTarget();
        try {            
            emitter.emitHashCode(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_HASHCODE, target.result());                 
        
    }    
 }
