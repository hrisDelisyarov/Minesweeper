package entities.interfaces;

public interface Board {

    void generateField();
    void iter();
    void generateMines();
    void printField();
    void move(int row,int col);
    int adjacentMines(int row,int col);
    boolean isMine(int row,int col);
    boolean isValid(int row,int col);

}
