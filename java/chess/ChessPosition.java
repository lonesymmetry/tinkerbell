package chess;
/**
 * Represents a position of the chess board
 */
public class ChessPosition{
    private static class Dimension{
        protected int value;
        public static final int DIMENSION=8;
        
        public int get(){
            return value;
        }
		
		public String toString(){
			return "NYI";
		}
		
		public boolean equals(Dimension b){
			return this.get()==b.get();
		}
        
        public void set(int value){
            MySystem.myAssert(value < DIMENSION && value >= 0,MySystem.getFileName(),MySystem.getLineNumber());
            this.value = value;
        }
        
        public Dimension(){
            value = 0;
        }
        public Dimension(int value){
            set(value);
        }
    }
    public static class Column extends Dimension{//called files
        public static final int A=0;
        public static final int B=1;
        public static final int C=2;
        public static final int D=3;
        public static final int E=4;
        public static final int F=5;
        public static final int G=6;
        public static final int H=7;
		@Override 
		public String toString(){
			final String[] COLUMNNAMES = {"A","B","C","D","E","F","G","H","DIMENSION"};
			return COLUMNNAMES[get()];
		}
        public Column(){
            super();
        }
        public Column(int value){
            super(value);
        }
    };
    private Column column;
    public static class Row extends Dimension{//called ranks
        public static final int _1=0;
        public static final int _2=1;
        public static final int _3=2;
        public static final int _4=3;
        public static final int _5=4;
        public static final int _6=5;
        public static final int _7=6;
        public static final int _8=7;
		public String toString(){
			final String[] ROWNAMES = {"1","2","3","4","5","6","7","8","DIMENSION"};
			return ROWNAMES[get()];
		}
        public Row(){
            super();
        }
        public Row(int value){
            super(value);
        }
    }
	public static class Tester{
		private int column;
		private int row;
		public int getRow(){
			return row;
		}
		public int getColumn(){
			return column;
		}
		public Tester(){
			column = 0;
			row = 0;
		}
		public Tester(int row,int column){
			this.row = row;
			this.column = column;
		}
	}
    private Row row;
    
    public boolean equals(ChessPosition b){
        if(!b.getRow().equals(this.getRow())) return false;
        if(!b.getColumn().equals(this.getColumn())) return false;
        return true;
    }
	
	public static boolean inBounds(int row, int column){
        if(column >= ChessPosition.Column.DIMENSION || column < 0) return false;
        if(row >= ChessPosition.Row.DIMENSION || row < 0) return false;
        return true;
    }
	
	public static boolean inBounds(Tester tester){
		return inBounds(tester.getRow(),tester.getColumn());
    }
	
	public String toString(){
		return column.toString() + row.toString();
	}
    
    public Column getColumn(){
        return column;
    }
    
    public Row getRow(){
        return row;
    }
    
    public ChessPosition(){
		this(Row._1,Column.A);
    }
    public ChessPosition(Row row,Column column){
		this.column = new Column();
		this.row = new Row();
        this.column = column;
        this.row = row;
    }
     public ChessPosition(int row,int column){
		MySystem.myAssert(column < Column.DIMENSION && column >= 0,MySystem.getFileName(),MySystem.getLineNumber());
		MySystem.myAssert(row < Row.DIMENSION && row >=0,MySystem.getFileName(),MySystem.getLineNumber());
		this.column = new Column();
		this.row = new Row();
		this.column.set(column);
        this.row.set(row);
    }
	public ChessPosition(ChessPosition.Tester tester){
		this(tester.getRow(),tester.getColumn());
	}
}