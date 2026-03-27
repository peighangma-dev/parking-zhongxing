package com.parking.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.vehicle.entity.Member;
import java.time.LocalDate;
import java.util.List;

public interface MemberService {

    IPage<Member> page(Integer current, Integer size, Long vehicleId, String memberType, String status);

    List<Member> list(Long vehicleId, String memberType, String status);

    Member getById(Long id);

    Member getByVehicleId(Long vehicleId);

    boolean isValid(Long vehicleId);

    Member save(Member member);

    Member renew(Long id, LocalDate newEndDate);

    Member update(Member member);

    boolean delete(Long id);
}
