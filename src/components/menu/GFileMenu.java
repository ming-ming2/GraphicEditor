package components.menu;

import components.GComponent;

import javax.swing.*;
import java.awt.*;

public class GFileMenu extends JMenu implements GComponent {
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem loadImageMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem saveAsImageMenuItem;
    private JMenuItem exitMenuItem;
    @Override
    public void createComponents() {
        newMenuItem = new JMenuItem("새로 만들기");
        openMenuItem = new JMenuItem("열기");
        loadImageMenuItem = new JMenuItem("배경 이미지 불러오기");
        saveMenuItem = new JMenuItem("저장");
        saveAsMenuItem = new JMenuItem("다른 이름으로 저장");
        saveAsImageMenuItem = new JMenuItem("이미지로 저장");
        exitMenuItem = new JMenuItem("끝내기");
    }

    @Override
    public void setAttributes() {

    }

    @Override
    public void arrangeComponents() {
        this.add(newMenuItem);
        this.add(openMenuItem);
        this.add(loadImageMenuItem);
        this.add(new JSeparator());
        this.add(saveMenuItem);
        this.add(saveAsMenuItem);
        this.add(saveAsImageMenuItem);
        this.add(new JSeparator());
        this.add(exitMenuItem);
    }

    @Override
    public void addEventHandler() {
        Font menuFont = new Font("맑은 고딕", Font.PLAIN, 12);
        this.setFont(menuFont);
    }

    @Override
    public void initialize() {

    }
}
