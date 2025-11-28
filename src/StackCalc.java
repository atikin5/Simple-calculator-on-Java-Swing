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
    private final List<String> manySymbolsOperations = Arrays.asList("ln", "lg", "sin", "cos", "tan");
    private final List<String> unaryOperations = Arrays.asList("lg", "lg" ,"sin", "cos", "tan");

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
        parse(input);
        if (argStack.size() == 1) {
            ans = argStack.peek();
            return returnNumber(argStack.pop());
        }
        throw new IllegalArgumentException("Illegal number of operations");
    }

    private static class NumberBuilder{
        private final StringBuilder number;
        private boolean unaryMinus;
        private boolean bigSizeNumber;
        private NumberBuilder() {
            number = new StringBuilder();
            unaryMinus = false;
            bigSizeNumber = false;
        }
        private void add(char symbol) {
            number.append(symbol);
        }
        private double build() {
            try {
                unaryMinus = false;
                bigSizeNumber = false;
                double ans = Double.parseDouble(number.toString());
                number.setLength(0);
                return ans;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Illegal number format: " + number);
            }

        }
        private boolean isNotEmpty() {
            return !number.isEmpty();
        }
    }

    private void parse(String input) {
        NumberBuilder numberBuilder = new NumberBuilder();
        StringBuilder op = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == ' ')
                continue;
            if (c == 'E') {
                numberBuilder.bigSizeNumber = true;
                numberBuilder.add(c);
                continue;
            }
            if (Character.isDigit(c) || c == '.' || (c == '-' && numberBuilder.bigSizeNumber)) {
                numberBuilder.add(c);
                numberBuilder.unaryMinus = false;
                numberBuilder.bigSizeNumber = false;
            } else {
                if (numberBuilder.isNotEmpty())
                    argStack.push(numberBuilder.build());
                switch (c) {
                    case '-':
                        if (numberBuilder.unaryMinus) {
                            argStack.push(-1.0);
                            operateMultiply("*");
                            numberBuilder.unaryMinus = false;
                        } else {
                            operateMultiply(String.valueOf(c));
                        }
                        break;
                    case '+', '*', '/', ')', '^':
                        numberBuilder.unaryMinus = false;
                        operateMultiply(String.valueOf(c));
                        break;
                    case '(':
                        numberBuilder.unaryMinus = true;
                        operateMultiply(String.valueOf(c));
                        break;
                    case 'π':
                        numberBuilder.unaryMinus = false;
                        argStack.push(Math.PI);
                        break;
                    case 'e':
                        numberBuilder.unaryMinus = false;
                        argStack.push(Math.E);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal operation: " + c);
                }
            }
        }
        if (numberBuilder.isNotEmpty()) {
            argStack.push(numberBuilder.build());
        }
        while (!opStack.isEmpty()) {
            if (opStack.peek().equals("("))
                throw new IllegalArgumentException("Unclosed braces");
            operate();
        }
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
        String operation = opStack.pop();
        if (argStack.isEmpty()) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        if (manySymbolsOperations.contains(operation)) {
            double a = argStack.pop();
            double res = switch (operation) {
                case "㏑" -> {
                    if (a < 0)
                        throw new ArithmeticException("Logarithm of negative number");
                    yield Math.log(a);
                }
                case "#" -> {
                    if (a < 0)
                        throw new ArithmeticException("Logarithm of negative number");
                    yield Math.log10(a);
                }
                default -> throw new IllegalArgumentException("Illegal operation: " + operation);
            };
            argStack.push(res);
            return;
        }
        if (argStack.size() < 2) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        double a = argStack.pop();
        double b = argStack.pop();
        double res = switch (operation) {
            case "+" -> b + a;
            case "-" -> b - a;
            case "*" -> b * a;
            case "/" -> {
                if (a == 0.0)
                    throw new ArithmeticException("Division by zero");
                yield b / a;
            }
            case "^" -> Math.pow(b, a);
            default -> throw new IllegalArgumentException("Illegal operation: " + operation);
        };
        argStack.push(res);
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
