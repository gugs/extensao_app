#!/usr/bin/env python
# -*- coding: utf-8 -*-

import re
import requests
import types
import sqlite3
import os
from BeautifulSoup import BeautifulSoup


# http://www.cpvs.com.br/cpvs/prodpesquisa.aspx?codigo=398

def get_text_by_id(parsed_html, id):
    r = replace_with_newlines(parsed_html.body.find(id=id))
    return r.replace("&nbsp;", " ");

def replace_with_newlines(element):
    text = ''
    for elem in element.recursiveChildGenerator():
        if isinstance(elem, types.StringTypes):
            text += elem.strip()
        elif elem.name == 'br':
            text += '\n'
    return text

def main():
    DBNAME = "cpv.db"
    try:
        os.remove(DBNAME)
    except:
        print "Não foi possível apagar o arquivo: {f}".format(f=DBNAME)

    conn = sqlite3.connect(DBNAME)

    cursor = conn.cursor()

    cursor.execute("""
                    CREATE TABLE product(
                    id INTEGER PRIMARY KEY,
                    produto TEXT,
                    empresa TEXT,
                    a1 INTEGER,
                    a2 INTEGER,
                    a3 INTEGER,
                    a4 INTEGER,
                    a5 INTEGER,
                    a6 INTEGER,
                    a7 INTEGER,
                    a8 INTEGER,
                    a9 INTEGER,
                    a10 INTEGER,
                    a11 INTEGER,
                    a12 INTEGER,
                    formula TEXT,
                    indicacao TEXT,
                    dosagem TEXT,
                    administracao TEXT,
                    apresentacao TEXT,
                    classe TEXT,
                    ativos TEXT,
                    registro TEXT,
                    responsavel TEXT,
                    sac TEXT
                    )""")

    pattern = re.compile(r'http://www.cpvs.com.br/cpvs/prodpesquisa.aspx\?codigo=[\d]*')

    f = open("CPVS.html")

    url_list = re.findall(pattern, f.read())

    print "Encontrados {len} registros".format(len=len(url_list))

    cont = 0;

    for url in url_list:
        print "############################################################################"
        cont = cont + 1
        print "Processando registro {cont} de {len} : {url}".format(cont=cont,len=len(url_list),url=url)

        id = int(re.search(r'\d+', url).group())

        req = requests.get(url)

        parsed_html = BeautifulSoup(req.text)

        for hidden in parsed_html.body.findAll(style='display:none'):
            hidden.decompose()

        produto = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblProduto")
        empresa = get_text_by_id(parsed_html, "ContentPlaceHolder1_LinkEmpresa")

        # Cria uma lista de 1 a 12
        ani = [0,0,0,0,0,0,0,0,0,0,0,0,0]
        for aniId in xrange(1, 13):
            if parsed_html.body.find(id="ContentPlaceHolder1_Image{id}".format(id=aniId)) is not None:
                print aniId
                ani[aniId-1] = 1

        formula = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblFormula")
        indicacao = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblIndicacao")
        dosagem = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblDosagem")
        administracao = get_text_by_id(parsed_html, "ContentPlaceHolder1_lbladministracao")
        apresentacao = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblApresentacao")
        classe = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblClasse")
        ativos = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblAtivos")
        registro = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblRegistro")
        responsavel = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblResponsavel")
        sac = get_text_by_id(parsed_html, "ContentPlaceHolder1_lblSac")

        #(id, produto, empresa, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, formula, indicacao, dosagem, administracao, apresentacao, classe, ativos, registro, responsavel, sac)
        cursor.execute("""
                        INSERT INTO product
                        (id, produto, empresa,
                        a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12,
                        formula, indicacao, dosagem, administracao, apresentacao, classe, ativos, registro, responsavel, sac)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                        """,
                        (id, produto, empresa,
                        ani[0], ani[1], ani[2], ani[3], ani[4], ani[5], ani[6], ani[7], ani[8], ani[9], ani[10], ani[11],
                        formula, indicacao, dosagem, administracao, apresentacao, classe, ativos, registro, responsavel, sac,))
        conn.commit()

    print "Programa concluído com sucesso."

if __name__ == '__main__':
    main()
