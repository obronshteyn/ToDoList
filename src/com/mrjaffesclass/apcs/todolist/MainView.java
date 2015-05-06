package com.mrjaffesclass.apcs.todolist;

import com.mrjaffesclass.apcs.messenger.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;
/**
 * To do list main view
 * @author Roger Jaffe
 * @version 1.0
 * 
 */
public class MainView extends javax.swing.JFrame implements MessageHandler {

  // Instance constants
  private final int ID_FIELD = 0;
  private final int DONE_FIELD = 1;
  private final int DESCRIPTION_FIELD = 2;
  private final int DATE_FIELD = 3;
  
  private final int DONE_COLUMN = 0;
  private final int DATE_COLUMN = 3;
  
  private final int DONE_FIELD_WIDTH = 65;
  private final int DESCRIPTION_FIELD_WIDTH = 475;
  private final int DATE_FIELD_WIDTH = 150;
  private final int ROW_HEIGHT = 25;
  
  private final int X_POSITION = 100;
  private final int Y_POSITION = 100;
  
  // Messenger class gets saved here
  private final Messenger messenger;
  
  /**
   * Creates a new main view
   * @param _messenger mvcMessaging object
   */
  public MainView(Messenger _messenger) {
    messenger = _messenger;   // Save the calling controller instance
    this.setLocation(X_POSITION, Y_POSITION); // Set main window location
    initComponents();           // Create and init the GUI components
    
    // Make adjustments to the column widths to suit our needs
    // Remove the ID column and set the row height
    jTable1.getColumnModel().getColumn(DONE_FIELD).setPreferredWidth(DONE_FIELD_WIDTH);  // Set width of checkbox column
    jTable1.getColumnModel().getColumn(DESCRIPTION_FIELD).setPreferredWidth(DESCRIPTION_FIELD_WIDTH);
    jTable1.getColumnModel().getColumn(DATE_FIELD).setPreferredWidth(DATE_FIELD_WIDTH);
    jTable1.getColumnModel().getColumn(DATE_FIELD).setCellRenderer(null);
    jTable1.removeColumn(jTable1.getColumnModel().getColumn(ID_FIELD));  // Remove the ID column from the table
    jTable1.setRowHeight(ROW_HEIGHT);
  }
  
  /**
   * Do any required initialization code and subscribe
   * to messages to which the view needs to respond
   */
  public void init() {
    messenger.subscribe("ready", this);
    messenger.subscribe("items", this);
  }
  
  // This method implements the messageHandler method defined in
  // the MessageHandler interface and will fire when a message 
  // that this module has subscribed to is sent
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    switch (messageName) {
      // The app is loaded and ready to go
      case "ready":
        // Ask for the to do list items
        messenger.notify("getItems", null, true);
        break;
        
      // The message contains the list of to do items in messagePayload
      // The tableModel has the data that's displayed in the table. 
      case "items":
        ArrayList list = (ArrayList)messagePayload;
        DefaultTableModel tableModel = (DefaultTableModel)jTable1.getModel();
        loadTableModel(tableModel, list);
        break;
    }
  }

  /**
   * Loads the to do list into the tableModel to display in the table
   * If we put the to do list's data in the table model the view will
   * paint the table with the table model's data.
   * @param tableModel  TableModel for the jTable
   * @param list        To do list of items
   */
  private void loadTableModel(DefaultTableModel tableModel, ArrayList list) {
    // Get the number of items in the list and
    // set the tableModel with this size
    int size = list.size();
    tableModel.setRowCount(size);
    
    // Look through the to do list and save the to do item fields
    // in the table model.  The table model will see the changes
    // and cause the table to repaint with the new data
    for (int i=0; i<size; i++) {
      ToDoItem item = (ToDoItem)(list.get(i));
      tableModel.setValueAt(item.getId(), i, ID_FIELD);
      tableModel.setValueAt(item.isDone(), i, DONE_FIELD);
      tableModel.setValueAt(item.getDescription(), i, DESCRIPTION_FIELD);
      //display formatted date, do nothing if no date present
      if(item.getDate() != null){
        Date today = item.getDate();
        SimpleDateFormat df = new SimpleDateFormat("EEE MM/dd");
        String dateOut = df.format(today);
        tableModel.setValueAt(dateOut, i, DATE_FIELD);
      }
      
    }
}
  
  /**
   * Light up the edit item dialog
   * @param item Item to edit
   */
  private void editItem(ToDoItem item) {
    EditView dialog = new EditView(this, item, messenger);
    dialog.init();
    dialog.setVisible(true);
  }
  
  /**
   * User clicked the completed check box. Don't show edit dialog
   * because the done field is toggled by clicking the check box
   * @param item Item to edit
   */
  private void toggleDone(ToDoItem item) {
    messenger.notify("saveItem", item, true);
  }
  
  /**
   * Creates a ToDoItem from the data in the table model
   */
  private ToDoItem createItemFromTableModelRow(int row) {
    DefaultTableModel tableModel = (DefaultTableModel)jTable1.getModel();
    
    String dateString = (String)tableModel.getValueAt(row, DATE_FIELD);
    DateFormat format = new SimpleDateFormat("EEE MM/dd");
    Date date = new Date();
      try {
          if(dateString != null)
            date = format.parse(dateString);
      } 
      catch (ParseException ex) {
          Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
      }
    // Now create a ToDoItem that we can pass to the editing dialog
    // The done field is toggled when the user clicks the checkbox
    ToDoItem item = new ToDoItem(              
      (int)tableModel.getValueAt(row, ID_FIELD),
      (String)tableModel.getValueAt(row, DESCRIPTION_FIELD),
      (boolean)tableModel.getValueAt(row, DONE_FIELD),
      date     
    );
    return item;
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        newItemBtn = new javax.swing.JButton();
        aboutBtn = new javax.swing.JButton();
        removeCompleteItemsBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sortUpButton = new javax.swing.JButton();
        sortDownButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Complete", "Description", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        newItemBtn.setText("New");
        newItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemBtnActionPerformed(evt);
            }
        });

        aboutBtn.setText("About...");
        aboutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutBtnActionPerformed(evt);
            }
        });

        removeCompleteItemsBtn.setText("Remove completed items");
        removeCompleteItemsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCompleteItemsBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("Sort by Date:");

        sortUpButton.setText("^");
        sortUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortUpButtonActionPerformed(evt);
            }
        });

        sortDownButton.setText("v");
        sortDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortDownButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(newItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(removeCompleteItemsBtn)
                .addGap(175, 175, 175)
                .addComponent(aboutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortDownButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newItemBtn)
                    .addComponent(aboutBtn)
                    .addComponent(removeCompleteItemsBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sortUpButton)
                    .addComponent(sortDownButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    // Get the clicked item data
    // Get the row number that was clicked in the table
    // then find the item id of the clicked item
    int row = jTable1.getSelectedRow(); 
    int col = jTable1.getSelectedColumn();
    ToDoItem item = this.createItemFromTableModelRow(row);
    
    // If the checkbox column was clicked, then we can just toggle the item's
    // done field.  If any other column was clicked we should open the editItem
    // dialog so the item can be edited.
    if (col == DONE_COLUMN) {
      toggleDone(item);
    } 
    else {
      editItem(item);
    }
  }//GEN-LAST:event_jTable1MouseClicked

  private void newItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemBtnActionPerformed
    ToDoItem item = new ToDoItem(-1, "New to do item", false, null);
    editItem(item);
  }//GEN-LAST:event_newItemBtnActionPerformed

  private void removeCompleteItemsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCompleteItemsBtnActionPerformed
    messenger.notify("removeCompletedItems");
  }//GEN-LAST:event_removeCompleteItemsBtnActionPerformed

  private void aboutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutBtnActionPerformed
    AboutView dialog = new AboutView(this, true);
    dialog.setVisible(true);
  }//GEN-LAST:event_aboutBtnActionPerformed

    private void sortUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortUpButtonActionPerformed
        messenger.notify("sortUp");
    }//GEN-LAST:event_sortUpButtonActionPerformed

    private void sortDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortDownButtonActionPerformed
        messenger.notify("sortDown");
    }//GEN-LAST:event_sortDownButtonActionPerformed

  /**
   * @param args the command line arguments
   */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton newItemBtn;
    private javax.swing.JButton removeCompleteItemsBtn;
    private javax.swing.JButton sortDownButton;
    private javax.swing.JButton sortUpButton;
    // End of variables declaration//GEN-END:variables
}
