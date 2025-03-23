import java.util.Map;

/**
 * Represents a factor in an arithmetic expression.
 */
public class Factor {
    private Expr expression;
    private int constant;
    private String varName;
    private String strValue;
    private boolean isId = false;
    private boolean isParen = false;
    private boolean isConst = false;

    public Factor() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        Core token = scanner.currentToken();

        switch (token) {
            case CONST:
                isConst = true;
                constant = scanner.getConst();
                scanner.nextToken();
                break;

            case LPAREN:
                isParen = true;
                scanner.nextToken();
                expression = new Expr();
                expression.parse(scanner, idMap);
                expect(scanner, Core.RPAREN, "expected ')'");
                scanner.nextToken();
                break;

            case ID:
                isId = true;
                varName = scanner.getId();

                if (!idMap.containsKey(varName)) {
                    error("variable " + varName + " not declared");
                }

                scanner.nextToken();

                if (scanner.currentToken() == Core.LBRACE) {
                    if (!"object".equals(idMap.get(varName))) {
                        error("variable " + varName + " must be of type object");
                    }
                    scanner.nextToken();
                    expect(scanner, Core.STRING, "expected string index for object");
                    strValue = scanner.getId();
                    scanner.nextToken();
                    expect(scanner, Core.RBRACE, "expected ']'");
                    scanner.nextToken();
                }
                break;

            default:
                error("invalid factor");
        }
    }

    public void print() {
        if (isId) {
            if (expression != null) {
                System.out.print(varName + "[");
                expression.print();
                System.out.print("]");
            } else if (strValue != null) {
                System.out.print(varName + "['" + strValue + "']");
            } else {
                System.out.print(varName);
            }
        } else if (isParen) {
            System.out.print("(");
            expression.print();
            System.out.print(")");
        } else if (isConst) {
            System.out.print(constant);
        }
    }

    public int execute(Scanner data, Map<String, int[]> memory) {
        if (isId) {
            int[] var = memory.get(varName);
            if (var == null) {
                error("variable " + varName + " not initialized");
            }

            if (strValue != null) {
                String key = varName + "['" + strValue + "']";
                int[] field = memory.get(key);
                if (field == null) {
                    error("undefined key '" + strValue + "'");
                }
                return field[0];
            } else if (expression != null) {
                return var[expression.execute(data, memory)];
            } else {
                return var[0];
            }
        } else if (isParen) {
            return expression.execute(data, memory);
        } else {
            return constant;
        }
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
