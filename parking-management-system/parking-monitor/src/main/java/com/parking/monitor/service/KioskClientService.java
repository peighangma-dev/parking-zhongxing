package com.parking.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.monitor.entity.KioskClient;
import com.parking.monitor.mapper.KioskClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KioskClientService {

    @Autowired
    private KioskClientMapper kioskClientMapper;

    public IPage<KioskClient> page(Integer current, Integer size, Long tenantId) {
        Page<KioskClient> page = new Page<>(current, size);
        return kioskClientMapper.selectPage(page, null);
    }

    public List<KioskClient> list(Long tenantId) {
        return kioskClientMapper.selectList(null);
    }

    public KioskClient getById(Long id) {
        return kioskClientMapper.selectById(id);
    }

    public KioskClient create(KioskClient kiosk) {
        kiosk.setStatus("offline");
        kiosk.setCreatedAt(LocalDateTime.now());
        kioskClientMapper.insert(kiosk);
        return kiosk;
    }

    public KioskClient update(Long id, KioskClient kiosk) {
        kiosk.setId(id);
        kioskClientMapper.updateById(kiosk);
        return kiosk;
    }

    public boolean delete(Long id) {
        return kioskClientMapper.deleteById(id) > 0;
    }

    public boolean updateStatus(Long id, String status) {
        KioskClient kiosk = new KioskClient();
        kiosk.setId(id);
        kiosk.setStatus(status);
        return kioskClientMapper.updateById(kiosk) > 0;
    }

    public boolean heartbeat(String kioskCode, String ipAddress) {
        KioskClient kiosk = new KioskClient();
        kiosk.setKioskCode(kioskCode);
        kiosk.setIpAddress(ipAddress);
        kiosk.setStatus("online");
        kiosk.setLastHeartbeat(LocalDateTime.now());
        
        List<KioskClient> list = kioskClientMapper.selectList(null);
        for (KioskClient k : list) {
            if (k.getKioskCode().equals(kioskCode)) {
                return kioskClientMapper.updateById(kiosk) > 0;
            }
        }
        return false;
    }
}
