package com.superview.imageview;

public class PictureStat {

	private float mScale;    //显示图片倍数
	private float angle;      //图片旋转角度
	private int rotateCenterX;     //图片旋转中心坐标
	private int rotateCenterY;
	private float baseY;          //图片位移坐标
	private float baseX;

	public float getmScale() {
		return mScale;
	}
	public void setmScale(float mScale) {
		this.mScale = mScale;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getRotateCenterX() {
		return rotateCenterX;
	}
	public void setRotateCenterX(int rotateCenterX) {
		this.rotateCenterX = rotateCenterX;
	}
	public int getRotateCenterY() {
		return rotateCenterY;
	}
	public void setRotateCenterY(int rotateCenterY) {
		this.rotateCenterY = rotateCenterY;
	}
	public float getBaseY() {
		return baseY;
	}
	public void setBaseY(float baseY) {
		this.baseY = baseY;
	}
	public float getBaseX() {
		return baseX;
	}
	public void setBaseX(float baseX) {
		this.baseX = baseX;
	}
}
