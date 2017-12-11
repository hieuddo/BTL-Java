package GUI;

import DataStore.*;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Draw extends JFrame {
    private final int screenWidth = 1600;
    private final int screenHeight = 900;
    private final int charWidth = 8;
    private final int textHeight = 14;

    private ObjectManagement manager;
    private drawPanel diagram;
    private JScrollPane scrollPane;

    private Vector < String > listClassName;
    private Vector < Vector<String> >listAtribute;
    private Vector < Vector<String> >listMethod;

    private int numberOfClass;
    private int[] rectWidth = new int[1000];
    private int[] rectHeight = new int[1000];
    private int[] shapeX = new int[1000];
    private int[] shapeY = new int[1000];
    private int pastX = 0, pastY = 0;

    public Draw(ObjectManagement manager) {
        super("Display UML");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setGUI();
        initData(manager);
        setInfo();
        calculate();
        diagram = new drawPanel();
        add(diagram);
        scrollPane = new JScrollPane(diagram, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setGUI() {
        setLayout(new BorderLayout());
        setSize(screenWidth, screenHeight);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initData(ObjectManagement manager) {
        this.manager = manager;
        listClassName = new Vector();
        listMethod = new Vector();
        listAtribute = new Vector();
    }

    private void setInfo() {
        this.numberOfClass = manager.getNumberOfClass();

        for (Class_info cl : manager.getMyList() ) {
            listClassName.add(cl.getName());
        }

        for (Class_info cl : manager.getMyList()) {
            Vector <String> atbList = new Vector();
            for (String atb : cl.getAttributesList() ) {
                atbList.add(atb);
            }
            listAtribute.add(atbList);
        }

        for (Class_info cl : manager.getMyList()) {
            Vector <String> metList = new Vector();
            for (String met : cl.getMethodsList()) {
                metList.add(met);
            }
            listMethod.add(metList);
        }
    }

    private void calculate() {
        for (int i = 0; i < numberOfClass; i++) {
            int maxWidth = listClassName.get(i).length();

            for (int j = 0; j < listAtribute.get(i).size(); j++) {
                maxWidth = Math.max(maxWidth, listAtribute.get(i).get(j).length());
            }
            for (int j = 0; j < listMethod.get(i).size(); j++) {
                maxWidth = Math.max(maxWidth, listMethod.get(i).get(j).length());
            }

            rectWidth[i] = maxWidth * charWidth;
            int line = 1 + listAtribute.get(i).size() + listMethod.get(i).size();
            rectHeight[i] = (3*line + 4) * textHeight / 2;
        }

        shapeX[0] = 50; shapeY[0] = 50;
        for (int i = 1; i < numberOfClass; i++) {
            shapeX[i] = shapeX[i-1] + rectWidth[i-1] + 50;
            shapeY[i] = shapeY[i-1];
            if (shapeX[i] + rectWidth[i] > screenWidth) {
                shapeX[i] = 50;

                int maxHeight = 0;
                for (int j = i-1; j >= 0; j ++) {
                    if (shapeY[j] != shapeY[i]) break;
                    maxHeight = Math.max(maxHeight, rectHeight[j]);
                }

                shapeY[i] = shapeY[i-1] + maxHeight;
            }
        }
    }

    class drawPanel extends JPanel {
        private double scale = 1;

        public drawPanel() {
            // Zoom and printscreen
            addKeyListener(new KeyWait());
            setFocusable(true);

            //Mouse GUI.drag
            mouseDrag drag = new mouseDrag();
            addMouseMotionListener(drag);
            addMouseListener(drag);
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            super.paintComponent(g2D);
            g2D.scale(scale, scale);

            //fill shape
            for (int i = 0; i < numberOfClass; i++) {
                //rect
                g2D.setColor(Color.WHITE);
                g2D.fillRect(shapeX[i], shapeY[i], rectWidth[i], rectHeight[i]);

                //bound
                g2D.setColor(Color.BLACK);
                g2D.drawRect(shapeX[i], shapeY[i], rectWidth[i], rectHeight[i]);

                //className
                int classNameWidth = listClassName.get(i).length() * charWidth;
                int yText = 3*textHeight/2;
                g2D.drawString(listClassName.get(i), shapeX[i] + (rectWidth[i] - classNameWidth) / 2, shapeY[i] + yText);

                //attributes
                yText += textHeight / 2;
                g2D.drawLine(shapeX[i], shapeY[i] + yText, shapeX[i] + rectWidth[i], shapeY[i] + yText);
                for (int j = 0; j < listAtribute.get(i).size(); j ++) {
                    yText += 3*textHeight/2;
                    g2D.drawString(listAtribute.get(i).get(j), shapeX[i] + rectWidth[i]/15, shapeY[i] + yText);
                }

                //methods
                yText += textHeight/2;
                g2D.drawLine(shapeX[i], shapeY[i] + yText, shapeX[i] + rectWidth[i], shapeY[i] + yText);
                for (int j = 0; j < listMethod.get(i).size(); j ++) {
                    yText += 3*textHeight/2;
                    g2D.drawString(listMethod.get(i).get(j), shapeX[i] + rectWidth[i]/15, shapeY[i] + yText);
                }
            }
            repaint();
        }
        @Override
        public Dimension getPreferredSize() {
            double width = (screenWidth * scale), height = screenHeight * scale;
            return new Dimension((int)width, (int)height);
        }

        class KeyWait extends KeyAdapter {
            @Override
            public void keyReleased(KeyEvent k) {
                if (k.getKeyCode() == KeyEvent.VK_ADD || k.getKeyCode() == KeyEvent.VK_I) { //zoom in
                    scale += 0.2;
                    if (scale > 3) scale = 3;
                } else if (k.getKeyCode() == KeyEvent.VK_SUBTRACT || k.getKeyCode() == KeyEvent.VK_O) { //zoom out
                    scale -= 0.2;
                    if (scale < 0.4) scale = 0.4;
                } else if (k.getKeyCode() == KeyEvent.VK_PRINTSCREEN) {
                    BufferedImage img = new BufferedImage(diagram.getWidth(), diagram.getHeight(), 1);
                    Graphics2D g2D = img.createGraphics();
                    diagram.printAll(g2D);
                    g2D.dispose();
                    try {
                        ImageIO.write(img, "pnd", new File("image.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        JOptionPane.showMessageDialog(null, "Image saved successfully");
                    }
                }
                repaint();
            }
        }
        class mouseDrag extends MouseMotionAdapter implements MouseMotionListener, MouseListener {
            int idx;

            @Override
            public void mousePressed(MouseEvent e) {
                //get old position
                pastX = e.getX();
                pastY = e.getY();

                //if mouse entered rect then break to move to dragged
                for (idx = 0; idx < numberOfClass; idx ++) {
                    if ((int)(e.getX() /scale) > shapeX[idx] && (int)(e.getX() /scale) < shapeX[idx] + rectWidth[idx]
                            && (int)(e.getY()) /scale > shapeY[idx] && (int)(e.getY() /scale) < shapeY[idx] + rectHeight[idx])
                        break;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = (int) ((e.getX() - pastX) /scale);
                int deltaY = (int) ((e.getY() - pastY) /scale);

                //update shape position
                shapeX[idx] += deltaX;
                shapeY[idx] += deltaY;

                //update mouse position
                pastX = e.getX();
                pastY = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

        }
    }
}