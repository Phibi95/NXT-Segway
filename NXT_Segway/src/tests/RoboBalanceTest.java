package tests;
import static org.junit.Assert.*;
import main.BalanceHandler;

import org.junit.Test;


public class RoboBalanceTest {
	double K_ANGULAR_VELO = 0.4; // Angular Velocity
	double K_ANGLE = 5.7; // Angle
	double K_MOTORPOS = 0.075; // Motorposition
	double K_MOTORSPEED = 0.175; // Motorspeed
	
	@Test
	public void testMotorSpeed() {
		System.out.println("Start testing MotorSpeed");
		BalanceHandler testClass = new BalanceHandler(K_ANGULAR_VELO,K_ANGLE,K_MOTORPOS,K_MOTORSPEED);
		double[] testTimeDiff={0.003,0.002,0.001,0.005,0.1};
		int[] testTacho={10,20,30,40,50,0};
		int[] testOldTacho={10,20,30,40,50,0};
		for(int i1=0;i1<testTimeDiff.length;i1++){
			for(int i2=0;i2<testTacho.length;i2++){
				for(int i3=0;i3<testOldTacho.length;i3++){
						double result = testClass.motorSpeed(testTacho[i2],testOldTacho[i3],testTimeDiff[i1]);
						double expected_result=(testTacho[i2] - testOldTacho[i3]) / testTimeDiff[i1];
						assertEquals("Integrate funktioniert nicht",result, expected_result,0);
					
				}
			}
		}
		System.out.println("Finished testing MotorSpeed");
	}

	@Test
	public void testCalcMotorPower() {
		System.out.println("Start testing CalcMotorPower");
		BalanceHandler testClass = new BalanceHandler(K_ANGULAR_VELO,K_ANGLE,K_MOTORPOS,K_MOTORSPEED);
		double[] testangle={-90,-50,50,-3,3,-2,2,-10,10,0,90};
		int[] testTacho={10,20,30,40,50,0};
		double[] testGyroSpeed={0,100,300,400,500,600,700,800,900,1000};
		double[] testMotorSpeed={50,10,20,30,0};
		for(int i1=0;i1<testangle.length;i1++){
			for(int i2=0;i2<testTacho.length;i2++){
				for(int i3=0;i3<testGyroSpeed.length;i3++){
					for(int i4=0;i4<testMotorSpeed.length;i4++){
						int result = testClass.calcMotorPower(testGyroSpeed[i3], testangle[i1], testTacho[i2], testMotorSpeed[i4]);
						int expected_result=(int) (testClass.getK_ANGULAR_VELO() * testGyroSpeed[i3] + testClass.getK_ANGLE() * testangle[i1] + testClass.getK_MOTORPOS() * testTacho[i2] + testClass.getK_MOTORSPEED() * testMotorSpeed[i4]);
						assertEquals("Integrate funktioniert nicht",result, expected_result,0);
					}
					
				}
			}
		}
		System.out.println("Finished testing CalcMotorPower");
	}

	@Test
	public void testIntegrate() {
		System.out.println("Start testing Integrate");
		BalanceHandler testClass = new BalanceHandler(K_ANGULAR_VELO,K_ANGLE,K_MOTORPOS,K_MOTORSPEED);
		double[] testangle={-90,-50,50,-3,3,-2,2,-10,10,0,90};
		double[] testTimeDiff={0.003,0.002,0.001,0.005,0.1};
		double[] testGyroSpeed={0,100,300,400,500,600,700,800,900,1000};
		for(int i1=0;i1<testangle.length;i1++){
			for(int i2=0;i2<testTimeDiff.length;i2++){
				for(int i3=0;i3<testGyroSpeed.length;i3++){
					double result = testClass.integrate(testangle[i1], testTimeDiff[i2], testGyroSpeed[i3]);
					double expected_result=testangle[i1] + testTimeDiff[i2] *testGyroSpeed[i3];
					assertEquals("Integrate funktioniert nicht",result, expected_result,0);
				}
			}
		}
		System.out.println("Finished testing Integrate");
	}

	@Test
	public void testGyroSpeed() {
		System.out.println("Start testing GyroSpeed");
		BalanceHandler testClass = new BalanceHandler(K_ANGULAR_VELO,K_ANGLE,K_MOTORPOS,K_MOTORSPEED);
		double[] testangle={0,540,580,700,200,300,400};
		double[] testOffset={580,400,0,200,600};
		for(int i1=0;i1<testangle.length;i1++){
			for(int i2=0;i2<testOffset.length;i2++){
				double result = testClass.gyroSpeed(testangle[i1], testOffset[i2]);
				double expected_result=testangle[i1] - testOffset[i2];;
				assertEquals("Integrate funktioniert nicht",result, expected_result,4);
			}
		}
		System.out.println("Finished testing GyroSpeed");
	}

}
