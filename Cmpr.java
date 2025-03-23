import java.util.Map;

/**
 * Cmpr represents a comparison expression such as a == b or a < b.
 */
public class Cmpr {
    private Expr leftExpr;
    private Expr rightExpr;
    private String operator;

    public Cmpr() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        leftExpr = new Expr();
        leftExpr.parse(scanner, idMap);

        if (scanner.currentToken() == Core.EQUAL) {
            scanner.nextToken();

            if (scanner.currentToken() == Core.EQUAL) {
                operator = "==";
                scanner.nextToken();
                parseRightExpression(scanner, idMap, "==");
            } else {
                error("expected '=='");
            }

        } else if (scanner.currentToken() == Core.LESS) {
            operator = "<";
            scanner.nextToken();
            parseRightExpression(scanner, idMap, "<");

        } else {
            error("expected comparison operator ('==' or '<')");
        }
    }

    private void parseRightExpression(Scanner scanner, Map<String, String> idMap, String op) {
        if (isValidExprStart(scanner.currentToken())) {
            rightExpr = new Expr();
            rightExpr.parse(scanner, idMap);
        } else {
            error("expected expression after " + op);
        }
    }

    private boolean isValidExprStart(Core token) {
        return token == Core.ID || token == Core.CONST || token == Core.LPAREN;
    }

    public void print() {
        leftExpr.print();
        System.out.print(" " + operator + " ");
        rightExpr.print();
    }

    public boolean execute(Scanner data, Map<String, int[]> memory) {
        int leftValue = leftExpr.execute(data, memory);
        int rightValue = rightExpr.execute(data, memory);
        return operator.equals("==") ? leftValue == rightValue : leftValue < rightValue;
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
