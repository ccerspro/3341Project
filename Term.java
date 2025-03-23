import java.util.Map;

/**
 * Represents a term in an expression (supports * and /).
 */
public class Term {
    private Factor factor;
    private Term nextTerm;
    private String operation;

    public Term() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        factor = new Factor();
        factor.parse(scanner, idMap);

        while (scanner.currentToken() == Core.MULTIPLY || scanner.currentToken() == Core.DIVIDE) {
            operation = (scanner.currentToken() == Core.MULTIPLY) ? "*" : "/";
            scanner.nextToken();

            if (scanner.currentToken() == Core.MULTIPLY || scanner.currentToken() == Core.DIVIDE ||
                scanner.currentToken() == Core.RPAREN) {
                error("extra or unexpected operator in term");
            }

            nextTerm = new Term();
            nextTerm.parse(scanner, idMap);
        }
    }

    public void print() {
        factor.print();
        if (operation != null) {
            System.out.print(operation);
            nextTerm.print();
        }
    }

    public int execute(Scanner data, Map<String, int[]> memory) {
        int value = factor.execute(data, memory);
        if (operation != null) {
            int nextVal = nextTerm.execute(data, memory);
            if (operation.equals("/") && nextVal == 0) {
                error("division by zero");
            }
            value = operation.equals("*") ? value * nextVal : value / nextVal;
        }
        return value;
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
