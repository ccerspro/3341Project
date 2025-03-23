import java.util.Map;

/**
 * Represents a print statement that outputs an expression's value.
 */
public class Print {
    private Expr expression;

    public Print() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        scanner.nextToken(); // consume 'print'

        expect(scanner, Core.LPAREN, "expected '(' after print");
        scanner.nextToken();

        expression = new Expr();
        expression.parse(scanner, idMap);

        expect(scanner, Core.RPAREN, "expected ')' after expression");
        scanner.nextToken();

        expect(scanner, Core.SEMICOLON, "expected semicolon in print statement");
        scanner.nextToken();
    }

    public void print() {
        System.out.print("print(");
        expression.print();
        System.out.println(");");
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        int value = expression.execute(data, memory);
        System.out.println(value);
    }

    private void expect(Scanner scanner, Core expected, String message) {
        if (scanner.currentToken() != expected) {
            System.out.println("ERROR: " + message);
            System.exit(1);
        }
    }
}
