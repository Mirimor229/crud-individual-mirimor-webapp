package org.alixar.daw2.mirimor.crud_individual_mirimor_webapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MercadoFinanciero {

    private Integer id;
    private LocalDate fechaVigencia;
    private Boolean esFija;
    private Integer valorTasa;
}