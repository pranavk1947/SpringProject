package SnakeLadder;

public class Cell {
    private int position;
    private CellType type;
    
    public Cell(int position, CellType type) {
        this.position = position;
        this.type = type;
    }    
    
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
        
        
    }
    
}
