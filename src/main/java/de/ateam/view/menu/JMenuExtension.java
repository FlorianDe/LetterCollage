package de.ateam.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JMenuExtension {
	int acceleratorModifiers;
	
	public JMenuExtension(){
		acceleratorModifiers = 0;
	}
	public JMenuExtension(int acceleratorModifiers){
		this.acceleratorModifiers = acceleratorModifiers;
	}
	
	private void setInformation(JMenuItem jmi, String description){
		char mnemonicKey = (jmi.getText().length()>0)?mnemonicKey = jmi.getText().charAt(0):'?';
		
		jmi.setMnemonic(mnemonicKey);
		jmi.getAccessibleContext().setAccessibleDescription(description);
		jmi.setToolTipText(description);
	}
	
	private void setInformation(JMenuItem jmi, String description, char acceleratorKey, int acceleratorModifiers){
		setInformation(jmi, description);
		jmi.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey,acceleratorModifiers));
	}
	
	public JMenu createJMenu(JMenu jm, String description, JMenuItem parent){
    	this.setInformationJM(jm, description);
    	parent.add(jm);
    	return jm;
	}
	public void setInformationJM(JMenu jm, String description){
		setInformation(jm, description);
	}
	
	public JMenuItem createJMenuItem(JMenuItem jmi, char acceleratorKey, String description, JMenuItem parent){
    	this.setInformationJMI(jmi, description, acceleratorKey);
    	parent.add(jmi);
    	return jmi;
	}
	public void setInformationJMI(JMenuItem jmi, String description, char acceleratorKey ){
		setInformation(jmi, description, acceleratorKey, this.acceleratorModifiers);
	}
}
