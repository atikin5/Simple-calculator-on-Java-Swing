import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

public class Calculator extends JFrame{
    private final StackCalc calc = new StackCalc();
    private JPanel panel;
    private JPanel inputPanel;
    private JPanel buttonPanel;
    private JTextField textField1;
    private JButton d0Button;
    private JButton d1Button;
    private JButton d2Button;
    private JButton d3Button;
    private JButton d4Button;
    private JButton d5Button;
    private JButton d6Button;
    private JButton d7Button;
    private JButton d8Button;
    private JButton d9Button;
    private JButton cButton;
    private JButton bSButton;
    private JButton openBraceButton;
    private JButton closeBraceButton;
    private JButton divButton;
    private JButton mulButton;
    private JButton plusButton;
    private JButton minusButton;
    private JButton powButton;
    private JButton factorButton;
    private JButton lnButton;
    private JButton lgButton;
    private JButton tanButton;
    private JButton cosButton;
    private JButton sinButton;
    private JButton resultButton;
    private JButton dotButton;
    private JButton ansButton;
    private JButton piButton;
    private JButton eButton;
    private boolean exception = false;

    private final List<Character> symbols = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '+', '-', '*', '/', '^', '!', '(', ')');

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        textField1.addKeyListener(fieldListener);
        d0Button.addKeyListener(keyListener);
        d1Button.addKeyListener(keyListener);
        d2Button.addKeyListener(keyListener);
        d3Button.addKeyListener(keyListener);
        d4Button.addKeyListener(keyListener);
        d5Button.addKeyListener(keyListener);
        d6Button.addKeyListener(keyListener);
        d7Button.addKeyListener(keyListener);
        d8Button.addKeyListener(keyListener);
        d9Button.addKeyListener(keyListener);
        cButton.addKeyListener(keyListener);
        bSButton.addKeyListener(keyListener);
        openBraceButton.addKeyListener(keyListener);
        closeBraceButton.addKeyListener(keyListener);
        divButton.addKeyListener(keyListener);
        mulButton.addKeyListener(keyListener);
        plusButton.addKeyListener(keyListener);
        minusButton.addKeyListener(keyListener);
        powButton.addKeyListener(keyListener);
        factorButton.addKeyListener(keyListener);
        eButton.addKeyListener(keyListener);
        ansButton.addKeyListener(keyListener);
        piButton.addKeyListener(keyListener);
        resultButton.addKeyListener(keyListener);
        dotButton.addKeyListener(keyListener);

        bSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bS();
            }
        });
        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText("");
            }
        });
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operate();
            }
        });
        ansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                click(calc.getAns());
            }
        });

        setVisible(true);
    }

    private void operate() {
        try {
            String res = calc.calculate(textField1.getText());
            textField1.setText(res);
        } catch (Exception e1) {
            exception = true;
            textField1.setText(e1.getMessage());
        }
    }

    private void bS() {
        if (exception) {
            textField1.setText("");
            exception = false;
            return;
        }
        if (!textField1.getText().isEmpty()) {
            textField1.setText(textField1.getText().substring(0, textField1.getText().length() - 1));
        }
    }

    private void click(String s) {
        if (exception) {
            textField1.setText("");
            exception = false;
        }
        textField1.setText(textField1.getText() +  s);

    }

    KeyListener fieldListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (exception) {
                textField1.setText("");
                exception = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                operate();
            }
            if (e.getKeyCode() == KeyEvent.VK_C) {
                textField1.setText("");
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!exception && e.getKeyCode() == KeyEvent.VK_ENTER) {
                operate();
                return;
            }
            if (exception) {
                textField1.setText("");
                exception = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_C) {
                textField1.setText("");
            }
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                bS();
            }
            if (symbols.contains(e.getKeyChar())) {
                click(String.valueOf(e.getKeyChar()));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    };

    private class JMyButton extends JButton {
        public JMyButton() {
            super();
            this.addKeyListener(keyListener);
        }
    }

    private class JSymbolsButton extends JMyButton {
        public JSymbolsButton(String symbols) {
            super();
            this.addKeyListener(keyListener);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    click(symbols);
                }
            });
        }
    }

    private void createUIComponents() {
        openBraceButton = new JSymbolsButton("(");
        closeBraceButton = new JSymbolsButton(")");
        d0Button = new JSymbolsButton("0");
        d1Button = new JSymbolsButton("1");
        d2Button = new JSymbolsButton("2");
        d3Button = new JSymbolsButton("3");
        d4Button = new JSymbolsButton("4");
        d5Button = new JSymbolsButton("5");
        d6Button = new JSymbolsButton("6");
        d7Button = new JSymbolsButton("7");
        d8Button = new JSymbolsButton("8");
        d9Button = new JSymbolsButton("9");
        dotButton = new JSymbolsButton(".");
        divButton = new JSymbolsButton("/");
        mulButton = new JSymbolsButton("*");
        plusButton = new JSymbolsButton("+");
        minusButton = new JSymbolsButton("-");
        powButton = new JSymbolsButton("^");
        factorButton = new JSymbolsButton("!");
        eButton = new JSymbolsButton("");
        piButton = new JSymbolsButton("");
        cButton = new JMyButton();
        bSButton = new JMyButton();
        ansButton = new JMyButton();
        resultButton = new JMyButton();
    }
}
