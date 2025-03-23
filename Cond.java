import java.util.Map;

public class Cond {
    Cmpr comparison;
    Cond nextCond;
    boolean isNot = false;
    String connector = null;
    boolean useBrackets = false;
    boolean hasBrackets = false;

    Cond() {}

    Cond(boolean useBrackets) {
        this.useBrackets = useBrackets;
    }

    void parse(Scanner scanner, Map<String, String> idMap) {
        if(scanner.currentToken() == Core.NOT){
            isNot = true;
            scanner.nextToken();
            if(scanner.currentToken() == Core.ID || scanner.currentToken() == Core.CONST || 
               scanner.currentToken() == Core.LPAREN || scanner.currentToken() == Core.NOT || 
               scanner.currentToken() == Core.LBRACE){
                nextCond = new Cond(useBrackets);
                nextCond.parse(scanner, idMap);
            }else{
                System.out.println("ERROR: expected condition after not");
                System.exit(1);
            }
        }else if(scanner.currentToken() == Core.LBRACE){
            hasBrackets = true;
            scanner.nextToken();
            comparison = new Cmpr();
            comparison.parse(scanner, idMap);
            if(scanner.currentToken() != Core.RBRACE){
                System.out.println("ERROR: expected ]");
                System.exit(1);
            }
            scanner.nextToken();
        }else{
            comparison = new Cmpr();
            comparison.parse(scanner, idMap);
            if(scanner.currentToken() == Core.OR){
                connector = "or";
                scanner.nextToken();
                if(scanner.currentToken() == Core.ID || scanner.currentToken() == Core.CONST || scanner.currentToken() == Core.LPAREN || scanner.currentToken() == Core.NOT || scanner.currentToken() == Core.LBRACE){
                    nextCond = new Cond(useBrackets);
                    nextCond.parse(scanner, idMap);
                }else{
                    System.out.println("ERROR: no condition after the \"or\"(compare)");
                    System.exit(1);
                }
            }else if(scanner.currentToken() == Core.AND){
                connector = "and";
                scanner.nextToken();
                if(scanner.currentToken() == Core.ID || scanner.currentToken() == Core.CONST || scanner.currentToken() == Core.LPAREN || scanner.currentToken() == Core.NOT || scanner.currentToken() == Core.LBRACE){
                    nextCond = new Cond(useBrackets);
                    nextCond.parse(scanner, idMap);
                }else{
                    System.out.println("ERROR: no condition after the \"and\"(compare)");
                    System.exit(1);
                }
            }
        }
    }

    void print(){
        if(isNot){
            System.out.print("not ");
            nextCond.print();
        }else if(comparison != null){
            if(connector == null){
                if (useBrackets && hasBrackets) {
                    System.out.print("[");
                }
                comparison.print();
                if (useBrackets && hasBrackets) {
                    System.out.print("]");
                }
            }else{
                comparison.print();
                System.out.print(" " + connector + " ");
                nextCond.print();
            }
        }
    }   

    boolean execute(Scanner data, Map<String, int[]> memory) {
        boolean result;
        if (isNot) {
            result = !nextCond.execute(data, memory);
        } else if (comparison != null) {
            result = comparison.execute(data, memory);
            if (connector != null) {
                if (connector.equals("or")) {
                    result = result || nextCond.execute(data, memory);
                } else if (connector.equals("and")) {
                    result = result && nextCond.execute(data, memory);
                }
            }
        } else {
            result = nextCond.execute(data, memory);
        }
        return result;
    }
}