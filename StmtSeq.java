import java.util.Map;

/**
 * Represents a sequence of statements.
 */
public class StmtSeq {
    public static int indentLevel = 0;
    public static boolean inLocalScope = false;

    public Stmt stmt;
    private StmtSeq nextStmtSeq;

    public StmtSeq() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.END) return;

        stmt = new Stmt();
        stmt.parse(scanner, idMap);

        Core next = scanner.currentToken();
        if (next == Core.ID || next == Core.IF || next == Core.FOR || next == Core.PRINT ||
            next == Core.READ || next == Core.INTEGER || next == Core.OBJECT || next == Core.BEGIN) {
            nextStmtSeq = new StmtSeq();
            nextStmtSeq.parse(scanner, idMap);
        }
    }

    public void print() {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("    ");
        }
        stmt.print();
        if (nextStmtSeq != null) nextStmtSeq.print();
    }

    public static void increaseIndent() {
        indentLevel++;
    }

    public static void decreaseIndent() {
        if (indentLevel > 0) indentLevel--;
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        execute(data, memory, FunctionMap.getFuncMap());
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (stmt == null) {
            System.out.println("ERROR: procedure body missing (no stmt-seq)");
            System.exit(1);
        }
        stmt.execute(data, memory, funcMap);
        if (nextStmtSeq != null) {
            nextStmtSeq.execute(data, memory, funcMap);
        }
    }
}
