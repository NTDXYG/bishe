package com.yg.bishe.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyUtil {
    // 验证码字符集
    private static final char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int SIZE = 4;
    private static final int LINES = 5;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final int FONT_SIZE = 30;
    public static Object[] createImage() {
        StringBuffer sb = new StringBuffer();
        BufferedImage image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = image.getGraphics();
        graphic.setColor(Color.LIGHT_GRAY);
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        Random ran = new Random();
        for (int i = 0; i < SIZE; i++) {
            int n = ran.nextInt(chars.length);
            graphic.setColor(getRandomColor());
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            graphic.drawString(
                    chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
            sb.append(chars[n]);
        }
        for (int i = 0; i < LINES; i++) {
            graphic.setColor(getRandomColor());
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
                    ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        return new Object[]{sb.toString(), image};
    }

    public static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
        return color;
    }
}