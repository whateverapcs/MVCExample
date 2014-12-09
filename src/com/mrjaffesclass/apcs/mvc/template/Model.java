package com.mrjaffesclass.apcs.mvc.template;

import com.mrjaffesclass.apcs.messenger.*;
import java.util.Random;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private int gridSize;
  private int numMines;
  private boolean[][] mineGrid;
  

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    mvcMessaging.subscribe("view:changeButton", this);
    mvcMessaging.subscribe("view:newGameClicked", this);
    mvcMessaging.subscribe("view:gameButtonClick", this);
  
    setGridSize(8);
    setNumMines(10);
    randomizeMines(getNumMines());
    //change to getNumMines() + 1 if missing a mine
  }
  /*
  randomizeMines takes the number of mines you want on the board and randomly inserts it into the minegrid as true values. If the mineGrid array equals true, then there is a bomb at that location in the array.
  @param number provides an integer value for the amount of mines to be seeded onto the board
  */
  public void randomizeMines(int number) {
    mineGrid = new boolean[getGridSize()][getGridSize()];
    for (int i=0; i<getGridSize(); i++)
    {
        for (int j=0; j<getGridSize(); j++)
        {
            mineGrid[i][j] = false;
        }
    }
    Random randomGenerator = new Random();
    for (int idx = 1; idx <= number; ++idx){
        int x = randomGenerator.nextInt(getGridSize());
        int y = randomGenerator.nextInt(getGridSize());
        mineGrid[x][y] = Boolean.TRUE;
    }
    mvcMessaging.notify("model:StartGame", gridSize, true);
    
  }
  
  
  
          
  
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    
    if (messageName == "view:gameButtonClick") {
        MessagePayload payload = (MessagePayload)messagePayload;
        int pCol = payload.getField();
        int pRow = payload.getDirection();
        if (mineGrid[pCol][pRow] == true) {
            //hit a bomb
            mvcMessaging.notify("model:hitABomb", new MessagePayload(pCol, pRow), true);
        }
        else {
            //hit a safe spot
            mvcMessaging.notify("model:hitASafeSpot", new MessagePayload(pCol, pRow), true);
        }
    }
    else if (messageName == "view:newGameClicked") {
        randomizeMines(getNumMines());
    }
    else {
        MessagePayload payload = (MessagePayload)messagePayload;
        int field = payload.getField();
        int direction = payload.getDirection();
    
        if (direction == Constants.UP) {
          if (field == 1) {
            setGridSize(getGridSize()+Constants.FIELD_1_INCREMENT);
          } else {
            setNumMines(getNumMines()+Constants.FIELD_2_INCREMENT);
          }
        } else {
          if (field == 1) {
            setGridSize(getGridSize()-Constants.FIELD_1_INCREMENT);
          } else {
            setNumMines(getNumMines()-Constants.FIELD_2_INCREMENT);
          }      
        }
     }
  }

  /**
   * Getter function for gridSize
   * @return Value of gridSize
   */
  public int getGridSize() {
    return gridSize;
  }

  /**
   * Setter function for GridSize
   * @param v New value of gridSize
   */
  public void setGridSize(int v) {
    gridSize = v;
    // When we set a new value to variable 1 we need to also send a
    // message to let other modules know that the variable value
    // was changed
    mvcMessaging.notify("model:gridSizeChanged", gridSize, true);
  }
  
  /**
   * Getter function for numMines
   * @return Value of numMines
   */
  public int getNumMines() {
    return numMines;
  }
  
  /**
   * Setter function for numMines
   * @param v New value of numMines
   */
  public void setNumMines(int v) {
    numMines = v;
    // When we set a new value to variable 2 we need to also send a
    // message to let other modules know that the variable value
    // was changed
    mvcMessaging.notify("model:numMinesChanged", numMines, true);
  }

}
