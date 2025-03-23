import java.util.Map;

/**
 * Cond represents a conditional expression that supports
 * negation, comparison, and boolean connectors (and/or).
 */
public class Cond {
    private Cmpr comparison;
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
        Core token = scanner.currentToken();

        if (token == Core.NOT) {
            isNot = true;
            scanner.nextToken();
            validateConditionStart(scanner);
            nextCond = new Cond(useBrackets);
            nextCond.parse(scanner, idMap);

        } else if (token == Core.LBRACE) {
            hasBrackets = true;
            scanner.nextToken();

            comparison = new Cmpr();
            comparison.parse(scanner, idMap);

            if (scanner.currentToken() != Core.RBRACE) {
                error("expected ']'");
            }
            scanner.nextToken();

        } else {
            comparison = new Cmpr();
            comparison.parse(scanner, idMap);
            parseConnector(scanner, idMap);
        }
    }

    private void parseConnector(Scanner scanner, Map<String, String> idMap) {
        Core token = scanner.currentToken();

        if (token == Core.OR || token == Core.AND) {
            connector = token == Core.OR ? "or" : "and";
            scanner.nextToken();

            validateConditionStart(scanner);
            nextCond = new Cond(useBrackets);
            nextCond.parse(scanner, idMap);
        }
    }

    private void validateConditionStart(Scanner scanner) {
        Core token = scanner.currentToken();
        if (!(token == Core.ID || token == Core.CONST || token == Core.LPAREN ||
              token == Core.NOT || token == Core.LBRACE)) {
            error("invalid condition start after operator");
        }
    }

    public void print() {
        if (isNot) {
            System.out.print("not ");
            nextCond.print();

        } else if (comparison != null) {
            if (connector == null) {
                if (useBrackets && hasBrackets) {
                    System.out.print("[");
                }
                comparison.print();
                if (useBrackets && hasBrackets) {
                    System.out.print("]");
                }
            } else {
                comparison.print();
                System.out.print(" " + connector + " ");
                nextCond.print();
            }
        }
    }

    public boolean execute(Scanner data, Map<String, int[]> memory) {
        boolean result;

        if (isNot) {
            result = !nextCond.execute(data, memory);
        } else if (comparison != null) {
            result = comparison.execute(data, memory);

            if (connector != null) {
                boolean nextResult = nextCond.execute(data, memory);
                result = connector.equals("or") ? result || nextResult : result && nextResult;
            }
        } else {
            result = nextCond.execute(data, memory);
        }

        return result;
    }

    private void error(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }
}
