package pogofish.jadt.checker;

import java.util.HashSet;
import java.util.Set;

import pogofish.jadt.ast.*;

public class StandardChecker implements Checker {
    @Override
    public Set<SemanticException> check(Doc doc) {
        final Set<SemanticException> errors = new HashSet<SemanticException>();
        final Set<String> dataTypeNames = new HashSet<String>();
        if (doc.dataTypes.isEmpty()) {
            errors.add(new MissingDataTypesException(doc));
        } else {
            for (DataType dataType : doc.dataTypes) {
                errors.addAll(check(dataType));
                if (dataTypeNames.contains(dataType.name)) {
                    errors.add(new DuplicateDataTypeException(dataType));
                } else {
                    dataTypeNames.add(dataType.name);
                }
            }
        }
        return errors;
    }

    private Set<SemanticException> check(DataType dataType) {
        final Set<SemanticException> errors = new HashSet<SemanticException>();
        final Set<String> constructorNames = new HashSet<String>();
        if (dataType.constructors.size() > 1) {
            for(Constructor constructor : dataType.constructors) {
                if (dataType.name.equals(constructor.name)) {
                    errors.add(new ConstructorDataTypeConflictException(dataType, constructor));
                }
                if (constructorNames.contains(constructor.name)) {
                    errors.add(new DuplicateConstructorException(dataType, constructor));
                } else {
                    constructorNames.add(constructor.name);
                }
            }
        }
        return errors;
    }

}
