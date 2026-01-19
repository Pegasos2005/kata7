package software.ulpgc.is2.kata7;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Kata 7");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("¡Kata 7 Funcionando!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(label);

        frame.setVisible(true);
    }
}
