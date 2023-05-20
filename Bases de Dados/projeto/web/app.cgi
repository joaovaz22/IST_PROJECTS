#!/usr/bin/python3

from wsgiref.handlers import CGIHandler
from flask import Flask
from flask import render_template, request, redirect, url_for
import psycopg2
import psycopg2.extras


# SGBD configs
DB_HOST="db.tecnico.ulisboa.pt"
DB_USER="ist199134"
DB_DATABASE=DB_USER
DB_PASSWORD="loth1608"
DB_CONNECTION_STRING = "host=%s dbname=%s user=%s password=%s" % (
    DB_HOST, 
    DB_DATABASE, 
    DB_USER, 
    DB_PASSWORD,
)
app = Flask(__name__)

def isInvalidInput(s):
    return '--' in s or ';' in s or "'" in s or s == ""


@app.route("/")
def main_menu():
    try:
        return render_template("index.html")
    except Exception as e:
        return str(e)  # Renders a page with the error.

@app.route("/search_categoria", methods=["POST"])
def search_categoria():
    try:
        nome = request.form["nome"]
        if (isInvalidInput(nome) and nome):
            return render_template("erro.html",  spot='Categoria', campo='Nome', literal=nome)

        return render_template("search.html", campo = [("Nome", nome)], link = 'categoria?nome={0}'.format(nome))
    except Exception as e:
        return str(e)

@app.route("/search_retalhista", methods=["POST"])
def search_retalhista():
    try:
        tin = request.form["tin"]
        if (isInvalidInput(tin) and tin):
            return render_template("erro.html",  spot='Retalhista', campo='tin', literal=tin)

        return render_template("search.html", campo = [("Tin", tin)], link = 'retalhista?tin={0}'.format(tin))
    except Exception as e:
        return str(e)
    
@app.route("/search_ivm", methods=["POST"])
def search_ivm():
    try:
        num_serie = request.form["num_serie"]
        fabricante = request.form["fabricante"]
        if (isInvalidInput(num_serie) and num_serie):
            return render_template("erro.html",  spot='Ivm', campo='Numero de Série', literal=num_serie)
        if (isInvalidInput(fabricante) and fabricante):
            return render_template("erro.html",  spot='Ivm', campo='Fabricante', literal=fabricante)

        return render_template("search.html", campo = [("Número de Série", num_serie), ("Fabricante", fabricante)], link = 'ivm?num_serie={0}&fabricante={1}'.format(num_serie, fabricante))
    except Exception as e:
        return str(e)
    
@app.route("/search_supercategoria", methods=["POST"])
def search_supercategoria():
    try:
        nome = request.form["nome"]
        if (isInvalidInput(nome) and nome):
            return render_template("erro.html",  spot='Super Categoria', campo='Nome', literal=nome)

        return render_template("search.html", campo = [("Nome", nome)], link = 'super_categoria?nome={0}'.format(nome))
    except Exception as e:
        return str(e)



@app.route("/categoria")
def list_categoria():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        nome = request.args.get('nome')
        if isInvalidInput(nome) and nome:
            return render_template("erro.html", spot = 'Categoria', campo = 'Nome', literal = nome)

        if nome:
            query = "SELECT * FROM categoria WHERE nome = '{0}';".format(nome)
        else:
            query = "SELECT * FROM categoria;"
        cursor.execute(query)
        return render_template("cat.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/add_cat")
def form_add_categoria():
    try:
        return render_template("categorias.html", params=request.args)
    except Exception as e:
        return str(e)



@app.route("/updt_cat_add", methods=["POST"])
def add_categoria():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        nome = request.form["nome"]
        if (isInvalidInput(nome)):
            return render_template("erro.html",  spot='Categoria', campo='Nome', literal=nome)

        query = "insert into categoria values('{0}'); insert into categoria_simples values('{0}');".format(nome)
        data = (nome)
        cursor.execute(query, data)
        return redirect(url_for("list_categoria")+ '?nome=')
    except Exception as e:
        return render_template("erro.html",  spot='Categoria', campo='Nome Duplicado', literal=nome)
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


@app.route("/updt_cat_remove")
def rem_categoria():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        nome = request.args.get("nome")
        query = """
        SELECT nome_sub
        INTO tab_aux
        FROM (
            SELECT nome_sub FROM tem_outra where nome_super = '{0}'
            ) AS x;


        WITH cteAux AS
        (SELECT ean
        FROM planograma
        WHERE ean IN(
            SELECT ean
            FROM tem_categoria
            WHERE nome IN
                (SELECT nome_sub 
                FROM tem_outra WHERE nome_super='{0}')  OR nome = '{0}'))
        DELETE FROM evento_reposicao
        WHERE ean in (SELECT ean
                        FROM   cteAux
                        WHERE  evento_reposicao.ean = cteAux.ean);

        WITH cteAux AS
        (
        SELECT nro, num_serie, fabricante
        FROM prateleira
            WHERE nome IN
                (SELECT nome_sub 
                FROM tem_outra WHERE nome_super='{0}') OR nome = '{0}')
        DELETE FROM evento_reposicao    
        WHERE EXISTS(SELECT nro, num_serie, fabricante
                        FROM   cteAux
                            WHERE  evento_reposicao.nro = cteAux.nro AND
                            evento_reposicao.num_serie = cteAux.num_serie AND
                            evento_reposicao.fabricante = cteAux.fabricante);


        WITH cteAux AS
        (
        SELECT nro, num_serie, fabricante
        FROM prateleira
            WHERE nome IN
                (SELECT nome_sub 
                FROM tem_outra WHERE nome_super='{0}') OR nome = '{0}')
        DELETE FROM planograma
        WHERE EXISTS(SELECT nro, num_serie, fabricante
                        FROM   cteAux
                            WHERE  planograma.nro = cteAux.nro AND
                            planograma.num_serie = cteAux.num_serie AND
                            planograma.fabricante = cteAux.fabricante);


        WITH cteAux AS
        (
        SELECT ean
        FROM planograma
        WHERE ean IN(
            SELECT ean
            FROM tem_categoria
            WHERE nome IN
                (SELECT nome_sub 
                FROM tem_outra WHERE nome_super='{0}') OR nome = '{0}'))
        DELETE FROM planograma
        WHERE ean in (SELECT ean
                        FROM   cteAux
                        WHERE  planograma.ean = cteAux.ean);

                        
        WITH cteAux AS
        (SELECT nome_sub 
        FROM tem_outra WHERE nome_super='{0}')                           
        DELETE FROM prateleira
        where nome in (SELECT nome_sub
                        FROM   cteAux
                        WHERE  prateleira.nome = cteAux.nome_sub) OR nome = '{0}';
                        

        DELETE FROM categoria_simples
        WHERE nome IN(SELECT nome_sub FROM tem_outra where nome_super = '{0}') OR
        nome = '{0}' 
        AND EXISTS(SELECT 1 FROM categoria_simples WHERE nome = '{0}');                  
        

        DELETE FROM tem_categoria
        where nome IN(SELECT nome_sub FROM tem_outra where nome_super = '{0}') OR
        nome = '{0}'
        AND EXISTS(SELECT 1 FROM tem_categoria WHERE nome = '{0}');

        DELETE FROM produto
        where cat IN(SELECT nome_sub FROM tem_outra where nome_super = '{0}') OR
        cat = '{0}'
        AND EXISTS(SELECT 1 FROM produto WHERE cat = '{0}');
                                    

        DELETE FROM tem_outra
        where nome_super = '{0}' OR nome_super in (SELECT nome_sub from tab_aux);


        DELETE FROM tem_outra
        where nome_sub = '{0}';


        DELETE FROM super_categoria
        WHERE nome IN(SELECT nome_sub FROM tab_aux) OR
        nome = '{0}' 
        AND EXISTS(SELECT 1 FROM super_categoria WHERE nome = '{0}');

        DELETE FROM responsavel_por
        WHERE nome_cat IN(SELECT nome_sub FROM tab_aux) OR
        nome_cat = '{0}';

        DELETE FROM categoria
        WHERE nome IN(SELECT nome_sub FROM tab_aux) OR
        nome = '{0}';
        
        DROP TABLE tab_aux;""".format(nome)
        data = (nome)
        cursor.execute(query, data)
        return redirect(url_for("list_categoria") + '?nome=', code=301)
    except Exception as e:
        return str(e)
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


@app.route("/ivm")
def list_ivms():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        num_serie = request.args.get('num_serie')
        fabricante = request.args.get('fabricante')
        if isInvalidInput(num_serie) and num_serie:
            return render_template("erro.html", spot = 'Ivm', campo = 'Número de Série', literal = num_serie)
        if isInvalidInput(fabricante) and fabricante:
            return render_template("erro.html", spot = 'Ivm', campo = 'Fabricante', literal = fabricante)

        if num_serie and fabricante:
            query = "SELECT * FROM ivm WHERE num_serie = {0} AND fabricante = '{1}';".format(num_serie, fabricante)
        elif num_serie:
            query = "SELECT * FROM ivm WHERE num_serie = {0};".format(num_serie)
        elif fabricante:
            query = "SELECT * FROM ivm WHERE fabricante = '{0}';".format(fabricante)
        else:
           query = "SELECT * FROM ivm;" 
           
        cursor.execute(query)
        return render_template("ivm.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/ivm_list")
def list_ivms_repo():
    dbConn = None
    cursor = None
    num_serie = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        num_serie = request.args.get('num_serie')
        fabricante = request.args.get('fabricante')
        if isInvalidInput(num_serie) and num_serie:
            return render_template("erro.html", spot = 'Ivm', campo = 'Número de Série', literal = num_serie)
        if isInvalidInput(fabricante) and fabricante:
            return render_template("erro.html", spot = 'Ivm', campo = 'Fabricante', literal = fabricante)
        
        query = """
        SELECT SUM(unidades) as Unidades,produto.cat as Categoria
        FROM (
            SELECT evento_reposicao.ean, SUM(unidades) AS unidades
            FROM evento_reposicao WHERE num_serie = '{0}' AND fabricante = '{1}' GROUP BY ean) AS tabela
        JOIN produto ON tabela.ean = produto.ean 
        GROUP BY produto.cat;""".format(num_serie, fabricante)
        cursor.execute(query)
        return render_template("show_repositions.html", cursor=cursor)
    except Exception as e:
        
        return str(e)  # Renders a page with the error.
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()



@app.route("/retalhista")
def list_retalhistas():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        tin = request.args.get('tin')
        if isInvalidInput(tin) and tin:
            return render_template("erro.html", spot = 'Retalhista', campo = 'Tin', literal = tin)

        if tin:
            query = "SELECT * FROM retalhista WHERE tin = '{0}';".format(tin)
        else:
            query = "SELECT * FROM retalhista;"
        cursor.execute(query)
        return render_template("retalhista.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()


@app.route("/add_retalhista")
def add_retalhista():
    try:
        return render_template("add_retalhista.html", params=request.args)
    except Exception as e:
        return str(e)


@app.route("/updt_retalhista", methods=["POST"])
def updt_retalhist():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        tin = request.form["tin"]
        nome = request.form["nome"]

        if isInvalidInput(tin):
            return render_template("erro.html", spot = 'Retalhista', campo = 'Tin', literal = tin)
        if isInvalidInput(nome):
            return render_template("erro.html", spot = 'Retalhista', campo = 'Nome', literal = nome)

        query = "insert into retalhista values('{0}', '{1}');".format(tin, nome)
        data = (tin, nome)
        cursor.execute(query, data)
        return redirect(url_for("list_retalhistas")+ '?tin=')
    except Exception as e:
        return render_template("erro.html", spot = 'Retalhista', campo = 'Nome Duplicado', literal = nome)
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


@app.route("/remove_retalhista")
def rem_retalhista():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        tin = request.args.get("tin")
        query = """
        DELETE FROM responsavel_por where tin = '{0}';
        DELETE FROM evento_reposicao where tin = '{0}';
        DELETE FROM retalhista where tin = '{0}';""".format(tin)
        data = (tin)
        cursor.execute(query, data)
        return redirect(url_for("list_retalhistas") + '?tin=')
    except Exception as e:
        return str(e)
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()

@app.route("/super_categoria")
def list_super_categoria():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        nome = request.args.get('nome')
        if isInvalidInput(nome) and nome:
            return render_template("erro.html", spot = 'Super Categoria', campo = 'Nome', literal = nome)
        if (nome):
            query = "SELECT * FROM super_categoria WHERE nome = '{0}';".format(nome)
        else:
            query = "SELECT * FROM super_categoria;"
        cursor.execute(query)
        return render_template("super_cat.html", cursor=cursor)
    except Exception as e:
        return str(e)  # Renders a page with the error.
    finally:
        cursor.close()
        dbConn.close()

@app.route("/list_sub_cat")
def list_sub_cats():
    dbConn = None
    cursor = None
    try:
        dbConn = psycopg2.connect(DB_CONNECTION_STRING)
        cursor = dbConn.cursor(cursor_factory=psycopg2.extras.DictCursor)
        nome = request.args.get('nome')
        query = """WITH RECURSIVE results AS
    (
        SELECT  nome_super, 
                nome_sub
        FROM    tem_outra
        WHERE   nome_super = '{0}'
        UNION ALL
        SELECT  t.nome_super, 
                t.nome_sub 
        FROM    tem_outra t
                INNER JOIN results r ON r.nome_sub = t.nome_super AND r.nome_super <> t.nome_super

    )
    SELECT nome_sub
    FROM results ;""".format(nome)
        cursor.execute(query)
        return render_template("show_sub_cats.html", cursor=cursor)
    except Exception as e:
        
        return str(e)  # Renders a page with the error.
    finally:
        dbConn.commit()
        cursor.close()
        dbConn.close()


CGIHandler().run(app)


