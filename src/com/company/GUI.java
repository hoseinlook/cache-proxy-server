package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI  {
    JFrame jFrame;
    JPanel main_panel;
    JPanel toppanel;
    JTextField text_input;
    JButton add_btn;
    JButton remove_btn;
    JTextArea text_area_blocked_urls;
    public GUI() {

        this.jFrame=new JFrame("PROXYYYYYY");
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main_panel = new JPanel();

        this.jFrame.setLayout(new BorderLayout());

        this.jFrame.setSize(1000, 1000);

        main_panel.setLayout(new BorderLayout());

        toppanel = new JPanel();
        toppanel.setLayout(new BorderLayout());

        text_input = new JTextField();
        add_btn = new JButton("             add new url");
        remove_btn = new JButton("remove url                 ");
        text_area_blocked_urls = new JTextArea();
        text_area_blocked_urls.setText(Configs.getUrlsAsStr());


        add_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("wtf");
                String text = text_input.getText();
                if (!text.equals("")) {
                    Configs.BLOCKED_URL.add(text);
                }
                refresh();
                text_area_blocked_urls.repaint();
            }
        });

        remove_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("wtf");
                String text = text_input.getText();
                if (!text.equals("")) {
                    Configs.BLOCKED_URL.remove(text);
                }
                refresh();
                text_area_blocked_urls.repaint();
            }
        });
        this.jFrame.add(main_panel, BorderLayout.CENTER);
        main_panel.add(toppanel, BorderLayout.NORTH);
        main_panel.add(text_area_blocked_urls, BorderLayout.CENTER);
        toppanel.add(add_btn, BorderLayout.WEST);
        toppanel.add(text_input, BorderLayout.CENTER);
        toppanel.add(remove_btn, BorderLayout.EAST);
        this.jFrame.setVisible(true);
    }

    private void refresh(){
        this.text_area_blocked_urls.setText(Configs.getUrlsAsStr());
    }


}
