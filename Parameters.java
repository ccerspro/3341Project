import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parameters {
    private List<String> params = new ArrayList<>();
    
    Parameters() {}
    
    void parse(Scanner scanner, Map<String, String> idMap, boolean isDeclaration) {
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected identifier");
            System.exit(1);
        }
        
        String name = scanner.getId();
        
        if (isDeclaration) {
            if (params.contains(name)) {
                System.out.println("Semantic Error: Duplicate parameter name '" + name + "'");
                System.exit(1);
            }
        } else {
            if (!idMap.containsKey(name)) {
                System.out.println("Semantic Error: variable '" + name + "' not declared");
                System.exit(1);
            }
            if (!idMap.get(name).equals("object")) {
                System.out.println("Semantic Error: parameter '" + name + "' must be an object");
                System.exit(1);
            }
        }
        
        params.add(name);
        scanner.nextToken();
        
        while (scanner.currentToken() == Core.COMMA) {
            scanner.nextToken();
            if (scanner.currentToken() != Core.ID) {
                System.out.println("ERROR: expected identifier after comma");
                System.exit(1);
            }
            
            name = scanner.getId();
            
            if (isDeclaration) {
                if (params.contains(name)) {
                    System.out.println("Semantic Error: Duplicate parameter name '" + name + "'");
                    System.exit(1);
                }
            } else {
                if (!idMap.containsKey(name)) {
                    System.out.println("Semantic Error: variable '" + name + "' not declared");
                    System.exit(1);
                }
                if (!idMap.get(name).equals("object")) {
                    System.out.println("Semantic Error: parameter '" + name + "' must be an object");
                    System.exit(1);
                }
            }
            
            params.add(name);
            scanner.nextToken();
        }
    }
    
    void print() {
        if (params.isEmpty()) {
            return;
        }
        
        System.out.print(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            System.out.print(", " + params.get(i));
        }
    }
    
    List<String> getParams() {
        return params;
    }
}