package navigation;

public class Coordinates {
	double posX, posY;

	public Coordinates(){
		posX=0;
		posY=0;
	}
	
	public void updateCoordiantes(int distance, double posDegree){
		if(posDegree <0) posDegree=posDegree%360+360;
		else posDegree=posDegree%360;
		if(posDegree%180 <= 90){
			if(posDegree >= 180){
				posX-=Math.cos(Math.toRadians(90-(posDegree%90)))*distance;
				posY-=Math.sin(Math.toRadians(90-(posDegree%90)))*distance;
			}
			else{
				posX+=Math.cos(Math.toRadians(90-(posDegree%90)))*distance;
				posY+=Math.sin(Math.toRadians(90-(posDegree%90)))*distance;
			}
		}
		else{
			if(posDegree >= 270){
				posX-=Math.cos(Math.toRadians((posDegree%90)))*distance;
				posY+=Math.sin(Math.toRadians((posDegree%90)))*distance;
			}
			else{
				posX+=Math.cos(Math.toRadians((posDegree%90)))*distance;
				posY-=Math.sin(Math.toRadians((posDegree%90)))*distance;
			}
		}
	}
	
	public int getPosX(){
		return (int) posX;
	}
	
	public int getPosY(){
		return (int) posY;
	}
}
