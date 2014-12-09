package com.mrjaffesclass.apcs.mvc.template;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class ComparisonSweeper extends Applet implements MouseListener
{
    public ComparisonSweeper(){
    }
    Button table[][];
    boolean bomb[][];
    boolean checkwinbool[][];
    boolean clicked[][];
    int count = 0, bombsremaining;
    int randx, randy;
    int row = 8, col = 8, numbombs = 10, score = 0, lives = 3;
    int sizex = 300, sizey = 346;
    int  totalScore = score;
    int livesleft = lives; 
    Font font;
    Panel P,P2, PB, PS, P3;
    Label Score, Lives;
    Button NewGame;
    GridLayout gl;
    public void init ()
    {
        setLayout (new BorderLayout ());
        gl = new GridLayout (row, col);
        P = new Panel (gl);
        font = new Font ("ComicSans", Font.BOLD, 17);
        setFont (font);
        P2 = new Panel (new BorderLayout ());
        P3 = new Panel ();
        NewGame = new Button ("New Game");
        NewGame.addMouseListener (this);
        PB = new Panel ();
        totalScore = score;
        Score = new Label ("Score: " + Integer.toString (totalScore));
        livesleft = lives;
        Lives = new Label ("Lives: " + Integer.toString (livesleft));
        table = new Button [row] [col];
        bomb = new boolean [row] [col];
        checkwinbool = new boolean [row] [col];
        clicked = new boolean [row] [col];
        for (int x = 0 ; x < row ; x++)
        {
            for (int y = 0 ; y < col ; y++)
            {
                table [x] [y] = new Button ();
                table [x] [y].addMouseListener (this);
                P.add (table [x] [y]);

            }
        }
        add (P2, "North");
        add (P, "Center");
        P2.add (Score, "West");
        P2.add (Lives, "East");
        P2.add (P3, "North");
        P2.add (PB, "Center");
        PB.add (NewGame);
        P2.setBackground(Color.lightGray);
        Score.setBackground(Color.lightGray);
        Score.setForeground(Color.white);
        Lives.setBackground(Color.lightGray);
        Lives.setForeground(Color.white);
        Restart_Game (row, col, numbombs, score, lives, row, col, sizex, sizey);
    }

    public void Restart_Game (int row, int col, int numbombs, int score, int lives, int prow, int pcol, int sizex, int sizey)
    {

        setSize (sizex, sizey);
        invalidate();
        validate();
        gl.setRows (row);
        gl.setColumns (col);
        int count = 0;
        totalScore = score;
        Score.setText ("Score: " + Integer.toString (totalScore));
        livesleft = lives;
        Lives.setText ("Lives: " + Integer.toString (livesleft));
        for (int x = 0 ; x < prow ; x++)
        {
            for (int y = 0 ; y < pcol ; y++)
            {
                P.remove (table [x] [y]);
            }
        }
        for (int x = 0 ; x < row ; x++)
        {
            for (int y = 0 ; y < col ; y++)
            {

                table [x] [y].setEnabled (true);
                table [x] [y].setLabel ("");
                table [x] [y].setBackground (Color.gray);
                table [x] [y].setForeground (Color.white);
                P.add (table [x] [y]);
                bomb [x] [y] = false;                      
                checkwinbool [x] [y] = false;
                clicked [x] [y] = false;
            }
        }
        setSize (sizex, sizey);
        invalidate();
        validate();
        //adds the bombs to random places on the grid
        while (count < numbombs)
        {
            randx = (int) (Math.random () * (row));
            randy = (int) (Math.random () * (col));
            if (bomb [randx] [randy] == false)
            {
                bomb [randx] [randy] = true;
                checkwinbool [randx] [randy] = true;
                count++;
            }
        }
    }

    public void mouseClicked (MouseEvent e)
    {
        int prow = 0, pcol = 0;
        if (e.getSource () == NewGame)
        {
            Restart_Game (row, col, numbombs, score, lives, prow, pcol, sizex, sizey);
        }                   
        boolean gameover = false;
        for (int x = 0 ; x < row ; x++)
        {
            for (int y = 0 ; y < col ; y++)
            {
                if (e.getSource () == table [x] [y])
                {

                    if (bomb [x] [y] == true && livesleft > 0 && clicked [x] [y] == false)  // if you you click on a bomb, results in game over
                    {

                        if( livesleft == 1)
                        {
                            table [x] [y].setLabel ("*");
                            livesleft--;
                            Lives.setText ("Lives: " + Integer.toString (livesleft));
                            gameover ();
                            table [x] [y].setBackground (Color.black);                            
                            gameover = true;
                            break;
                        }
                        else if( livesleft > 1){
                            table [x] [y].setLabel ("*");
                            table [x] [y].setBackground (Color.black);
                            clicked [x] [y] = true;
                            livesleft--;
                            Lives.setText ("Lives: " + Integer.toString (livesleft));
                            break;
                        }
                        break;
                    }
                    else if(clicked [x] [y] == false)
                    {
                        totalScore++;
                        Score.setText ("Score: " + Integer.toString(totalScore));
                        checkwinbool [x] [y] = true;
                        clicked [x] [y] = true;
                        if (gameover == false) //this just calls the method for changing the colours of the buttons if they have been clicked
                            clicked ();
                        break;
                    }

                }
            }
        }

    }

    public void clicked ()     //changes the color of the buttons and if [x][y] is "0" set the label to nothing
    {
        for (int x = 0 ; x < row ; x++)
        {
            for (int y = 0 ; y < col ; y++)
            {
                if (checkwinbool [x] [y] == true && bomb [x] [y] == false)
                {
                    table [x] [y].setBackground (Color.green);                    
                }

            }
        }
    }

    public void gameover ()  // is called if bomb is clicked or on the double click if flag is not on a bomb
    {
        for (int x = 0 ; x < row ; x++)
        {
            for (int y = 0 ; y < col ; y++)
            {
                if (bomb [x] [y] == true)
                {
                    table [x] [y].setLabel ("*"); //exposes all bombs
                    table [x] [y].setBackground (Color.red);
                }
                table [x] [y].setEnabled (false); //disable all buttons
            }
        }
        int x2 = (int) col / 2;
        int y2 = (int) row / 2;
        table [y2] [x2 - 4].setLabel ("Y");
        table [y2] [x2 - 3].setLabel ("O");
        table [y2] [x2 - 2].setLabel ("U");
        table [y2] [x2 - 1].setLabel ("");
        table [y2] [x2].setLabel ("L");
        table [y2] [x2 + 1].setLabel ("O");
        table [y2] [x2 + 2].setLabel ("S");
        table [y2] [x2 + 3].setLabel ("E");
        table [y2] [x2 + 4].setLabel ("!");
        for (int i = -4 ; i < 5 ; i++)
        {
            table [y2] [x2 + i].setBackground (Color.black);
            table [y2] [x2 + i].setForeground (Color.white);
        }
    }

    public void mouseEntered (MouseEvent e)
    {
    }

    public void mouseExited (MouseEvent e)
    {
    }

    public void mousePressed (MouseEvent e)
    {
    }

    public void mouseReleased (MouseEvent e)
    {
    }
}

