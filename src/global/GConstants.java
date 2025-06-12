package global;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GShape;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public final class GConstants {

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
                    } /* else if (node.getNodeName().equals(EMenu.class.getSimpleName())) {*/
//                        EMenu.setValues(node);
//                    } else if (node.getNodeName().equals(EFileMenuItem.class.getSimpleName())) {
//                        EFileMenuItem.setValue(node);
//                    } else if (node.getNodeName().equals(EEditMenuItem.class.getSimpleName())) {
//                        EEditMenuItem.setValue(node);
//                    } else if (node.getNodeName().equals(EGraphicsMenuItem.class.getSimpleName())) {
//                        EGraphicsMenuItem.setValue(node);
//                    } else if (node.getNodeName().equals(EToolBarButton.class.getSimpleName())) {
//                        EToolBarButton.setValue(node);
//                    }
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }
    }

    public enum EMainFrame {
        eW(1200),eH(600);

        private int value;
        EMainFrame(int value) {
            this.value = value;
        }

        public static void setValues(Node node){
            for(EMainFrame eMainFrame : EMainFrame.values()){
                Node attribute = node.getAttributes().getNamedItem(eMainFrame.name());
                eMainFrame.value = Integer.parseInt(attribute.getNodeValue());
            }
        }

        public int getValues() {
            return value;
        }
    }

    public static enum EAnchor {
        eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNE(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eNW(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSE(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eSW(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        eRR(new Cursor(Cursor.HAND_CURSOR)),
        eMM(new Cursor(Cursor.MOVE_CURSOR));
        private Cursor cursor;

        EAnchor(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return this.cursor;
        }

    }

    public static enum EShapeTool {
        eSelect("select", GShape.EPoints.e2P, GRectangle.class),
        eRectangle("rectangle", GShape.EPoints.e2P, GRectangle.class),
        eEllipse("ellipse", GShape.EPoints.e2P, GRectangle.class),
        eLine("line", GShape.EPoints.e2P, GRectangle.class),
        ePolygon("polygon", GShape.EPoints.eNP, GPolygon.class);

        private String name;
        private GShape.EPoints ePoints;
        private Class<?> classShape;
        private EShapeTool(String name, GShape.EPoints ePoints, Class<?> classShape) {
            this.name = name;
            this.ePoints = ePoints;
            this.classShape = classShape;
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
        eNew("새 파일", "newPanel"),
        eOpen("열기", "open"),
        eSave("저장", "save"),
        eSaveAs("다른 이름으로 저장", "saveAs"),
        ePrint("프린트", "print"),
        eClose("닫기", "close"),
        eQuit("나가기","quit"),;

        private final String name;
        private final String methodName;

        EFileMenuItem(String name, String methodName) {
            this.name = name;
            this.methodName = methodName;
        }
        public String getName(){
            return name;
        }

        public String getMethodName(){
            return methodName;
        }
    }

}
