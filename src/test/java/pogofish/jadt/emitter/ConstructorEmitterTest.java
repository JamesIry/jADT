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
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.util.Util;


public class ConstructorEmitterTest {
    private static final String NON_PRIMITIVE = 
    "   public static final class Foo extends NonPrimitive {\n" +
    "      public final String um;\n" +
    "      public final int yeah;\n" +
    "\n" +
    "      public Foo(String um, int yeah) {\n" +
    "         this.um = um;\n" +
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
    "          result = prime * result + ((um == null) ? 0 : um.hashCode());\n" +
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
    "         if (um == null) {\n" +
    "            if (other.um != null) return false;\n" +
    "         } else if (!um.equals(other.um)) return false;\n" +
    "         if (yeah != other.yeah) return false;\n" +
    "         return true;\n" +
    "      }\n" +
    "\n" +
    "      @Override\n" +
    "      public String toString() {\n" +
    "         return \"Foo(um = \" + um + \", yeah = \" + yeah + \")\";\n" +
    "      }\n" +
    "\n" +
    "   }";
    
    private static final String PRIMITIVE_NON_INT = 
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
    "   }";
    
    private static final String PRIMITIVE_INT = 
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
    "   }";
        
    private static final String NO_ARG_FACTORY =
    "   public static WhateverDataType _Whatever = new Whatever();";
    
    private static final String ARGS_FACTORY = 
    "   public static final FooBar _Foo(Integer yeah, String hmmm) { return new Foo(yeah, hmmm); }";    
    
    @Test
    public void testNoArgFactory() {
        final Constructor constructor = new Constructor("Whatever", Util.<Arg>list());
        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new StandardClassBodyEmitter());
            emitter.constructorFactory(target, "WhateverDataType", constructor);
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
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new StandardClassBodyEmitter());
            
            emitter.constructorFactory(target, "FooBar", constructor);
        } finally {
            target.close();            
        }
        assertEquals(ARGS_FACTORY, target.result());
    }
    
    @Test
    public void testPrimitiveNonInt() {
        final Constructor constructor = new Constructor("Foo", list(
                                new Arg(_Primitive(_LongType), "yeah")
                        ));
        
        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new StandardClassBodyEmitter());
            
            emitter.constructorDeclaration(target, constructor, "PrimitiveNonInt");
        } finally {
            target.close();            
        }
        assertEquals(PRIMITIVE_NON_INT, target.result());
    }
    
    @Test
    public void testPrimitiveInt() {
        final Constructor constructor = new Constructor("Foo", list(new Arg(_Primitive(_IntType), "yeah")));

        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new StandardClassBodyEmitter());

            emitter.constructorDeclaration(target, constructor, "PrimitiveInt");
        } finally {
            target.close();
        }
        assertEquals(PRIMITIVE_INT, target.result());
    }
    
    @Test
    public void testNonPrimitive() {
        final Constructor constructor = new Constructor("Foo", list(new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "um"), new Arg(_Primitive(_IntType), "yeah")));

        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new StandardClassBodyEmitter());

            emitter.constructorDeclaration(target, constructor, "NonPrimitive");
        } finally {
            target.close();
        }
        assertEquals(NON_PRIMITIVE, target.result());
    }
    
}
