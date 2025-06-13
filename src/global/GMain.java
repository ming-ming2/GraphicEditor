package global;

import frames.GMainFrame;

public class GMain {

	public static void main(String[] args) {
		GConstants constants = new GConstants();
		constants.readFromFile("config.xml");

		GMainFrame mainFrame = new GMainFrame();
		mainFrame.initialize();
	}
}