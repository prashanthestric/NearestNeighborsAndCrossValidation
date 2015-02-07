package knnandkfoldcrossval;

/**
 * Data model for an instance (x1,x2,y)
 * 
 * @author Prashanth Govindaraj
 */
public class TheDataModel {
	
	int x1,x2;
	String y;
	
	public TheDataModel(int xOne,int xTwo){
		
		x1=xOne;
		x2=xTwo;
		y=".";
		
	}
	public TheDataModel(int xOne,int xTwo,String why)
	{
		
		x1=xOne;
		x2=xTwo;
		y=why;
		
	}
	
	public int getX1() {
		return x1;
	}
	public int getX2() {
		return x2;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}

}
