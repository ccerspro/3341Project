CSE 3341 - Project 4 - Procedure Interpreter

FILES SUBMITTED:
1. Assign.java – Implements assignment statements.
2. Call.java – Implements procedure calls.
3. CallStack.java – Implements the call stack for procedure calls.
4. Cond.java – Implements conditional expressions (==, <, and, or, not).
5. Core.java – Defines token types used by the language and parser.
6. Decl.java – Implements variable declarations (integer, object).
7. DeclSeq.java – Represents a sequence of declarations.
8. Expr.java – Represents arithmetic expressions (+, -).
9. Factor.java – Represents atomic elements in expressions (constants, variables, sub-expressions).
10. Function.java – Represents a user-defined procedure with parameters and a statement body.
11. FunctionMap.java – Stores and manages all function definitions. (Can be removed if merged into Function.java.)
12. If.java – Implements conditional control flow (if-then[-else]).
13. Loop.java – Implements for-loops with initialization, condition, and increment.
14. Main.java – Entry point of the interpreter. Reads and executes input code.
15. Parameters.java – Handles parameter parsing and validation for procedures.
16. Print.java – Implements output statements (e.g., print(expr);).
17. Procedure.java – Manages the main program procedure and top-level declarations/statements.
18. Read.java – Implements input statements (e.g., read(id);).
19. Scanner.java – Tokenizes the input program into recognizable language tokens.
20. Stmt.java – Represents a single statement (dispatches to the correct type).
21. StmtSeq.java – Represents a sequence of statements (the body of procedures and blocks).
22. Term.java – Represents sub-expressions within Expr that use * and / operators.

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
