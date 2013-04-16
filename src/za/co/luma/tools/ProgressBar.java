package za.co.luma.tools;

import javax.swing.JProgressBar;

public class ProgressBar {
	JProgressBar pbar;
		
	static final int MY_MINIMUM = 0;

	static final int MY_MAXIMUM = 100;

	public ProgressBar() {
		// initialize Progress Bar
		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
	}

	public void updateBar(int newValue) {
		pbar.setValue(newValue);
	}
	
	
	/***/
	
	public JProgressBar getPbar(){
		return this.pbar;
	}
	
}
