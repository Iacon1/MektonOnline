// Simple menu for editing things in the editor.

package GameEngine.Editor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import GameEngine.MenuSlate;
import GameEngine.MenuSlate.InfoFunction;


@SuppressWarnings("serial")
public class EditorPanel extends JPanel implements MenuSlate
{
	private interface UpdateTask {public void onUpdate();}
	private interface ResizeTask {public void onResize();}
	private int cellsH;
	private int cellsV;
	private int cellWidth;
	private int cellHeight;
	private boolean propogate; // See addSubSlate
	private List<UpdateTask> updateTasks; // Tasks to do on update
	private List<ResizeTask> resizeTasks; // Resize components on screen resize
	private void update() {for (UpdateTask task : updateTasks) task.onUpdate();}
	private void resize() {for (ResizeTask task : resizeTasks) task.onResize();}
	/**
	 * Create the panel.
	 */
	public EditorPanel()
	{
		super();
		
		updateTasks = new ArrayList<UpdateTask>();
		resizeTasks = new ArrayList<ResizeTask>();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(null);
		setSize(640, 480);
	}
	/**
	 * Create the panel.
	 */
	public EditorPanel(int width, int height, int cellsH, int cellsV)
	{
		super();
		
		updateTasks = new ArrayList<UpdateTask>();
		resizeTasks = new ArrayList<ResizeTask>();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(null);
		
		setVisible(true);
		setCells(cellsH, cellsV);
		setSize(width, height);
	}
	
	@Override
	public void setCells(int h, int v)
	{		
		cellWidth = getWidth() / h;
		cellHeight = getHeight() / v;
		
		cellsH = h;
		cellsV = v;
		resize();
		update();
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		if (cellsH != 0 && cellsV != 0) setCells(cellsH, cellsV);
		resize();
		update();
	}
	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		if (cellsH != 0 && cellsV != 0) setCells(cellsH, cellsV);
		resize();
		update();
	}
	
	@Override
	public void addInfo(int x, int y, String label, int labelLength, int contentLength,
			InfoFunction<String> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JLabel contentLabel = new JLabel();
		add(contentLabel);
		resizeTasks.add(() -> {contentLabel.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentLabel.setText(function.getValue());
		
		updateTasks.add(() -> {contentLabel.setText(function.getValue());});
		resize();
		update();
	}

	@Override
	public void addTextbar(int x, int y, String label, int labelLength, int maxLength, int contentLength,
			DataFunction<String> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JTextField contentField = new JTextField();
		add(contentField);
		resizeTasks.add(() -> {contentField.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentField.setText(function.getValue());
		
		updateTasks.add(() -> {contentField.setText(function.getValue());});
		contentField.getDocument().addDocumentListener(new DocumentListener()
		{
			private void onUpdate()
			{
				function.setValue(contentField.getText());
				update();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {onUpdate();}
			@Override
			public void removeUpdate(DocumentEvent e) {onUpdate();}
			@Override
			public void changedUpdate(DocumentEvent e) {onUpdate();}
		});
		resize();
		update();
	}
	
	@Override
	public void addIntegerWheel(int x, int y, String label, int labelLength, int min, int max, int contentLength,
			DataFunction<Integer> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JSpinner contentSpinner = new JSpinner();
		add(contentSpinner);
		contentSpinner.setModel(new SpinnerNumberModel(min, min, max, 1));
		resizeTasks.add(() -> {contentSpinner.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentSpinner.setValue(function.getValue());
		
		updateTasks.add(() -> {contentSpinner.setValue(function.getValue());});
		contentSpinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				function.setValue((Integer) contentSpinner.getValue());
				update();
			}
		});
		resize();
		update();
	}
	@Override
	public void addDoubleWheel(int x, int y, String label, int labelLength, double min, double max, int digits, int contentLength,
			DataFunction<Double> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JSpinner contentSpinner = new JSpinner();
		add(contentSpinner);
		contentSpinner.setModel(new SpinnerNumberModel(min, min, max, 1));
		resizeTasks.add(() -> {contentSpinner.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentSpinner.setValue(function.getValue());
		
		updateTasks.add(() -> {contentSpinner.setValue(function.getValue());});
		contentSpinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				function.setValue((Double) contentSpinner.getValue());
				update();
			}
		});
		resize();
		update();
	}

	@Override
	public void addCheckbox(int x, int y, String label, int labelLength, int contentLength,
			DataFunction<Boolean> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JCheckBox contentBox = new JCheckBox();
		add(contentBox);
		resizeTasks.add(() -> {contentBox.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentBox.setSelected(function.getValue());
		
		updateTasks.add(() -> {contentBox.setSelected(function.getValue());});
		contentBox.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				function.setValue(contentBox.isSelected());
				update();
			}
		});
		resize();
		update();
	}

	@Override
	public <T> void addOptions(int x, int y, String label, int labelLength, int contentLength, String[] optionLabels, T[] options,
			DataFunction<T> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, cellHeight);});
		JComboBox<String> contentBox = new JComboBox<String>(optionLabels);
		add(contentBox);
		resizeTasks.add(() -> {contentBox.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, cellHeight);});
		
		labelLabel.setText(label);
		contentBox.setEditable(false);
		
		updateTasks.add(() -> {contentBox.setSelectedIndex(Arrays.asList(options).indexOf(function.getValue()));});
		contentBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				function.setValue(options[contentBox.getSelectedIndex()]);
				update();
			}
		});
		resize();
		update();
	}
	@Override
	public <T> void addOptions(int x, int y, String label, int labelLength, int contentLength, int contentHeight, String[] optionLabels, T[] options,
			DataFunction<T> function)
	{
		JLabel labelLabel = new JLabel();
		add(labelLabel);
		resizeTasks.add(() -> {labelLabel.setBounds(x * cellWidth, y * cellHeight, labelLength * cellWidth, contentHeight * cellHeight);});
		
		DefaultListModel<String> contentModel = new DefaultListModel<String>();
		for (int i = 0; i < optionLabels.length; ++i) contentModel.add(i, optionLabels[i]);
		JList<String> contentList = new JList<String>();
		contentList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentList.setModel(contentModel);
		
		add(contentList);
		resizeTasks.add(() -> {contentList.setBounds((x + labelLength) * cellWidth, y * cellHeight, contentLength * cellWidth, contentHeight * cellHeight);});
		
		labelLabel.setText(label);
		
		updateTasks.add(() -> {contentList.setSelectedIndex(Arrays.asList(options).indexOf(function.getValue()));});
		contentList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				function.setValue(options[contentList.getSelectedIndex()]);
				update();
			}
		});
		resize();
		update();
	}
	
	@Override
	public <E extends Enum<E>> void addOptions(int x, int y, String label, int labelLength, int contentLength, 
			E[] options, DataFunction<E> function)
	{
		String[] optionLabels = new String[options.length];
		for (int i = 0; i < options.length; ++i) optionLabels[i] = options[i].name();
		addOptions(x, y, label, labelLength, contentLength, optionLabels, options, function);
		resize();
		update();
	}
	@Override
	public <E extends Enum<E>> void addOptions(int x, int y, String label, int labelLength, int contentLength, int contentHeight, 
			E[] options, DataFunction<E> function)
	{
		String[] optionLabels = new String[options.length];
		for (int i = 0; i < options.length; ++i) optionLabels[i] = options[i].name();
		addOptions(x, y, label, labelLength, contentLength, contentHeight, optionLabels, options, function);
		resize();
		update();
	}
	
	@Override
	public void addButton(int x, int y, String label, int w, int h, ButtonFunction function)
	{
		JButton contentButton = new JButton();
		resizeTasks.add(() -> {contentButton.setBounds(x * cellWidth, y * cellHeight, w * cellWidth, h * cellHeight);});
		
		contentButton.setText(label);
		
		contentButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				function.onClick();
				update();
			}
		});
		resize();
		update();
	}
	
	@Override
	public MenuSlate addSubSlate(int x, int y, int w, int h, DataFunction<MenuSlate> function)
	{
		EditorPanel contentPanel = new EditorPanel();
		contentPanel.setSize(w * cellWidth, h * cellHeight);
		add(contentPanel);
		resizeTasks.add(() -> {contentPanel.setBounds(x * cellWidth, y * cellHeight, w * cellWidth, h * cellHeight);});
		updateTasks.add(() -> {propogate = false; contentPanel.update(); propogate = true;});
		contentPanel.updateTasks.add(() -> {if (propogate) update();});
		contentPanel.setCells(w, h);
		// TODO allow swapping of subslates
		resize();
		update();
		return contentPanel;
	}
}
