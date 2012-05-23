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
import static pogofish.jadt.ast.PrimitiveType._IntType;
import static pogofish.jadt.ast.PrimitiveType._LongType;
import static pogofish.jadt.ast.RefType._ArrayType;
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;


public class ClassBodyEmitterTest {
          
    private static final String NO_ARG_FACTORY =
    "   public static SomeDataType _SomeFactory = new Whatever();";
    
    private static final String ARGS_FACTORY = 
    "   public static final SomeDataType _SomeFactory(Integer yeah, String hmmm) { return new Foo(yeah, hmmm); }";    
    
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

    private static final String ARGS_EQUALS =
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
    "          result = prime * result + (int)shorty;\n" +
    "          result = prime * result + ((yeah == null) ? 0 : yeah.hashCode());\n" +
    "          result = prime * result + java.util.Arrays.hashCode(oh);\n" +
    "          return result;\n" +
    "      }";
    
    private final ClassBodyEmitter emitter = new StandardClassBodyEmitter();
    
    @Test
    public void testNoArgFactory() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        final StringTarget target = new StringTarget();
        try {
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_FACTORY, target.result());
    }

    @Test
    public void testArgsFactory() {
        final Constructor constructor = new Constructor("Foo", list(
                                new Arg(_Ref(_ClassType("Integer", Util.<RefType>list())), "yeah"),
                                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.constructorFactory(target, "SomeDataType", "SomeFactory", constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_FACTORY, target.result());
    }

    @Test
    public void testConstructorMethod() {
        final Constructor constructor = new Constructor("Foo",
                list(new Arg(_Ref(_ClassType("String", Util.<RefType> list())), "um"), new Arg(_Primitive(_IntType),
                        "yeah")));

        final StringTarget target = new StringTarget();
        try {
            emitter.emitConstructorMethod(target, constructor);
        } finally {
            target.close();
        }
        assertEquals(CONSTRUCTOR_METHOD, target.result());
    }
    
    
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
    
    @Test
    public void testNoArgsEquals() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitEquals(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(NO_ARG_EQUALS, target.result());                 
    }
    
    @Test
    public void testArgsEquals() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Primitive(_IntType), "um"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(_Ref(_ArrayType(_Primitive(_IntType))), "oh")
        ));
        
        final StringTarget target = new StringTarget();
        try {
            
            emitter.emitEquals(target, constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_EQUALS, target.result());                 
    }
    
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
    
    @Test
    public void testArgHashCode() {
        final Constructor constructor = new Constructor("Foo", list(
                new Arg(_Primitive(_IntType), "um"),
                new Arg(_Primitive(_LongType), "shorty"),
                new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(_Ref(_ArrayType(_Primitive(_IntType))), "oh")
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
