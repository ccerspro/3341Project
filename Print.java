import java.util.Map;

public class Print {
    Expr expression;
    Print() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        scanner.nextToken();
        if(scanner.currentToken() != Core.LPAREN){
            System.out.println("ERROR: expected (");
            System.exit(1);
        }
        scanner.nextToken();
        expression = new Expr();
        expression.parse(scanner, idMap);
        if(scanner.currentToken() != Core.RPAREN){
            System.out.println("ERROR: expected )");
            System.exit(1);
        }
        scanner.nextToken();
        if(scanner.currentToken() != Core.SEMICOLON){
            System.out.println("ERROR: expected semicolon in print statement");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print(){
        System.out.print("print(");
        expression.print();
        System.out.println(");");
    }

    void execute(Scanner data, Map<String, int[]> memory) {
        int value = expression.execute(data, memory);
        System.out.println(value);
    }
}