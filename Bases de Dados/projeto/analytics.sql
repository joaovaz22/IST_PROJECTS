--1
--Inicio: 2020-10-20
--Fim:    2022-06-16

SELECT 
    dia_semana as "Dia da Semana",
    concelho as "Concelho",
    sum(unidades) as "Total de Unidades"
FROM
    vendas
WHERE (ano = 2020 AND mes = 10 AND dia_mes >= 20) OR 
    (ano = 2020 AND mes > 10) OR 
    (ano > 2020 AND ano < 2022) OR 
    (ano = 2022 AND mes < 6) OR
    (ano = 2022 AND mes = 6 AND dia_mes <= 16)
GROUP BY
   GROUPING SETS(dia_semana, concelho, unidades);


--2
SELECT
    concelho as "Concelho",
    cat as "Categoria",
    dia_semana as "Dia da Semana",
    SUM(unidades) as "Total de Unidades"
FROM
   vendas
WHERE distrito = 'Lisboa'

GROUP BY
   GROUPING SETS(concelho, cat, dia_semana, unidades);
