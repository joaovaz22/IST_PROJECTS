#joao vaz 98946
def eh_tabuleiro(tab):
    '''

    Determina a validade de um tabuleiro.

    Apenas aceita um tuplo constituido por 3 tuplos, de comprimento 3, onde apenas pode conter os digitos (1,-1,0)

    '''
    n = 0
    while n < 3:  # Percorre cada tuplo dentro de tab, verificando se cumpre os requisitos.
        if type(tab) != tuple or type(tab[n]) != tuple or len(tab[n]) != 3 or len(tab) != 3:
            return False
        for valor in tab[n]:
            if type(valor) != int or valor > 1 or valor < -1:
                return False
        n += 1
    return True


def eh_posicao(num):
    '''

    Determina a validade da posicao introduzida.

    Apenas aceita valores inteiros entre 1 e 9.
    '''
    return bool(type(num) == int and 0 < num < 10)


def obter_coluna(tab, num):
    '''

    Calcula coluna de um tabuleiro.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :param num: Numero inteiro de 1 a 3,  em que o numero 1 corresponde a coluna mais a esquerda e o numero 3 corresponde a coluna mais a direita.

    '''
    if type(num) != int or num < 1 or num > 3 or not eh_tabuleiro(tab):
        raise ValueError("obter_coluna: algum dos argumentos e invalido")
    else:
        return tuple(tab[i][num - 1] for i in range(3))


def obter_linha(tab, num):
    """
    Calcula linha de um tabuleiro.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :param num: Numero inteiro de 1 a 3,  em que o numero 1 corresponde a linha superior e o numero 3 corresponde a linha inferior.

    """
    if type(num) != int or num < 1 or num > 3 or not eh_tabuleiro(tab):
        raise ValueError("obter_linha: algum dos argumentos e invalido")
    else:
        return tab[num - 1]


def obter_diagonal(tab, num):
    '''

    Calcula a diagonal de um tabuleiro

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :param num: Numero inteiro de 1 a 2, em que 1 e 2 correspondem a diagonal da esquerda para a direita, descendente e ascendente respetivamente.
    '''
    diagonal = ()
    if not eh_tabuleiro(tab) or num < 1 or num > 2 or type(num) != int:
        raise ValueError("obter_diagonal: algum dos argumentos e invalido")
    else:
        if num == 1:
            for n in range(0, 3, 1):
                diagonal += (tab[n][n],)
        if num == 2:
            for n in range(0, 3, 1):
                diagonal += (tab[num][n],)
                num -= 1
        return diagonal


def tabuleiro_str(tab):
    '''

    Determina o tabuleiro para a vista do jogador.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    '''
    if not eh_tabuleiro(tab):
        raise ValueError("tabuleiro_str: o argumento e invalido")
    else:#Cria uma lista composta por todos os elementos do tabuleiro
        res1 = tab[0]
        res = ()
        for i in tab[1]:
            res1 += (i,)
        for a in tab[2]:
            res1 += (a,)
        for b in res1:#Cria uma nova lista onde substitui os valores pelos respetivos caracteres
            if b == 1:
                b = ' X '
                res += (b,)
            if b == 0:
                b = '   '
                res += (b,)
            if b == -1:
                b = ' O '
                res += (b,)
        tabuleiro = (res[0] + '|' + res[1] + '|' + res[2] + '\n-----------\n' + res[3] + '|' + res[4] + '|' + res[
            5] + '\n-----------\n' + res[6] + '|' + res[7] + '|' + res[8])
        return tabuleiro


def eh_posicao_livre(tab, num):
    '''

    Determina se a posicao introduzida corresponde a uma posicao livre

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :param num: Posicao validada a partir da funcao eh_posicao().
    '''
    if not eh_tabuleiro(tab) or not eh_posicao(num):
        raise ValueError("eh_posicao_livre: algum dos argumentos e invalido")
    else:#Sabendo a que linha pertence, sabemos que a posicao referida em num sera respetivamente uma posicao fixa na coluna
        if 0 < num < 4:
            a = num
        if 3 < num < 7:
            a = num - 3
        if 6 < num < 10:
            a = num - 6
        coluna = obter_coluna(tab, a)
        if 0 < num < 4:
            a = coluna[0]
        elif 3 < num < 7:
            a = coluna[1]
        elif 6 < num < 10:
            a = coluna[2]
        if a == 0:#Se essa a posicao tiver o valor zero e devolvido True e vice versa
            return True
        else:
            return False


def obter_posicoes_livres(tab):
    '''

    Obtem as posicoes livres do tabuleiro

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    '''
    n = 0
    posicoes_livres = ()
    if not eh_tabuleiro(tab):
        raise ValueError("obter_posicoes_livres: o argumento e invalido")
    else:#E criado um unico tuplo, juncao dos tres tuplos de tab
        res = tab[0]
        linha2 = tab[1]
        linha3 = tab[2]
        for i in linha2:
            res += (i,)
        for a in linha3:
            res += (a,)
        while n < 9:#Percorre o tuplo, registando as posicoes em que o valor for 0
            if res[n] == 0:
                posicoes_livres += (n + 1,)
            n += 1
        return posicoes_livres


def soma_linha(tab, num):
    '''

    Calcula a soma dos valores de uma linha, sem testar a validade dos seus objetos.
    '''
    res = 0
    linha = obter_linha(tab, num)
    for i in linha:
        res += i
    return res


def soma_coluna(tab, num):
    '''

    Calcula a soma dos valores de uma coluna, sem testar a validade dos seus objetos.
    '''
    res = 0
    coluna = obter_coluna(tab, num)
    for i in coluna:
        res += i
    return res


def soma_diagonal(tab, num):
    '''
    Calcula a soma dos valores de uma diagonal, sem testar a validade dos seus objetos.
    '''
    res = 0
    diagonal = obter_diagonal(tab, num)
    for i in diagonal:
        res += i
    return res


def jogador_ganhador(tab):
    '''

    Determina qual o jogador vencedor.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :return: Devolve o valor 1 ou o valor -1, onde 1 corresponde a 'X' e -1 corresponde a 'O'.
    '''
    n = 1
    if not eh_tabuleiro(tab):
        raise ValueError("jogador_ganhador: o argumento e invalido")
    else:
        while n < 4:#Se a soma da coluna/linha/diagonal for igual a 3 ou -3, devolve 1 e -1(jogador vencedor) respetivamente
            if soma_linha(tab, n) == 3 or soma_coluna(tab, n) == 3:
                return 1
            if soma_linha(tab, n) == -3 or soma_coluna(tab, n) == -3:
                return -1
            if n < 3:
                if soma_diagonal(tab, n) == 3:
                    return 1
                if soma_diagonal(tab, n) == -3:
                    return -1
            n += 1
        return 0


def eh_jogador(n):
    '''

    Determina se o valor introduzido corresponde a um jogador valido(1 ou -1) e a um numero inteiro.
    '''
    return bool((n == 1 or n == -1) and type(n)==int )


def eh_estrategia(estrategia):
    '''

    Determina se a estrategia introduzida corresponde a uma estrategia valida (perfeito, basico ou normal).
    
    :param estrategia:
    :return:
    '''
    return bool(estrategia == 'perfeito' or estrategia == 'normal' or estrategia == 'basico')


def marcar_posicao(tab, n, num):
    '''

    Marca a posicao no tabuleiro.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :param n: Numero do jogador validado pela funcao eh_jogador().
    :param num: Posicao validada a partir da funcao eh_posicao().
    :return: Devolve o tabuleiro com a posicao introduzida.
    '''
    if not eh_tabuleiro(tab) or not eh_posicao(num) or not eh_posicao_livre(tab, num) or not eh_jogador(n):
        raise ValueError("marcar_posicao: algum dos argumentos e invalido")
    else:  # Cria uma lista com todos os valores do tabuleiro.
        res = tab[0]
        lista = []
        tab1 = ()
        tab2 = ()
        tab3 = ()
        p = 0
        for i in tab[1]:
            res += (i,)
        for a in tab[2]:
            res += (a,)
        res = list(res)
        res.pop(num - 1)  # retira o valor da posicao n-1 da lista
        while p < 8:  # # cria uma nova lista com os valores posteriores e incrementa n na posicao que foi retirada
            if num == 1:
                if n == 1:
                    lista.append(1)
                elif n == -1:
                    lista.append(-1)
                num = 0
            else:
                lista.append(res[p])
                if p == num - 2:
                    if n == 1:
                        lista.append(1)
                    elif n == -1:
                        lista.append(-1)
                p += 1
        for m in range(0, 3, 1):  # conversao para o tabuleiro original
            tab1 += (lista[m],)
        for r in range(3, 6, 1):
            tab2 += (lista[r],)
        for o in range(6, 9, 1):
            tab3 += (lista[o],)
        tab = (tab1, tab2, tab3)
        return tab


def escolher_posicao_manual(tab):
    '''

    Pede e devolve uma posicao valida ao jogador.

    :param tab: Tabuleiro validado a partir da funcao eh_tabuleiro().
    :return: Devolve a posicao escolhida pelo jogador.
    '''
    if not eh_tabuleiro(tab):
        raise ValueError("escolher_posicao_manual: o argumento e invalido")
    num = int(input("Turno do jogador. Escolha uma posicao livre: "))
    if not eh_posicao(num) or not eh_posicao_livre(tab, num):
        raise ValueError("escolher_posicao_manual: a posicao introduzida e invalida")
    else:
        return num


def escolher_posicao_auto(tab, n, estrategia):
    '''

    Funcao que analisa o tabuleiro e escolhe a posicao mais favoravel de acordo com a estrategia escolhida.

    :return: Posicao escolhida pelo programa.
    '''
    if not eh_tabuleiro(tab) or not eh_jogador(n) or not eh_estrategia(estrategia):
        raise ValueError("escolher_posicao_auto: algum dos argumentos e invalido")
    else:
        i = b = 1
        n1 = n2 = p = 0
        if n == 1 or n == -1:
            if n == 1:
                n1 = -1
                n2 = 1
                j = 1
            if n == -1:
                n1 = 1
                n2 = -1
                j = -1
            pl = obter_posicoes_livres(tab)
            if estrategia == 'perfeito' or estrategia == 'normal':
                while b < 10:  # Analisa todas as posicoes do tabuleiro, calculando a soma de cada linha e de cada coluna, atraves das respetivas funcoes
                    if 0 < b < 4:
                        a = b
                        l = 1
                    if 3 < b < 7:
                        a = b - 3
                        l = 2
                    if 6 < b < 10:
                        a = b - 6
                        l = 3
                    s_linha = soma_linha(tab, l)
                    s_coluna = soma_coluna(tab, a)
                    if s_linha == 2 * j:  # Variavel j define a prioridade de avaliacao do tabuleiro do computador e so depois do jogador
                        if eh_posicao_livre(tab, b):
                            return b
                    if s_coluna == 2 * j:
                        if eh_posicao_livre(tab, b):
                            return b
                    if soma_diagonal(tab, 1) == 2 * j:
                        if eh_posicao_livre(tab, 1):
                            return 1
                        if eh_posicao_livre(tab, 9):
                            return 9
                    if soma_diagonal(tab, 2) == 2 * j:
                        if eh_posicao_livre(tab, 7):
                            return 7
                        if eh_posicao_livre(tab, 3):
                            return 3
                    b += 1
                    if b == 10:  # Alteracao de j para analisar o tabuleiro do jogador, se nao houver situacao de vitoria para o computador
                        p += 1
                        b = 1
                        j = j * -1
                        if p == 2:
                            break
                p = 0
                j = n2
                b = 1
                tab1 = tab
                if estrategia == 'perfeito':
                    while b < 10:  # Cria um tabuleiro alternativo onde analisa a possibilidade de bifurcacao do computador devolvendo o valor(b) se for verdade
                        if 0 < b < 4:
                            a = b
                            l = 1
                        if 3 < b < 7:
                            a = b - 3
                            l = 2
                        if 6 < b < 10:
                            a = b - 6
                            l = 3
                        if eh_posicao_livre(tab, b):
                            tab1 = marcar_posicao(tab, n2, b)
                        if soma_linha(tab1, l) == soma_coluna(tab1,
                                                              a) == 2 * j:  # bifurcacao existe quando a soma de duas das tres coluna/linha/diagonal for igual a 2/-2
                            return b
                        if soma_linha(tab1, l) == soma_diagonal(tab1, 1) == 2 * j or soma_coluna(tab1,
                                                                                                 a) == soma_diagonal(
                            tab1, 1) == 2 * j or soma_linha(tab1, l) == soma_diagonal(tab1,
                                                                                      2) == 2 * j or soma_coluna(
                            tab1, a) == soma_diagonal(tab1, 2) == 2 * j or soma_diagonal(tab1, 1) == soma_diagonal(
                            tab, 2) == 2 * j:
                            return b
                        b += 1
                        tab1 = tab
                    j = n2
                    b = 1
                    p = 0
                if estrategia == 'perfeito':
                    lista = []
                    while b < 10:  # Mesmo processo anterior mas em vez de devolver qual o valor(b), procura uma forma de defender.
                        if 0 < b < 4:
                            a = b
                            l = 1
                        if 3 < b < 7:
                            a = b - 3
                            l = 2
                        if 6 < b < 10:
                            a = b - 6
                            l = 3
                        if eh_posicao_livre(tab, b):
                            tab1 = marcar_posicao(tab, n1, b)
                        if soma_linha(tab1, l) == soma_coluna(tab1, a) == 2 * n1:
                            lista.append(b)
                            i = 2
                        if soma_linha(tab1, l) == soma_diagonal(tab1, 1) == 2 * n1 or soma_coluna(tab1,
                                                                                                  a) == soma_diagonal(
                            tab1, 1) == 2 * n1 or soma_linha(tab1, l) == soma_diagonal(tab1,
                                                                                       2) == 2 * n1 or soma_coluna(
                            tab1, a) == soma_diagonal(tab1, 2) == 2 * n1 or soma_diagonal(tab1, 1) == soma_diagonal(
                            tab, 2) == 2 * n1:
                            lista.append(b)
                            i = 2
                        b += 1
                        tab1 = tab
                    tab1 = tab
                    b = 1
                    if i == 2:#Existe uma bifurcacao do adversario
                        if len(lista) > 1:#Existe mais do que uma possibilidade de bifurcacao do adversario
                            if len(lista)>2:#Se existirem mais de 2 possibilidades de bifurcacoes apenas se pode jogar nos cantos
                                g=2
                            else:
                                g=1
                            while b < 10:#Procura uma posicao, num tabuleiro alternativo (tab1), que crie um dois em linha para forcar o oponente a defender
                                if 0 < b < 4:
                                    a = b
                                    l = 1
                                if 3 < b < 7:
                                    a = b - 3
                                    l = 2
                                if 6 < b < 10:
                                    a = b - 6
                                    l = 3
                                if p == 0:
                                    if eh_posicao_livre(tab, b):
                                        tab1 = marcar_posicao(tab, n2, b)
                                        s_linha = soma_linha(tab1, l)
                                        s_coluna = soma_coluna(tab1, a)
                                    if s_linha == 2 * j:
                                        if eh_posicao_livre(tab, b):
                                            return b
                                    if s_coluna == 2 * j:
                                        if eh_posicao_livre(tab, b):
                                            return b
                                if p == 1:
                                    if soma_diagonal(tab1, 1) == 2 * j:
                                        if eh_posicao_livre(tab, 1):
                                            return 1
                                        if eh_posicao_livre(tab, 9):
                                            return 9
                                    if soma_diagonal(tab1, 2) == 2 * j:
                                        if eh_posicao_livre(tab, 7):
                                            return 7
                                        if eh_posicao_livre(tab1, 3):
                                            return 3
                                b += g
                                i = 1
                                if b == 10:
                                    b = 1
                                    p += 1
                                    if p == 2:
                                        break
                            b = 1
                        else:#Se so existe uma possibilidade de bifurcacao devolve essa posicao
                            return lista[0]
            if 5 in pl:#Se 5 for uma posicao livre
                return 5
            if estrategia == 'normal' or estrategia == 'perfeito':
                if tab[0][0] == n1 and eh_posicao_livre(tab, 9):#se o adversario tiver num canto jogar no canto oposto
                    return 9
                if tab[0][2] == n1 and eh_posicao_livre(tab, 7):
                    return 7
                if tab[2][0] == n1 and eh_posicao_livre(tab, 3):
                    return 3
                if tab[2][2] == n1 and eh_posicao_livre(tab, 1):
                    return 1
            for v in range(1, 10, 2):
                if v in pl:
                    return v
            for p in pl:
                return p


def jogo_do_galo(n, estrategia):
    '''

    E gerado um tabuleiro vazio onde vai ser jogado o jogo.

    :param n: 'X' ou 'O' que corresponde o caracter com que o jogador ira jogar. O 'X' e sempre o primeiro a comecar.
    :param estrategia: Estrategia validada pela funcao eh_estrategia().
    :return: Devolve o caracter do jogador vencedor, ou em caso de empate devolve 'EMPATE'
    '''
    jg = 0
    print("Bem-vindo ao JOGO DO GALO.\nO jogador joga com \'" + n + "\'.")
    tab = ((0, 0, 0), (0, 0, 0), (0, 0, 0))
    if n == 'O':#Define quem e o jogador 1(Jogador) e o jogador2(Computador)
        j1 = -1
        j2 = 1
        r = 1
    if n == 'X':
        j1 = 1
        j2 = -1
        r = 2
    while jg == 0:
        if r == 1:
            print('Turno do computador (' + estrategia + '):')
            num = escolher_posicao_auto(tab, j2, estrategia)
            tab = marcar_posicao(tab, j2, num)
            print(tabuleiro_str(tab))
            r = 2
        pl = obter_posicoes_livres(tab)
        if jogador_ganhador(tab) != 0 or len(pl) == 0:#Devolve o jogador ganhador ou Empate senao houverem posicoes livres
            if jogador_ganhador(tab) == 1:
                return 'X'
            if jogador_ganhador(tab) == -1:
                return 'O'
            if len(pl) == 0:
                return 'EMPATE'
            else:
                return 'EMPATE'
        if r == 2:
            num = escolher_posicao_manual(tab)
            tab = marcar_posicao(tab, j1, num)
            print(tabuleiro_str(tab))
            r = 1

        pl = obter_posicoes_livres(tab)
        if jogador_ganhador(tab) != 0 or len(pl) == 0:
            if jogador_ganhador(tab) == 1:
                return 'X'
            if jogador_ganhador(tab) == -1:
                return 'O'
            if len(pl) == 0:
                return 'EMPATE'
            else:
                return 'EMPATE'