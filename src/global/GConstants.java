package global;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import shapes.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.KeyStroke;

public final class GConstants {

    public final static int TEXT_MARGIN = 5;

    public void readFromFile(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(fileName);
            Document document = builder.parse(file);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i=0; i<nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals(EMainFrame.class.getSimpleName())) {
                        EMainFrame.setValues(node);
                    } else if (node.getNodeName().equals(EFileMenuItem.class.getSimpleName())) {
                        EFileMenuItem.setValues(node);
                    } else if (node.getNodeName().equals(EEditMenuItem.class.getSimpleName())) {
                        EEditMenuItem.setValues(node);
                    } else if (node.getNodeName().equals(EGraphicsMenuItem.class.getSimpleName())) {
                        EGraphicsMenuItem.setValues(node);
                    } else if (node.getNodeName().equals(EShapeTool.class.getSimpleName())) {
                        EShapeTool.setValues(node);
                    } else if (node.getNodeName().equals(ETextConstants.class.getSimpleName())) {
                        ETextConstants.setValues(node);
                    }
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
    }

    public enum EMainFrame {
        eX(100), eY(100), eW(1200), eH(600);

        private int value;
        EMainFrame(int value) {
            this.value = value;
        }

        public static void setValues(Node node){
            for(EMainFrame eMainFrame : EMainFrame.values()){
                Node attribute = node.getAttributes().getNamedItem(eMainFrame.name());
                if (attribute != null) {
                    eMainFrame.value = Integer.parseInt(attribute.getNodeValue());
                }
            }
        }

        public int getValues() {
            return value;
        }
    }

    public static enum EAnchor {
        eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
        eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
        eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
        eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
        eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        eRR(new Cursor(Cursor.CROSSHAIR_CURSOR)),
        eMM(new Cursor(Cursor.MOVE_CURSOR));

        private Cursor cursor;

        EAnchor(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return this.cursor;
        }
    }

    public enum EShapeTool {
        eSelect(null, GShape.EPoints.e2P, GRectangle.class),
        eRectangle(null, GShape.EPoints.e2P, GRectangle.class),
        eEllipse(null, GShape.EPoints.e2P, GEllipse.class),
        eLine(null, GShape.EPoints.e2P, GLine.class),
        ePolygon(null, GShape.EPoints.eNP, GPolygon.class),
        eTextBox(null, GShape.EPoints.e2P, GTextBox.class);

        private String name;
        private GShape.EPoints ePoints;
        private Class<?> classShape;

        private EShapeTool(String name, GShape.EPoints ePoints, Class<?> classShape) {
            this.name = name;
            this.ePoints = ePoints;
            this.classShape = classShape;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EShapeTool eShapeTool = EShapeTool.valueOf(element.getNodeName());
                        Node labelAttr = element.getAttributes().getNamedItem("label");
                        if (labelAttr != null) {
                            eShapeTool.name = labelAttr.getNodeValue();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getName() {
            return this.name;
        }

        public GShape.EPoints getEPoints() {
            return this.ePoints;
        }

        public GShape newShape() {
            try {
                GShape shape = (GShape) classShape.getConstructor().newInstance();
                return shape;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public enum EFileMenuItem {
        eNew(null, "newPanel", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)),
        eOpen(null, "open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)),
        eSave(null, "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)),
        eSaveAs(null, "saveAs", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
        ePrint(null, "print", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK)),
        eClose(null, "close", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK)),
        eQuit(null, "quit", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

        private String name;
        private String methodName;
        private KeyStroke accelerator;
        private String toolTipText;
        private static String defaultPathName = "/Users/abcd/IdeaProjects/GraphicEditor_project";

        EFileMenuItem(String name, String methodName, KeyStroke accelerator) {
            this.name = name;
            this.methodName = methodName;
            this.accelerator = accelerator;
            this.toolTipText = null;
        }

        public static void setValues(Node node) {
            Node defaultPathAttr = node.getAttributes().getNamedItem("defaultPathName");
            if (defaultPathAttr != null) {
                defaultPathName = defaultPathAttr.getNodeValue();
            }

            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EFileMenuItem eFileMenuItem = EFileMenuItem.valueOf(element.getNodeName());
                        Node labelAttr = element.getAttributes().getNamedItem("label");
                        Node toolTipAttr = element.getAttributes().getNamedItem("toolTipText");
                        if (labelAttr != null) {
                            eFileMenuItem.name = labelAttr.getNodeValue();
                        }
                        if (toolTipAttr != null) {
                            eFileMenuItem.toolTipText = toolTipAttr.getNodeValue();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getName(){
            return name;
        }

        public String getMethodName(){
            return methodName;
        }

        public KeyStroke getAccelerator() {
            return accelerator;
        }

        public String getToolTipText() {
            return toolTipText;
        }

        public static String getDefaultPathName() {
            return defaultPathName;
        }
    }

    public enum EEditMenuItem {
        eUndo(null, "undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)),
        eRedo(null, "redo", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK)),
        eCut(null, "cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)),
        eCopy(null, "copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)),
        ePaste(null, "paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK)),
        eDelete(null, "delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)),
        eGroup(null, "group", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)),
        eUnGroup(null, "unGroup", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));

        private String name;
        private String methodName;
        private KeyStroke accelerator;
        private String toolTipText;

        EEditMenuItem(String name, String methodName, KeyStroke accelerator) {
            this.name = name;
            this.methodName = methodName;
            this.accelerator = accelerator;
            this.toolTipText = null;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EEditMenuItem eEditMenuItem = EEditMenuItem.valueOf(element.getNodeName());
                        Node labelAttr = element.getAttributes().getNamedItem("label");
                        Node toolTipAttr = element.getAttributes().getNamedItem("toolTipText");
                        if (labelAttr != null) {
                            eEditMenuItem.name = labelAttr.getNodeValue();
                        }
                        if (toolTipAttr != null) {
                            eEditMenuItem.toolTipText = toolTipAttr.getNodeValue();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getName() {
            return name;
        }

        public String getMethodName() {
            return methodName;
        }

        public KeyStroke getAccelerator() {
            return accelerator;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }

    public enum ETextConstants {
        eDefaultText("Text"),
        eInputPrompt("텍스트를 입력하세요:"),
        eDefaultFontName("Arial"),
        eDefaultFontSize("20");

        private String value;

        ETextConstants(String value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        ETextConstants eTextConstants = ETextConstants.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eTextConstants.value = valueAttr.getNodeValue();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getValue() {
            return value;
        }
    }

    public enum EGraphicsMenuItem {
        eFontStyle(null, "setFontStyle"),
        eLineStyle(null, "setLineStyle"),
        eLineColor(null, "setLineColor"),
        eFillColor(null, "setFillColor");

        private String name;
        private String methodName;
        private String toolTipText;

        EGraphicsMenuItem(String name, String methodName) {
            this.name = name;
            this.methodName = methodName;
            this.toolTipText = null;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EGraphicsMenuItem eGraphicsMenuItem = EGraphicsMenuItem.valueOf(element.getNodeName());
                        Node labelAttr = element.getAttributes().getNamedItem("label");
                        Node toolTipAttr = element.getAttributes().getNamedItem("toolTipText");
                        if (labelAttr != null) {
                            eGraphicsMenuItem.name = labelAttr.getNodeValue();
                        }
                        if (toolTipAttr != null) {
                            eGraphicsMenuItem.toolTipText = toolTipAttr.getNodeValue();
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getName() {
            return name;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }
}