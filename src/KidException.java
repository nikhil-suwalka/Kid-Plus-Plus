class InvalidExpression extends Exception {
    public InvalidExpression() {
        super("Invalid Expression");
    }
}

class NotANumber extends Exception {
    public NotANumber() {
        super("Not a number");
    }
}

class CantFindSymbol extends Exception {
    public CantFindSymbol(String data) {
        super("Cant find symbol: " + data);
    }
}

class InvalidOperation extends Exception {
    public InvalidOperation(String data) {
        super("Invalid operation: " + data);
    }
}

class InvalidVariableName extends Exception {
    public InvalidVariableName(String data) {
        super("Invalid variable name: " + data);
    }
}