package org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.controllers;

import org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.dao.MercadoFinancieroDAO;
import org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.entities.MercadoFinanciero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MercadoFinancieroController {

    private static final Logger logger = LoggerFactory.getLogger(MercadoFinancieroController.class);

    @Autowired
    private MercadoFinancieroDAO mercadoDAO;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/mercados")
    @ResponseBody
    public List<MercadoFinanciero> listMercados() {
        logger.info("API: Listando todos los mercados financieros");
        return mercadoDAO.listAllMercados();
    }

    @GetMapping("/api/mercados/{id}")
    @ResponseBody
    public ResponseEntity<MercadoFinanciero> getMercado(@PathVariable int id) {
        MercadoFinanciero mercado = mercadoDAO.getMercadoById(id);
        if (mercado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mercado);
    }

    @PostMapping("/api/mercados")
    @ResponseBody
    public ResponseEntity<?> createMercado(@RequestBody MercadoFinanciero mercado) {
        try {
            if (mercadoDAO.existsMercadoByFechaAndFija(mercado.getFechaVigencia(), mercado.getEsFija())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un mercado con esa fecha y tipo de tasa"));
            }
            mercadoDAO.insertMercado(mercado);
            return ResponseEntity.ok(Map.of("message", "Mercado financiero insertado correctamente"));
        } catch (Exception e) {
            logger.error("Error al insertar mercado: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/api/mercados/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMercado(@PathVariable int id, @RequestBody MercadoFinanciero mercado) {
        try {
            MercadoFinanciero existente = mercadoDAO.getMercadoById(id);
            if (existente == null) {
                return ResponseEntity.notFound().build();
            }
            mercado.setId(id);
            mercadoDAO.updateMercado(mercado);
            return ResponseEntity.ok(Map.of("message", "Mercado financiero actualizado correctamente"));
        } catch (Exception e) {
            logger.error("Error al actualizar mercado: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/api/mercados/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMercado(@PathVariable int id) {
        try {
            MercadoFinanciero existente = mercadoDAO.getMercadoById(id);
            if (existente == null) {
                return ResponseEntity.notFound().build();
            }
            mercadoDAO.deleteMercado(id);
            return ResponseEntity.ok(Map.of("message", "Mercado financiero eliminado correctamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar mercado: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
