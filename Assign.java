import java.util.Map;
import java.util.HashMap;

public class Assign {
    private String varName;
    private String secondVar;
    private Expr expression;
    private Expr secondExpr;
    private boolean isBraceAccess = false;
    private boolean isNewObject = false;
    private boolean isNewInt = false;
    private boolean isArray = false;
    private String strValue;

    private final Map<String, String> typeMap = new HashMap<>();

    public Assign() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        varName = scanner.getId();

        // Check for undeclared variable
        if (!idMap.containsKey(varName)) {
            System.out.println("Semantic Error: variable " + varName + " not declared(assign)");
            System.exit(1);
        }

        typeMap.put(varName, idMap.get(varName));
        scanner.nextToken();

        // Handle object field assignment e.g., obj["field"] = ...
        if (scanner.currentToken() == Core.LBRACE) {
            validateObjectType(idMap);
            isBraceAccess = true;
            scanner.nextToken();

            if (scanner.currentToken() != Core.STRING) {
                System.out.println("ERROR: expected string literal");
                System.exit(1);
            }

            strValue = scanner.getId();
            scanner.nextToken();

            if (scanner.currentToken() != Core.RBRACE) {
                System.out.println("ERROR: expected ]");
                System.exit(1);
            }

            scanner.nextToken();
        }

        // Handle array assignment: a : b;
        if (scanner.currentToken() == Core.COLON) {
            isArray = true;
            scanner.nextToken();

            if (scanner.currentToken() != Core.ID) {
                System.out.println("ERROR: expected identifier after :");
                System.exit(1);
            }

            secondVar = scanner.getId();
            if (!idMap.containsKey(secondVar)) {
                System.out.println("ERROR: variable " + secondVar + " not declared");
                System.exit(1);
            }

            scanner.nextToken();
        } else if (scanner.currentToken() != Core.ASSIGN) {
            System.out.println("ERROR: expected = or :");
            System.exit(1);
        } else {
            Core nextToken = scanner.nextToken();

            if (nextToken == Core.EQUAL) {
                System.out.println("ERROR: equals used in assignment statement");
                System.exit(1);
            }

            if (scanner.currentToken() == Core.RPAREN) {
                System.out.println("ERROR: unmatched ) found without matching ( in assignment");
                System.exit(1);
            }

            if (isBraceAccess) {
                secondExpr = new Expr();
                secondExpr.parse(scanner, idMap);
            } else if (scanner.currentToken() == Core.NEW) {
                handleNewObject(scanner, idMap);
            } else {
                expression = new Expr();
                expression.parse(scanner, idMap);

                if (scanner.currentToken() == Core.RPAREN) {
                    System.out.println("ERROR: unmatched ) found without matching ( in assignment");
                    System.exit(1);
                }
            }
        }

        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected semicolon");
            System.exit(1);
        }
        scanner.nextToken();
    }

    private void validateObjectType(Map<String, String> idMap) {
        if (!idMap.get(varName).equals("object")) {
            System.out.println("ERROR: [string] used with an integer variable");
            System.exit(1);
        }
    }

    private void handleNewObject(Scanner scanner, Map<String, String> idMap) {
        if (!idMap.get(varName).equals("object")) {
            System.out.println("ERROR: integer used in 'new record' declaration");
            System.exit(1);
        }

        isNewObject = true;
        scanner.nextToken();

        if (scanner.currentToken() != Core.OBJECT) {
            System.out.println("ERROR: expected object keyword");
            System.exit(1);
        }

        scanner.nextToken();
        if (scanner.currentToken() != Core.LPAREN) {
            System.out.println("ERROR: expected (");
            System.exit(1);
        }

        scanner.nextToken();
        if (scanner.currentToken() != Core.STRING) {
            System.out.println("ERROR: expected string literal");
            System.exit(1);
        }

        strValue = scanner.getId();
        scanner.nextToken();

        if (scanner.currentToken() != Core.COMMA) {
            System.out.println("ERROR: expected ,");
            System.exit(1);
        }

        scanner.nextToken();
        expression = new Expr();
        expression.parse(scanner, idMap);

        if (scanner.currentToken() != Core.RPAREN) {
            System.out.println("ERROR: expected )");
            System.exit(1);
        }

        scanner.nextToken();
    }

    public void print() {
        if (isBraceAccess) {
            System.out.print(varName + "['" + strValue + "'] = ");
            secondExpr.print();
            System.out.println(";");
        } else if (isNewObject) {
            System.out.print(varName + " = new object('" + strValue + "', ");
            expression.print();
            System.out.println(");");
        } else {
            System.out.print(varName + " = ");
            expression.print();
            System.out.println(";");
        }
    }

    public void execute(Scanner data, Map<String, int[]> memory) {
        int[] var = memory.get(varName);
        String type = typeMap.get(varName);

        if (var == null && !type.equals("object")) {
            var = new int[1];
            memory.put(varName, var);
        }

        if (isBraceAccess) {
            int value = secondExpr.execute(data, memory);
            String fieldKey = varName + "['" + strValue + "']";

            int[] fieldVar = memory.getOrDefault(fieldKey, new int[1]);
            fieldVar[0] = value;
            memory.put(fieldKey, fieldVar);
        } else if (isNewInt) {
            var = new int[expression.execute(data, memory)];
        } else if (isArray) {
            int[] source = memory.get(secondVar);
            if (source == null) {
                source = new int[1];
                memory.put(secondVar, source);
            }
            var = source;
        } else if (isNewObject) {
            var = new int[1];
            var[0] = expression != null ? expression.execute(data, memory) : 0;
        } else {
            if (expression == null) {
                System.out.println("Error: assignment expression is null.");
                System.exit(1);
            }

            if (type.equals("object") && var == null) {
                System.out.println("ERROR: assignment to null object variable");
                System.exit(1);
            }

            if (var == null) {
                var = new int[1];
            }

            var[0] = expression.execute(data, memory);
        }

        memory.put(varName, var);
    }
}
