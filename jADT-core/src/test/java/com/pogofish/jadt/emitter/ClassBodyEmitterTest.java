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
package com.pogofish.jadt.emitter;

import static com.pogofish.jadt.ast.ASTConstants.NO_COMMENTS;
import static com.pogofish.jadt.ast.JDTagSection._JDTagSection;
import static com.pogofish.jadt.ast.JDToken._JDAsterisk;
import static com.pogofish.jadt.ast.JDToken._JDEOL;
import static com.pogofish.jadt.ast.JDToken._JDTag;
import static com.pogofish.jadt.ast.JDToken._JDWhiteSpace;
import static com.pogofish.jadt.ast.JDToken._JDWord;
import static com.pogofish.jadt.ast.JavaComment._JavaDocComment;
import static com.pogofish.jadt.ast.PrimitiveType._BooleanType;
import static com.pogofish.jadt.ast.PrimitiveType._ByteType;
import static com.pogofish.jadt.ast.PrimitiveType._CharType;
import static com.pogofish.jadt.ast.PrimitiveType._DoubleType;
import static com.pogofish.jadt.ast.PrimitiveType._FloatType;
import static com.pogofish.jadt.ast.PrimitiveType._IntType;
import static com.pogofish.jadt.ast.PrimitiveType._LongType;
import static com.pogofish.jadt.ast.PrimitiveType._ShortType;
import static com.pogofish.jadt.ast.RefType._ArrayType;
import static com.pogofish.jadt.ast.RefType._ClassType;
import static com.pogofish.jadt.ast.Type._Primitive;
import static com.pogofish.jadt.ast.Type._Ref;
import static com.pogofish.jadt.util.Util.list;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pogofish.jadt.ast.Arg;
import com.pogofish.jadt.ast.ArgModifier;
import com.pogofish.jadt.ast.Constructor;
import com.pogofish.jadt.ast.RefType;
import com.pogofish.jadt.sink.StringSink;
import com.pogofish.jadt.util.Util;


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
    "      /**\n" +
    "       * this is the um\n" +
    "       */\n" +
    "      public String um;\n" +
    "      public final int yeah;\n" +
    "\n" +
    "      /**\n" +
    "       * @param um this is the um\n" +
    "       */\n" +
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
    "          result = prime * result + (boolShit ? 1 : 0);\n" +
    "          result = prime * result + dracula;\n" +
    "          result = prime * result + whatACharacter;\n" +
    "          result = prime * result + shorty;\n" +
    "          result = prime * result + integrity;\n" +
    "          result = prime * result + (int)likeYourMomLikesIt;\n" +
    "          result = prime * result + (int)turd;\n" +
    "          result = prime * result + (int)yourPleasure;\n" +
    "          result = prime * result + ((yeah == null) ? 0 : yeah.hashCode());\n" +
    "          result = prime * result + java.util.Arrays.hashCode(oh);\n" +
    "          return result;\n" +
    "      }";
    
    private final ClassBodyEmitter emitter = new StandardClassBodyEmitter();
    

    /**
     * Make sure non parameterized type names are handled properly
     */
    public void testNonParameterizedTypeName() {
        final StringSink sink = new StringSink("test");
        try {
            emitter.emitParameterizedTypeName(sink, Util.<String>list());
        } finally {
            sink.close();            
        }
        assertEquals("", sink.result());
    }
    
    /**
     * Make sure parameterized type names with one type parameter are handled properly
     */
    public void testOneParameterizedTypeName() {
        final StringSink sink = new StringSink("test");
        try {
            emitter.emitParameterizedTypeName(sink, list("A"));
        } finally {
            sink.close();            
        }
        assertEquals("<A>", sink.result());
	
	}
    
    /**
     * Make sure parameterized type names with multiple type parameters are handled properly
     */
    public void testMultiParameterizedTypeName() {
        final StringSink sink = new StringSink("test");
        try {
            emitter.emitParameterizedTypeName(sink, list("A", "B", "C"));
        } finally {
            sink.close();            
        }
        assertEquals("<A, B, C>", sink.result());
	
	}
    
    /**
     * If a factory has no args and no type parameters it should be a simple constant
     */
    @Test
    public void testNoArgNoTypesFactory() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list());
        final StringSink sink = new StringSink("test");
        try {
            emitter.constructorFactory(sink, "SomeDataType", "SomeFactory", Util.<String>list(), constructor);
        } finally {
            sink.close();            
        }
        assertEquals(NO_ARG_NO_TYPES_FACTORY, sink.result());
    }

    /**
     * If a factory has no args but does have type parameters it should be a constant with some warning suppression
     */
    @Test
    public void testNoArgTypesFactory() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list());
        final StringSink sink = new StringSink("test");
        try {
            emitter.constructorFactory(sink, "SomeDataType", "SomeFactory", list("A", "B"), constructor);
        } finally {
            sink.close();            
        }
        assertEquals(NO_ARG_TYPES_FACTORY, sink.result());
    }

    /**
     * If a factory has args but not types then it must be a simple method
     */
    @Test
    public void testArgsNoTypesFactory() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType>list())), "yeah"),
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.constructorFactory(sink, "SomeDataType", "SomeFactory", Util.<String>list(), constructor);
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_NO_TYPES_FACTORY, sink.result());
    }

    /**
     * If a factory has args and types then it must be a slightly more complicated method
     */
    @Test
    public void testArgsTypesFactory() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType>list())), "yeah"),
                                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "hmmm")
                        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.constructorFactory(sink, "SomeDataType", "SomeFactory", list("A", "B"), constructor);
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_TYPES_FACTORY, sink.result());
    }    
    /**
     * How does the Java class constructor look?
     */
    @Test
    public void testConstructorMethod() {
        final Constructor constructor = new Constructor(list(_JavaDocComment("/**", list(_JDEOL("\n")), list(_JDTagSection("@param", list(_JDWhiteSpace(" "), _JDAsterisk(), _JDWhiteSpace(" "), _JDTag("@param"), _JDWhiteSpace(" "), _JDWord("um"), _JDWhiteSpace(" "), _JDWord("this is the um"), _JDEOL("\n"), _JDWhiteSpace(" ")))), "*/")), "Foo",
                list(new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType> list())), "um"), new Arg(Util.list(ArgModifier._Final()), _Primitive(_IntType()),
                        "yeah")));

        final StringSink sink = new StringSink("test");
        try {
            emitter.emitConstructorMethod(sink, constructor);
        } finally {
            sink.close();
        }
        assertEquals(CONSTRUCTOR_METHOD, sink.result());
    }
        
    /**
     * toString with no args
     */
    @Test
    public void testNoArgToString() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list());
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitToString(sink, constructor);
        } finally {
            sink.close();            
        }
        assertEquals(NO_ARG_TO_STRING, sink.result());         
    }
    
    /**
     * toString with one arg
     */
    @Test
    public void testOneArgToString() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType>list())), "um")
        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitToString(sink, constructor);
        } finally {
            sink.close();            
        }
        assertEquals(ONE_ARG_TO_STRING, sink.result());         
    }
    
    /**
     * toString with multiple args
     */
    @Test
    public void testArgsToString() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("Integer", Util.<RefType>list())), "um"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "yeah")
        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitToString(sink, constructor);
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_TO_STRING, sink.result());         
    }
    
    /**
     * equals with no args
     */
    @Test
    public void testNoArgsNoTypesEquals() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list());
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitEquals(sink, constructor, Util.<String>list());
        } finally {
            sink.close();            
        }
        assertEquals(NO_ARG_EQUALS, sink.result());                 
    }
    
    
    /**
     * equals with args and no types
     */
    @Test
    public void testArgsNoTypesEquals() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "um"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitEquals(sink, constructor, Util.<String>list());
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_NO_TYPES_EQUALS, sink.result());                 
    }
    
    /**
     * equals with args and types
     */
    @Test
    public void testArgsTypesEquals() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "um"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitEquals(sink, constructor, list("A", "B"));
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_TYPES_EQUALS, sink.result());                 
    }
    
    /**
     * hashCode with no args
     */
    @Test
    public void testNoArgHashCode() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Whatever", Util.<Arg>list());
        
        final StringSink sink = new StringSink("test");
        try {
            
            emitter.emitHashCode(sink, constructor);
        } finally {
            sink.close();            
        }
        assertEquals(NO_ARG_HASHCODE, sink.result());                 
        
    }
    
    /**
     * hashCode with args
     */
    @Test
    public void testArgHashCode() {
        final Constructor constructor = new Constructor(NO_COMMENTS, "Foo", list(
                new Arg(Util.<ArgModifier>list(), _Primitive(_BooleanType()), "boolShit"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_ByteType()), "dracula"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_CharType()), "whatACharacter"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_ShortType()), "shorty"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_IntType()), "integrity"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_LongType()), "likeYourMomLikesIt"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_FloatType()), "turd"),
                new Arg(Util.<ArgModifier>list(), _Primitive(_DoubleType()), "yourPleasure"),
                               new Arg(Util.<ArgModifier>list(), _Ref(_ClassType("String", Util.<RefType>list())), "yeah"),
                new Arg(Util.<ArgModifier>list(), _Ref(_ArrayType(_Primitive(_IntType()))), "oh")
        ));
        
        final StringSink sink = new StringSink("test");
        try {            
            emitter.emitHashCode(sink, constructor);
        } finally {
            sink.close();            
        }
        assertEquals(ARGS_HASHCODE, sink.result());                 
        
    }    
 }
