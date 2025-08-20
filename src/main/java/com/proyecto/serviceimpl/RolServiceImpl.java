package com.proyecto.serviceimpl;

import com.proyecto.dao.RolDao;
import com.proyecto.domain.Rol;
import com.proyecto.service.RolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolDao rolDao;

    @Override
    public List<Rol> listarTodos() {
        return rolDao.findAll();
    }
}
