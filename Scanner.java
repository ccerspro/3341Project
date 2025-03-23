import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

class Scanner {
    String idPattern = "[a-zA-Z][a-zA-Z0-9]*";
    String constantPattern = "[0-9]|[1-9][0-9]*";
    BufferedReader reader;
    StringBuilder tokenBuilder;
    public Core currentToken;

    Scanner(String filename) {
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
        }
        this.currentToken = this.nextToken();
    }

    public Core nextToken() {
        try {
            int c = this.reader.read();
            while (Character.isWhitespace(c) && c != -1) {
                c = this.reader.read();
            }
            if (c == -1) {
                this.currentToken = Core.EOS;
            } else {
                switch ((char) c) {
                    case '+':
                        this.currentToken = Core.ADD;
                        break;

                    case '-':
                        this.currentToken = Core.SUBTRACT;
                        break;

                    case '*':
                        this.currentToken = Core.MULTIPLY;
                        break;

                    case '/':
                        this.currentToken = Core.DIVIDE;
                        break;

                    case '=':
                        this.reader.mark(1);
                        int nextChar = this.reader.read();
                        if ((char) nextChar == '=') {
                            this.currentToken = Core.EQUAL;
                        } else {
                            this.reader.reset();
                            this.currentToken = Core.ASSIGN;
                        }
                        break;

                    case '<':
                        this.currentToken = Core.LESS;
                        break;

                    case ';':
                        this.currentToken = Core.SEMICOLON;
                        break;

                    case '.':
                        this.currentToken = Core.PERIOD;
                        break;

                    case ',':
                        this.currentToken = Core.COMMA;
                        break;

                    case '(':
                        this.currentToken = Core.LPAREN;
                        break;

                    case ')':
                        this.currentToken = Core.RPAREN;
                        break;

                    case '[':
                        this.currentToken = Core.LBRACE;
                        break;

                    case ']':
                        this.currentToken = Core.RBRACE;
                        break;

                    case ':':
                        this.currentToken = Core.COLON;
                        break;

                    default: {
                        boolean continued = true;
                        this.tokenBuilder = new StringBuilder();
                        if (Character.isDigit((char) c)) {
                            while (continued) {
                                this.tokenBuilder.append((char) c);
                                this.reader.mark(1);
                                c = this.reader.read();
                                continued = c != -1
                                        && Character.isDigit((char) c);
                                if (!continued) {
                                    this.reader.reset();
                                }
                            }
                        }
                        else if (Character.isLetter((char) c)) {
                            while (continued) {
                                this.tokenBuilder.append((char) c);
                                this.reader.mark(1);
                                c = this.reader.read();
                                continued = c != -1
                                        && Character.isLetterOrDigit((char) c);
                                if (!continued) {
                                    this.reader.reset();
                                }
                            }
                        }
                        else {
                            this.tokenBuilder.append((char) c);
                        }

                        switch (this.tokenBuilder.toString()) {
                            case "and":
                                this.currentToken = Core.AND;
                                break;

                            case "begin":
                                this.currentToken = Core.BEGIN;
                                break;

                            case "do":
                                this.currentToken = Core.DO;
                                break;

                            case "else":
                                this.currentToken = Core.ELSE;
                                break;

                            case "end":
                                this.currentToken = Core.END;
                                break;

                            case "if":
                                this.currentToken = Core.IF;
                                break;

                            case "integer":
                                this.currentToken = Core.INTEGER;
                                break;

                            case "is":
                                this.currentToken = Core.IS;
                                break;

                            case "new":
                                this.currentToken = Core.NEW;
                                break;

                            case "not":
                                this.currentToken = Core.NOT;
                                break;

                            case "or":
                                this.currentToken = Core.OR;
                                break;

                            case "print":
                                this.currentToken = Core.PRINT;
                                break;

                            case "procedure":
                                this.currentToken = Core.PROCEDURE;
                                break;

                            case "object":
                                this.currentToken = Core.OBJECT;
                                break;

                            case "string":
                                this.currentToken = Core.STRING;
                                break;

                            case "then":
                                this.currentToken = Core.THEN;
                                break;

                            case "for":
                                this.currentToken = Core.FOR;
                                break;

                            case "read":
                                this.currentToken = Core.READ;
                                break;

                            default: {
                                if (this.tokenBuilder.toString().matches(this.idPattern)) {
                                    this.currentToken = Core.ID;

                                }
                                else if (this.tokenBuilder.toString()
                                        .matches(this.constantPattern)
                                        && Integer.parseInt(
                                                this.tokenBuilder.toString()) < 100003) {
                                    this.currentToken = Core.CONST;
                                }
                                else {
                                    throw new Exception();
                                }
                                break;
                            }

                        }
                        break;
                    }

                    case '\'': {
                        this.tokenBuilder = new StringBuilder();
                        c = this.reader.read();
                        while (c != -1 && (char)c != '\'') {
                            this.tokenBuilder.append((char)c);
                            c = this.reader.read();
                        }
                        if (c == -1 || (char)c != '\'') {
                            throw new Exception();
                        }
                        this.currentToken = Core.STRING;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: Invalid input " + this.tokenBuilder.toString());
            this.currentToken = Core.ERROR;
        }
        return this.currentToken;
    }

    public Core currentToken() {
        return this.currentToken;
    }

    public String getId() {
        return this.tokenBuilder.toString();
    }

    public int getConst() {
        return Integer.parseInt(this.tokenBuilder.toString());
    }

    public void setId(String id) {
        this.tokenBuilder = new StringBuilder(id);
    }

}