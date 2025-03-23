import java.util.HashMap;
import java.util.Map;

public class Procedure {

    static String procName;
    static DeclSeq declSeq;
    static StmtSeq stmtSeq;
    Map<String, String> idMap = new HashMap<>();

    Procedure() {}

    void parse(Scanner scanner) {
        if (scanner.currentToken() != Core.PROCEDURE) {
            System.out.println("ERROR: expected procedure");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected procedure name");
            System.exit(1);
        }
        procName = scanner.getId();
        scanner.nextToken();

        if (scanner.currentToken() != Core.IS) {
            System.out.println("ERROR: expected is");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() == Core.INTEGER || scanner.currentToken() == Core.OBJECT || scanner.currentToken() == Core.PROCEDURE) {
            declSeq = new DeclSeq();
            declSeq.parse(scanner, idMap);
        }

        if (scanner.currentToken() != Core.BEGIN) {
            System.out.println("ERROR: expected begin");
            System.exit(1);
        }
        scanner.nextToken();

        stmtSeq = new StmtSeq();
        stmtSeq.parse(scanner, idMap);

        if (scanner.currentToken() != Core.END) {
            System.out.println("ERROR: expected end");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.EOS) {
            System.out.println("ERROR: extra token after end");
            System.exit(1);
        }
    }

    void print() {
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

    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (declSeq != null) {
            declSeq.execute(data, memory);
        }
        stmtSeq.execute(data, memory, funcMap);
    }
}