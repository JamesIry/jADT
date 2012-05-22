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

import java.io.IOException;

import pogofish.jadt.ast.DataType;
import pogofish.jadt.ast.Doc;
import pogofish.jadt.printer.StandardPrinter;


public class StandardDocEmitter implements DocEmitter {    
    private final TargetFactory factory;
    private final DataTypeEmitter dataTypeEmitter;
    private final StandardPrinter printer;
        
    public StandardDocEmitter(TargetFactory factory, DataTypeEmitter dataTypeEmitter, StandardPrinter printer) {
        super();
        this.factory = factory;
        this.dataTypeEmitter = dataTypeEmitter;
        this.printer = printer;
    }

    /* (non-Javadoc)
     * @see sfdc.adt.emitter.Emitter#emit(sfdc.adt.ast.Doc, sfdc.adt.emitter.TargetFactory)
     */
    @Override
    public void emit(Doc doc) throws IOException {
        final StringBuilder header = new StringBuilder(doc.pkg.isEmpty() ? "" : ("package " + doc.pkg + ";\n\n"));
        if (!doc.imports.isEmpty()) {
            for (String imp : doc.imports) {
                header.append("import " + imp + ";\n");
            }
            header.append("\n");
        }
        header.append("/*\nThis file was generated based on " + doc.srcInfo + ". Please do not modify directly.\n\n");
        header.append("The source was parsed as: \n\n");
        header.append(printer.print(doc));
        header.append("\n*/\n");
        
        for (DataType dataType : doc.dataTypes) {
            Target target = factory.createTarget(doc.pkg.isEmpty() ? dataType.name : doc.pkg + "." + dataType.name);
            try {
                dataTypeEmitter.emit(target, dataType, header.toString());
            } finally {
                target.close();
            }
        }        
    }
}
