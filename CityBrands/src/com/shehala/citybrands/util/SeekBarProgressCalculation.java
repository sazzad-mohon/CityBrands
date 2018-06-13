package com.shehala.citybrands.util;

public class SeekBarProgressCalculation {

	public static void getProgressValue(int progressValue,int topMarginValue,double spinnerDistanceInKm)
	
	{
		if (spinnerDistanceInKm>=100.0)

		{

			progressValue = 99;
			topMarginValue = -3;

		}

		else if (spinnerDistanceInKm>=200.0)

		{

			progressValue = 5;
			topMarginValue = 433;

		}

		else if (spinnerDistanceInKm>=300.0)

		{
			progressValue = 97;
			topMarginValue = 0;

		}

		else if (spinnerDistanceInKm>=400.0)

		{
			progressValue = 95;
			topMarginValue = 0;

		}

		else if (spinnerDistanceInKm>=500.0)

		{
			progressValue = 93;
			topMarginValue = 0;

		}

		else if (spinnerDistanceInKm>=600.0)

		{
			progressValue = 91;
			topMarginValue = 5;

		}

		else if (spinnerDistanceInKm>=700.0)

		{
			progressValue = 89;
			topMarginValue = 23;

		}

		else if (spinnerDistanceInKm>=800.0)

		{
			progressValue = 87;
			topMarginValue = 33;

		}

		else if (spinnerDistanceInKm>=900.0)

		{
			progressValue = 85;
			topMarginValue = 40;

		}

		else if (spinnerDistanceInKm>=1000.0)

		{
			progressValue = 83;
			topMarginValue = 50;

		}

		else if (spinnerDistanceInKm>=1500.0)

		{
			progressValue = 81;
			topMarginValue = 60;

		}

		else if (spinnerDistanceInKm>=2000.0)

		{
			progressValue = 77;
			topMarginValue = 80;

		}

		else if (spinnerDistanceInKm>=3000.0)

		{
			progressValue = 75;
			topMarginValue = 90;

		}

		else if (spinnerDistanceInKm>=5000.0)

		{
			progressValue = 73;
			topMarginValue = 100;

		}

		else if (spinnerDistanceInKm>=6000.0)

		{
			progressValue = 67;
			topMarginValue = 123;

		}

		else if (spinnerDistanceInKm>=7000.0)

		{
			progressValue = 65;
			topMarginValue = 133;

		}

		else if (spinnerDistanceInKm>=8000.0)

		{
			progressValue = 63;
			topMarginValue = 140;

		}

		else if (spinnerDistanceInKm>=9000.0)

		{
			progressValue = 61;
			topMarginValue = 150;

		}

		else if (spinnerDistanceInKm>=10000.0)

		{
			progressValue = 59;
			topMarginValue = 160;

		}

		else if (spinnerDistanceInKm>=20000.0)

		{
			progressValue = 53;
			topMarginValue = 190;

		}

		else if (spinnerDistanceInKm>=30000.0)

		{
			progressValue = 47;
			topMarginValue = 220;

		}

		else if (spinnerDistanceInKm>=40000.0)

		{
			progressValue = 41;
			topMarginValue = 250;

		}

		else if (spinnerDistanceInKm>=50000.0)

		{
			progressValue = 35;
			topMarginValue = 252;

		}

		else if (spinnerDistanceInKm>=60000.0)

		{
			progressValue = 29;
			topMarginValue = 297;

		}

		else if (spinnerDistanceInKm>=70000.0)

		{
			progressValue = 22;
			topMarginValue = 330;

		}

		else if (spinnerDistanceInKm>=80000.0)

		{
			progressValue = 17;
			topMarginValue = 355;

		}

		else if (spinnerDistanceInKm>=90000.0)

		{
			progressValue = 9;
			topMarginValue = 397;

		}
	}
	
}
