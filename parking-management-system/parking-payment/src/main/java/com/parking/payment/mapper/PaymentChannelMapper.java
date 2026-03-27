package com.parking.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parking.payment.entity.PaymentChannel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentChannelMapper extends BaseMapper<PaymentChannel> {
}
