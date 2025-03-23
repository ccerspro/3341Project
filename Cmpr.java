import java.util.Map;

public class Cmpr {
    Expr leftExpr;
    Expr rightExpr;
    String operator;

    Cmpr() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        leftExpr = new Expr();
        leftExpr.parse(scanner, idMap);
        if(scanner.currentToken() == Core.EQUAL){
            scanner.nextToken();
            if(scanner.currentToken() == Core.EQUAL){
                operator = "==";
                scanner.nextToken();
                if(scanner.currentToken() == Core.ID || scanner.currentToken() == Core.CONST || scanner.currentToken() == Core.LPAREN){
                    rightExpr = new Expr();
                    rightExpr.parse(scanner, idMap);
                }else{
                    System.out.println("ERROR: expected expression after ==");
                    System.exit(1);
                }
            }else{
                System.out.println("ERROR: expected ==");
                System.exit(1);
            }
        }else if(scanner.currentToken() == Core.LESS){
            operator = "<";
            scanner.nextToken();
            if(scanner.currentToken() == Core.ID || scanner.currentToken() == Core.CONST || scanner.currentToken() == Core.LPAREN){
                rightExpr = new Expr();
                rightExpr.parse(scanner, idMap);
            }else{
                System.out.println("ERROR: expected expression after <");
                System.exit(1);
            }
        }else{
            System.out.println("ERROR: expected comparison operator");
            System.exit(1);
        }
    }

    void print(){
        leftExpr.print();
        System.out.print(" " + operator + " ");
        rightExpr.print();
    }   

    boolean execute(Scanner data, Map<String, int[]> memory) {
        int left = leftExpr.execute(data, memory);
        int right = rightExpr.execute(data, memory);
        if (operator.equals("==")) {
            return left == right;
        } else if (operator.equals("<")) {
            return left < right;
        }
        return false;
    }
}