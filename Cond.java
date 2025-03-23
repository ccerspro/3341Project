import java.util.Map;

/**
 * Represents a condition with optional logical connectors (and/or) and NOT.
 * Includes direct comparison functionality (was previously in Cmpr).
 */
public class Cond {
    private Expr leftExpr;
    private Expr rightExpr;
    private String operator; // "==" or "<"

    private Cond nextCond;
    private boolean isNot = false;
    private String connector = null;
    private boolean useBrackets = false;
    private boolean hasBrackets = false;

    public Cond() {}
    public Cond(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    public void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.NOT) {
            isNot = true;
            scanner.nextToken();
            nextCond = new Cond(useBrackets);
            nextCond.parse(scanner, idMap);
            return;
        }

        if (scanner.currentToken() == Core.LBRACE) {
            hasBrackets = true;
            scanner.nextToken();
        }

        parseComparison(scanner, idMap);

        if (hasBrackets) {
            expect(scanner, Core.RBRACE, "expected ']'");
            scanner.nextToken();
        }

        if (scanner.currentToken() == Core.OR || scanner.currentToken() == Core.AND) {
            connector = (scanner.currentToken() == Core.OR) ? "or" : "and";
            scanner.nextToken();
            nextCond = new Cond(useBrackets);
            nextCond.parse(scanner, idMap);
        }
    }

    private void parseComparison(Scanner scanner, Map<String, String> idMap) {
        leftExpr = new Expr();
        leftExpr.parse(scanner, idMap);

        if (scanner.currentToken() == Core.EQUAL) {
            scanner.nextToken();
            expect(scanner, Core.EQUAL, "expected '=='");
            operator = "==";
            scanner.nextToken();
        } else if (scanner.currentToken() == Core.LESS) {
            operator = "<";
            scanner.nextToken();
        } else {
            error("expected comparison operator (== or <)");
        }

        rightExpr = new Expr();
        rightExpr.parse(scanner, idMap);
    }

    public void print() {
        if (isNot) {
            System.out.print("not ");
            nextCond.print();
        } else {
            if (useBrackets && hasBrackets) System.out.print("[");
            leftExpr.print();
            System.out.print(" " + operator + " ");
            rightExpr.print();
            if (useBrackets && hasBrackets) System.out.print("]");
        }

        if (connector != null) {
            System.out.print(" " + connector + " ");
            nextCond.print();
        }
    }

    public boolean execute(Scanner data, Map<String, int[]> memory) {
        boolean result;

        if (isNot) {
            result = !nextCond.execute(data, memory);
        } else {
            int left = leftExpr.execute(data, memory);
            int right = rightExpr.execute(data, memory);
            result = operator.equals("==") ? (left == right) : (left < right);
        }

        if (connector != null) {
            boolean nextResult = nextCond.execute(data, memory);
            if (connector.equals("or")) result = result || nextResult;
            else result = result && nextResult;
        }

        return result;
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
