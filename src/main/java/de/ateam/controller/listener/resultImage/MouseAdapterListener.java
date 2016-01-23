package main.java.de.ateam.controller.listener.resultImage;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.model.ResultImageModel;
import main.java.de.ateam.model.roi.RegionOfInterest;
import main.java.de.ateam.utils.ShapeUtils;
import main.java.de.ateam.view.dialog.SetRoiWeightingDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Created by Florian on 13.11.2015.
 */
public class MouseAdapterListener extends MouseAdapter {
    Point pPressed;
    Point pReleased;
    ResultImageModel.MouseMode lastMouseMode;

    ICollageController controller;
    public MouseAdapterListener(ICollageController controller){
        this.controller  = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.lastMouseMode = this.controller.getResultImageModel().getMouseMode();
        pPressed = new Point(e.getPoint());
        if(isPaintMode(e)){
            this.controller.getResultImageModel().setActualDrawColor(Color.RED);
        }else if(isEraseMode(e)){
            this.controller.getResultImageModel().setActualDrawColor(Color.YELLOW);
        }
        //System.out.printf("[mousePressed] X:%s  Y:%s\n", e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.pReleased = new Point(e.getPoint());
        boolean roiImageChanged = false;

        if(isPaintMode(e)){
            if(!pReleased.equals(pPressed)) {
                if((pReleased.distance(pPressed)>4.0)){
                    this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.PAINT);
                    Rectangle r = new Rectangle(pPressed);
                    r.add(pReleased);
                    this.controller.getResultImageModel().getActualVisibleRoiImage().addRegionOfInterest(this.controller.getResultImageModel().getRealCoordinates(r));
                    roiImageChanged=true;
                }
            }
            this.controller.getResultImageModel().setActualDrawnRoi(null);
        }
        else if(isEraseMode(e)){
            if(pPressed.equals(pReleased)){
                ArrayList<RegionOfInterest> rois = this.controller.getResultImageModel().getActualVisibleRoiImage().getIntersectingRegionOfInterests(this.controller.getResultImageModel().getRealCoordinates(pReleased));
                this.controller.getResultImageModel().getActualVisibleRoiImage().deleteRegionOfInterests(rois);
            }
            else{
                Rectangle r = new Rectangle(pPressed);
                r.add(pReleased);
                ArrayList<RegionOfInterest> rois = this.controller.getResultImageModel().getActualVisibleRoiImage().getIntersectingRegionOfInterests(this.controller.getResultImageModel().getRealCoordinates(r));
                this.controller.getResultImageModel().getActualVisibleRoiImage().deleteRegionOfInterests(rois);
            }
            roiImageChanged=true;
            this.controller.getResultImageModel().setActualDrawnRoi(null);
        }
        else if(isSetWeightMode(e)){
            //TODO evtl f�r alle rois einbaun
            ArrayList<RegionOfInterest> rois = this.controller.getResultImageModel().getActualVisibleRoiImage().getIntersectingRegionOfInterests(this.controller.getResultImageModel().getRealCoordinates(pReleased));
            if(rois.size()>0){
                SwingUtilities.convertPointToScreen(pReleased, this.controller.getCollageView().getResultImagePanel());
                SetRoiWeightingDialog srwd = new SetRoiWeightingDialog(this.controller.getCollageView(), pReleased ,rois.get(0).getWeighting());
                srwd.setOnAcceptListener(e1 -> {
                    rois.get(0).setWeighting(srwd.getWeighting());
                    this.controller.getResultImageModel().getActualVisibleRoiImage().calculateCenterWeight();
                    this.controller.getResultImageModel().getActualVisibleRoiImage().repaintRoiImage();
                    srwd.closeAction();
                    controller.getResultImageModel().getActualVisibleRoiImage().repaintRoiImage();
                });
                srwd.setVisible(true);
            }
        }
        else if(isSelectSimilarMode(e)) {
            //Rectangle r =  this.controller.getResultImageModel().getRealCoordinates(new Rectangle(pPressed));
            this.controller.getRoiController().getRoiDetector().similarDetection(this.controller.getResultImageModel().getActualVisibleRoiImage(), this.controller.getResultImageModel().getRealCoordinates(pPressed.getLocation()));
        }
        else if(isPolygonPaintMode(e)){
            boolean addPoint = true;
            ResultImageModel rim = this.controller.getResultImageModel();
            //Point pPressedReal = rim.getRealCoordinates(pPressed);
            if(rim.getPolygon().size()>2) {
                Ellipse2D e2d = ShapeUtils.getEllipseFromCenter(rim.getPolygon().get(0).getX(), rim.getPolygon().get(0).getY(), rim.getPolygonSnapRadius(), rim.getPolygonSnapRadius());
                if(e2d.contains(pPressed.getX(),pPressed.getY())){
                    addPoint = false;
                    Polygon drawPoly = new Polygon();
                    for (Point point : this.controller.getResultImageModel().getPolygon()) {
                        Point p = rim.getRealCoordinates(new Point((int)point.getX(),(int)point.getY()));
                        drawPoly.addPoint((int)p.getX(),(int)p.getY());
                    }
                    rim.getActualVisibleRoiImage().addRegionOfInterest(drawPoly);
                    rim.clearPolygon();
                    roiImageChanged=true;
                }
            }
            if(addPoint) {
                this.controller.getResultImageModel().addPointToPolygon(pPressed);
            }
        }



        if(!isPolygonPaintMode(e)){
            this.controller.getResultImageModel().clearPolygon();
        }
        if(roiImageChanged){
            controller.getRoiModel().getRoiCollection().roiImageUpdated(controller.getResultImageModel().getActualVisibleRoiImage());
        }
        this.controller.getResultImageModel().setMouseMode(this.lastMouseMode);
        //System.out.printf("[mouseReleased] X:%s  Y:%s\n", e.getX(), e.getY());
    }



    @Override
    public void mouseDragged(MouseEvent e) {
        Point pAct = e.getPoint();
        if (pPressed != null) {
            if(isDragMode(e)){
                if(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.DRAG )) {
                    this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.DRAG);
                }
                JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, (JComponent)e.getSource());
                if (viewPort != null) {
                    int deltaX = pPressed.x - e.getX();
                    int deltaY = pPressed.y - e.getY();

                    Rectangle view = viewPort.getViewRect();
                    view.x += deltaX;
                    view.y += deltaY;

                    //map.scrollRectToVisible(view);
                    this.controller.getResultImageModel().setViewRect(view);
                }
            } else if(isPaintMode(e)){
                if(!this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.PAINT )) {
                    this.controller.getResultImageModel().setMouseMode(ResultImageModel.MouseMode.PAINT);
                }
                Rectangle r = new Rectangle(pPressed);
                r.add(pAct);
                this.controller.getResultImageModel().setActualDrawnRoi(r);
            } else if(isEraseMode(e)){
                Rectangle r = new Rectangle(pPressed);
                r.add(pAct);
                this.controller.getResultImageModel().setActualDrawnRoi(r);
            }
        }
        //System.out.printf("[mouseDragged] X:%s  Y:%s\n", e.getX(), e.getY());
    }



    public boolean isPaintMode(MouseEvent e){
        return (((e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK) || this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.PAINT );
    }

    public boolean isEraseMode(MouseEvent e){
        return (this.controller.getResultImageModel().getMouseMode().equals(ResultImageModel.MouseMode.ERASE));
    }

    public boolean isDragMode(MouseEvent e){
        return (((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) || this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.DRAG);
    }

    public boolean isSetWeightMode(MouseEvent e){
        return (this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.SETWEIGHT);
    }

    private boolean isSelectSimilarMode(MouseEvent e) {
        return (this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.SIMILAR_SELECT);
    }

    private boolean isPolygonPaintMode(MouseEvent e) {
        return (this.controller.getResultImageModel().getMouseMode() == ResultImageModel.MouseMode.POLYGONPAINT);
    }

}
