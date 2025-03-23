import java.util.Map;

public class Factor {
    Expr expression;
    int constant;
    String varName = null;
    String strValue;
    boolean isId = false;
    boolean isParen = false;
    boolean isConst = false;

    Factor() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.RPAREN) {
            System.out.println("ERROR: unmatched ) found without matching (");
            System.exit(1);
        }
        if(scanner.currentToken() == Core.CONST){
            isConst = true;
            constant = scanner.getConst();
            scanner.nextToken();
        }else if(scanner.currentToken() == Core.LPAREN){
            isParen = true;
            scanner.nextToken();
            expression = new Expr();
            expression.parse(scanner, idMap);
            if(scanner.currentToken() != Core.RPAREN){
                System.out.println("ERROR: expected )");
                System.exit(1);
            }
            scanner.nextToken();
        }else if(scanner.currentToken() == Core.ID){
            isId = true;
            varName = scanner.getId();
            if(!idMap.containsKey(varName)){
                System.out.println("ERROR: variable " + varName + " not declared");
                System.exit(1);
            }
            scanner.nextToken();
            if(scanner.currentToken() == Core.LBRACE){
                if(!idMap.get(varName).equals("object")){
                    System.out.println("ERROR: variable " + varName + " must be of type object");
                    System.exit(1);
                }
                scanner.nextToken();
                if(scanner.currentToken() != Core.STRING){
                    System.out.println("ERROR: object index must be a string");
                    System.exit(1);
                }
                strValue = scanner.getId();
                scanner.nextToken();
                if(scanner.currentToken() != Core.RBRACE){
                    System.out.println("ERROR: expected ]");
                    System.exit(1);
                }
                scanner.nextToken();
            }
        }else{
            System.out.println("ERROR: invalid factor");
            System.exit(1);
        }
    }

    void print(){
        if(isId){
            if(expression != null){
                System.out.print(varName+"[");
                expression.print();
                System.out.print("]");
            }else if(strValue != null){
                System.out.print(varName + "['" + strValue + "']");
            }else{
                System.out.print(varName);
            }
        }else if(isParen){
            System.out.print("(");
            expression.print();
            System.out.print(")");
        }else if(isConst){
            System.out.print(constant);
        }
    }
    
    int execute(Scanner data, Map<String, int[]> memory) {
        int rvalue = 0;
        if (isId) {
            int[] var = memory.get(varName);
            if (var == null) {
                System.out.println("ERROR: variable " + varName + " not initialized");
                System.exit(1);
            }
            
            if (strValue != null) {
                String fieldKey = varName + "['" + strValue + "']";
                int[] fieldVar = memory.get(fieldKey);
                if (fieldVar == null) {
                    System.out.println("ERROR: undefined key '" + strValue + "'");
                    System.exit(1);
                }
                rvalue = fieldVar[0];
            } else if (expression != null) {
                rvalue = var[expression.execute(data, memory)];
            } else {
                rvalue = var[0];
            }
        } else if (isParen) {
            rvalue = expression.execute(data, memory);
        } else if (isConst) {
            rvalue = constant;
        }
        return rvalue;
    }   
}