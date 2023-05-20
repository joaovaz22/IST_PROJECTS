-- Qual o nome do retalhista (ou retalhistas) responsáveis pela reposição do maior número de categorias?
WITH freq_cte as
(
    SELECT nome, count(*) AS freq
    FROM(retalhista
    INNER JOIN responsavel_por ON retalhista.tin = responsavel_por.tin)
    GROUP BY nome
)
SELECT nome FROM freq_cte
WHERE freq = (SELECT MAX(freq) FROM freq_cte);


-- Qual o nome do ou dos retalhistas que são responsáveis por todas as categorias simples?
SELECT retalhista.nome
FROM ((retalhista
    INNER JOIN responsavel_por ON retalhista.tin = responsavel_por.tin)
    INNER JOIN categoria_simples ON responsavel_por.nome_cat = categoria_simples.nome)
GROUP BY retalhista.nome
HAVING COUNT(* ) = (SELECT COUNT(* ) FROM categoria_simples);


-- Quais os produtos (ean) que nunca foram repostos?
SELECT ean FROM produto
EXCEPT
SELECT ean FROM evento_reposicao;


-- Quais os produtos (ean) que foram repostos sempre pelo mesmo retalhista?
WITH ean_freq AS
(
    SELECT ean, Count(*)
    FROM evento_reposicao
    GROUP BY ean, tin
    INTERSECT 
    SELECT ean, COUNT(ean)
    FROM evento_reposicao 
    GROUP BY ean
)
SELECT ean FROM ean_freq;

