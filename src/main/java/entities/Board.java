package entities;

import java.util.Random;

public class Board implements entities.interfaces.Board {
    private final int GRID_SIZE;
    private final int NUMBER_OF_MINES;
    private String[][] playerField;
    private String[][] gameField;
    private final String unknown = "?";
    private final String mine = "*";
    private final String empty = "   ";
    private int cellsLeft;
    boolean gameOver=false;
    public Board(int gridSize, int numberOfMines) {
        this.GRID_SIZE=gridSize;
        this.gameField = new String[gridSize][gridSize];
        this.playerField = new String[gridSize][gridSize];
        this.NUMBER_OF_MINES = numberOfMines;
        this.cellsLeft= calcInitialFreeCells();
        System.out.println(cellsLeft);
        this.generateField();
        this.printField();
    }
  public void printField()
    {
        for(int x = 0; x <playerField.length; x++){
            for(int y = 0; y < playerField[x].length ; y++){
                //Formats row.
                if(y > 0 && y < playerField[x].length)
                    System.out.print("|");
                else
                    System.out.println();

                System.out.print(playerField[x][y]);  //Prints out content of each tile.
            }
        }
    }
//The move method first makes a bound check, then checks if it's the first turn and if it's not - proceed to normal movement
    @Override
    public void move(int row, int col) {
        if (isValid(row, col)) {
           // System.out.println("it's valid");
            boolean isAMine=isMine(row,col);
            if (this.cellsLeft == calcInitialFreeCells()) {
                if (isMine(row, col)) {
                  //  System.out.println("it's a mine");
                    gameField[row][col] = unknown;
                    playerField[row][col]=Integer.toString(adjacentMines(row, col));
                    cellsLeft--;
                    generateANewMine();
                    printField();
                    return;
                }else {
                    int count = adjacentMines(row, col);
                    if (count == 0) {
                        this.cellsLeft -= recursiveMovement(row, col);
                    }else
                    {
                        playerField[row][col]=Integer.toString(count);
                        cellsLeft--;
                    }
                }
            } else {
                if (isMine(row, col)) {
                    this.gameOver = true;
                    return;
                } else {
                   int count=adjacentMines(row, col);
                   if (count==0)
                   {
                    this.cellsLeft-=recursiveMovement(row,col);
                   }
                   else
                   {
                       playerField[row][col]=Integer.toString(count);
                       this.cellsLeft--;
                   }
                }
            }
        }
        printField();
    }
// This method handles recursive "movement" - revealing of all adjacent cells. Returns int for the calculation of remaining "moves".
    private int recursiveMovement(int row, int col) {
        int cellCount=1;
        int adjacentMines=adjacentMines(row, col);
        playerField[row][col]=Integer.toString(adjacentMines);
        if (adjacentMines>0)
        {
            return cellCount;
        }
        else{
            if (isValid(row-1,col)&& playerField[row-1][col].equals(unknown))
            {
               cellCount+= recursiveMovement(row-1,col);
            }
            if (isValid(row,col+1)&& playerField[row][col+1].equals(unknown))
            {
                cellCount+=recursiveMovement(row,col+1);
            }
            if (isValid(row+1,col)&& playerField[row+1][col].equals(unknown))
            {
                cellCount+=recursiveMovement(row+1,col);
            }
            if (isValid(row,col-1)&& playerField[row][col-1].equals(unknown))
            {
                cellCount+=recursiveMovement(row,col-1);
            }
        }

        return cellCount;
    }

    //This method is used to generate a new mine in case of the player stepping on it during his first turn
    private void generateANewMine() {
        Random random=new Random();
        while (true) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            if (!isMine(x,y))
            {
                gameField[x][y]=mine;
                break;
            }
        }
    }
//This method initializes the playing field
    @Override
    public void generateField() {
        //generates the field
        for(int x = 0; x < gameField.length; x++){
            for(int y = 0; y < gameField[x].length; y++){

                    gameField[x][y] = unknown;
                    playerField[x][y] = unknown;
            }
        }
        generateMines();
    }
// This method Generates all the mines BEFORE the first move. Handling of the first move is done in the move() method.
    public void generateMines() {
        Random random=new Random();
        int mines=0;
        while (mines<NUMBER_OF_MINES)
        {
            int x=random.nextInt(GRID_SIZE);
            int y=random.nextInt(GRID_SIZE);
            if (!isMine(x,y))
            {
                gameField[x][y]=mine;
                mines++;
            }

        }
    }
//TODO: Adjacent is not properly interacting with the Array, fix upon noticing this, thank you me!
    @Override
    public int adjacentMines(int row, int col) {
        int count=0;
        //Check North
        if (isValid(row-1,col))
        {
            if (isMine(row-1,col))
            {
                count++;
            }
        }
        //Check North-East
        if (isValid(row-1,col+1))
        {
            if (isMine(row-1,col+1))
            {
                count++;
            }
        }
        //Check East
        if (isValid(row,col+1))
        {
            if (isMine(row, col+1))
            {
                count++;
            }
        }
        //Check South-East
        if (isValid(row+1, col+1))
        {
            if (isMine(row+1, col+1))
            {
                count++;
            }
        }
        //Check South
        if (isValid(row+1, col))
        {
            if (isMine(row+1,col))
            {
                count++;
            }
        }
        //Check South-West
        if (isValid(row+1,col-1))
        {
            if (isMine(row+1,col-1))
            {
                count++;
            }
        }
        //Check West
        if (isValid(row,col-1))
        {
            if(isMine(row,col-1))
            {
                count++;
            }
        }
        //Check North-West
        if (isValid(row-1, col-1))
        {
            if (isMine(row-1, col-1))
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isMine(int row, int col) {
        if (isValid(row, col))
        {
            return this.gameField[row][col].equals(mine);
        }
        return false;
    }

    @Override
    public boolean isValid(int row, int col) {
        return row>=0&& row<this.GRID_SIZE && col>=0&& col<this.GRID_SIZE;
    }



    @Override
    public void iter() {

    }
    private int calcInitialFreeCells()
    {
        return (GRID_SIZE*GRID_SIZE)-NUMBER_OF_MINES;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public int getCellsLeft() {
        return cellsLeft;
    }
}
