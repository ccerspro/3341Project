import java.util.Map;

/**
 * Represents a single statement in the language.
 */
public class Stmt {
    private Assign assign;
    private If ifStmt;
    private Loop loop;
    private Print print;
    private Read read;
    private DeclSeq declSeq;
    private Call call;

    public Stmt() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        Core token = scanner.currentToken();

        switch (token) {
            case ID:
                assign = new Assign();
                assign.parse(scanner, idMap);
                break;
            case IF:
                ifStmt = new If();
                ifStmt.parse(scanner, idMap);
                break;
            case FOR:
                loop = new Loop();
                loop.parse(scanner, idMap);
                break;
            case PRINT:
                print = new Print();
                print.parse(scanner, idMap);
                break;
            case READ:
                read = new Read();
                read.parse(scanner, idMap);
                break;
            case INTEGER:
            case OBJECT:
                declSeq = new DeclSeq();
                declSeq.parse(scanner, idMap);
                break;
            case BEGIN:
                call = new Call();
                call.parse(scanner, idMap);
                break;
            default:
                System.out.println("ERROR: invalid statement");
                System.exit(1);
        }
    }

    public void print() {
        if (declSeq != null) declSeq.print();
        else if (assign != null) assign.print();
        else if (ifStmt != null) ifStmt.print();
        else if (loop != null) loop.print();
        else if (print != null) print.print();
        else if (read != null) read.print();
        else if (call != null) call.print();
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (declSeq != null) declSeq.execute(data, memory);
        else if (assign != null) assign.execute(data, memory);
        else if (ifStmt != null) ifStmt.execute(data, memory, funcMap);
        else if (loop != null) loop.execute(data, memory, funcMap);
        else if (print != null) print.execute(data, memory);
        else if (read != null) read.execute(data, memory);
        else if (call != null) call.execute(data, memory, funcMap);
    }
}
