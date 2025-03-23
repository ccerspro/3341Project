import java.util.HashMap;
import java.util.Map;

public class If {
    Cond condition;
    StmtSeq thenStmtSeq;
    StmtSeq elseStmtSeq;
    boolean hasElse;
    Map<String, String> localIdMap = new HashMap<>();

    If() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        localIdMap.putAll(idMap);
        scanner.nextToken();
        condition = new Cond();
        condition.parse(scanner, localIdMap);
        if (scanner.currentToken() != Core.THEN) {
            System.out.println("ERROR: expected then");
            System.exit(1);
        }
        scanner.nextToken();
        thenStmtSeq = new StmtSeq();
        thenStmtSeq.parse(scanner, localIdMap);
        if (scanner.currentToken() == Core.ELSE) {
            hasElse = true;
            scanner.nextToken();
            elseStmtSeq = new StmtSeq();
            elseStmtSeq.parse(scanner, localIdMap);
        }
        if (scanner.currentToken() != Core.END) {
            System.out.println("ERROR: expected end");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
        System.out.print("if ");
        condition.print();
        System.out.println(" then");
        StmtSeq.increaseIndent();
        thenStmtSeq.print();
        StmtSeq.decreaseIndent();
        if (hasElse) {
            for (int i = 0; i < StmtSeq.indentLevel; i++) {
                System.out.print("    ");
            }
            System.out.println("else");
            StmtSeq.increaseIndent();
            elseStmtSeq.print();
            StmtSeq.decreaseIndent();
        }
        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
        System.out.println("end");
    }

    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (condition.execute(data, memory)) {
            thenStmtSeq.execute(data, memory, funcMap);
        } else if (hasElse) {
            elseStmtSeq.execute(data, memory, funcMap);
        }
    }
}