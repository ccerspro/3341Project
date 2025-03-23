import java.util.Map;

public class StmtSeq {
    Stmt stmt;
    StmtSeq nextStmtSeq;
    static int indentLevel = 0; 
    public static boolean inLocalScope = false;

    StmtSeq() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.END) {
            return; 
        }
        stmt = new Stmt();
        stmt.parse(scanner, idMap);
        if (scanner.currentToken() == Core.ID || 
            scanner.currentToken() == Core.IF || 
            scanner.currentToken() == Core.FOR || 
            scanner.currentToken() == Core.PRINT || 
            scanner.currentToken() == Core.READ ||
            scanner.currentToken() == Core.INTEGER || 
            scanner.currentToken() == Core.OBJECT ||
            scanner.currentToken() == Core.BEGIN) {
            nextStmtSeq = new StmtSeq();
            nextStmtSeq.parse(scanner, idMap); 
        }
    }

    void print() {
        for(int i = 0; i < indentLevel; i++) {
            System.out.print("    ");
        }
        stmt.print();
        if (nextStmtSeq != null) {
            nextStmtSeq.print();
        }
    }

    public static void increaseIndent() {
        indentLevel++;
    }

    public static void decreaseIndent() {
        if(indentLevel > 0) {
            indentLevel--;
        }
    }

    void execute(Scanner data, Map<String, int[]> memory) {
        execute(data, memory, FunctionMap.getFuncMap());
    }
    
    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
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