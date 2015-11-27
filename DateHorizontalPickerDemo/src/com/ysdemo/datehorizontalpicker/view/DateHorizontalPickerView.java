package com.ysdemo.datehorizontalpicker.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yedemo.datehorizontalpicker.R;
import com.ysdemo.datehorizontalpicker.view.DateHorizontalPickerAdapter.DatePickListener;

/**
 * @Author: 尘笑ys
 * @date 2015-11-5 下午2:34:53
 * @Description: 横向日期选择器自定义ViewGroup
 */

public class DateHorizontalPickerView extends RelativeLayout implements DatePickListener {

	private static final String TAG = null;
	private List<Map<String, Object>> timelist;
	private Context mContext;
	private ImageView leftArrowImg,rightArrowImg;
	private RelativeLayout theDayBeforeLayout, theDayAfterLayout;
	private TextView beforeYearTv,beforeMonthTv,beforeDayTv,
								afterYearTv,afterMonthTv,afterDayTv;
	private int yearColor,monthColor,dayColor;
	private ViewPager datePager;
	/**
	 * 是否显示左右箭头
	 */
	private boolean isShowArrows;
	/**
	 * 是否显示透明度变化
	 */
	private boolean isShowAlphaChange;

	/**
	 * 绘制时控制内容绘制的范围
	 */
	private Rect mBound;

	private OnViewPagerScrollListener scrollListener;

	private OnArrowClickListener buttonListener;
	
	private OnLayoutClickListener layoutListener;
	
	public interface OnViewPagerScrollListener{
		/**
		 * 日期选择完成时调用
		 */
		void onPageSelected(int arg0);
		/** 
		 * 滑动时调用
		 * @param arg0 当前页面，及你点击滑动的页面
		 * @param arg1 当前页面偏移的百分比
		 * @param arg2 当前页面偏移的像素位置   
		 */
		void onPageScrolled(int arg0,float arg1,int arg2);
		/**
		 * 滑动状态改变时调用
		 */
		void onPageScrollStateChanged();
	}
	
	public interface OnArrowClickListener{
		/**
		 * 左边箭头点击事件
		 */
		void onLeftClicked(View view);
		/** 
		 * 右边箭头点击事件
		 */
		void onRightClicked(View view);
	}

	public interface OnLayoutClickListener{
		/**
		 * 前一天被点击
		 */
		void onBeforeDayClicked(View view);
		/**
		 * 后一天被点击
		 */
		void onAfterDayClicked(View view);
	}

	public void setButtonListener(OnArrowClickListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public void setLayoutListener(OnLayoutClickListener layoutListener) {
		this.layoutListener = layoutListener;
	}

	public void setScrollListener(OnViewPagerScrollListener scrollListener) {
		this.scrollListener = scrollListener;
	}

	public DateHorizontalPickerView(Context context) {
		super(context);
	}
	
	public DateHorizontalPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
        /** 
         * 获得我们所定义的自定义样式属性 
         */  
        View view = LayoutInflater.from(context).inflate(R.layout.layout_horizontal_date_picker, this, true);  
        leftArrowImg = (ImageView) view.findViewById(R.id.iv_arrow_left);
        rightArrowImg = (ImageView) view.findViewById(R.id.iv_arrow_right);
        theDayBeforeLayout = (RelativeLayout) view.findViewById(R.id.layout_day_before);
        theDayAfterLayout = (RelativeLayout) view.findViewById(R.id.layout_day_after);
        beforeYearTv = (TextView) view.findViewById(R.id.tv_lastday_year);
        beforeMonthTv = (TextView) view.findViewById(R.id.tv_lastday_month);
        beforeDayTv = (TextView) view.findViewById(R.id.tv_lastday);
        afterYearTv = (TextView) view.findViewById(R.id.tv_afterday_year);
        afterMonthTv = (TextView) view.findViewById(R.id.tv_afterday_month);
        afterDayTv = (TextView) view.findViewById(R.id.tv_afterday);
        datePager = (ViewPager) view.findViewById(R.id.vp_date_pager);
        
        //监听回调实现
        leftArrowImg.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		if (null != buttonListener) {
        			buttonListener.onLeftClicked(v);
				}
        	}
        });
        rightArrowImg.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		if (null != buttonListener) {
            		buttonListener.onRightClicked(v);
				}
        	}
        });
        theDayBeforeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		if (null != layoutListener) {
        			layoutListener.onBeforeDayClicked(v);
				}
			}
		});
        theDayAfterLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		if (null != layoutListener) {
    				layoutListener.onAfterDayClicked(v);
				}
			}
		});
        //获取attribute
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DateHorizontalPickerView, R.style.AppTheme, 0);  
        int n = a.getIndexCount();  
        for (int i = 0; i < n; i++)  
        {  
            int attr = a.getIndex(i);  
            switch (attr)  
            {
            case R.styleable.DateHorizontalPickerView_yearColor:  
            	yearColor = a.getColor(attr,0xff8b2c8c);
                break;  
            case R.styleable.DateHorizontalPickerView_monthColor:  
            	monthColor = a.getColor(attr,0xff8b2c8c);
                break;  
            case R.styleable.DateHorizontalPickerView_dayColor:  
            	dayColor = a.getColor(attr,0xff8b2c8c);
                break;  
            case R.styleable.DateHorizontalPickerView_showarrow:  
            	isShowArrows = a.getBoolean(attr, true);
                break;  
            }  
        }  

        a.recycle();  
        
        if (!isShowArrows) {
        	leftArrowImg.setVisibility(View.GONE);
        	rightArrowImg.setVisibility(View.GONE);
        } else {
        	leftArrowImg.setVisibility(View.VISIBLE);
        	rightArrowImg.setVisibility(View.VISIBLE);
        }
    	beforeYearTv.setTextColor(yearColor);
    	afterYearTv.setTextColor(yearColor);
    	beforeMonthTv.setTextColor(monthColor);
    	afterMonthTv.setTextColor(monthColor);
    	beforeDayTv.setTextColor(dayColor);
    	afterDayTv.setTextColor(dayColor);
    	setDateScrollListener();
	}

	/**
	 * 设置ViewPager滑动监听
	 */
	protected void setDateScrollListener() {
    	datePager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
        		if (null != scrollListener) {
        			scrollListener.onPageSelected(arg0);
				}
        		setOtherDate(arg0);
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (isShowAlphaChange) {
					theDayBeforeLayout.setAlpha((float)( 0.1 + (arg1 * 0.9)));
					theDayAfterLayout.setAlpha((float)( 0.1 + (arg1 *0.9)));
				}
        		if (null != scrollListener) {
        			scrollListener.onPageScrolled(arg0, arg1, arg2);
				}
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onPageScrollStateChanged(int arg0) {
//				arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
				if (isShowAlphaChange && arg0 == ViewPager.SCROLL_STATE_SETTLING) {
					theDayBeforeLayout.setAlpha(1);
					theDayAfterLayout.setAlpha(1);
				} else if (isShowAlphaChange && arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
					theDayBeforeLayout.setAlpha((float) 0.1);
					theDayAfterLayout.setAlpha((float) 0.1);
				}
        		if (null != scrollListener) {
        			scrollListener.onPageScrollStateChanged();
				}
			}
		});
	}
	

	/**
	 * 是否显示透明度变化
	 * @return true显示，false不显示
	 */
	public boolean isShowAlphaChange() {
		return isShowAlphaChange;
	}

	/**
	 * 是否显示透明度变化
	 * @param isShowAlphaChange	true显示，false不显示
	 */
	public void setShowAlphaChange(boolean isShowAlphaChange) {
		this.isShowAlphaChange = isShowAlphaChange;
	}

	protected void setOtherDate(int arg0) {
//		private TextView beforeYearTv,beforeMonthTv,beforeDayTv,
//									afterYearTv,afterMonthTv,afterDayTv;
		beforeYearTv.setText((String) timelist.get( arg0-1 ).get("year"));
		beforeMonthTv.setText((String) timelist.get( arg0-1 ).get("month"));
		beforeDayTv.setText((String) timelist.get( arg0-1 ).get("day"));
		afterYearTv.setText((String) timelist.get( arg0+1 ).get("year"));
		afterMonthTv.setText((String) timelist.get( arg0+1 ).get("month"));
		afterDayTv.setText((String) timelist.get( arg0+1 ).get("day"));
	}

	/**
	 * 初始化，统一传入参数
	 * @param startDate
	 * @param endDate
	 * @param scrollListener
	 * @param layoutClickListener
	 * @param arrowClickListener
	 */
	public void initDateView(String startDate,String endDate,OnViewPagerScrollListener scrollListener
								,OnLayoutClickListener layoutClickListener,OnArrowClickListener arrowClickListener) {
		setDateRange(startDate, endDate);
		setScrollListener(scrollListener);
		setButtonListener(arrowClickListener);
		setLayoutListener(layoutListener);
		datePager.setCurrentItem(1);
		setOtherDate(1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//		mBound = new Rect();
//		int width = 0;
//		int height = 0;
//
//		/**
//		 * 设置宽度
//		 */
//		int specMode = MeasureSpec.getMode(widthMeasureSpec);
//		int specSize = MeasureSpec.getSize(widthMeasureSpec);
//		switch (specMode)
//		{
//		case MeasureSpec.EXACTLY:// 明确指定了
//			width = getPaddingLeft() + getPaddingRight() + specSize;
//			break;
//		case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
//			width = getPaddingLeft() + getPaddingRight() + mBound.width();
//			break;
//		}
//
//		/**
//		 * 设置高度
//		 */
//		specMode = MeasureSpec.getMode(heightMeasureSpec);
//		specSize = MeasureSpec.getSize(heightMeasureSpec);
//		switch (specMode)
//		{
//		case MeasureSpec.EXACTLY:// 明确指定了
//			height = getPaddingTop() + getPaddingBottom() + specSize;
//			break;
//		case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
//			height = getPaddingTop() + getPaddingBottom() + mBound.height();
//			break;
//		}
//
//		setMeasuredDimension(width, height);

	}

	/**
	 * @param startDate		开始日期("yyyy-MM-dd")
	 * @param endDate		结束日期("yyyy-MM-dd")
	 */
	public void setDateRange(String startDate,String endDate) {
		setDateList(startDate, endDate);
		setDateAdapter(new DateHorizontalPickerAdapter(mContext,this,this.timelist));
	}

	/**
	 * 一般不用，用于重写Adapter的直接选择日期的回调事件
	 * @param startDate		开始日期("yyyy-MM-dd")
	 * @param endDate		结束日期("yyyy-MM-dd")
	 */
	public void setDateRange(String startDate,String endDate,DatePickListener listener) {
		setDateList(startDate, endDate);
		setDateAdapter(new DateHorizontalPickerAdapter(mContext,listener,this.timelist));
	}

	private void setDateAdapter(PagerAdapter adapter) {
		datePager.setAdapter(adapter);
	}

	public PagerAdapter getDateHorizontalPickerAdapter(){
		return datePager.getAdapter();
	}

	@Override
	public void onDateChanged(String mYear, String mMonth, String dayOfMonthe,int index) {
		datePager.setCurrentItem(index);
		if (null != scrollListener ) {
			scrollListener.onPageSelected(index);
		}
	}
	/**
	 * @param dateFirst		开始日期("yyyy-MM-dd")
	 * @param dateSecond	结束日期("yyyy-MM-dd")
	 */
	public void setDateList(String dateFirst, String dateSecond){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);
        try{
            Date dateOne = dateFormat.parse(dateFirst);
            Date dateTwo = dateFormat.parse(dateSecond);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOne);
            Map<String,Object> map;
            while(calendar.getTime().before(dateTwo)){
            	map = new HashMap<String,Object>();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String date = dateFormat.format(calendar.getTime());
                int year = calendar.get(Calendar.YEAR),months = calendar.get(Calendar.MONTH),day = calendar.get(Calendar.DAY_OF_MONTH);
                months++;
                map.put("year", year+"");

        		// 单数前加0,强迫症！！！
        		if((months+"").length() == 2){
        			map.put("month", months + "");
        		}else {	// 1
        			map.put("month", "0"+(months));
        		}
        		
        		if((day+"").length() == 2){
        			map.put("day", day + "");
        		}else{
        			map.put("day","0"+ day);
        		}
        		
                
                list.add(map);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        this.timelist = list;
        Log.i(TAG, "data be set!counts:"+ this.timelist.size());
    }
	
	/**
	 * @param original		传入的日期("yyyy-MM-dd")
	 * @param addorless	加或减的天数
	 * @return
	 */
	public static String getDateAddOrLess(String original , int addorless){
		try {
			// 将结束时间天数加1
			Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(original);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, addorless);
			String newDate = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
			if(newDate != null){
				return newDate;
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
