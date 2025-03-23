import java.util.Map;

/**
 * Represents an arithmetic expression with optional addition or subtraction.
 */
public class Expr {
    private Term term;
    private Expr nextExpr;
    private boolean isAdd = false;
    private boolean isSub = false;

    public Expr() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.RPAREN) {
            error("unexpected right parenthesis in expression");
        }

        term = new Term();
        term.parse(scanner, idMap);

        while (scanner.currentToken() == Core.ADD || scanner.currentToken() == Core.SUBTRACT) {
            if (scanner.currentToken() == Core.ADD) {
                isAdd = true;
            } else {
                isSub = true;
            }

            scanner.nextToken();

            Core next = scanner.currentToken();
            if (next == Core.ADD || next == Core.SUBTRACT || next == Core.RPAREN) {
                error("extra " + (isAdd ? "+" : "-") + " in expression");
            }

            if (next != Core.ID && next != Core.CONST && next != Core.LPAREN) {
                error("expected valid token after " + (isAdd ? "+" : "-"));
            }

            nextExpr = new Expr();
            nextExpr.parse(scanner, idMap);
        }
    }

    public void print() {
        term.print();
        if (isAdd) {
            System.out.print("+");
            nextExpr.print();
        } else if (isSub) {
            System.out.print("-");
            nextExpr.print();
        }
    }

    public int execute(Scanner data, Map<String, int[]> memory) {
        int result = term.execute(data, memory);
        if (isAdd) {
            result += nextExpr.execute(data, memory);
        } else if (isSub) {
            result -= nextExpr.execute(data, memory);
        }
        return result;
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
