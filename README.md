CSE 3341 - Project 4 - Procedure Interpreter

FILES SUBMITTED:
1. Main.java - Entry point of the interpreter.
2. Scanner.java - Tokenizes the input code.
3. Core.java - Defines token types for the language.
4. CallStack.java - Implements the call stack for procedure calls.
5. FunctionMap.java - Stores and manages procedure definitions.
6. Function.java - Represents a procedure definition with parameters and body.
7. Call.java - Implements procedure calls.
8. Parameters.java - Handles parameter parsing and validation.
9. Procedure.java - Manages procedure definitions.
10. StmtSeq.java - Represents a sequence of statements.
11. Stmt.java - Base class for statements.
12. If.java - Implements if statements.
13. Loop.java - Implements loop statements.
14. Assign.java - Implements assignment statements.
15. Decl.java - Implements declaration statements.
16. DeclSeq.java - Represents a sequence of declarations.
17. Read.java - Implements read statements.
18. Print.java - Implements print statements.
19. Expr.java - Represents expressions.
20. Term.java - Represents terms in expressions.
21. Factor.java - Represents factors in terms.
22. Cmpr.java - Implements comparison operations.
23. Cond.java - Implements conditional expressions.

DESCRIPTION:
This project implements a simple interpreter for a procedural programming language. The interpreter reads and executes code written in a custom language, supporting procedures, control flow, variable declarations, assignments, and basic I/O operations.

KEY FEATURES:
- Procedure definitions and calls with parameter passing.
- Support for if statements and loops.
- Variable declarations and assignments.
- Basic I/O operations (read and print).
- Expression evaluation with arithmetic and comparison operations.

HOW TO RUN:
1. Compile all Java files using the command:
   javac *.java
2. Run the interpreter using the command:
   java Main <input_file> <data_file>
   - <input_file>: The file containing the source code to interpret.
   - <data_file>: The file containing input data for read statements.

EXAMPLE USAGE:
   java Main program.txt data.txt

NOTES:
- Ensure that the input and data files are correctly formatted.
- The interpreter supports basic error handling and will report syntax or semantic errors during execution.

AUTHOR:
Weilun Zhou
