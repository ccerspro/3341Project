import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a procedure (function) with parameters and a statement body.
 */
public class Function {
    public String funcName;
    public List<String> parameters = new ArrayList<>();
    private final Map<String, String> localIdMap = new HashMap<>();
    private StmtSeq stmtSeq;

    public Function() {}

    public void parse(Scanner scanner, Map<String, String> idMap, Map<String, Function> funcMap) {
        expect(scanner, Core.PROCEDURE, "expected 'procedure'");
        scanner.nextToken();

        expect(scanner, Core.ID, "expected procedure name");
        funcName = scanner.getId();
        if (funcMap.containsKey(funcName)) {
            error("Procedure name '" + funcName + "' already defined");
        }
        scanner.nextToken();

        expect(scanner, Core.LPAREN, "expected '(' after procedure name");
        scanner.nextToken();

        expect(scanner, Core.OBJECT, "expected 'object' keyword before parameter list");
        scanner.nextToken();

        parseParameters(scanner);

        expect(scanner, Core.RPAREN, "expected ')' after parameter list");
        scanner.nextToken();

        expect(scanner, Core.IS, "expected 'is'");
        scanner.nextToken();

        stmtSeq = new StmtSeq();
        stmtSeq.parse(scanner, localIdMap);

        if (stmtSeq.stmt == null) {
            error("procedure body missing (no stmt-seq)");
        }

        expect(scanner, Core.END, "expected 'end'");
        scanner.nextToken();
    }

    private void parseParameters(Scanner scanner) {
        expect(scanner, Core.ID, "expected parameter name");
        String paramName = scanner.getId();
        addParameter(paramName);
        scanner.nextToken();

        while (scanner.currentToken() == Core.COMMA) {
            scanner.nextToken();
            expect(scanner, Core.ID, "expected parameter name after comma");
            paramName = scanner.getId();
            if (parameters.contains(paramName)) {
                error("Duplicate parameter name '" + paramName + "'");
            }
            addParameter(paramName);
            scanner.nextToken();
        }
    }

    private void addParameter(String name) {
        parameters.add(name);
        localIdMap.put(name, "object");
    }

    public void print() {
        System.out.print("procedure " + funcName + " (object ");
        for (int i = 0; i < parameters.size(); i++) {
            System.out.print(parameters.get(i));
            if (i < parameters.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(") is");

        StmtSeq.increaseIndent();
        stmtSeq.print();
        StmtSeq.decreaseIndent();

        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
        System.out.println("end");
    }

    public void execute(Scanner data, Map<String, int[]> memory, List<String> arguments, Map<String, Function> funcMap) {
        Map<String, int[]> localMemory = new HashMap<>();

        for (int i = 0; i < parameters.size(); i++) {
            localMemory.put(parameters.get(i), memory.get(arguments.get(i)));
        }

        stmtSeq.execute(data, localMemory, funcMap);
    }

    private void expect(Scanner scanner, Core expected, String message) {
        if (scanner.currentToken() != expected) {
            error(message);
        }
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }

    public String getName() {
        return funcName;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
