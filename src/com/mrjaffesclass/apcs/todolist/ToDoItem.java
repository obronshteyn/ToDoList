package com.mrjaffesclass.apcs.todolist;

import java.util.Date;

/**
 * To do item
 * 
 * @author Roger Jaffe
 * @version 1.0
 */
public class ToDoItem {

  /**
   * id:          ID number of to do item. Assigned when added to list
   * description: Description of to do item
   * done:        True if to do item is complete
   * date:        Date of item
   */
  private int id;               
  private String description;
  private boolean done;
  private Date date;
  
  /**
   * Constructor including date and completed parameter
   * @param _id           ID number of to do item
   * @param _description  Description of to do item
   * @param _done         Done flag
   * @param _date         Date of item
   */
  public ToDoItem(int _id, String _description, boolean _done, Date _date) {
    description = _description;
    id = _id;
    done = _done;     
    date = _date;
  } 
  
  /**
   * Constructor including date, not done default
   * @param _id ID number of to do item
   * @param _description Description of to do item
   * @param _date Date of item
   */
  public ToDoItem(int _id, String _description, Date _date) {
    description = _description;
    id = _id;
    done = false;     // Default to not completed
    date = _date;     
  }

  /**
   * Get the to do item description
   * @return The description of to do item
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set the to do item description
   * @param description The value to be set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the done flag
   * @return The done flag
   */
  public boolean isDone() {
    return done;
  }

  /**
   * Set the done flag
   * @param done The value to be set
   */
  public void setDone(boolean done) {
    this.done = done;
  }
  
  /**
   * Toggles the done flag
   */
  public void toggleDone() {
    this.done = !this.done;
  }

  /**
   * Get the to do item ID
   * @return ID of the to do item
   */
  public int getId() {
    return id;
  }
  
  /**
   * Sets the ID of the to do item. Can only be called from inside this class
   * @param id ID value to set
   */
  public void setId(int id) {
    this.id = id;
  }
  
  /**
   * Get the to do item date
   * @return Date of the to do item
   */
  public Date getDate() {
    return date;
  }
  
  /**
   * Sets the date of the to do item. Can only be called from inside this class
   * @param _date Date to be set
   */
  public void setDate(Date _date) {
    this.date = _date;
  }
  
  /**
   * Transfer the description, date, and done flag of another to do item into this one
   * @param anotherItem Item whose data values we are copying
   */
  public void merge(ToDoItem anotherItem) {
    this.setDescription(anotherItem.getDescription());
    this.setDone(anotherItem.isDone()); 
    this.setDate(anotherItem.getDate());
  }

}
