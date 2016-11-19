package chess;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Vector;

/**
 * A king piece
 */
public class King extends ChessPiece
{
	private boolean check;
	private boolean checkmate;

	private static final Type type = Type.KING;

	/**
	 * Fetches the type of this piece
	 * @return the type of this piece
	 */
	@Override
	public Type getType(){
		return King.type;
	}

	public void updateCheck(final ChessPieces CHESS_PIECES){
		for(ChessPiece enemyPiece: CHESS_PIECES.toArray()){
			if(enemyPiece.getColor() == Color.not(this.color)){//if it's an enemy piece
				for(ChessPosition possiblePosition : enemyPiece.getNewPositions(CHESS_PIECES)){
					if(possiblePosition.equals(this.position)){//if it can take this piece (the king)
						check = true;
						return;
					}
				}
			}
		}
		check = false;
	}

	private boolean checkMyMoves(final ChessPieces CHESS_PIECES){
		MySystem.myAssert(this.getCheck(),MySystem.getFileName(),MySystem.getLineNumber());
		final King original = new King(this);
		for(ChessPosition possibleMove: this.getNewPositions(CHESS_PIECES)){
			ChessPieces postMovePieces = ChessPieces.makePieces(CHESS_PIECES);
			King testMe = new King(this);
			int index = postMovePieces.getIndexOf(testMe);
			testMe.move(possibleMove,postMovePieces);
			postMovePieces.set(index,testMe);
			testMe.updateCheck(postMovePieces);
			boolean leftCheck = !testMe.getCheck();//did the king move out of check
			{//reset king to start
				this.position = new ChessPosition(original.position);
				this.alive = original.alive;
				this.color = original.color;
				this.check = original.check;
				this.checkmate = original.checkmate;
			}
			if(leftCheck){
				return false;
			}
		}
		return true;
	}

	private void updateCheckmate(final ChessPieces CHESS_PIECES){
		this.updateCheck(CHESS_PIECES);//TODO: should this be here?
		if(!this.check){
			this.checkmate = false;
			return;//if it's not in check, then it doesn't need to check if it's in checkmate
		}
		ChessPiece[] allPieces = ChessPieces.makePieces(CHESS_PIECES).toArray();
		for(ChessPiece defendingPiece : allPieces){
			if(defendingPiece.getColor() == this.getColor() && !defendingPiece.equalsByType(this)){
				Vector<ChessPosition> possibleMoves = defendingPiece.getNewPositions(ChessPieces.makePieces(CHESS_PIECES));
				for(ChessPosition possibleMove : possibleMoves){
					ChessPieces postMovePieces = ChessPieces.makePieces(CHESS_PIECES);
					{
						ChessPiece testPiece = ChessPiece.makePiece(defendingPiece);
						MySystem.myAssert(postMovePieces.checkExists(testPiece),MySystem.getFileName(),MySystem.getLineNumber());
						int position = postMovePieces.getIndexOf(testPiece);
						if(postMovePieces.getPieceAt(position).checkMove(possibleMove,postMovePieces)){
							if(postMovePieces.isOccupied(possibleMove, ChessPiece.Color.not(postMovePieces.getPieceAt(position).getColor()))) postMovePieces.capture(possibleMove);
							testPiece.move(possibleMove,postMovePieces);
							postMovePieces.set(position,testPiece);
						} else {
							MySystem.error("Error: trying to move piece to invalid location.",MySystem.getFileName(),MySystem.getLineNumber());
						}
					}
					{
						this.updateCheck(postMovePieces);

						if(!this.check){//if a move moves it out of check, then it isn't in checkmate
							this.checkmate = false;
							return;
						}
					}
				}
			}
		}
		this.checkmate = this.checkMyMoves(CHESS_PIECES);
	}

	public void update(final ChessPieces ORIGINAL_PIECES){
		ChessPieces chessPieces = new ChessPieces(ORIGINAL_PIECES);
		int index = chessPieces.getIndexOf(this);
		updateCheck(chessPieces);
		chessPieces.set(index, this);
		updateCheckmate(chessPieces);
	}

	@Override
	public String toString(){
		return "ChessPiece( type:" + this.getType() + " color:" + this.color + " position:" + this.position + " check:" + this.check + " checkmate:" + this.checkmate + " )";
	}

	/**
	 * The symbol to print given the type
	 * @return the letter representing this chess piece
	 */
   	@Override
    public String print(){
        return "K";
    }
	@Override
    public Vector<ChessPosition> getNewPositions(ChessPieces chessPieces){
		Vector<ChessPosition> possibleMoves = new Vector<>(0);
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				ChessPosition.Tester testPosition = new ChessPosition.Tester(this.position.getRow().get() + x, this.position.getColumn().get() + y);
				if(ChessPosition.inBounds(testPosition) && !chessPieces.isOccupied(new ChessPosition(testPosition), this.color)){
					possibleMoves.addElement(new ChessPosition(testPosition));
				}
			}
		}
        return possibleMoves;
    }

	/**
	 * Checks for equality by value with a given piece
	 * @param b the piece to be compared to
	 * @return true if the pieces are equal by value
	 */
	@Override
	public boolean equals(final ChessPiece b){
		if(!(b instanceof King)) return false;
		King testPiece = new King(b);
		if(this.getType() != testPiece.getType()) return false;
		if(this.getColor()!=testPiece.getColor()) return false;
		if(!this.getPosition().equals(testPiece.getPosition())) return false;
		if(this.getAlive() != testPiece.getAlive()) return false;
		if(this.getCheck() != testPiece.getCheck()) return  false;
		if(this.getCheckmate() != testPiece.getCheckmate()) return  false;
		return true;
	}

    @Override
    public void move(ChessPosition newPosition, ChessPieces chessPieces){
        for(ChessPosition a: getNewPositions(chessPieces)){
            if(newPosition.equals(a)){
                this.position = newPosition;
                return;
            }
        }
		MySystem.error("Move failed: Not a valid move: trying to move from " + this.getPosition().toString() + " to " + newPosition.toString(),MySystem.getFileName(),MySystem.getLineNumber());
		MySystem.myAssert(false,MySystem.getFileName(),MySystem.getLineNumber());
	}
	public boolean getCheck(){
		return check;
	}
	public boolean getCheckmate(){
		return checkmate;
	}
    public King(){
        super();
		check = false;
		checkmate = false;
    }
	public King(King toCopy) {
		this.position = new ChessPosition(toCopy.position);
		this.alive = toCopy.alive;
		this.color = toCopy.color;
		this.check = toCopy.check;
		this.checkmate = toCopy.checkmate;
	}

    public King(ChessPiece chessPiece){
		this(chessPiece.getPosition(),chessPiece.getColor());
		if(chessPiece instanceof King){
			King toCopy = (King)chessPiece;
			this.position = new ChessPosition(toCopy.position);
			this.alive = toCopy.alive;
			this.color = toCopy.color;
			this.check = toCopy.check;
			this.checkmate = toCopy.checkmate;
		}
	}
    public King(ChessPosition position, Color color){
        super(position,color);
		check = false;
		checkmate = false;
    }
}