package de.ateam.controller.listener.collage;

import de.ateam.controller.ICollageController;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputTextChangedListener implements DocumentListener {
	protected ICollageController controller;

	public InputTextChangedListener(ICollageController controller) {
		this.controller = controller;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateInputText(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateInputText(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateInputText(e);
	}

	private void updateInputText(DocumentEvent e){
		try {
			this.controller.getRoiModel().setInputText(e.getDocument().getText(0, e.getDocument().getLength()));
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
}
