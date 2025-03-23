import java.util.Map;

/**
 * Represents a variable declaration (either integer or object).
 */
public class Decl {
    private String varName;
    private String varType;

    public Decl() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.INTEGER) {
            varType = "integer";
        } else if (scanner.currentToken() == Core.OBJECT) {
            varType = "object";
        } else {
            error("expected 'integer' or 'object'");
        }

        scanner.nextToken();
        if (scanner.currentToken() != Core.ID) {
            error("expected identifier");
        }

        varName = scanner.getId();
        idMap.put(varName, varType);

        scanner.nextToken();
        if (scanner.currentToken() != Core.SEMICOLON) {
            error("expected semicolon");
        }

        scanner.nextToken();
    }

    public void print() {
        print(StmtSeq.indentLevel);
    }

    public void print(int indent) {
        printIndent(indent);
        System.out.println(varType + " " + varName + ";");
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        if ("integer".equals(varType)) {
            memory.put(varName, new int[] { 0 });
        } else if ("object".equals(varType)) {
            memory.put(varName, null); // Object references are null by default
        }
    }

    private void printIndent(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("    ");
        }
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
