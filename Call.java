import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Call {
    private String procedureName;
    private final List<String> argumentList = new ArrayList<>();

    public Call() {}

    public void parse(Scanner scanner, Map<String, String> idMap) {
        expectToken(scanner, Core.BEGIN, "expected 'begin'");
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            throwSyntaxError("expected procedure name");
        }

        procedureName = scanner.getId();
        scanner.nextToken();

        expectToken(scanner, Core.LPAREN, "expected '('");
        scanner.nextToken();

        parseArguments(scanner, idMap);

        expectToken(scanner, Core.RPAREN, "expected ')'");
        scanner.nextToken();

        expectToken(scanner, Core.SEMICOLON, "expected ';'");
        scanner.nextToken();
    }

    private void parseArguments(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() != Core.ID) {
            throwSyntaxError("expected at least one argument name");
        }

        while (true) {
            String arg = scanner.getId();

            validateArgument(arg, idMap);
            argumentList.add(arg);
            scanner.nextToken();

            if (scanner.currentToken() != Core.COMMA) {
                break;
            }

            scanner.nextToken();
            if (scanner.currentToken() != Core.ID) {
                throwSyntaxError("expected argument name after comma");
            }
        }
    }

    private void validateArgument(String argName, Map<String, String> idMap) {
        if (!idMap.containsKey(argName)) {
            throwSemanticError("variable '" + argName + "' not declared");
        }

        if (!"object".equals(idMap.get(argName))) {
            throwSemanticError("argument '" + argName + "' must be an object");
        }
    }

    public void print() {
        System.out.print("begin " + procedureName + "(");
        for (int i = 0; i < argumentList.size(); i++) {
            System.out.print(argumentList.get(i));
            if (i < argumentList.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(");");
    }

    public void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        Function function = resolveFunction(funcMap);
        
        if (argumentList.size() != function.parameters.size()) {
            throwSemanticError("procedure '" + procedureName + "' called with incorrect number of arguments");
        }

        function.execute(data, memory, argumentList, funcMap);
    }

    private Function resolveFunction(Map<String, Function> funcMap) {
        if (funcMap.containsKey(procedureName)) {
            return funcMap.get(procedureName);
        }

        if (FunctionMap.hasFunction(procedureName)) {
            return FunctionMap.getFunction(procedureName);
        }

        throwSemanticError("procedure '" + procedureName + "' not defined");
        return null; // Unreachable but required by compiler
    }

    private void expectToken(Scanner scanner, Core expected, String errorMessage) {
        if (scanner.currentToken() != expected) {
            throwSyntaxError(errorMessage);
        }
    }

    private void throwSyntaxError(String message) {
        System.out.println("SYNTAX ERROR: " + message);
        System.exit(1);
    }

    private void throwSemanticError(String message) {
        System.out.println("SEMANTIC ERROR: " + message);
        System.exit(1);
    }
}
