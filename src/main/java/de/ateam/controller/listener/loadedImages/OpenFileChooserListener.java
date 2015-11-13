package main.java.de.ateam.controller.listener.loadedImages;

import main.java.de.ateam.controller.ICollageController;
import main.java.de.ateam.utils.FileLoader;
import main.java.de.ateam.view.panel.CVFileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenFileChooserListener implements ActionListener {
	protected ICollageController controller;

	public OpenFileChooserListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CVFileChooser cvfc = new CVFileChooser(this.controller);

		int result = cvfc.showDialog(null, "Load");
		switch (result) {
			case JFileChooser.APPROVE_OPTION:
				//TODO IMPLEMENT LOGIC HERE
				BufferedImage buf = null;
				for(File f : cvfc.getSelectedFiles()){
					try {
						buf = ImageIO.read(f.toURI().toURL());
						this.controller.getImageLoaderModel().addImage(buf);
					} catch (MalformedURLException err) {
						System.out.println("Wrong path for :" + f.getPath());
					} catch (IOException err) {
						System.out.println("Cannot load:" + f.getPath());
					}
				}
				break;
			case JFileChooser.CANCEL_OPTION:
				System.out.println("Cancel or the close-dialog icon was clicked");
				break;
			case JFileChooser.ERROR_OPTION:
				System.out.println("Error");
				break;
		}
	}
}
