import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Call {
    String procName;
    List<String> args = new ArrayList<>();

    Call() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() != Core.BEGIN) {
            System.out.println("ERROR: expected begin");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected procedure name");
            System.exit(1);
        }
        procName = scanner.getId();
        scanner.nextToken();

        if (scanner.currentToken() != Core.LPAREN) {
            System.out.println("ERROR: expected (");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected argument name");
            System.exit(1);
        }
        
        String argName = scanner.getId();
        
        if (!idMap.containsKey(argName)) {
            System.out.println("Semantic Error: variable '" + argName + "' not declared");
            System.exit(1);
        }
        if (!idMap.get(argName).equals("object")) {
            System.out.println("Semantic Error: argument '" + argName + "' must be an object");
            System.exit(1);
        }
        
        args.add(argName);
        scanner.nextToken();
        
        while (scanner.currentToken() == Core.COMMA) {
            scanner.nextToken();
            if (scanner.currentToken() != Core.ID) {
                System.out.println("ERROR: expected argument name after comma");
                System.exit(1);
            }
            argName = scanner.getId();
            
            if (!idMap.containsKey(argName)) {
                System.out.println("Semantic Error: variable '" + argName + "' not declared");
                System.exit(1);
            }
            if (!idMap.get(argName).equals("object")) {
                System.out.println("Semantic Error: argument '" + argName + "' must be an object");
                System.exit(1);
            }
            
            args.add(argName);
            scanner.nextToken();
        }
        
        if (scanner.currentToken() != Core.RPAREN) {
            System.out.println("ERROR: expected )");
            System.exit(1);
        }
        scanner.nextToken();

        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected ;");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
        System.out.print("begin " + procName + "(");
        for (int i = 0; i < args.size(); i++) {
            System.out.print(args.get(i));
            if (i < args.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(");");
    }

    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (!funcMap.containsKey(procName) && !FunctionMap.hasFunction(procName)) {
            System.out.println("Semantic Error: procedure '" + procName + "' not defined");
            System.exit(1);
        }
        
        Function func;
        if (funcMap.containsKey(procName)) {
            func = funcMap.get(procName);
        } else {
            func = FunctionMap.getFunction(procName);
        }
        
        // Fixed: Changed func.params to func.parameters
        if (args.size() != func.parameters.size()) {
            System.out.println("Semantic Error: procedure '" + procName + "' called with incorrect number of arguments");
            System.exit(1);
        }
        
        func.execute(data, memory, args, funcMap);
    }
}