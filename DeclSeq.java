import java.util.Map;

/**
 * Represents a sequence of declarations and/or function definitions.
 */
public class DeclSeq {
    private Decl declaration;
    private Function function;
    private DeclSeq nextDeclSeq;
    private boolean isFunction = false;

    public DeclSeq() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        Core token = scanner.currentToken();

        if (token == Core.PROCEDURE) {
            isFunction = true;
            function = new Function();
            function.parse(scanner, idMap, FunctionMap.getFuncMap());
            FunctionMap.addFunction(function.funcName, function);
        } else {
            declaration = new Decl();
            declaration.parse(scanner, idMap);
        }

        // Parse additional declarations or functions recursively
        token = scanner.currentToken();
        if (token == Core.INTEGER || token == Core.OBJECT || token == Core.PROCEDURE) {
            nextDeclSeq = new DeclSeq();
            nextDeclSeq.parse(scanner, idMap);
        }
    }

    public void print() {
        printIndent(StmtSeq.indentLevel);

        if (isFunction) {
            function.print();
        } else {
            declaration.print(StmtSeq.indentLevel);
        }

        if (nextDeclSeq != null) {
            nextDeclSeq.print();
        }
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        if (!isFunction) {
            declaration.execute(data, memory);
        }

        if (nextDeclSeq != null) {
            nextDeclSeq.execute(data, memory);
        }
    }

    private void printIndent(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("    ");
        }
    }
}
