package com.proyecto.serviceimpl;

import com.proyecto.dao.InventarioEntradaDao;
import com.proyecto.domain.InventarioEntrada;
import com.proyecto.service.InventarioEntradaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventarioEntradaServiceImpl implements InventarioEntradaService {

    private final InventarioEntradaDao entradaDao;

    @Override
    @Transactional
    public InventarioEntrada registrar(InventarioEntrada entrada) {
        return entradaDao.save(entrada);
    }
}
