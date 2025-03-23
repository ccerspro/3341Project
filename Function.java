import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function {
    String funcName;
    List<String> parameters = new ArrayList<>();
    StmtSeq stmtSeq;
    Map<String, String> localIdMap = new HashMap<>();

    Function() {}

    void parse(Scanner scanner, Map<String, String> idMap, Map<String, Function> funcMap) {
        if (scanner.currentToken() != Core.PROCEDURE) {
            System.out.println("ERROR: expected procedure");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected procedure name");
            System.exit(1);
        }
        funcName = scanner.getId();
        
        if (funcMap.containsKey(funcName)) {
            System.out.println("Semantic Error: Procedure name '" + funcName + "' already defined");
            System.exit(1);
        }
        
        scanner.nextToken();

        if (scanner.currentToken() != Core.LPAREN) {
            System.out.println("ERROR: expected (");
            System.exit(1);
        }
        scanner.nextToken();
        
        if (scanner.currentToken() != Core.OBJECT) {
            System.out.println("ERROR: expected object keyword for parameter type");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected parameter name");
            System.exit(1);
        }
        
        String paramName = scanner.getId();
        parameters.add(paramName);
        localIdMap.put(paramName, "object");
        scanner.nextToken();
        
        while (scanner.currentToken() == Core.COMMA) {
            scanner.nextToken();
            if (scanner.currentToken() != Core.ID) {
                System.out.println("ERROR: expected parameter name after comma");
                System.exit(1);
            }
            paramName = scanner.getId();
            
            if (parameters.contains(paramName)) {
                System.out.println("Semantic Error: Duplicate parameter name '" + paramName + "'");
                System.exit(1);
            }
            
            parameters.add(paramName);
            localIdMap.put(paramName, "object");
            scanner.nextToken();
        }
        
        if (scanner.currentToken() != Core.RPAREN) {
            System.out.println("ERROR: expected )");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.IS) {
            System.out.println("ERROR: expected is");
            System.exit(1);
        }
        scanner.nextToken();

        stmtSeq = new StmtSeq();
        stmtSeq.parse(scanner, localIdMap);
        
        // Fixed: Changed stmtSeq.st to stmtSeq.stmt
        if (stmtSeq.stmt == null) {
            System.out.println("ERROR: procedure body missing (no stmt-seq)");
            System.exit(1);
        }

        if (scanner.currentToken() != Core.END) {
            System.out.println("ERROR: expected end");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
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

    void execute(Scanner data, Map<String, int[]> memory, List<String> arguments, Map<String, Function> funcMap) {
        Map<String, int[]> localMemory = new HashMap<>();
        
        for (int i = 0; i < parameters.size(); i++) {
            localMemory.put(parameters.get(i), memory.get(arguments.get(i)));
        }
        
        stmtSeq.execute(data, localMemory, funcMap);
    }
}