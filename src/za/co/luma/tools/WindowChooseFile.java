package za.co.luma.tools;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class WindowChooseFile extends JFrame{

	private static WindowChooseFile instance;

	public static WindowChooseFile getInstance(ImageCompressor ic){
		if (instance == null){
			instance = new WindowChooseFile(ic);
		}

		return instance; 
	}

	//====================================================== fields
	JTextField   _fileNameTF  = new JTextField(50);
	JFileChooser _fileChooser = new JFileChooser();
	JFileChooser _destinationFolderChooser = new JFileChooser();
	JTextField   _destinationFolderTF  = new JTextField(50);
	ImageCompressor imageCompressor;
	private JPanel content;
	private String pathFile = "";
	private String destinationFolder = "";
	private JProgressBar finished = new JProgressBar(0,1);
	
	public void setFinished(JProgressBar finished) {
		this.finished = finished;
	}

	public JProgressBar getFinished() {
		return finished;
	}

	//getPathFile
	public String getPathFile(){
		return this.pathFile;
	}

	//getContent
	public JPanel getContent(){
		return this.content;
	}

	//================================================= constructor
	WindowChooseFile(ImageCompressor ic) {
		this.pathFile = "";
		this.imageCompressor = ic;
		//finished progress bar
		finished.setValue(1);
		finished.setForeground(new Color(255, 0, 0));
		finished.setPreferredSize(new Dimension(800, 15));
		_destinationFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		//... Create / set component characteristics.
		_fileNameTF.setEditable(false);
		_destinationFolderTF.setEditable(false);

		//... Add listeners

		//... Create content pane, layout components
		this.content = new JPanel();
		JButton button_ok = new JButton("OK");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem openDestinationFolder = new JMenuItem("Destination Folder");

		content.setLayout(new FlowLayout());
		content.add(new JLabel("File:"));
		content.add(openItem);
		content.add(_fileNameTF);
		/****/
		content.setLayout(new FlowLayout());
		content.add(openDestinationFolder);
		content.add(_destinationFolderTF);
		/****/
		content.add(button_ok);
		content.add(finished);
		/***/
		
		openItem.addActionListener(new OpenAction());
		openDestinationFolder.addActionListener(new OpenDestinationFAction());
		button_ok.addActionListener(new OkAction());

		

		//... Set window characteristics
		this.setContentPane(content);
		this.setTitle("Choose your image");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(825,110));
		this.pack();                      // Layout components.
		this.setLocationRelativeTo(null); // Center window.
	}

	////////////////////////////////////////////////////// OpenDestinationFAction
	
	class OpenDestinationFAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			//... Open a file dialog.
			int retval = _destinationFolderChooser.showOpenDialog(WindowChooseFile.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				//... The user selected a file, get it, use it.
				File file = _destinationFolderChooser.getSelectedFile();
				//... Update user interface.
				_destinationFolderTF.setText(file.getAbsolutePath());
				destinationFolder = file.getAbsolutePath();
			}
		}
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
			imageCompressor.setPathOfDestDir(destinationFolder);
			Color green = new Color(0, 255, 0);
			imageCompressor.launchCompression(pathFile);
			finished.setForeground(green);
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
