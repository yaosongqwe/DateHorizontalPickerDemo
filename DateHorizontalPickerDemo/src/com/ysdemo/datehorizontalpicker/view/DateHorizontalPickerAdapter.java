package com.ysdemo.datehorizontalpicker.view;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yedemo.datehorizontalpicker.R;

/**
 * @Author: 尘笑ys
 * @date 2015-11-5 下午2:36:18
 * @Description:	日期选择器Adapter
 */
@SuppressLint("UseValueOf")
public class DateHorizontalPickerAdapter extends PagerAdapter {
	private final static String TAG = "DateHorizontalPickerAdapter";
	//缓存View的容器
	private SparseArray<View> mViewList;
	private Context mContext;
	private LayoutInflater inflater;
	// 时间临时记录
	private String mYear,mMonth,dayOfMonthe;
	
	private List<Map<String, Object>> timelist;
	private DatePickListener mCallback;
	
	public interface DatePickListener{
		public void onDateChanged(String mYear,String mMonth,String dayOfMonthe,int index);
	}

	/**
	 * @param context
	 * @param listener
	 */
	private DateHorizontalPickerAdapter(Context context ,DatePickListener listener) {
		this.mContext = context;
		mCallback = listener;
		inflater = LayoutInflater.from(context);
		mViewList = new SparseArray<View>();
	}
	/**
	 * @param context
	 * @param listener
	 * @param startDate
	 * @param endDate
	 */
	public DateHorizontalPickerAdapter(Context context ,DatePickListener listener,List<Map<String, Object>> timelist) {
		this(context, listener);
		this.timelist = timelist;
	}
	
	@Override
	public int getCount() {
		return timelist != null ? timelist.size() : 0;
	}
	
	@Override
	public Object instantiateItem(ViewGroup view, final int index) {
		View counterView = inflater.inflate(R.layout.layout_date,null);
		TextView chooseTimeYear = (TextView) counterView.findViewById(R.id.tv_year);
		TextView chooseTimeMonth = (TextView) counterView.findViewById(R.id.tv_month);
		TextView chooseTimeDay = (TextView) counterView.findViewById(R.id.tv_day);
		chooseTimeYear.setText(timelist.get(index).get("year").toString());
		chooseTimeMonth.setText(timelist.get(index).get("month").toString());
		chooseTimeDay.setText(timelist.get(index).get("day").toString());
		view.addView(counterView);
		((RelativeLayout)counterView.findViewById(R.id.layout_date)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeChoose();
			}
		});
		mViewList.put(index, counterView);
		return view;
	}
	
	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView((View) arg2);
		mViewList.remove(position);
	}
	
	@Override
	public void finishUpdate(View arg0) {
		
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return (view == obj);
	}
	
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public void startUpdate(View arg0) {
		
	}
	
	/**	dialog监听器	 **/
	OnDateSetListener endDateset = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker dp, int year, int month,
				int dayOfMonth) {
			month = month + 1;
			if(year == new Integer(mYear)){
				if((month) == new Integer(mMonth.length()==1?mMonth.substring(1, mMonth.length()+1):mMonth)){
					if(dayOfMonth == new  Integer(dayOfMonthe.length()==1?dayOfMonthe.substring(1, dayOfMonthe.length()+1):dayOfMonthe)){
						return;
					}
				}
			}
			if((month+"").length() == 2){
				mMonth = ""+(month);
			}else{
				mMonth = "0"+(month);
			}
			if((dayOfMonth+"").length() == 1){
				dayOfMonthe = "0"+dayOfMonth;
			}else{
				dayOfMonthe = ""+dayOfMonth;
			}
			mYear = year+"";
			
			//选择日期回调
			if (null != mCallback) {
				int index = getIndex(mYear+"-"+mMonth+"-"+dayOfMonthe);
				if(index != -1){
					mCallback.onDateChanged(mYear, mMonth, dayOfMonthe,index);
				}else{
					Toast.makeText(mContext, "该日期数据不存在", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
	
	/** 时间弹窗 */
	public void timeChoose(){
		MyDatePickerDialog endDatePicker = new MyDatePickerDialog(mContext, endDateset, new Integer(mYear), (new Integer(mMonth)-1),
				new Integer(dayOfMonthe));
		endDatePicker.show();
	}
	
	/**
	 * 获取时间在map里的index，不存在则返回-1
	 * @param thisTime
	 * @return
	 */
	public int getIndex(String thisTime){
		for(int i = 0;i<timelist.size();i++){
			String year = timelist.get(i).get("year").toString();
			String month = timelist.get(i).get("month").toString();
			String day = timelist.get(i).get("day").toString();
			if(thisTime.equals(year+"-"+month+"-"+day)){
				return i;
			}
		}
		return -1;
	}
	
}
