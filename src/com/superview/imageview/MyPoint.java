package com.superview.imageview;

public class MyPoint 
{
	   float x;  //坐标点
	   float y;
	   float oldX;  //旧坐标点
	   float oldY;	
	   boolean hasOld=false; //是否存在旧坐标点
	   
	   public MyPoint(float x,float y)
	   {
		   this.x=x;     //初始化坐标点
		   this.y=y;
	   }
	   
	   public static float calDistance(MyPoint a,MyPoint b)   //返回两点间距离
	   {
		   float result=0;
		   
		   result=(float)Math.sqrt(    //计算两点间距离
		     (a.x-b.x)*(a.x-b.x)+
		     (a.y-b.y)*(a.y-b.y)
		   );	   
		   return result;            //两点间距离
	   }
	   
	   public void setLocation(float x,float y)  //记录位置坐标
	   {
		   oldX=this.x;  //记录历史位置坐标
		   oldY=this.y;
		   hasOld=true;  //标记存在历史坐标记录
		   this.x=x;     //记录新位置坐标
		   this.y=y;
	   }
}
