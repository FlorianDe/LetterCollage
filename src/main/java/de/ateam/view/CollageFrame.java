package main.java.de.ateam.view;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.view.menu.CVMenubar;
import main.java.de.ateam.view.panel.CVImageLoaderContainerPanel;
import main.java.de.ateam.view.panel.CVResultImagePanel;
import main.java.de.ateam.view.toolbar.CVToolbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Florian on 13.11.2015.
 */
public class CollageFrame extends JFrame {
    private CVMenubar menubar;
    private CVToolbar toolbar;
    private JScrollPane loadedImagesScrollPane;
    private JScrollPane resultImageScrollPane;
    private CVImageLoaderContainerPanel loadedImagesPanel;
    private CVResultImagePanel resultImagePanel;
    JSplitPane splitPane;

    ICollageController controller;

    public CollageFrame(ICollageController controller){
        this.controller = controller;
        this.setTitle("OpenCV - Buchstaben Collage [A-Team B]");
        this.setMinimumSize(new Dimension());
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.menubar = new CVMenubar(controller);
        this.toolbar = new CVToolbar(controller);
        this.loadedImagesPanel = new CVImageLoaderContainerPanel(this.controller);
        this.resultImagePanel = new CVResultImagePanel(this.controller);
        this.loadedImagesScrollPane = new JScrollPane(this.loadedImagesPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.resultImageScrollPane = new JScrollPane(this.resultImagePanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, loadedImagesScrollPane, resultImageScrollPane);;

        this.splitPane.setOneTouchExpandable(true);
        //TODO Anpassen mit getPrefSize dies das
        this.splitPane.setDividerLocation(250);


        /*
        //CENTER STUFF INSIDE THE SCROLLPANE!
        resultImageScrollPane.setLayout(new ScrollPaneLayout() {
            public void layoutContainer(Container parent) {
                super.layoutContainer(parent);
                Component view = viewport.getView();
                if (view != null) {
                    Dimension viewPortSize = viewport.getSize();
                    Dimension viewSize = view.getSize();

                    if ((viewPortSize.width > viewSize.width) ||
                            (viewPortSize.height > viewSize.height)) {

                        int spaceX = (viewPortSize.width - viewSize.width) / 2;
                        int spaceY = (viewPortSize.height - viewSize.height) / 2;
                        if (spaceX < 0) spaceX = 0;
                        if (spaceY < 0) spaceY = 0;

                        viewport.setLocation(spaceX, spaceY);
                        viewport.setSize(viewPortSize.width - spaceX, viewPortSize.height - spaceY);
                    }
                }
            }
        });
        */

        this.setJMenuBar(menubar);
        this.add(this.toolbar, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(500,300);
    }
}
