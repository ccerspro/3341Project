import java.util.Map;

/**
 * Handles reading a constant from input and assigning to a variable.
 */
public class Read {
    private String varName;

    public Read() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        scanner.nextToken(); // consume 'read'

        expect(scanner, Core.LPAREN, "expected '(' after 'read'");
        scanner.nextToken();

        expect(scanner, Core.ID, "expected identifier");
        varName = scanner.getId();

        if (!idMap.containsKey(varName)) {
            error("variable " + varName + " not declared");
        }
        scanner.nextToken();

        expect(scanner, Core.RPAREN, "expected ')'");
        scanner.nextToken();

        expect(scanner, Core.SEMICOLON, "expected ';' at end of read statement");
        scanner.nextToken();
    }

    public void print() {
        System.out.println("read(" + varName + ");");
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        if (data.currentToken() != Core.CONST) {
            if (data.currentToken() == Core.EOS) {
                error("not enough values in data file");
            } else {
                error("expected constant in data file");
            }
        }

        int[] var = memory.get(varName);
        if (var == null) {
            error("variable " + varName + " not initialized");
        }

        var[0] = data.getConst();
        data.nextToken();
    }

    private void expect(Scanner scanner, Core expected, String message) {
        if (scanner.currentToken() != expected) {
            error(message);
        }
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
