package com.superview.imageview;

import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("UseSparseArrays")
public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private Paint paint;  
	private Bitmap bitmapTmp; //用以显示的图片
	private float PicHeight;  //图片高度
	private float PicWidth;   //图片宽度
	private float currScale = 0.5f;//图片初始倍率
	private float angle = 0;//图片初始角度
	private HashMap<Integer,MyPoint> hm=new HashMap<Integer,MyPoint>(); //触摸指针集
	private float distance = 0; // 距离
	
	private int rotateCenterX;
	private int rotateCenterY;
	
	private float baseX;  //基础坐标X
	private float baseY;   //基础坐标Y
	
	private float minScale = 0.5f;
	private float maxScale = 2.0f;
	
	private int background = Color.BLACK;
	private int rotaParameter = 1;
	private int zoomParameter = 1000;
	
	private PictureStat stat;

	public ImageSurfaceView(Context context) 
	{
		super(context);
		this.getHolder().addCallback(this);    
		paint=new Paint();    //初始化画笔
		paint.setAntiAlias(true);  // 居中显示
		bitmapTmp = Bitmap.createBitmap(1000, 2000, Bitmap.Config.ARGB_8888);   //初始化图片
		init();
	}
	
	public ImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context,attrs,defStyleAttr);
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		bitmapTmp = Bitmap.createBitmap(1000, 2000, Bitmap.Config.ARGB_8888);
		init();
	}
	
	public ImageSurfaceView(Context context, AttributeSet attrs){
		super(context,attrs);
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		bitmapTmp = Bitmap.createBitmap(1000, 2000, Bitmap.Config.ARGB_8888);
		init();
	}
	
	/*
	 * 设置图片背景颜色
	 */
	public void setImageBackgroundColor(int color){
		background = color;
	}
	
	/*
	 * 设置旋转灵敏度(默认值为1)
	 */
	public void setRotaParameter(int rotaParameter){
		this.rotaParameter = rotaParameter;
	}
	
	/*
	 * 设置伸缩灵敏度参数(默认值为1000)
	 */
	public void setZoomParameter(int zoomParameter){
		this.zoomParameter = zoomParameter;
	}
	
	public void setImageRourcese(int resource){
		bitmapTmp = BitmapFactory.decodeResource(getResources(), resource);
		init();
	}
	
	public void setImageBitmap(Bitmap bitmap){
		bitmapTmp = bitmap;
		init();
	}
	
	/*
	 * 设置最小伸缩倍数
	 */
	public void setMinScale(float minScale){
		this.minScale = minScale;
	}
	
	/*
	 * 设置最大伸缩倍数
	 */
	public void setMaxScale(float maxScale){
		this.maxScale = maxScale;
	}
	
	public void setImageUri(Uri uri){
		bitmapTmp = BitmapFactory.decodeStream(Util.readWallpaper(getContext(), uri));
		init();
	}
	
	private void init(){
		currScale = 0.5f;
		angle = 0;
		distance = 0;
		baseX = 0;
		baseY = 0;
		PicHeight = bitmapTmp.getHeight(); 	//初始化图片高度
		PicWidth = bitmapTmp.getWidth();  	//初始化图片宽度
	}
	
	/*
	 * 调整图片大小
	 */
	private void adjustPhoto(){
		currScale = Math.min(getHeight()/PicHeight,getWidth()/PicWidth);
		maxScale = currScale * 2;
	}

	@SuppressLint("DrawAllocation")
	public void draw(Canvas canvas)
	{
		paint.setColor(background);			 //初始化画笔颜色
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);  //初始化画框
				
		float picWidthTemp=PicWidth*currScale;   //显示图片的宽度
		float picHeightTemp=PicHeight*currScale;  //显示图片的高度
		
		float left = (getWidth() - picWidthTemp)/2; //图片原始坐标X
		float top = (getHeight() - picHeightTemp)/2;//图片原始坐标Y
		
		Matrix m1=new Matrix();     //更新原始坐标
		m1.setTranslate(left,top);   //
		
		Matrix m2=new Matrix();     //更新图片倍率
		m2.setScale(currScale, currScale);
		
		Matrix m3=new Matrix();     //旋转图片
		m3.setRotate(angle, rotateCenterX, rotateCenterY);
		
		Matrix m4 = new Matrix();      //更新图片基础坐标，平移图片
		m4.setTranslate(baseX, baseY);

		Matrix mz=new Matrix();
		mz.setConcat(m1, m2);
		
		Matrix mzz=new Matrix();
		mzz.setConcat(m3, mz);
		
		Matrix mzzz = new Matrix();
		mzzz.setConcat(m4, mzz);
		
		canvas.drawBitmap(bitmapTmp, mzzz, paint);
	}
	
	/*
	 * 复位
	 */
	public void restore(){
		if(stat != null && bitmapTmp != null){
			angle = stat.getAngle();
			rotateCenterX = stat.getRotateCenterX();
			rotateCenterY = stat.getRotateCenterY();
			baseX = stat.getBaseX();
			baseY = stat.getBaseY();
			currScale = stat.getmScale();
		}
		this.repaint();
	}
	
	/*
	 * 复位到指定的状态
	 */
	public void restore(PictureStat stat){
		if(stat != null && bitmapTmp != null){
			angle = stat.getAngle();
			rotateCenterX = stat.getRotateCenterX();
			rotateCenterY = stat.getRotateCenterY();
			baseX = stat.getBaseX();
			baseY = stat.getBaseY();
			currScale = stat.getmScale();
		}
		this.repaint();
	}
	
	/*
	 * 保存图片状态
	 */
	public void save(){
		stat = new PictureStat();
		stat.setAngle(angle);
		stat.setRotateCenterX(rotateCenterX);
		stat.setRotateCenterY(rotateCenterY);
		stat.setBaseX(baseX);
		stat.setBaseY(baseY);
		stat.setmScale(currScale);
	}
	
	/*
	 * 获取图片状态
	 */
	public PictureStat getPictureStat(){
		return this.stat;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//获取时间类型
		int action=e.getAction()&MotionEvent.ACTION_MASK;
		//获取时间id
		int id=(e.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)>>>MotionEvent.ACTION_POINTER_ID_SHIFT;	
		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN: //按下事件
			case MotionEvent.ACTION_POINTER_DOWN: //多点按下事件
				//收集按下事件集
                hm.put(id, new MyPoint(e.getX(id),e.getY(id)));   
                //多点触控时，两指间距离
                if(hm.size()==2)
				{					
					MyPoint bpTempA=hm.get(0);
					MyPoint bpTempB=hm.get(1);
					distance=MyPoint.calDistance(bpTempA, bpTempB);											
				}		
			break;
			case MotionEvent.ACTION_MOVE: //移动事件
				//变更手指位置
				Set<Integer> ks=hm.keySet();
				for(int i:ks)
				{	
					try {
						hm.get(i).setLocation(e.getX(i), e.getY(i));					
					} catch (Exception e2) {
						break;
					}
				}	
				//size == 2表示双指操作
				//ͬsize == 1表示单手操作
				if(hm.size()==2)
				{					
					MyPoint bpTempA=hm.get(0);
					MyPoint bpTempB=hm.get(1);
					//伸缩操作
					if(bpTempA == null || bpTempB == null) break;
					float currDis=MyPoint.calDistance(bpTempA, bpTempB);
					currScale=currScale+(currDis-distance) / zoomParameter;		
					if(currScale > maxScale||currScale < minScale)
					{
						currScale=currScale-(currDis-distance) / zoomParameter;
					}
					if(distance != currDis && mResizeListener !=null){
						mResizeListener.resize(currScale);
					}
					distance=currDis;
					//旋转操作
					if(bpTempA.hasOld||bpTempB.hasOld)
					{
						RotateData mRotateData;
						if(mRotateMethod != null){
							mRotateData = mRotateMethod.rotate(bpTempA, bpTempB);
						}else{
							double alphaOld = Math.atan2((bpTempA.oldY- bpTempB.oldY), (bpTempA.oldX- bpTempB.oldX));
							double alphaNew = Math.atan2((bpTempA.y- bpTempB.y), (bpTempA.x- bpTempB.x));
							float tmp=angle+(float)Math.toDegrees(alphaNew-alphaOld);
							mRotateData = new RotateData(tmp,getWidth()/2,getHeight()/2);
						}
						float tmp = mRotateData.getAngle();
						if(Math.abs(tmp) > rotaParameter){
							angle = tmp;
							rotateCenterX = mRotateData.getRotateCenterX();
							rotateCenterY = mRotateData.getRotateCenterY();
							if(mRotateListener != null){
								mRotateListener.rotate(angle);
							}
						}
					}
					//重新画图
					this.repaint();
				}else if(hm.size() == 1){
					MyPoint bpTempA = hm.get(0);
					if(bpTempA != null && bpTempA.hasOld){
						baseX = baseX + (bpTempA.x - bpTempA.oldX);
						baseY = baseY + (bpTempA.y - bpTempA.oldY);
						if(mReMoveListener != null){
							mReMoveListener.remove(baseX, baseY);
						}
					}
					this.repaint();
				}
			break;
			case MotionEvent.ACTION_UP: //放松手指事件
				//清空松手事件集
				hm.clear();
			break;
			case MotionEvent.ACTION_POINTER_UP: //放松一个手指事件
				//移除该id的指针事件
				hm.remove(id);
			break;
		} 			
		return true;
	}	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
		this.repaint();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		adjustPhoto();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{	
		//释放bitmap
		if(bitmapTmp != null){
			if(!bitmapTmp.isRecycled()){
				bitmapTmp.recycle();
			}
			bitmapTmp = null;
		}
	}
	
	private void repaint()  //重绘
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas=holder.lockCanvas();
		try
		{
			synchronized(holder)
			{
				draw(canvas);//执行绘画操作
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas != null)
			{
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	/*
	 * 获取当前显示图层
	 */
	public Bitmap getSurfaceViewBitmap(){
		Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return bitmap;
	}
	
	private OnResizeListener mResizeListener;
	private OnRotateListener mRotateListener;
	private OnReMoveListener mReMoveListener;
	private RotateMethod mRotateMethod;

	/*
	 * 图片伸缩监听器
	 */
	public interface OnResizeListener{
		public void resize(float scale);
	}
	
	/*
	 * 图片旋转监听器
	 */
	public interface OnRotateListener{
		public void rotate(float angle);
	}
	
	/*
	 * 图片平移监听器
	 */
	public interface OnReMoveListener{
		public void remove(float x,float y);
	}
	
	/*
	 * 图片旋转策略接口
	 */
	public interface RotateMethod{
		/*
		 * 实现图片旋转策略算法
		 */
		public RotateData rotate(MyPoint point1,MyPoint point2);
	}
}
