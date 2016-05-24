package br.edu.ifce.ppd.chat.view.helper;

import javax.swing.ImageIcon;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class Assets {

    private static ImageIcon chat;
    private static ImageIcon online;
    private static ImageIcon offline;

    static {
        chat = new ImageIcon(Assets.class.getClassLoader().getResource("chat.png"));
        online = new ImageIcon(Assets.class.getClassLoader().getResource("online.png"));
        offline = new ImageIcon(Assets.class.getClassLoader().getResource("offline.png"));
    }

    public static ImageIcon chat() {
        return chat;
    }
    public static ImageIcon online() {
        return online;
    }

    public static ImageIcon offline() {
        return offline;
    }
}
