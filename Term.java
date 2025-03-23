import java.util.Map;

public class Term {
    Factor factor;
    Term nextTerm;
    String operation = null;

    Term() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        factor = new Factor();
        factor.parse(scanner, idMap);
        while (scanner.currentToken() == Core.MULTIPLY || scanner.currentToken() == Core.DIVIDE) {
            operation = scanner.currentToken() == Core.MULTIPLY ? "*" : "/";
            scanner.nextToken();
            
            if (scanner.currentToken() == Core.MULTIPLY || scanner.currentToken() == Core.DIVIDE) {
                System.out.println("ERROR: extra " + operation + " in expression");
                System.exit(1);
            }
            
            if (scanner.currentToken() == Core.RPAREN) {
                System.out.println("ERROR: unexpected right parenthesis in term");
                System.exit(1);
            }
            nextTerm = new Term();
            nextTerm.parse(scanner, idMap);
        }
    }

    void print(){
        factor.print();
        if(operation != null){
            System.out.print(operation);
            nextTerm.print();
        }
    }
    
    int execute(Scanner data, Map<String, int[]> memory) {
        int rvalue = factor.execute(data, memory);
        if (operation != null) {
            int var2 = nextTerm.execute(data, memory);
            if (operation.equals("*")) {
                rvalue *= var2;
            } else if (operation.equals("/")) {
                if (var2 == 0) {
                    System.out.println("ERROR: division by zero");
                    System.exit(1);
                }
                rvalue /= var2;
            }
        }
        return rvalue;
    }
}