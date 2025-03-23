import java.util.Map;

public class Decl {
    String varName;
    String varType;

    Decl() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        if (scanner.currentToken() == Core.INTEGER) {
            varType = "integer";
        } else if (scanner.currentToken() == Core.OBJECT) {
            varType = "object";
        } else {
            System.out.println("ERROR: expected integer or object");
            System.exit(1);
        }
        
        scanner.nextToken();
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected identifier");
            System.exit(1);
        }
        
        varName = scanner.getId();
        
        idMap.put(varName, varType);
        
        scanner.nextToken();
        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected semicolon");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
        print(StmtSeq.indentLevel);
    }

    void print(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("    ");
        }
        System.out.println(varType + " " + varName + ";");
    }

    void execute(Scanner data, Map<String, int[]> memory) {
        if (varType.equals("integer")) {
            int[] value = new int[1];
            value[0] = 0;
            memory.put(varName, value);
        } else if (varType.equals("object")) {
            memory.put(varName, null);
        }
    }
}