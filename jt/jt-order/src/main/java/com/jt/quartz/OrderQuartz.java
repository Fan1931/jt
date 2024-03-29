package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	//修改数据库超时订单
	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 当用户订单提交30分钟后,如果还没有支付.则交易关闭
	 * 现在时间 - 订单创建时间 > 30分钟  则超时
	 * new date - 30 分钟 > 订单创建时间
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//专门操作时间的api是Calender
		Calendar calendar = Calendar.getInstance(); //获取当前时间
		calendar.add(Calendar.MINUTE, -30);
		//获取计算之后的时间
		Date date = calendar.getTime();
		Order order = new Order();
		order.setStatus(6);
		order.setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", "1").lt("created",date);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务执行");
	}
}
