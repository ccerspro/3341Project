import java.util.Map;
import java.util.HashMap;

public class Assign {
    String varName;
    String secondVar;
    Expr expression;
    Expr secondExpr;
    boolean isBrace = false;
    boolean isNewObject = false;
    String strValue;
    boolean isNewInt = false;
    Map<String, String> typeMap = new HashMap<>();
    boolean isArray = false;

    Assign() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        varName = scanner.getId();
        if (!idMap.containsKey(varName)) {
            System.out.println("Semantic Error: variable " + varName + " not declared(assign)");
            System.exit(1);
        }
        
        typeMap.put(varName, idMap.get(varName));
        
        scanner.nextToken();
        
        if (scanner.currentToken() == Core.LBRACE) {
            if (!idMap.get(varName).equals("object")) {
                System.out.println("ERROR: [string] used with an integer variable");
                System.exit(1);
            }
            isBrace = true;
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

            if (isBrace) {
                secondExpr = new Expr();
                secondExpr.parse(scanner, idMap);
            } else {
                if (scanner.currentToken() == Core.NEW) {
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
                } else {
                    expression = new Expr();
                    expression.parse(scanner, idMap);
                    if (scanner.currentToken() == Core.RPAREN) {
                        System.out.println("ERROR: unmatched ) found without matching ( in assignment");
                        System.exit(1);
                    }
                }
            }
        }
        
        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected semicolon");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
        if (isBrace) {
            System.out.print(varName + "['");
            System.out.print(strValue);
            System.out.print("'] = ");
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

    void execute(Scanner data, Map<String, int[]> memory) {
        int[] var = memory.get(varName);
        String typev = typeMap.get(varName);
        
        if (var == null && !typeMap.get(varName).equals("object")) {
            var = new int[1];
            memory.put(varName, var);
        }
        
        if (isBrace) {
            if (secondExpr == null) {
                System.out.println("Error: assignment expression is null.");
                System.exit(1);
            }
            int value = secondExpr.execute(data, memory);
            
            String fieldKey = varName + "['" + strValue + "']";
            int[] fieldVar = memory.get(fieldKey);
            if (fieldVar == null) {
                fieldVar = new int[1];
                memory.put(fieldKey, fieldVar);
            }
            fieldVar[0] = value;
            
        } else if (isNewInt) {
            if (expression == null) {
                System.out.println("Error: expression is null in new integer array.");
                System.exit(1);
            }
            var = new int[expression.execute(data, memory)];
        } else if (isArray) {
            int[] var2 = memory.get(secondVar);
            if (var2 == null) {
                var2 = new int[1];
                memory.put(secondVar, var2);
            }
            var = var2;
        } else if (isNewObject) {
            var = new int[1];
            var[0] = 0;  
            if (expression != null) { 
                var[0] = expression.execute(data, memory);
            }
        } else {
            if (expression == null) {
                System.out.println("Error: assignment expression is null.");
                System.exit(1);
            }
            
            if (typeMap.containsKey(varName) && typeMap.get(varName).equals("object") && var == null) {
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