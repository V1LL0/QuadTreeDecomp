package za.co.luma.tools;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WindowChooseFile extends JFrame{

	private static WindowChooseFile instance;

	public static WindowChooseFile getInstance(ImageCompressor ic, ProgressBar pb){
		if (instance == null)
		{
			instance = new WindowChooseFile(ic, pb);
		}

		return instance; 
	}

	//====================================================== fields
	JTextField   _fileNameTF  = new JTextField(50);
	JFileChooser _fileChooser = new JFileChooser();
	ImageCompressor imageCompressor;
	ProgressBar pBar;
	private String pathFile = "";

	//getPBar
	public ProgressBar getPBar(){
		return this.pBar;
	}

	//getPathFile
	public String getPathFile(){
		return this.pathFile;
	}


	//================================================= constructor
	WindowChooseFile(ImageCompressor ic, ProgressBar pb) {
		this.pathFile = "";
		this.pBar = pb;
		this.imageCompressor = ic;
		//... Create / set component characteristics.
		_fileNameTF.setEditable(false);

		//... Add listeners

		//... Create content pane, layout components
		JPanel content = new JPanel();
		JButton button_ok = new JButton("OK");
		JMenuItem openItem = new JMenuItem("Open");

		content.setLayout(new FlowLayout());
		content.add(new JLabel("File:"));
		content.add(openItem);
		content.add(_fileNameTF);
		content.add(button_ok);
		content.add(pBar.getPbar());

		openItem.addActionListener(new OpenAction());
		button_ok.addActionListener(new OkAction());


		//... Set window characteristics
		this.setContentPane(content);
		this.setTitle("Choose your image");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();                      // Layout components.
		this.setLocationRelativeTo(null); // Center window.
	}


	///////////////////////////////////////////////////// OpenAction
	class OpenAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			int retval = _fileChooser.showOpenDialog(WindowChooseFile.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = _fileChooser.getSelectedFile();

				//... Update user interface.
				_fileNameTF.setText(file.getName());
				pathFile = file.getAbsolutePath();
			}
		}
	}

	///////////////////////////////////////////////////// OkAction
	class OkAction implements Action {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			imageCompressor.launchCompression(pathFile, pBar);
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object getValue(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setEnabled(boolean arg0) {
			// TODO Auto-generated method stub

		}




	}



	/*******************************************************/









}
