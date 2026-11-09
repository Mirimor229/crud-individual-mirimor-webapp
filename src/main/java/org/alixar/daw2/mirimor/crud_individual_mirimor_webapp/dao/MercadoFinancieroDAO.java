package org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.dao;

import org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.entities.MercadoFinanciero;

import java.time.LocalDate;
import java.util.List;

public interface MercadoFinancieroDAO {
    List<MercadoFinanciero> listAllMercados();
    MercadoFinanciero getMercadoById(int id);
    boolean existsMercadoByFechaAndFija(LocalDate fechaVigencia, Boolean esFija);
    void insertMercado(MercadoFinanciero mercado);
    void updateMercado(MercadoFinanciero mercado);
    void deleteMercado(int id);
}