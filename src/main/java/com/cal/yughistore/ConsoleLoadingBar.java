package com.cal.yughistore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsoleLoadingBar {

    private final int width = 40;
    private int lastPercent = -1;

    public void printProgress(int current, int total) {

        int percent = (int)((current * 100.0) / total);

        if (percent == lastPercent) {
            return;
        }

        lastPercent = percent;

        int progress = (percent * width) / 100;

        StringBuilder bar = new StringBuilder();

        bar.append("\r[");
        for (int i = 0; i < width; i++) {
            bar.append(i < progress ? "#" : " ");
        }
        bar.append("] ").append(percent).append("% (")
                .append(current).append("/").append(total).append(")");

        System.out.print(bar);
        try {
            Thread.sleep(0,1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void finish() {
        System.out.println("Done!");
    }
}
