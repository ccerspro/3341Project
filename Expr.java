import java.util.Map;

public class Expr {
    Term term;
    Expr nextExpr;
    boolean isAdd = false;
    boolean isSub = false;

    Expr() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.RPAREN) {
            System.out.println("ERROR: unexpected right parenthesis in expression");
            System.exit(1);
        }
        
        term = new Term();
        term.parse(scanner, idMap);
        
        while (scanner.currentToken() == Core.ADD || scanner.currentToken() == Core.SUBTRACT) {
            if (scanner.currentToken() == Core.ADD) {
                isAdd = true;
            } else {
                isSub = true;
            }
            scanner.nextToken();
            
            if (scanner.currentToken() == Core.ADD || scanner.currentToken() == Core.SUBTRACT) {
                System.out.println("ERROR: extra " + (isAdd ? "+" : "-") + " in expression");
                System.exit(1);
            }
            
            if (scanner.currentToken() != Core.ID && 
                scanner.currentToken() != Core.CONST && 
                scanner.currentToken() != Core.LPAREN) {
                System.out.println("ERROR: extra " + (isAdd ? "+" : "-") + " in expression");
                System.exit(1);
            }
            
            if (scanner.currentToken() == Core.RPAREN) {
                System.out.println("ERROR: unexpected right parenthesis in expression");
                System.exit(1);
            }
            nextExpr = new Expr();
            nextExpr.parse(scanner, idMap);
        }
    }

    void print(){
        term.print();
        if(isAdd){
            System.out.print("+");
            nextExpr.print();
        }else if(isSub){
            System.out.print("-");
            nextExpr.print();
        }
    }

    int execute(Scanner data, Map<String, int[]> memory) {
        int rvalue = term.execute(data, memory);
        if (isAdd) {
            rvalue += nextExpr.execute(data, memory);
        } else if (isSub) {
            rvalue -= nextExpr.execute(data, memory);
        }
        return rvalue;
    }
}