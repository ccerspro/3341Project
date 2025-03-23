import java.util.HashMap;
import java.util.Map;

/**
 * Represents an if-then[-else] control structure.
 */
public class If {
    private Cond condition;
    private StmtSeq thenStmtSeq;
    private StmtSeq elseStmtSeq;
    private boolean hasElse;
    private final Map<String, String> localIdMap = new HashMap<>();

    public If() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        localIdMap.putAll(idMap);
        scanner.nextToken(); // consume 'if'

        condition = new Cond();
        condition.parse(scanner, localIdMap);

        expect(scanner, Core.THEN, "expected 'then'");
        scanner.nextToken();

        thenStmtSeq = new StmtSeq();
        thenStmtSeq.parse(scanner, localIdMap);

        if (scanner.currentToken() == Core.ELSE) {
            hasElse = true;
            scanner.nextToken();
            elseStmtSeq = new StmtSeq();
            elseStmtSeq.parse(scanner, localIdMap);
        }

        expect(scanner, Core.END, "expected 'end'");
        scanner.nextToken();
    }

    public void print() {
        System.out.print("if ");
        condition.print();
        System.out.println(" then");

        StmtSeq.increaseIndent();
        thenStmtSeq.print();
        StmtSeq.decreaseIndent();

        if (hasElse) {
            printIndent();
            System.out.println("else");
            StmtSeq.increaseIndent();
            elseStmtSeq.print();
            StmtSeq.decreaseIndent();
        }

        printIndent();
        System.out.println("end");
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (condition.execute(data, memory)) {
            thenStmtSeq.execute(data, memory, funcMap);
        } else if (hasElse) {
            elseStmtSeq.execute(data, memory, funcMap);
        }
    }

    private void printIndent() {
        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
    }

    private void expect(Scanner scanner, Core expected, String msg) {
        if (scanner.currentToken() != expected) {
            System.out.println("ERROR: " + msg);
            System.exit(1);
        }
    }
}
