package services;

import entities.Board;
import utils.Constants;

import java.util.Scanner;

public class Controller {

    private final Board board;
    private Scanner scanner;
    private String[] input;
    public Controller()
    {
        this.scanner=new Scanner(System.in);
        this.board=this.start();
        this.play();
    }

    private void play() {
        while (!this.board.isGameOver()&& this.board.getCellsLeft()>0)
        {
            System.out.println(Constants.INPUT);
            int x;
            int y;
            input=scanner.nextLine().split(" ");
            try {
            x=Integer.parseInt(input[0]);
            y=Integer.parseInt(input[1]);
            }
            catch (Exception ex)
            {
                System.out.println(Constants.INVALID_INPUT);
                continue;
            }
            this.board.move(x,y);
        }
        if (this.board.isGameOver())
        {
            System.out.println(Constants.LOST_GAME);
        }
        else
        {
            System.out.println(Constants.WON_GAME);
        }
    }

    private Board start() {
        int gridSize=0;
        int minesNumber=0;
        while(gridSize<1 || gridSize>3) {
            System.out.println(Constants.CHOOSE_DIFFICULTY);
            System.out.println(Constants.BEGINNER_DIFFICULTY);
            System.out.println(Constants.INTERMEDIATE_DIFFICULTY);
            System.out.println(Constants.ADVANCED_DIFFICULTY);

            try {
                gridSize = Integer.parseInt(scanner.nextLine());
            } catch (IllegalArgumentException ex) {
                System.out.println(Constants.INVALID_INPUT);
            }
        }
        switch(gridSize)
        {
            case 1:
                gridSize=9;
                minesNumber=10;
                break;
            case 2:
                gridSize=16;
                minesNumber=40;
                break;
            case 3:
                gridSize=24;
                minesNumber=99;
                break;
        }
        return new Board(gridSize,minesNumber);

    }


}
