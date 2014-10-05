package com.superview.imageview;

public class RotateData {

	private int rotateCenterX;     //旋转坐标
	private int rotateCenterY;
	private float angle;            //旋转角度
	
	public RotateData(float angle,int rotateCenterX,int rotateCenterY){
		this.angle = angle;
		this.rotateCenterX = rotateCenterX;
		this.rotateCenterY = rotateCenterY;
	}

	public int getRotateCenterX() {
		return rotateCenterX;
	}

	public int getRotateCenterY() {
		return rotateCenterY;
	}

	public float getAngle() {
		return angle;
	}
}
