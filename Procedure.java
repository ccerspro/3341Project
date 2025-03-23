import java.util.HashMap;
import java.util.Map;

/**
 * Represents the main entry procedure of a program.
 */
public class Procedure {
    private static String procName;
    private static DeclSeq declSeq;
    private static StmtSeq stmtSeq;

    private final Map<String, String> idMap = new HashMap<>();

    public Procedure() {}

    public void parse(Scanner scanner) {
        expect(scanner, Core.PROCEDURE, "expected 'procedure'");
        scanner.nextToken();

        expect(scanner, Core.ID, "expected procedure name");
        procName = scanner.getId();
        scanner.nextToken();

        expect(scanner, Core.IS, "expected 'is'");
        scanner.nextToken();

        if (scanner.currentToken() == Core.INTEGER || scanner.currentToken() == Core.OBJECT || scanner.currentToken() == Core.PROCEDURE) {
            declSeq = new DeclSeq();
            declSeq.parse(scanner, idMap);
        }

        expect(scanner, Core.BEGIN, "expected 'begin'");
        scanner.nextToken();

        stmtSeq = new StmtSeq();
        stmtSeq.parse(scanner, idMap);

        expect(scanner, Core.END, "expected 'end'");
        scanner.nextToken();

        if (scanner.currentToken() != Core.EOS) {
            error("extra token after 'end'");
        }
    }

    public void print() {
        System.out.println("procedure " + procName + " is");
        if (declSeq != null) {
            StmtSeq.increaseIndent();
            declSeq.print();
            StmtSeq.decreaseIndent();
        }
        System.out.println("begin");
        StmtSeq.increaseIndent();
        stmtSeq.print();
        StmtSeq.decreaseIndent();
        System.out.println("end");
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (declSeq != null) {
            declSeq.execute(data, memory);
        }
        stmtSeq.execute(data, memory, funcMap);
    }

    private void expect(Scanner scanner, Core expected, String msg) {
        if (scanner.currentToken() != expected) {
            error(msg);
        }
    }

    private void error(String msg) {
        System.out.println("ERROR: " + msg);
        System.exit(1);
    }
}
