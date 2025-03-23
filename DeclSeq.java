import java.util.Map;

public class DeclSeq {
    Decl declaration;
    Function function;
    DeclSeq nextDeclSeq;
    boolean isFunction = false;

    DeclSeq() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.PROCEDURE) {
            isFunction = true;
            function = new Function();
            function.parse(scanner, idMap, FunctionMap.getFuncMap());
            // Fixed: Changed function.name to function.funcName
            FunctionMap.addFunction(function.funcName, function);
        } else {
            declaration = new Decl();
            declaration.parse(scanner, idMap);
        }
        
        if (scanner.currentToken() == Core.INTEGER || scanner.currentToken() == Core.OBJECT || scanner.currentToken() == Core.PROCEDURE) {
            nextDeclSeq = new DeclSeq();
            nextDeclSeq.parse(scanner, idMap); 
        }
    }

    void print() {
        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
        
        if (isFunction) {
            function.print();
        } else {
            declaration.print(StmtSeq.indentLevel);
        }
        
        if (nextDeclSeq != null) {
            nextDeclSeq.print();
        }
    }

    void execute(Scanner data, Map<String, int[]> memory) {
        if (isFunction) {
            // Functions are not executed directly, they are called
        } else {
            declaration.execute(data, memory);
        }
        
        if (nextDeclSeq != null) {
            nextDeclSeq.execute(data, memory);
        }
    }
}