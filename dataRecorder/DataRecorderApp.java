package dataRecorder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import ubiEvents.UbiEventListener;
import clientConnector.ClientConnector;
import data.live.Position;
import data.live.Tag;
import data.live.TagManager;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * UbiGui - Data Recorder App
 * 
 * @author Joe Sankey
 */
public class DataRecorderApp extends javax.swing.JPanel implements
		UbiEventListener {

	/**
	 * Waits 30 seconds and then gets the tag from the tagmanager
	 * 
	 * @author Joe Sankey
	 */
	class GetTagResults extends TimerTask {
		@Override
		public void run() {
			System.out.println("Time's up!");
			final Tag tag = tagManager.getTag((String) tagSelectorModel
					.getSelectedItem());
			final Position pos = tag.getAverages();

			// get the actual height
			final double actualHeight = Double
					.valueOf((String) heightSelectorModel.getSelectedItem());
			
			if(actualHeight == 0)
			{
				zero = pos.z;
				zeroField.setText(Double.toString(zero));
				tagSelector.setEnabled(true);
				heightSelector.setEnabled(true);
				takeReadingButton.setEnabled(true);
				return;
			}
			
			//take away the offset
			pos.z = pos.z - zero;

			resultsTable.setValueAt(tag.getId().toString(), resultsTableCounter, 0);
			resultsTable.setValueAt(Double.toString(pos.x), resultsTableCounter, 1);
			resultsTable.setValueAt(Double.toString(pos.y), resultsTableCounter, 2);
			resultsTable.setValueAt(Double.toString(pos.z), resultsTableCounter, 3);
			resultsTable.setValueAt(Double.toString(actualHeight), resultsTableCounter, 4);
			resultsTable.setValueAt(Double.toString(Math.abs(pos.z
					- actualHeight)), resultsTableCounter, 5);
			
			 resultsTableCounter++;
			 
			 tagSelector.setEnabled(true);
			 heightSelector.setEnabled(true);
			 takeReadingButton.setEnabled(true);
			 
		}
		
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Start the app
	 * 
	 * @author Joe Sankey
	 */
	public static void main(String[] args) {
		new DataRecorderApp();
	}

	// --------------------------------------------------------------------------
	// Attributes
	// --------------------------------------------------------------------------

	private JTable tagTable;
	private JComboBox tagSelector;
	private JComboBox heightSelector;
	private JButton takeReadingButton;
	private JScrollPane resultsScrollPane;
	private JScrollPane tagsScrollPane;

	private JTable resultsTable;
	private JDialog jDialog1;
	private JLabel tagSelectorLabel;
	private TableModel TagTableModel;
	private JTextField zeroField;
	private JLabel jLabel1;
	private JProgressBar jProgressBar1;
	private JLabel zeroLabel;
	private DefaultComboBoxModel tagSelectorModel;
	private DefaultComboBoxModel heightSelectorModel;
	private String[][] tagTableData;
	private String[][] resultsTableData;
	JFrame frame;
	
	private int tagTableCounter;
	private int resultsTableCounter;
	private TagManager tagManager;
	private double zero;
	// --------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------

	/**
	 * Setup and Create the UI of the app
	 * 
	 * @author Joe Sankey
	 */
	public DataRecorderApp() {
		super();
		
		/*
		 *  Testing the google talk status update
		 
			GoogleTalkInterface gChat = new GoogleTalkInterface("ubisense@notesnapper.com", "dcucomputing");
		
			gChat.setStatus("From the dataRecorder");
		
		*/
		try {

			// Set the layout type and UI options
			GroupLayout thisLayout = new GroupLayout((JComponent)this);
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(701, 549));
			setAutoscrolls(true);

			// separator to keep the 2 tables a part
			final JSeparator separator = new JSeparator();

			// tag selector data model
			tagSelectorModel = new DefaultComboBoxModel(new String[] {});

			// tag selector control
			tagSelector = new JComboBox(tagSelectorModel);
			tagSelector.setEnabled(false);
			
			// tag selector label
			tagSelectorLabel = new JLabel("Selected Tag:");

			// height selector
			final JLabel heightSelectorLabel = new JLabel("Actual Height:");

			// height selector model
			heightSelectorModel = new DefaultComboBoxModel(new String[] {
					"0.0", "0.1", "0.2", "0.3", "0.4", "0.5" });

			// height selector control
			heightSelector = new JComboBox(heightSelectorModel);
			heightSelector.setEnabled(false);
			
			// take reading button
			takeReadingButton = new JButton("Take Reading");
			takeReadingButton.setEnabled(false);
			takeReadingButton.addActionListener(new ActionListener() {

				/**
				 * Takes readings from the selected tag for 30 seconds, then
				 * calculates the average and the error from the actual height.
				 * Results are displayed in the results table.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Taking Reading");
					
					//getJDialog1().pack();
					//getJDialog1().setVisible(true);
					
					
					// If there is no tag selected, do nothing
					if(tagSelectorModel.getSize() == 0)
						return;
					
					// start the recording of data
					tagManager.addTag((String) tagSelectorModel
							.getSelectedItem());
					
					tagSelector.setEnabled(false);
					heightSelector.setEnabled(false);
					takeReadingButton.setEnabled(false);
					
					// wait 30 seconds and get the results
					final TimerTask recordResults = new GetTagResults();
					final Timer timer = new Timer();
					timer.schedule(recordResults, 10000);
				}
			});

			// counts the number of tags in the Tables
			tagTableCounter = 0;
			resultsTableCounter = 0;
			
			// scroll Pane for tag table
			tagsScrollPane = new JScrollPane();
			tagsScrollPane.setEnabled(true);

			// Column names for tagTable
			final String[] columnNames = { "Tag ID", "X", "Y", "Z" };

			// holds the data of the tag table
			tagTableData = new String[15][4];

			// The first cell needs to be set to something so we can search the
			// table - not good
			for (int i = 0; i < tagTableData.length; i++) {
				tagTableData[i][0] = " ";
			}

			// The data for the tag table is stored in this model
			TagTableModel = new AbstractTableModel() {
				private static final long serialVersionUID = 1L;

				public int getColumnCount() {
					return columnNames.length;
				}

				@Override
				public String getColumnName(int col) {
					return columnNames[col].toString();
				}

				public int getRowCount() {
					return tagTableData.length;
				}

				public Object getValueAt(int row, int col) {
					return tagTableData[row][col];
				}

				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}

				@Override
				public void setValueAt(Object value, int row, int col) {
					tagTableData[row][col] = (String) value;
					fireTableCellUpdated(row, col);
				}
			};

			// The tag table which display all detected tags
			tagTable = new JTable(TagTableModel);
			tagsScrollPane.setViewportView(tagTable);
			tagTable.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1,
					false));

			// Scroll pane for the results table
			resultsScrollPane = new JScrollPane();
			resultsScrollPane.setEnabled(true);
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(tagsScrollPane, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
				.addGap(18)
				.addComponent(separator, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(tagSelector, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(tagSelectorLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(heightSelectorLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(heightSelector, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(takeReadingButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(getZeroLabel(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(getZerField(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(17)
				.addComponent(resultsScrollPane, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(25, Short.MAX_VALUE));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.addComponent(separator, GroupLayout.Alignment.LEADING, 0, 701, Short.MAX_VALUE)
				.addGroup(thisLayout.createSequentialGroup()
				    .addPreferredGap(separator, tagSelectorLabel, LayoutStyle.ComponentPlacement.INDENT)
				    .addGroup(thisLayout.createParallelGroup()
				        .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				            .addComponent(tagSelectorLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
				            .addComponent(tagSelector, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
				            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				            .addComponent(heightSelectorLabel, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
				            .addComponent(heightSelector, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				            .addComponent(takeReadingButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
				            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				            .addComponent(getZeroLabel(), GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
				            .addComponent(getZerField(), GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
				            .addGap(0, 39, Short.MAX_VALUE))
				        .addComponent(tagsScrollPane, GroupLayout.Alignment.LEADING, 0, 677, Short.MAX_VALUE)
				        .addComponent(resultsScrollPane, GroupLayout.Alignment.LEADING, 0, 677, Short.MAX_VALUE))
				    .addContainerGap()));

			final String[][] tableData = new String[15][6];

			// Column names for the results table
			final String[] resultsColumnNames = { "Tag ID", "Mean X",
					"Mean Y", "Mean Z", "Actual Height", "Error" };

			// The data for the results table is stored in this model
			final TableModel resultsTableModel = new AbstractTableModel() {

				private static final long serialVersionUID = 1L;

				public int getColumnCount() {
					return resultsColumnNames.length;
				}

				@Override
				public String getColumnName(int col) {
					return resultsColumnNames[col].toString();
				}

				public int getRowCount() {
					return tableData.length;
				}

				public Object getValueAt(int row, int col) {
					return tableData[row][col];
				}

				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}

				@Override
				public void setValueAt(Object value, int row, int col) {
					tableData[row][col] = (String) value;
					fireTableCellUpdated(row, col);
				}
			};

			resultsTable = new JTable(resultsTableModel);
			resultsScrollPane.setViewportView(resultsTable);

			/*
			 * This is all layout code which was generated by a GUI builder
			 * tool.
			 */
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// start the gui
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		// start the tag manager
		tagManager = new TagManager();

		// listen for tag events
		tagManager.addUbiEventListener(this);

		// keep tag history for 30 seconds.
		tagManager.setTimeWindow(30000);

		// connect to the tag server
		final ClientConnector clientConnector = new ClientConnector(tagManager);
		clientConnector.connect("192.168.33.39", 12000);

	}

	/**
	 * Method to check if the tagTable already contains a tag.
	 * 
	 * @param id
	 *            The tag id to check
	 * @return If the tag is found this method will return its row in the table
	 *         as an integer, if the tag isn't found -1 is returned.
	 * @author Joe Sankey
	 */
	private int containsTag(String id) {
		for (int x = 0; x < tagTableData.length; x++) {
			if ((id.compareTo(tagTableData[x][0])) == 0) {
				return x;
			}
		}
		return -1;

	}

	/**
	 * Setup the GUI and show it.
	 * 
	 * @author Joe Sankey
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("UbiGui - Data Recorder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		final DataRecorderApp newContentPane = this;
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This method was meant to be used for new tags but ive put everything into
	 * tagUpdated, for now....
	 * 
	 * @author Joe Sankey
	 */
	@Override
	public void tagAdded(Tag tag) {

	}

	/**
	 * This method is used to communicate between the GUI and server --
	 * something better is needed, XML This is the point where tag information
	 * comes into the GUI app. It updates the table and other controls with the
	 * latest tag information.
	 * 
	 * @param tag
	 *            The tag which needs to be updated or added.
	 * @author Joe Sankey
	 */
	@Override
	public void tagStream(BigInteger tag, Position newPosition) {

		// get the Position of the tag
		final Position currentPos = newPosition;

		// lookup the tag in the table
		final int pos = containsTag(tag.toString());

		// If the tag is found in the table, update it.
		if (pos != -1) {
			TagTableModel.setValueAt(tag.toString(), pos, 0);
			TagTableModel.setValueAt(Double.toString(currentPos.x), pos, 1);
			TagTableModel.setValueAt(Double.toString(currentPos.y), pos, 2);
			TagTableModel.setValueAt(Double.toString(currentPos.z), pos, 3);
			System.out.println("Tag update - GUI");
		} else // tag cant be found add it to the next empty row, which
		// tagCounter stores.
		{
			TagTableModel.setValueAt(tag.toString(), tagTableCounter, 0);
			TagTableModel.setValueAt(Double.toString(currentPos.x), tagTableCounter,
					1);
			TagTableModel.setValueAt(Double.toString(currentPos.y), tagTableCounter,
					2);
			TagTableModel.setValueAt(Double.toString(currentPos.z), tagTableCounter,
					3);

			if(tagSelectorModel.getSize() == 0)
			{
				tagSelector.setEnabled(true);
				heightSelector.setEnabled(true);
				takeReadingButton.setEnabled(true);
			}
			
			// add the tag to the tag selector combo box
			tagSelectorModel.addElement(tag.toString());

			
			System.out.println("Tag added - GUI");
			tagTableCounter++;
		}
	}

	/**
	 * This method was meant to be used for new tags but ive put everything into
	 * tagStream, for now....
	 * 
	 * @author Joe Sankey
	 */
	@Override
	public void tagUpdated(Tag tag) {

	}
	
	private JDialog getJDialog1() {
		if(jDialog1 == null) {
			jDialog1 = new JDialog();
			jDialog1.setEnabled(true);
			JLabel jLabel1 = new JLabel("Please wait while readings are taken");
			
			jDialog1.getContentPane().add(jLabel1, BorderLayout.CENTER);

		}
		return jDialog1;
	}
	
	private JLabel getZeroLabel() {
		if(zeroLabel == null) {
			zeroLabel = new JLabel("Zero: ");
		}
		return zeroLabel;
	}
	
	private JTextField getZerField() {
		if(zeroField == null) {
			zeroField = new JTextField("Not Set");
			zeroField.setEditable(false);
		}
		return zeroField;
	}
	
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			
		}
		return jLabel1;
	}

}
