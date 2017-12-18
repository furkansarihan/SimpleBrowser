package yeninesilarge.image.paint;

import yeninesilarge.image.ImagePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


public class PaintPanel extends JPanel implements ToolButtonGroup.ToolButtonListener{
	ImagePanel pnlImage;
	String selectedTool;

	public static JFrame buildFrame(String title, JPanel panel, int x, int y, int width, int height){
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		frame.setContentPane(panel);
		frame.setBounds(x,y,width,height);
		//frame.pack();
		frame.setVisible(true);
		return frame;
	}

	PaintPanel(){
		init();
	}

	public static void main(String... argv){
		
		PaintPanel pp = new PaintPanel();

		buildFrame("Simple Paint", pp, 500, 50, 900, 900);
	}

	private void init(){
		setLayout(new GridBagLayout());

		// --------- TOOLS ---------------
		GridBagConstraints constTools = new GridBagConstraints();
		constTools.gridx = 0;
        constTools.gridy = 0;
        constTools.weightx = 0.1;
        constTools.weighty = 0.7;
        constTools.anchor = GridBagConstraints.NORTHWEST;
        constTools.fill = GridBagConstraints.BOTH;

		JPanel pnlTools = new JPanel();
		pnlTools.setBackground(Color.WHITE);
		pnlTools.setOpaque(true);
		add(pnlTools,constTools);

		pnlTools.setLayout(new BoxLayout(pnlTools, BoxLayout.PAGE_AXIS));

		ButtonGroup groupDrawing = new ToolButtonGroup(this);
		makeTool(pnlTools, groupDrawing, Tool.SELECT);
		makeTool(pnlTools, groupDrawing, Tool.LINE);
		makeTool(pnlTools, groupDrawing, Tool.TRIANGLE);
		makeTool(pnlTools, groupDrawing, Tool.RECTANGLE);
		makeTool(pnlTools, groupDrawing, Tool.OVAL); 

		// --------- IMAGE ---------------
		GridBagConstraints constImage= new GridBagConstraints();
		constImage.gridx = 3;
        constImage.gridy = 0;
        constImage.weightx = 0.9;
        constImage.gridheight = 9;
        constImage.fill = GridBagConstraints.BOTH;

		pnlImage = new ImagePanel();
		pnlImage.addMouseListener(mouseListener);
		File sampleFile = new File("yeninesilarge/images","dog.jpg");
		try { pnlImage.setImage(sampleFile); } 
		catch(IOException e) { e.printStackTrace(); };
		add(pnlImage, constImage);

		// --------- COLOR ---------------
		GridBagConstraints constColor = new GridBagConstraints();
		constColor.gridx = 0;
        constColor.gridy = 8;
        constColor.weightx = 0.1;
        constColor.weighty = 0.3;
        constColor.anchor = GridBagConstraints.SOUTHWEST;
		constColor.fill = GridBagConstraints.BOTH;

		JPanel pnlColor = new JPanel();
		pnlColor.setForeground(java.awt.Color.YELLOW);
		pnlColor.setBackground(java.awt.Color.YELLOW);
		pnlColor.setOpaque(true);
		add(pnlColor, constColor);

	}
	
	private JToggleButton makeTool(JPanel tools, ButtonGroup group, String name){
		tools.add(Box.createRigidArea(new Dimension(5,5)));
		JToggleButton tool = new JToggleButton(name);
		tool.setAlignmentX(Component.CENTER_ALIGNMENT);	
		tool.setActionCommand(name); // stackoverflow.com/questions/27916896/what-is-an-action-command-that-is-set-by-setactioncommand
		tools.add(tool);
		group.add(tool);
		return tool;
	}

	@Override
	public void onSelection(String selectedTool){
		this.selectedTool = selectedTool;
	}

	private MouseListener mouseListener = new MouseListener() {
		public void mousePressed(MouseEvent e) {

		}
		public void mouseReleased(MouseEvent e) {

		}
		public void mouseEntered(MouseEvent e) {

		}
		public void mouseExited(MouseEvent e) {

		}
		public void mouseClicked(MouseEvent e) {
			//Testing
			Point coordinates = coordinatesFromPoint(e.getComponent());

			Tool t = ToolFactory.getInstance(selectedTool);
			Map<String, Object> params = new HashMap<>();
			params.put("x1", (int) coordinates.getX() );
			params.put("y1", (int) coordinates.getY() );
			params.put("x2", (int) coordinates.getX());
			params.put("y2", (int) coordinates.getY() + 50);
			params.put("fill", true); 
			params.put("color", Color.YELLOW);

			t.draw(e.getComponent(), params);
		}
	};

	//stackoverflow.com/questions/23730793/how-to-get-image-pixel-coordinates-in-java-by-pointing-mouse-to-a-specific-poin
	Point coordinatesFromPoint(Component comp){
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		Point p = new Point(pointerInfo.getLocation());
		SwingUtilities.convertPointFromScreen(p, comp);
		return p;
	}
	
	
}

//src : dzone.com/articles/unselect-all-toggle-buttons
class ToolButtonGroup extends ButtonGroup {

	private	ToolButtonListener toolListener;

	ToolButtonGroup(ToolButtonListener listener){
		toolListener = listener;
	}

	@Override
	public void setSelected(ButtonModel model, boolean selected) {
		if (selected) {
			super.setSelected(model, selected);
			String actionCommand = model.getActionCommand();
			toolListener.onSelection(actionCommand);
		} else {
			clearSelection();
			toolListener.onSelection(null);
		}
	}

	interface ToolButtonListener{
		void onSelection(String selectedTool);
	}
}


