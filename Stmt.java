import java.util.Map;

public class Stmt {
    Assign assign;
    If ifStmt;
    Loop loop;
    Print print;
    Read read;
    DeclSeq declSeq;
    Call call;

    Stmt() {}

    void parse(Scanner scanner, Map<String, String> idMap) {
        Core currentToken = scanner.currentToken();
        if(currentToken == Core.ID){
            assign = new Assign();
            assign.parse(scanner, idMap);
        }else if(currentToken == Core.IF){
            ifStmt = new If();
            ifStmt.parse(scanner, idMap);
        }else if(currentToken == Core.FOR){
            loop = new Loop();
            loop.parse(scanner, idMap);
        }else if(currentToken == Core.PRINT){
            print = new Print();
            print.parse(scanner, idMap);
        }else if(currentToken == Core.READ){
            read = new Read();
            read.parse(scanner, idMap);
        }else if(currentToken == Core.INTEGER || currentToken == Core.OBJECT){
            declSeq = new DeclSeq();
            declSeq.parse(scanner, idMap); 
        }else if(currentToken == Core.BEGIN){
            call = new Call();
            call.parse(scanner, idMap);
        }else{
            System.out.println("ERROR: invalid statement");
            System.exit(1);
        }
    }

    void print() {
        if (declSeq != null) {
            declSeq.print();
        } else if (assign != null) {
            assign.print();
        } else if (ifStmt != null) {
            ifStmt.print();
        } else if (loop != null) {
            loop.print();
        } else if (print != null) {
            print.print();
        } else if (read != null) {
            read.print();
        } else if (call != null) {
            call.print();
        }
    }

    void execute(Scanner data, Map<String, int[]> memory, Map<String, Function> funcMap) {
        if (declSeq != null) {
            declSeq.execute(data, memory);
        } else if (assign != null) {
            assign.execute(data, memory);
        } else if (ifStmt != null) {
            ifStmt.execute(data, memory, funcMap);
        } else if (loop != null) {
            loop.execute(data, memory, funcMap);
        } else if (print != null) {
            print.execute(data, memory);
        } else if (read != null) {
            read.execute(data, memory);
        } else if (call != null) {
            call.execute(data, memory, funcMap);
        }
    }
}