import java.util.HashMap;
import java.util.Map;

public class Loop {
    String loopVar;
    Expr initExpr;
    Cond loopCond;
    Expr incrementExpr;
    StmtSeq loopBody;
    Map<String, String> localIdMap = new HashMap<>();

    Loop() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        localIdMap.putAll(idMap);
        scanner.nextToken(); // Skip 'for'
        
        if (scanner.currentToken() != Core.LPAREN) {
            System.out.println("ERROR: expected (");
            System.exit(1);
        }
        scanner.nextToken();
        
        if (scanner.currentToken() != Core.ID) {
            System.out.println("ERROR: expected id");
            System.exit(1);
        }
        loopVar = scanner.getId();
        
        if (!idMap.containsKey(loopVar)) {
            System.out.println("Semantic Error: variable '" + loopVar + "' not declared");
            System.exit(1);
        }
        
        scanner.nextToken();
        
        if (scanner.currentToken() != Core.ASSIGN) {
            System.out.println("ERROR: expected =");
            System.exit(1);
        }
        scanner.nextToken();
        
        initExpr = new Expr();
        initExpr.parse(scanner, localIdMap);
        
        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected ;");
            System.exit(1);
        }
        scanner.nextToken();
        
        loopCond = new Cond();
        loopCond.parse(scanner, localIdMap);
        
        if (scanner.currentToken() != Core.SEMICOLON) {
            System.out.println("ERROR: expected ;");
            System.exit(1);
        }
        scanner.nextToken();
        
        incrementExpr = new Expr();
        incrementExpr.parse(scanner, localIdMap);
        
        if (scanner.currentToken() != Core.RPAREN) {
            System.out.println("ERROR: expected )");
            System.exit(1);
        }
        scanner.nextToken();
        
        if (scanner.currentToken() != Core.DO) {
            System.out.println("ERROR: expected do");
            System.exit(1);
        }
        scanner.nextToken();
        
        loopBody = new StmtSeq();
        loopBody.parse(scanner, localIdMap);
        
        if (scanner.currentToken() != Core.END) {
            System.out.println("ERROR: expected end");
            System.exit(1);
        }
        scanner.nextToken();
    }

    void print() {
        System.out.print("for (");
        System.out.print(loopVar + " = ");
        initExpr.print();
        System.out.print("; ");
        loopCond.print();
        System.out.print("; ");
        incrementExpr.print();
        System.out.println(") do");
        StmtSeq.increaseIndent();
        loopBody.print();
        StmtSeq.decreaseIndent();
        for (int i = 0; i < StmtSeq.indentLevel; i++) {
            System.out.print("    ");
        }
        System.out.println("end");
    }

    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        int[] var = memory.get(loopVar);
        if (var == null) {
            var = new int[1];
            memory.put(loopVar, var);
        }
        
        var[0] = initExpr.execute(data, memory);
        
        while (loopCond.execute(data, memory)) {
            loopBody.execute(data, memory, funcMap);
            var[0] = incrementExpr.execute(data, memory);
        }
    }
}