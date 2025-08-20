package com.proyecto.serviceimpl;

import com.proyecto.dao.DashboardDao;
import com.proyecto.dao.KpisAdminProjection;
import com.proyecto.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardDao dashboardDao;

    @Override
    public KpisAdminProjection getKpis() {
        return dashboardDao.getKpis();
    }
}
