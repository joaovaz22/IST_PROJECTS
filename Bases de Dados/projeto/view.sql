DROP VIEW IF EXISTS vendas;

CREATE VIEW vendas					
(ean, cat, ano, trimestre, mes, dia_mes, dia_semana, distrito, concelho, unidades)		
AS(
    SELECT evento_reposicao.ean, 
        tem_categoria.nome, 
        EXTRACT(YEAR FROM evento_reposicao.instante), 
        EXTRACT(QUARTER FROM evento_reposicao.instante), 
        EXTRACT(MONTH FROM evento_reposicao.instante),
        EXTRACT(DAY FROM evento_reposicao.instante),
        EXTRACT(DOW FROM evento_reposicao.instante), 
        ponto_de_retalho.distrito, 
        ponto_de_retalho.concelho,
        evento_reposicao.unidades 
        FROM evento_reposicao, tem_categoria, ponto_de_retalho, instalada_em 
        WHERE 
        evento_reposicao.ean = tem_categoria.ean AND
        evento_reposicao.num_serie = instalada_em.num_serie AND 
        evento_reposicao.fabricante = instalada_em.fabricante AND
        instalada_em.local = ponto_de_retalho.nome
);