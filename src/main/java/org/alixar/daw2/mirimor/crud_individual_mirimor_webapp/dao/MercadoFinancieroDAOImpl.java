package org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.dao;

import org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.entities.MercadoFinanciero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MercadoFinancieroDAOImpl implements MercadoFinancieroDAO {

    private static final Logger logger = LoggerFactory.getLogger(MercadoFinancieroDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<MercadoFinanciero> rowMapper = (rs, rowNum) -> MercadoFinanciero.builder()
            .id(rs.getInt("id"))
            .fechaVigencia(rs.getObject("fecha_vigencia", LocalDate.class))
            .esFija(rs.getBoolean("es_fija"))
            .valorTasa(rs.getInt("valor_tasa"))
            .build();

    @Override
    public List<MercadoFinanciero> listAllMercados() {
        logger.info("Listing all mercados financieros from the database.");
        List<MercadoFinanciero> lista = jdbcTemplate.query(
                "SELECT id, fecha_vigencia, es_fija, valor_tasa FROM mercados_financieros ORDER BY fecha_vigencia DESC",
                rowMapper
        );
        logger.info("Retrieved {} mercados financieros.", lista.size());
        return lista;
    }

    @Override
    public MercadoFinanciero getMercadoById(int id) {
        logger.info("Getting mercado financiero with ID {}", id);
        List<MercadoFinanciero> result = jdbcTemplate.query(
                "SELECT id, fecha_vigencia, es_fija, valor_tasa FROM mercados_financieros WHERE id = ?",
                rowMapper, id
        );
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean existsMercadoByFechaAndFija(LocalDate fechaVigencia, Boolean esFija) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM mercados_financieros WHERE fecha_vigencia = ? AND es_fija = ?",
                Integer.class, fechaVigencia, esFija
        );
        return count != null && count > 0;
    }

    @Override
    public void insertMercado(MercadoFinanciero mercado) {
        logger.info("Inserting mercado financiero con fecha {}", mercado.getFechaVigencia());
        jdbcTemplate.update(
                "INSERT INTO mercados_financieros (fecha_vigencia, es_fija, valor_tasa) VALUES (?, ?, ?)",
                mercado.getFechaVigencia(), mercado.getEsFija(), mercado.getValorTasa()
        );
    }

    @Override
    public void updateMercado(MercadoFinanciero mercado) {
        logger.info("Updating mercado financiero con ID {}", mercado.getId());
        jdbcTemplate.update(
                "UPDATE mercados_financieros SET fecha_vigencia = ?, es_fija = ?, valor_tasa = ? WHERE id = ?",
                mercado.getFechaVigencia(), mercado.getEsFija(), mercado.getValorTasa(), mercado.getId()
        );
    }

    @Override
    public void deleteMercado(int id) {
        logger.info("Deleting mercado financiero with ID {}", id);
        jdbcTemplate.update("DELETE FROM mercados_financieros WHERE id = ?", id);
    }
}