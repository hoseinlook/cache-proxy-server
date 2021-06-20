package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        try {
        File file=new File("cached_files/mihan.png");
        BufferedImage screenshot = ImageIO.read(file);

            ImageIO.write(screenshot, "png", new File("cached_files/screenshot2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
