CREATE TABLE IF NOT EXISTS mercados_financieros (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    fecha_vigencia DATE NOT NULL,
                                                    es_fija BOOLEAN NOT NULL,
                                                    valor_tasa INT NOT NULL
);