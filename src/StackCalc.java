import java.util.*;

public class StackCalc {

    private double ans;

    private final Deque<Double> argStack = new ArrayDeque<>();
    private final Deque<String> opStack = new ArrayDeque<>();
    private final Map<String, Integer> opPriorities = Map.of (
            "(", 1,
            ")", 2,
            "+", 3,
            "-", 3,
            "*", 4,
            "/", 4,
            "^", 4);

    public static void main(String[] args) {
        StackCalc calc = new StackCalc();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        System.out.println(calc.calculate(input));
    }

    public String calculate(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        boolean unaryMinus = true;
        StringBuilder num = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == ' ')
                continue;
            if (Character.isDigit(c) || c == '.') {
                num.append(c);
                unaryMinus = false;
            } else {
                if (!num.isEmpty()) {
                    if (num.charAt(num.length() - 1) == '.') {
                        throw new NumberFormatException("Point in the end of number : " + num);
                    }
                    double numVal = Double.parseDouble(num.toString());
                    argStack.push(numVal);
                    num.setLength(0);
                }
                switch (c) {
                    case '-':
                        if (unaryMinus) {
                            argStack.push(-1.0);
                            operateMultiply("*");
                            unaryMinus = false;
                        } else {
                            operateMultiply(String.valueOf(c));
                        }
                        break;
                    case '+', '*', '/', ')', '^':
                        unaryMinus = false;
                        operateMultiply(String.valueOf(c));
                        break;
                    case '(':
                        unaryMinus = true;
                        operateMultiply(String.valueOf(c));
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal operation: " + c);
                }
            }
        }
        if (!num.isEmpty()) {
            if (num.charAt(num.length() - 1) == '.')
                throw new NumberFormatException("Point in the end of number : " + num);
            double numVal = Double.parseDouble(num.toString());
            argStack.push(numVal);
        }
        while (!opStack.isEmpty()) {
            if (opStack.peek().equals("("))
                throw new IllegalArgumentException("Unclosed braces");
            operate();
        }
        if (argStack.size() == 1) {
            ans = argStack.peek();
            return returnNumber(argStack.pop());
        }
        throw new IllegalArgumentException("Illegal number of operations");
    }

    private void operateMultiply(String op) {
        if (argStack.size() > 1 && !opStack.isEmpty()) {
            boolean operateNext = opPriorities.get(opStack.peek()) >= opPriorities.get(op);
            while (operateNext) {
                operate();
                if (!opStack.isEmpty())
                    operateNext =  argStack.size() > 1 && opPriorities.get(opStack.peek()) >= opPriorities.get(op);
                else
                    operateNext = false;
            }
        }
        if (op.equals(")")) {
            if (!opStack.isEmpty()) {
                if (opStack.peek().equals("("))
                    opStack.pop();
                else
                    throw new IllegalArgumentException("Unopened braces");
            } else
                throw new IllegalArgumentException("Unopened braces");
        }
        else
            opStack.push(op);
    }

    private void operate() {
        double a = argStack.pop();
        double b = argStack.pop();
        String operation = opStack.pop();
        argStack.push(applyOperation(operation, a, b));
    }

    private double applyOperation(String operation, double a, double b) {
        switch (operation) {
            case "+":
                return b + a;
            case "-":
                return b - a;
            case "*":
                return b * a;
            case "/":
                if (a == 0.0)
                    throw new ArithmeticException("Division by zero");
                return b / a;
            case "^":
                return Math.pow(b, a);
            default:
                throw new IllegalArgumentException("Illegal operation: " + operation);
        }
    }

    public String getAns() {
        return returnNumber(ans);
    }

    private String returnNumber(double num) {
        String ans = String.valueOf(num);
        if (ans.charAt(ans.length() - 1) == '0') {
            ans = ans.substring(0, ans.length() - 2);
        }
        return ans;
    }
}
