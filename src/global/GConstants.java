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
                    } else if (node.getNodeName().equals(ECanvasConstants.class.getSimpleName())) {
                        ECanvasConstants.setValues(node);
                    } else if (node.getNodeName().equals(EShapeConstants.class.getSimpleName())) {
                        EShapeConstants.setValues(node);
                    } else if (node.getNodeName().equals(ETextBoxConstants.class.getSimpleName())) {
                        ETextBoxConstants.setValues(node);
                    } else if (node.getNodeName().equals(EGraphicsDefaults.class.getSimpleName())) {
                        EGraphicsDefaults.setValues(node);
                    } else if (node.getNodeName().equals(EFontSizes.class.getSimpleName())) {
                        EFontSizes.setValues(node);
                    } else if (node.getNodeName().equals(EClipboardConstants.class.getSimpleName())) {
                        EClipboardConstants.setValues(node);
                    } else if (node.getNodeName().equals(EMenuTexts.class.getSimpleName())) {
                        EMenuTexts.setValues(node);
                    } else if (node.getNodeName().equals(EDialogTexts.class.getSimpleName())) {
                        EDialogTexts.setValues(node);
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
        eDuplicate(null, "duplicate", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK)),
        eSelectAll(null, "selectAll", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK)),
        eDeselectAll(null, "deselectAll", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
        eGroup(null, "group", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)),
        eUnGroup(null, "unGroup", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK)),
        eBringToFront(null, "bringToFront", KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
        eSendToBack(null, "sendToBack", KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
        eBringForward(null, "bringForward", KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, InputEvent.CTRL_MASK)),
        eSendBackward(null, "sendBackward", KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, InputEvent.CTRL_MASK)),
        eRotateRight(null, "rotateRight", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)),
        eRotateLeft(null, "rotateLeft", KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)),
        eFlipHorizontal(null, "flipHorizontal", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK)),
        eFlipVertical(null, "flipVertical", KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK));

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

    public enum ECanvasConstants {
        eBaseWidth(800),
        eBaseHeight(600),
        eZoomInFactor(1.2),
        eZoomOutFactor(0.9),
        eMinZoom(0.1),
        eMaxZoom(10.0);

        private double value;

        ECanvasConstants(double value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        ECanvasConstants eCanvasConstants = ECanvasConstants.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eCanvasConstants.value = Double.parseDouble(valueAttr.getNodeValue());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public double getValue() {
            return value;
        }

        public int getIntValue() {
            return (int) value;
        }
    }

    public enum EShapeConstants {
        eAnchorWidth(10),
        eAnchorHeight(10),
        eClickTolerance(8.0),
        eRotationHandleOffset(30);

        private double value;

        EShapeConstants(double value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EShapeConstants eShapeConstants = EShapeConstants.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eShapeConstants.value = Double.parseDouble(valueAttr.getNodeValue());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public double getValue() {
            return value;
        }

        public int getIntValue() {
            return (int) value;
        }

        public float getFloatValue() {
            return (float) value;
        }
    }

    public enum ETextBoxConstants {
        eCursorBlinkInterval(500),
        ePadding(5);

        private int value;

        ETextBoxConstants(int value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        ETextBoxConstants eTextBoxConstants = ETextBoxConstants.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eTextBoxConstants.value = Integer.parseInt(valueAttr.getNodeValue());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public int getValue() {
            return value;
        }
    }

    public enum EGraphicsDefaults {
        eDefaultLineWidth(2.0f),
        eDefaultFontSize(14),
        eDefaultFontName("Arial"),
        eThinLineWidth(1.0f),
        eNormalLineWidth(2.0f),
        eThickLineWidth(4.0f),
        eDashPattern("10.0,5.0"),
        eDotPattern("2.0,3.0");

        private String value;

        EGraphicsDefaults(String value) {
            this.value = value;
        }

        EGraphicsDefaults(float value) {
            this.value = String.valueOf(value);
        }

        EGraphicsDefaults(int value) {
            this.value = String.valueOf(value);
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EGraphicsDefaults eGraphicsDefaults = EGraphicsDefaults.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eGraphicsDefaults.value = valueAttr.getNodeValue();
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

        public float getFloatValue() {
            return Float.parseFloat(value);
        }

        public int getIntValue() {
            return Integer.parseInt(value);
        }

        public float[] getFloatArray() {
            String[] parts = value.split(",");
            float[] result = new float[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Float.parseFloat(parts[i].trim());
            }
            return result;
        }
    }

    public enum EFontSizes {
        eSize8(8), eSize10(10), eSize12(12), eSize14(14),
        eSize16(16), eSize20(20), eSize24(24), eSize32(32);

        private int value;

        EFontSizes(int value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EFontSizes eFontSizes = EFontSizes.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eFontSizes.value = Integer.parseInt(valueAttr.getNodeValue());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public int getValue() {
            return value;
        }

        public static int[] getAllValues() {
            EFontSizes[] sizes = EFontSizes.values();
            int[] result = new int[sizes.length];
            for (int i = 0; i < sizes.length; i++) {
                result[i] = sizes[i].value;
            }
            return result;
        }
    }

    public enum EClipboardConstants {
        ePasteOffset(20),
        eMaxUndoSize(50),
        eDuplicateOffset(25);

        private int value;

        EClipboardConstants(int value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EClipboardConstants eClipboardConstants = EClipboardConstants.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eClipboardConstants.value = Integer.parseInt(valueAttr.getNodeValue());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        public int getValue() {
            return value;
        }
    }

    public enum EMenuTexts {
        eFileMenu("파일"),
        eEditMenu("편집"),
        eViewMenu("보기"),
        eGraphicMenu("그래픽"),
        eLineWidthMenu("선 두께"),
        eLineStyleMenu("선 스타일"),
        eFontStyleMenu("글자 스타일"),
        eFontSizeMenu("글자 크기"),
        eLineColorItem("선 색상..."),
        eFillColorItem("채우기 색상..."),
        eThinLine("얇게"),
        eNormalLine("보통"),
        eThickLine("굵게"),
        eSolidLine("실선"),
        eDashedLine("점선"),
        eDottedLine("점점선"),
        ePlainFont("표준"),
        eBoldFont("굵게"),
        eItalicFont("기울임"),
        eBoldItalicFont("굵은기울임"),
        eZoomIn("확대"),
        eZoomOut("축소"),
        eResetZoom("원본크기 (100%)"),
        eSelectLineColor("선 색상 선택"),
        eSelectFillColor("채우기 색상 선택");

        private String value;

        EMenuTexts(String value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EMenuTexts eMenuTexts = EMenuTexts.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eMenuTexts.value = valueAttr.getNodeValue();
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

    public enum EDialogTexts {
        eSaveConfirm("변경된 내용이 있습니다. 저장하시겠습니까?"),
        eQuitConfirm("정말로 종료하시겠습니까?"),
        eCloseConfirm("종료 확인"),
        ePrintError("프린트 오류"),
        eFileOpenError("파일을 열 수 없습니다: "),
        eFileSaveError("파일을 저장할 수 없습니다: "),
        eErrorTitle("오류"),
        eCannotFindIcon("아이콘 파일을 찾을 수 없습니다: /rsc/icon.jpg"),
        eIconLoadFailed("아이콘 로드 실패: ");

        private String value;

        EDialogTexts(String value) {
            this.value = value;
        }

        public static void setValues(Node node) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                try {
                    Node element = node.getChildNodes().item(i);
                    if (element.getNodeType() == Node.ELEMENT_NODE) {
                        EDialogTexts eDialogTexts = EDialogTexts.valueOf(element.getNodeName());
                        Node valueAttr = element.getAttributes().getNamedItem("value");
                        if (valueAttr != null) {
                            eDialogTexts.value = valueAttr.getNodeValue();
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
}