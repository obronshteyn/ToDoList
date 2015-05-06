package com.mrjaffesclass.apcs.todolist;

import java.util.*;
import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class AppModel implements MessageHandler {
  // Instance variables
  // messenger:   The Messenger object that handles messaging between classes
  // toDoList:    Keeps the list of to do items
  // nextId:      Keeps track of the ID that should be given to the next 
  //              ToDoItem that's added to the list
  private final Messenger messenger;
  private final ArrayList<ToDoItem> toDoList;   
  private int nextId = 0;                 
  
  /**
   * Model constructor: Create the data representation of the program
   * @param _messenger Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public AppModel(Messenger _messenger) {
    messenger = _messenger;
    toDoList = new ArrayList<>();   // We start with an empty list
  }
  
  /**
   * Initialize the model here, subscribe to any required messages and
   * load with some initial to do list items
   * 
   */
  public void init() {
    messenger.subscribe("getItems", this);
    messenger.subscribe("getItem", this);
    messenger.subscribe("saveItem", this);
    messenger.subscribe("deleteItem", this);
    messenger.subscribe("removeCompletedItems", this);
    messenger.subscribe("sortDown", this);
    messenger.subscribe("sortUp", this);
  }

  // This method implements the messageHandler method defined in
  // the MessageHandler interface
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    // Handle messages sent from other modules
    ToDoItem item;
    
    // Process the message
    switch (messageName) {
      // View has asked for the list of to do items
      // Send back the items
      case "getItems":
        messenger.notify("items", this.getItems(), true);
        break;
        
      // Somebody has asked for an individual item
      // Find the item and send it back
      case "getItem":
        messenger.notify("item", this.getItem((int)messagePayload), true);
        break;
        
      // Somebody wants us to save an item
      // Grab the to do item from the payload, save or update it to the 
      // to do list, send a confirmation message that it was saved
      // and send the updated to do list to others
      case "saveItem":        
        item = (ToDoItem)messagePayload;                  
        putItem(item);                                    
        messenger.notify("saved", null, true);            
        messenger.notify("items", this.getItems(), true); 
        break;
        
      // Somebody wants us to delete an item
      // Grab the to do item from the payload, delete it from
      // to do list, send a confirmation message that it was saved
      // and send the updated to do list to others
      case "deleteItem":
        item = (ToDoItem)messagePayload;                  
        this.deleteItem(item);
        messenger.notify("saved", null, true);
        messenger.notify("items", this.getItems(), true);
        break;
        
      // We've been told to remove all items that have their 'done' flag
      // set.  Do it, then send a confirmation message, then send the
      // updated to do list to others
      case "removeCompletedItems":
        removeCompletedItems();
        messenger.notify("saved");
        messenger.notify("items", this.getItems());
          
      case "sortDown":
        ArrayList sortList = sort(this.getItems());
        messenger.notify("saved", null, true);
        messenger.notify("items", sortList, true);
        refreshList(sortList);
          
      case "sortUp":
        ArrayList sortListUp = sortUp(sort(this.getItems()));
        messenger.notify("saved", null, true);
        messenger.notify("items", sortListUp, true);
        refreshList(sortListUp);
    }
  }

  /** 
   * Find item by id number
   * @param id ID of the item to fine
   * @return The item with id that was found in the list, null if the ID wasn't found
   */
  private ToDoItem find(int id) {
    ToDoItem foundItem = null;
    for (ToDoItem item : toDoList) {
      if (item.getId() == id) {
        foundItem = item;
        break;    // Break the for loop when item is found
      }
    }  
    return foundItem;
  }
  
  /**
   * Get an item with the supplied ID
   * @param id ID of the item to return
   * @return The desired item; null if item ID is not found
   */
  public ToDoItem getItem(int id) {
    return find(id);
  }
  
  /**
   * Get the to do list
   * @return The to do list
   */
  public ArrayList getItems() {
    return toDoList;
  }
  
  /** 
   * Add a new to do item to the list
   * @param item To do item to add
   * @return Item added
   */
  public ToDoItem putItem(ToDoItem item) {
    if (item.getId() == -1) {
      // Item is new, add it to the list
      item.setId(nextId);
      toDoList.add(item);
      nextId++;
    } else {
      // Item already exists, modify the fields of the existing item
      ToDoItem storedItem = this.find(item.getId());
      storedItem.merge(item);
    }
    return item;
  }
  
  /**
   * Delete a to do item
   * @param id ID of the item to delete
   * @return True if the item was found and removed
   */
  public boolean deleteItem(int id) {
    ToDoItem item = find(id);
    return toDoList.remove(item);
  }

  /**
   * Delete a to do item
   * @param item ToDoItem to delete
   * @return true if the item was found and removed
   */
  public boolean deleteItem(ToDoItem item) {
    return deleteItem(item.getId());
  }
  
  /**
   * Removes all completed items from the to do list
   */
  public void removeCompletedItems() {
    // Create an empty list
    ArrayList<ToDoItem> newList = new ArrayList<>();
    
    // Loop through the to do list and if isDone is false
    // add it to the new list
    for (ToDoItem item : toDoList) {
      if (!item.isDone()) {
        newList.add(item);
      }
    }
    // Clear the to do list and add the items that were not completed
    refreshList(newList);
  }
  
  /**
   * Clears and replaces existing list
   * @param newList list to replace existing list
   */
  public void refreshList(ArrayList<ToDoItem> newList){
    toDoList.clear();
    for (ToDoItem item : newList) {
      toDoList.add(item);
    }
  }
  
  /**
   * Sorts ArrayList by date using merge method
   * @param a ArrayList of ToDoItems to be sorted
   * @return sorted ArrayList
   */
  public ArrayList<ToDoItem> sort(ArrayList<ToDoItem> a){
      if(a.size() <= 1)
          return a;
      int middle = a.size() / 2;
      ArrayList<ToDoItem> left = new ArrayList();
      ArrayList<ToDoItem> right = new ArrayList();
      ArrayList<ToDoItem> leftovers = new ArrayList();
      for (ToDoItem a1 : a) {
        if(a1.getDate() == null){
          leftovers.add(a1);
        }
        else{ 
          if(a.indexOf(a1) < middle)
            left.add((ToDoItem)a1);
          else
            right.add((ToDoItem)a1);
        }   
      }
      left = sort(left);
      right = sort(right);
      ArrayList result = merge(left, right);
      result.addAll(leftovers);
      return result;
  }
  /**
   * Sorts list and then inverts it
   * @param a list to be sorted
   * @return list sorted in reverse order
   */
  public ArrayList<ToDoItem> sortUp(ArrayList<ToDoItem> a){
    ArrayList sortList = sort(a);
    for(int i = 0; i < sortList.size() / 2; i++){
        ToDoItem tempItem = (ToDoItem)sortList.get(i);
        sortList.set(i , sortList.set(sortList.size() - (i + 1), tempItem));
    }
    return null;
  }
  /**
   * Used in conjunction with sort method to sort list of items by date
   * @param left 1st half of split ArrayList
   * @param right 2nd half of split ArrayList
   * @return merged ArrayList
   */
  public ArrayList<ToDoItem> merge(ArrayList<ToDoItem> left, ArrayList<ToDoItem> right){
    ArrayList<ToDoItem> result = new ArrayList();
    int leftIndex = 0;
    int rightIndex = 0;
    int len = left.size() + right.size();
    for(int i = 0; i < len; i++){
        if((leftIndex < left.size()) && (rightIndex < right.size())){
            if(left.get(leftIndex).getDate().compareTo(right.get(rightIndex).getDate()) < 0){
                result.add(left.get(leftIndex));
                leftIndex++;
            }
            else{
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }
        else{
            if(leftIndex >= left.size()){
                result.add(right.get(rightIndex));
                rightIndex++;
            }
            else{
                result.add(left.get(leftIndex));
                leftIndex++;
            }
        }
    }
    return result;
  }
}