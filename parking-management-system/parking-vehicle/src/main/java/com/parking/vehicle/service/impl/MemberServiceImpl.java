package com.parking.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.vehicle.entity.Member;
import com.parking.vehicle.mapper.MemberMapper;
import com.parking.vehicle.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Override
    public IPage<Member> page(Integer current, Integer size, Long vehicleId, String memberType, String status) {
        Page<Member> page = new Page<>(current, size);
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (vehicleId != null) {
            wrapper.eq(Member::getVehicleId, vehicleId);
        }
        if (StringUtils.hasText(memberType)) {
            wrapper.eq(Member::getMemberType, memberType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Member::getStatus, status);
        }
        wrapper.orderByDesc(Member::getCreatedAt);
        return memberMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Member> list(Long vehicleId, String memberType, String status) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (vehicleId != null) {
            wrapper.eq(Member::getVehicleId, vehicleId);
        }
        if (StringUtils.hasText(memberType)) {
            wrapper.eq(Member::getMemberType, memberType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Member::getStatus, status);
        }
        wrapper.orderByDesc(Member::getCreatedAt);
        return memberMapper.selectList(wrapper);
    }

    @Override
    public Member getById(Long id) {
        Member member = memberMapper.selectById(id);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

    @Override
    public Member getByVehicleId(Long vehicleId) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getVehicleId, vehicleId);
        wrapper.eq(Member::getStatus, "active");
        wrapper.ge(Member::getEndDate, LocalDate.now());
        wrapper.orderByDesc(Member::getEndDate);
        wrapper.last("LIMIT 1");
        return memberMapper.selectOne(wrapper);
    }

    @Override
    public boolean isValid(Long vehicleId) {
        Member member = getByVehicleId(vehicleId);
        if (member == null) {
            return false;
        }
        return member.getEndDate().isAfter(LocalDate.now()) || member.getEndDate().isEqual(LocalDate.now());
    }

    @Override
    public Member save(Member member) {
        member.setStatus("active");
        memberMapper.insert(member);
        return member;
    }

    @Override
    public Member renew(Long id, LocalDate newEndDate) {
        Member member = getById(id);
        member.setEndDate(newEndDate);
        memberMapper.updateById(member);
        return member;
    }

    @Override
    public Member update(Member member) {
        if (member.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Member existing = memberMapper.selectById(member.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        memberMapper.updateById(member);
        return member;
    }

    @Override
    public boolean delete(Long id) {
        return memberMapper.deleteById(id) > 0;
    }
}
