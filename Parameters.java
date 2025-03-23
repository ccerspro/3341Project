import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles parsing and validation of function parameters.
 */
public class Parameters {
    private final List<String> params = new ArrayList<>();

    public Parameters() {}

    public void parse(Scanner scanner, Map<String, String> idMap, boolean isDeclaration) {
        parseParameter(scanner, idMap, isDeclaration);

        while (scanner.currentToken() == Core.COMMA) {
            scanner.nextToken();
            parseParameter(scanner, idMap, isDeclaration);
        }
    }

    private void parseParameter(Scanner scanner, Map<String, String> idMap, boolean isDeclaration) {
        if (scanner.currentToken() != Core.ID) {
            error("expected identifier");
        }

        String name = scanner.getId();

        if (isDeclaration) {
            if (params.contains(name)) {
                semanticError("Duplicate parameter name '" + name + "'");
            }
        } else {
            if (!idMap.containsKey(name)) {
                semanticError("variable '" + name + "' not declared");
            }
            if (!"object".equals(idMap.get(name))) {
                semanticError("parameter '" + name + "' must be an object");
            }
        }

        params.add(name);
        scanner.nextToken();
    }

    public void print() {
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(params.get(i));
        }
    }

    public List<String> getParams() {
        return params;
    }

    private void error(String msg) {
        System.out.println("ERROR: " + msg);
        System.exit(1);
    }

    private void semanticError(String msg) {
        System.out.println("Semantic Error: " + msg);
        System.exit(1);
    }
}
