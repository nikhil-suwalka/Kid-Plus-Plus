import java.util.EmptyStackException;
import java.util.Stack;


public class ExpEval
{

    public static boolean countDot(String data){
        int count=0;
        for (char c:data.toCharArray()){
            if(c=='.')
                count++;
        }
        return count<=1;
    }
    public static Double evaluate(String expression) throws InvalidExpression, NotANumber {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators: 'ops' 
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number, push it to stack for numbers 
            if ((tokens[i] >= '0' && tokens[i] <= '9')||(tokens[i] == '.'))
            {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number 
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9')||(tokens[i] == '.')))
                    sbuf.append(tokens[i++]);
                if(sbuf.toString().contains(".") && !countDot(sbuf.toString()))
                    throw new NotANumber();
                try {
                    values.push(Double.parseDouble(sbuf.toString()));
                }catch (NumberFormatException e){
                    throw new NotANumber();
                }
            }

            // Current token is an opening brace, push it to 'ops' 
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }

            // Current token is an operator. 
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '%')
            {
                // While top of 'ops' has same or greater precedence to current 
                // token, which is an operator. Apply operator on top of 'ops' 
                // to top two elements in values stack 
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                // Push current token to 'ops'. 
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining 
        // ops to remaining values
        try {
            while (!ops.empty())
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));

            // Top of 'values' contains result, return it
            return values.pop();
        }catch (EmptyStackException e){
            throw new InvalidExpression();
        }
    }

    // Returns true if 'op2' has higher or same precedence as 'op1', 
    // otherwise returns false. 
    public static boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/' || op1 == '%' ) && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'  
    // and 'b'. Return the result. 
    public static double applyOp(char op, double b, double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case '%':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot mod by zero");
                return a % b;
        }
        return 0;
    }

//    // Driver method to test above methods
//    public static void main(String[] args)
//    {
//
//        try {
//            System.out.println(ExpEval.evaluate("10 * 2 - 3.5 + ( 100 % 13 )"));
//            System.out.println(ExpEval.evaluate("100 * 2 + 12"));
//            System.out.println(ExpEval.evaluate("100 * ( 2 + 12 )"));
//            System.out.println(ExpEval.evaluate("100 * ( 2 + 12 ) / 14"));
//        } catch (InvalidExpression | NotANumber invalidExpression) {
//            invalidExpression.printStackTrace();
//        }
//
//    }
}