package com.proyecto.serviceimpl;

import com.proyecto.dao.InventarioSalidaDao;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.service.InventarioSalidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventarioSalidaServiceImpl implements InventarioSalidaService {

    private final InventarioSalidaDao salidaDao;

    @Override
    @Transactional
    public InventarioSalida registrar(InventarioSalida salida) {
        return salidaDao.save(salida);
    }
}
