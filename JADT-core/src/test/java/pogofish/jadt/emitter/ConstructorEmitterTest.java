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
import static pogofish.jadt.ast.RefType._ClassType;
import static pogofish.jadt.ast.Type._Primitive;
import static pogofish.jadt.ast.Type._Ref;
import static pogofish.jadt.util.Util.list;

import org.junit.Test;

import pogofish.jadt.ast.*;
import pogofish.jadt.target.StringTarget;
import pogofish.jadt.util.Util;

/**
 * Test the StandardConstructorEmitter
 *
 * @author jiry
 */
public class ConstructorEmitterTest {
    private static final String CONSTRUCTOR_CLASS = 
    "   public static final class Foo extends NonPrimitive {\n" +
    "/* constructor method Foo*/\n" +
    "\n" +
    "      @Override\n" +
    "      public <A> A accept(Visitor<A> visitor) { return visitor.visit(this); }\n" +
    "\n" +
    "      @Override\n" +
    "      public void accept(VoidVisitor visitor) { visitor.visit(this); }\n" +
    "\n" +
    "/* hashCode method Foo*/\n" +
    "\n" +
    "/* equals method Foo*/\n" +
    "\n" +
    "/* toString method Foo*/\n" +
    "\n" +
    "   }";

    
    private static final String FACTORY = 
    "/* constructor factory SomeDataType Foo Foo*/";    
    
    /**
     * Create a factory
     */
    @Test
    public void testFactory() {
        final Constructor constructor = new Constructor("Foo", list(new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "um"), new Arg(_Primitive(_IntType), "yeah")));

        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new DummyClassBodyEmitter());

            emitter.constructorFactory(target, "SomeDataType", constructor);
        } finally {
            target.close();
        }
        assertEquals(FACTORY, target.result());
    }
    
    /**
     * Create a constructor class
     */
    @Test
    public void testConstrucorDeclaration() {
        final Constructor constructor = new Constructor("Foo", list(new Arg(_Ref(_ClassType("String", Util.<RefType>list())), "um"), new Arg(_Primitive(_IntType), "yeah")));

        final StringTarget target = new StringTarget();
        try {
            final ConstructorEmitter emitter = new StandardConstructorEmitter(new DummyClassBodyEmitter());

            emitter.constructorDeclaration(target, constructor, "NonPrimitive");
        } finally {
            target.close();
        }
        assertEquals(CONSTRUCTOR_CLASS, target.result());
    }
}
