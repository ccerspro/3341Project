import java.util.HashMap;
import java.util.Map;

/**
 * Represents a for-loop with init, condition, increment, and body.
 */
public class Loop {
    private String loopVar;
    private Expr initExpr;
    private Cond loopCond;
    private Expr incrementExpr;
    private StmtSeq loopBody;
    private final Map<String, String> localIdMap = new HashMap<>();

    public Loop() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        localIdMap.putAll(idMap);
        scanner.nextToken(); // consume 'for'

        expect(scanner, Core.LPAREN, "expected '(' after for");
        scanner.nextToken();

        expect(scanner, Core.ID, "expected loop variable name");
        loopVar = scanner.getId();

        if (!idMap.containsKey(loopVar)) {
            semanticError("variable '" + loopVar + "' not declared");
        }
        scanner.nextToken();

        expect(scanner, Core.ASSIGN, "expected '='");
        scanner.nextToken();

        initExpr = new Expr();
        initExpr.parse(scanner, localIdMap);

        expect(scanner, Core.SEMICOLON, "expected ';'");
        scanner.nextToken();

        loopCond = new Cond();
        loopCond.parse(scanner, localIdMap);

        expect(scanner, Core.SEMICOLON, "expected ';'");
        scanner.nextToken();

        incrementExpr = new Expr();
        incrementExpr.parse(scanner, localIdMap);

        expect(scanner, Core.RPAREN, "expected ')'");
        scanner.nextToken();

        expect(scanner, Core.DO, "expected 'do'");
        scanner.nextToken();

        loopBody = new StmtSeq();
        loopBody.parse(scanner, localIdMap);

        expect(scanner, Core.END, "expected 'end'");
        scanner.nextToken();
    }

    public void print() {
        System.out.print("for (" + loopVar + " = ");
        initExpr.print();
        System.out.print("; ");
        loopCond.print();
        System.out.print("; ");
        incrementExpr.print();
        System.out.println(") do");

        StmtSeq.increaseIndent();
        loopBody.print();
        StmtSeq.decreaseIndent();

        printIndent();
        System.out.println("end");
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        int[] var = memory.computeIfAbsent(loopVar, k -> new int[1]);
        var[0] = initExpr.execute(data, memory);

        while (loopCond.execute(data, memory)) {
            loopBody.execute(data, memory, funcMap);
            var[0] = incrementExpr.execute(data, memory);
        }
    }

    private void expect(Scanner scanner, Core expected, String msg) {
        if (scanner.currentToken() != expected) {
            System.out.println("ERROR: " + msg);
            System.exit(1);
        }
    }

    private void semanticError(String msg) {
        System.out.println("Semantic Error: " + msg);
        System.exit(1);
    }

    private void printIndent() {
        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
    }
}
