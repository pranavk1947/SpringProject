package ChessGame;

import ChessGame.pieces.Piece;

public class Move {
	private Piece piece;
	private int destRow;
	private int destCol;
	public Move(Piece piece, int destRow, int destCol) {
		this.piece = piece;
		this.destRow = destRow;
		this.destCol = destCol;
	}
	public Piece getPiece() {
		return piece;
	}
	
	public int getDestRow() {
		return destRow;
	}

	public int getDestCol() {
		return destCol;
	}
	
	
	
	
	
	
}
