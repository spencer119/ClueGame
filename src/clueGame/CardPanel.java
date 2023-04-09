package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CardPanel extends JPanel {
    public CardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(3, 0));
        JPanel peoplePanel = new JPanel();
        JPanel roomsPanel = new JPanel();
        JPanel weaponsPanel = new JPanel();
        peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));


    }

    public static void main(String[] args) {
        CardPanel cardPanel = new CardPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        
    }
}
