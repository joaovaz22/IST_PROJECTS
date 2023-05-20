--RI-1
CREATE OR REPLACE FUNCTION categoria_err()
RETURNS TRIGGER
AS $$
BEGIN
    IF NEW.nome_super = NEW.nome_sub THEN
        RAISE EXCEPTION 'Uma Categoria não pode estar contida em si própria';
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER categoria_trigger
BEFORE INSERT OR UPDATE ON tem_outra
FOR EACH ROW EXECUTE PROCEDURE categoria_err();

--RI-4

CREATE OR REPLACE FUNCTION unidades_proc()
RETURNS TRIGGER 
AS $$
DECLARE	un INTEGER;
BEGIN
    un := (SELECT unidades 
        FROM planograma 
        WHERE ean = NEW.ean AND nro = NEW.nro AND num_serie = NEW.num_serie AND  fabricante = NEW.fabricante);

    IF NEW.unidades > un THEN
        NEW.unidades := un;
    END IF;
    
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER unidades_trigger
BEFORE INSERT OR UPDATE ON evento_reposicao
FOR EACH ROW EXECUTE PROCEDURE unidades_proc();





--RI-5 
--Um Produto só pode ser reposto numa Prateleira 
--que apresente (pelo menos) uma das Categorias desse produto

CREATE OR REPLACE FUNCTION produto_reposto()
RETURNS TRIGGER
AS $$
DECLARE n INTEGER;
DECLARE categoria_produto TEXT;
DECLARE categoria_prateleira TEXT;
BEGIN

    categoria_produto := (SELECT nome FROM prateleira WHERE nro = NEW.nro AND num_serie = NEW.num_serie AND fabricante = NEW.fabricante);

    n := (SELECT COUNT(*) FROM tem_categoria WHERE nome = categoria_produto AND ean = NEW.ean);

    IF n = 0 THEN
        RAISE EXCEPTION 'Este produto não pode ser resposto nesta prateleira.';
    END IF;

    RETURN NEW;

END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER planograma_trigger
BEFORE INSERT OR UPDATE ON planograma
FOR EACH ROW EXECUTE PROCEDURE produto_reposto();

