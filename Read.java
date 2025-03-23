import java.util.Map;

public class Read {
    String varName;
    Read() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        scanner.nextToken();
        if(scanner.currentToken() != Core.LPAREN){
            System.out.println("ERROR: expected (");
            System.exit(1);
        }
        scanner.nextToken();
        if(scanner.currentToken() != Core.ID){
            System.out.println("ERROR: expected identifier");
            System.exit(1);
        }
        varName = scanner.getId();
        if(!idMap.containsKey(varName)){
            System.out.println("ERROR: variable " + varName + " not declared");
            System.exit(1);
        }
        scanner.nextToken();
        if(scanner.currentToken() != Core.RPAREN){
            System.out.println("ERROR: expected )");
            System.exit(1);
        }
        scanner.nextToken();
        if(scanner.currentToken() != Core.SEMICOLON){
            System.out.println("ERROR: expected semicolon in read statement");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print(){
        System.out.println("read(" + varName + ");");
    }

    void execute(Scanner data, Map<String, int[]> memory) {
        if (data.currentToken() != Core.CONST) {
            if (data.currentToken() == Core.EOS) {
                System.out.println("ERROR: not enough values in data file");
            } else {
                System.out.println("ERROR: expected constant in data file");
            }
            System.exit(1);
        }
        int[] var = memory.get(varName);
        if (var == null) {
            System.out.println("ERROR: variable " + varName + " not initialized");
            System.exit(1);
        }
        var[0] = data.getConst();
        data.nextToken();
    }
}