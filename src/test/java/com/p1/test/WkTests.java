package com.p1.test;

import java.io.IOException;

public class WkTests {
    public static void main(String[] args) {
        String cmd = "d:/work/wkhtmltopdf/bin/wkhtmltoimage   https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/basis/java%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93.md d:/work/data/wk-images/guide.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
