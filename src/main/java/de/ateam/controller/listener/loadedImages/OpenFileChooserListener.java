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
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenFileChooserListener implements ActionListener {
	protected ICollageController controller;

	public OpenFileChooserListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CVFileChooser cvfc = new CVFileChooser(this.controller);

		//TODO CHECK IF FILE IN SUBPATH IS IMG FILE!!
		int result = cvfc.showDialog(null, "Load");
		switch (result) {
			case JFileChooser.APPROVE_OPTION:
				BufferedImage buf = null;
				for(File f0 : cvfc.getSelectedFiles()){
					try {
						if(f0.isFile()) {
							buf = ImageIO.read(f0.toURI().toURL());
							this.controller.getImageLoaderModel().addImage(buf);
						}
						else if(f0.isDirectory()){
							for(File  f1 : f0.listFiles()){
								if(f1.isFile()) {
									buf = ImageIO.read(f1.toURI().toURL());
									this.controller.getImageLoaderModel().addImage(buf);
								}
							}
						}
					} catch (MalformedURLException err) {
						System.out.println("Wrong path for :" + f0.getPath());
					} catch (IOException err) {
						System.out.println("Cannot load:" + f0.getPath());
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

	//TODO IMPLEMENT LOGIC HERE ATM ONLY DEPTH 1...
	private void walkThroughFileTree(String path){
		try {
			Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
		} catch (IOException e) {
			System.out.println("Error in walkThroughFileTree");
		}
	}
}
