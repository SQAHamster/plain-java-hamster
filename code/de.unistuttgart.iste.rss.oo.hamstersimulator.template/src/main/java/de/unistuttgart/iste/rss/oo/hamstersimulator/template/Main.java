package de.unistuttgart.iste.rss.oo.hamstersimulator.template;

public class Main {

    public static void main(String[] args) {
        try {
            new MyHamsterProgram().doRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
