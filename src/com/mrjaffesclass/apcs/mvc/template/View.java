package com.mrjaffesclass.apcs.mvc.template;
import com.mrjaffesclass.apcs.messenger.*;
import javax.swing.ImageIcon;
import java.awt.*;
import javax.swing.*;


/**
 * MVC Template
 * This is a template of an MVC framework used by APCS for the 
 * LandMine project (and others)
 * @author Roger Jaffe
 * @version 1.0
 * 
 */
public class View extends javax.swing.JFrame implements MessageHandler {

  private final Messenger mvcMessaging;
  //pButtonarray is an array of buttons which are the game board.
  private JButton pButtonarray[][] ;
  private Boolean clicked[][];
  int lives = 3;
  int score = 0;
  
  /**
   * Creates a new view
   * @param messages mvcMessaging object
   */
  public View(Messenger messages) {
    mvcMessaging = messages;   // Save the calling controller instance
    initComponents();           // Create and init the GUI components
  }
  
  /**
   * Initialize the model here and subscribe
   * to any required messages
   */
  public void init() {
    // Subscribe to messages here
    mvcMessaging.subscribe("model:gridSizeChanged", this);
    mvcMessaging.subscribe("model:numMinesChanged", this);
    mvcMessaging.subscribe("model:StartGame", this);
    mvcMessaging.subscribe("model:hitASafeSpot", this);
    mvcMessaging.subscribe("model:hitABomb", this);      
    
  }
  
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    if (messagePayload != null) {
      System.out.println("MSG: received by view: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by view: "+messageName+" | No data sent");
    }
    if (messageName.equals("model:StartGame")) {
        StartGame(Integer.parseInt(jLabel8.getText()));
        //dblClick(Integer.parseInt(jLabel8.getText()));
        jLabel6.setText("");
        lives = 3;
        jLabel5.setText(Integer.toString(lives));
        score = 0;
        jLabel3.setText(Integer.toString(score));
    }
    else if (messageName.equals("model:gridSizeChanged")) {
      jLabel8.setText(messagePayload.toString());
      //jButton1.setText("xyz");
      
    }
    else if (messageName.equals("model:numMinesChanged")) {
      jLabel10.setText(messagePayload.toString());
      //jButton1.setText("xyz");
      
    }
    else if (messageName.equals("model:hitABomb")) {
       if (lives > 1){
        lives--;       
       }
       else {
       lives = 0;
       jLabel6.setText("YOU LOSE!!!!!");
       }
       
        jLabel5.setText(Integer.toString(lives));
        hitABomb(((MessagePayload)messagePayload).getField(),((MessagePayload)messagePayload).getDirection());
    }
    else if (messageName.equals("model:hitASafeSpot")) {
        if(lives > 0){
        score++;
    }
        jLabel3.setText(Integer.toString(score));        
        hitASafeSpot(((MessagePayload)messagePayload).getField(),((MessagePayload)messagePayload).getDirection());
    }
    else
    {
      //jLabel10.setText(messagePayload.toString());      
    }
  }
/*private void dblClick(int gameSize){
    clicked = new Boolean [gameSize] [gameSize];
    int ppositionx, ppositiony = 0;
        
        for (int row = 0 ; row < gameSize ; row++)
        {
            ppositiony = ppositiony + 23;
            ppositionx = 25;
            for (int x = 0 ; x < gameSize ; x++)
            {
                clicked [x] [row] = false;
            }
        }*/

/* StartGame creates the JButton Array that will be clicked to play hardcore minesweeper
  *@param gamesize is the integer value for the size of the board
  */
  private void StartGame(int gameSize){
    Container contentPane;
    JPanel infoPane;      
    pButtonarray = new JButton [gameSize] [gameSize];
    contentPane = getContentPane ();
    
        infoPane = jPanel1;
        jPanel1.removeAll();
       
        int ppositionx, ppositiony = 0;
        
        for (int row = 0 ; row < gameSize ; row++)
        {
            ppositiony = ppositiony + 23;
            ppositionx = 25;
            for (int x = 0 ; x < gameSize ; x++)
            {
                pButtonarray [x] [row] = new JButton ();
                pButtonarray [x] [row].setOpaque (false);
                pButtonarray [x] [row].setBounds (ppositionx, ppositiony, 25, 25);
                pButtonarray [x] [row].setPreferredSize(new Dimension(25, 25));
                
                
                pButtonarray [x] [row].addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       gameButtonActionPerformed(evt);
                    }
                });

                pButtonarray [x] [row].putClientProperty ("px", new Integer (x));
                pButtonarray [x] [row].putClientProperty ("prow", new Integer (row));
                infoPane.add (pButtonarray [x] [row]);
                ppositionx = ppositionx + 23;
            }
        }
        infoPane.add(new JLabel());  // This was a test to get last button to show up but it fixed the problem.  Hmmm.
        contentPane.add (infoPane);
        validate ();
    
  }
  /* 
  hitABomb is a method that changes the image of revealed bomb buttons. Makes it so they appear as red squares.
  @param x is the integer value for the xcoord of the button, whose image is to be changed
  @param y is the integer value for the ycoord of the button, whose image is to be changed
  */
  private void hitABomb(int x, int y) {
    ImageIcon ii = new ImageIcon("src\\com\\mrjaffesclass\\apcs\\mvc\\template\\bombred.png");
    pButtonarray[x][y].setIcon(ii);
  }
/*
  hitASafeSPot is a method that changes the image of a revealed safe spot to an image of a green square.
  @param x is the integer value for the xcoord of the button, whose image is to be changed
  @param y is the integer value for the ycoord of the button, whose image is to be changed
  */
  private void hitASafeSpot(int x, int y) {
    ImageIcon ii = new ImageIcon("src\\com\\mrjaffesclass\\apcs\\mvc\\template\\greennobomb.png");
    pButtonarray[x][y].setIcon(ii);
  }

  
  /**
   * Instantiate an object with the field number that was clicked (1 or 2) and
   * the direction that the number should go (up or down)
   * @param fieldNumber 1 or 2 for the field being modified
   * @param direction this.UP (1) or this.DOWN (-1), constants defined above
   * @return the HashMap payload to be sent with the message
   */
  private MessagePayload createPayload(int fieldNumber, int direction) {
    MessagePayload payload = new MessagePayload(fieldNumber, direction);
    return payload;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        upButton1 = new javax.swing.JButton();
        upButton2 = new javax.swing.JButton();
        downButton1 = new javax.swing.JButton();
        downButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        startButton.setText("Start New Game");
        startButton.setToolTipText("Click me to see the View send a message to the Controller to let it know that the button was pushed");
        startButton.setActionCommand("button1Clicked");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonbuttonClickActionPerformed(evt);
            }
        });

        jLabel1.setText("Land Mine!");

        jLabel7.setText("Game size");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("jLabel8");

        jLabel9.setText("#Mines");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("jLabel10");

        jLabel11.setText("Game Settings");

        upButton1.setText("Up");
        upButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButton1ActionPerformed(evt);
            }
        });

        upButton2.setText("Up");
        upButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButton2ActionPerformed(evt);
            }
        });

        downButton1.setText("Down");
        downButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButton1ActionPerformed(evt);
            }
        });

        downButton2.setText("Down");
        downButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButton2ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Score:");

        jLabel3.setText("0");

        jLabel4.setText("Lives:");

        jLabel5.setText("3");

        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton))
                        .addComponent(jLabel11)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(upButton2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(downButton2))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(upButton1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(downButton1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(521, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(startButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(upButton1)
                    .addComponent(downButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(upButton2)
                    .addComponent(downButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(0, 208, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void startButtonbuttonClickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonbuttonClickActionPerformed
    // Send a message to the controller!
    mvcMessaging.notify("view:buttonClick", null, true);
    mvcMessaging.notify("view:newGameClicked", null, true);
  }//GEN-LAST:event_startButtonbuttonClickActionPerformed

  private void gameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                         
    // Send a message to the controller!
    JButton btn = (JButton) evt.getSource ();
    
    //getting coordinates
    int pCol = ((Integer) btn.getClientProperty ("px")).intValue ();
    int pRow = ((Integer) btn.getClientProperty ("prow")).intValue ();
    mvcMessaging.notify("view:gameButtonClick", createPayload(pCol, pRow), true);

    //pButtonarray [px] [prow].setIcon (image);

  }                                                        

  /**
   * Handler for the up button for field 1
   * @param evt 
   */
  private void upButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButton1ActionPerformed
    // Increment the 1st data field
    mvcMessaging.notify("view:changeButton", createPayload(1, Constants.UP), true);
  }//GEN-LAST:event_upButton1ActionPerformed

  private void upButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButton2ActionPerformed
    // Increment the 2nd data field
    mvcMessaging.notify("view:changeButton", createPayload(2, Constants.UP), true);
  }//GEN-LAST:event_upButton2ActionPerformed

  private void downButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButton1ActionPerformed
    // Decrement the first data field
    mvcMessaging.notify("view:changeButton", createPayload(1, Constants.DOWN), true);
  }//GEN-LAST:event_downButton1ActionPerformed

  private void downButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButton2ActionPerformed
    // Decrement the second data field
    mvcMessaging.notify("view:changeButton", createPayload(2, Constants.DOWN), true);
  }//GEN-LAST:event_downButton2ActionPerformed

  /**
   * @param args the command line arguments
   */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downButton1;
    private javax.swing.JButton downButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton startButton;
    private javax.swing.JButton upButton1;
    private javax.swing.JButton upButton2;
    // End of variables declaration//GEN-END:variables
}
