# joao vaz 98946
def cria_posicao(c, l):
    """
    :param c: Coluna de acordo com o tabuleiro
    :param l: Linha de acordo com o tabuleiro
    :return: Devolve uma posicao
    """
    if not isinstance(c, str) or not (c == 'a' or c == 'b' or c == 'c'):
        raise ValueError('cria_posicao: argumentos invalidos')
    if int(l) <= 0 or int(l) >= 4 or not isinstance(l, str):
        raise ValueError('cria_posicao: argumentos invalidos')
    return c + l


def cria_copia_posicao(p):
    """
    :param p: Posicao
    :return: Copia de uma posicao
    """
    return cria_posicao(p[0], p[1])


def obter_pos_c(p):
    """
    :param p: Posicao
    :return: Devolve qual a coluna de uma posicao em relacao ao tabuleiro(a, b, c)
    """
    return p[0]


def obter_pos_l(p):
    """
    :param p: Posicao
    :return: Devolve qual a linha de uma posicao em relacao ao tabuleiro(1, 2, 3)
    """
    return p[1]


def eh_posicao(arg):
    """
    :param arg: Recebe qualquer tipo de argumento
    :return: Validade de uma posicao de acordo com os parametros apresentados no guiao
    """
    return isinstance(arg, str) and len(arg) == 2 and \
           isinstance(obter_pos_c(arg), str) and (obter_pos_c(arg) == 'a' or
                                                  obter_pos_c(arg) == 'b' or 'c' == obter_pos_c(arg)) \
           and isinstance(obter_pos_l(arg), str) and 48 < ord(obter_pos_l(arg)) < 52


def posicoes_iguais(p1, p2):
    """
    :param p1: Posicao 1
    :param p2: Posicao 2
    :return: Devolve se 'True' se as posicoes forem iguasis e 'False' se nao forem iguais
    """
    return bool(p1 == p2)


def posicao_para_str(p):
    """
    :param p: Posicao
    :return: Devolve a posicao em str sem validar o seu argumento
    """
    return str(p)


def obter_posicoes_adjacentes(p):
    """
    :param p: Posicao
    :return: Devolve as posicoes adjacentes a p, de acordo com a ordem e regras do tabuleiro
    """

    def eh_canto(p):  # Devolve 'True' se a posicao for um canto se 'False' se nao
        return bool(p == 'a1' or p == 'a3' or p == 'c1' or p == 'c3')

    def up(p):  # Posicao acima
        return obter_pos_c(p) + str(int(obter_pos_l(p)) - 1)

    def left(p):  # Posicao a esquerda
        return chr(ord(obter_pos_c(p)) - 1) + obter_pos_l(p)

    def right(p):  # Posicao a direita
        return chr(ord(obter_pos_c(p)) + 1) + obter_pos_l(p)

    def down(p):  # Posicao abaixo
        return obter_pos_c(p) + str(int(obter_pos_l(p)) + 1)

    def up_left(p):  # Posicao na diagonal ascendente para a esquerda
        p1 = obter_pos_c(p) + str(int(obter_pos_l(p)) - 1)
        return chr(ord(obter_pos_c(p1)) - 1) + obter_pos_l(p1)

    def up_right(p):  # Posicao na diagonal ascendente para a direita
        p1 = obter_pos_c(p) + str(int(obter_pos_l(p)) - 1)
        return chr(ord(obter_pos_c(p1)) + 1) + obter_pos_l(p1)

    def down_left(p):  # Posicao na diagonal decendente para a esquerda
        p1 = obter_pos_c(p) + str(int(obter_pos_l(p)) + 1)
        return chr(ord(obter_pos_c(p1)) - 1) + obter_pos_l(p1)

    def down_right(p):  # Posicao na diagonal descendente para a direita
        p1 = obter_pos_c(p) + str(int(obter_pos_l(p)) + 1)
        return chr(ord(obter_pos_c(p1)) + 1) + obter_pos_l(p1)

    # Configuracoes diferentes se for um canto ou nao
    lista_posicoes_canto = [up_left(p), up(p), up_right(p), left(p), right(p), down_left(p), down(p), down_right(p)]
    lista_posicoes_lado = [up(p), left(p), right(p), down(p)]
    resultado = ()
    if eh_canto(p) or p == 'b2':
        for i in lista_posicoes_canto:
            if eh_posicao(i):
                resultado += (i,)
        return resultado

    for i in lista_posicoes_lado:
        if eh_posicao(i):
            resultado += (i,)
    return resultado


def cria_peca(s):
    """
    :param s: Recebe uma str que sera um 'X' ou 'O'
    :return: Devolve uma peca no formato de '['s']'
    """
    if not isinstance(s, str) or not (s == 'X' or s == 'O' or s == ' '):
        raise ValueError('cria_peca: argumento invalido')
    return '[{}]'.format(*s)


def cria_copia_peca(j):
    """
    :param j: Peca
    :return: Cria uma copia da peca recebida
    """
    return cria_peca(j[1])


def eh_peca(arg):
    """
    :param arg: Argumento qualquer
    :return: Devolve 'True' se arg for uma peca e 'False' caso contrario
    """
    return isinstance(arg, str) and len(arg) == 3 and \
           (arg == '[X]' or arg == '[O]' or arg == '[ ]')


def pecas_iguais(j1, j2):
    """
    :param j1: Peca 1
    :param j2: Peca 2
    :return: Devolve 'True' se j1 for igual a j2 e 'False' caso contrario
    """
    return j1 == j2 and eh_peca(j1) and eh_peca(j2)


def peca_para_str(j):
    """
    :param j: Peca
    :return: String da peca recebida
    """
    return str(j)


def peca_para_inteiro(j):
    """
    :param j: Peca
    :return: 1,-1, 0 se j for igual a [X], [O], [ ] respetivamente
    """
    dic = {'[X]': 1, '[O]': -1, '[ ]': 0}
    return dic[j]


def cria_tabuleiro():
    """
    :return: Tabuleiro com todos os espacos livres
    """
    return ['[ ]', '[ ]', '[ ]'], ['[ ]', '[ ]', '[ ]'], ['[ ]', '[ ]', '[ ]']


def cria_copia_tabuleiro(t):
    """
    :param t: Tabuleiro
    :return: Copia do tabuleiro recebido
    """
    tab = cria_tabuleiro()
    for row in range(3):
        for val in range(3):
            pos = coordenadas_de_t_to_pos(row, val)
            peca = obter_peca(t, pos)
            coloca_peca(tab, peca, pos)
    return tab


def coordenadas(p):
    """
    :param p: Posicao
    :return: Coordenadas de uma certa posicao
    """
    return int(obter_pos_l(p)), ord(
        obter_pos_c(p)) - 96  # Sendo ord(a)=97,ord(b)=98 (...), ao retirar 96, obtemos 1,2,3


def coordenadas_de_t_to_pos(c, l):
    """
    :param c: Coluna
    :param l: Linha
    :return: Contrario da funcao anterior
    """
    return chr(l + 97) + str(c + 1)


def obter_peca(t, p):
    """
    :param t: Tabuleiro
    :param p: Posicao
    :return: Devolve a peca no tabuleiro, da posicao indicada(p)
    """
    return t[coordenadas(p)[0] - 1][coordenadas(p)[1] - 1]


def obter_vetor(t, s):
    """
    :param t: Tabuleiro
    :param s: str que correspondera a uma coluna ou linha
    :return: linha ou coluna se se s for (1,2,3) ou (a,b,c) respetivamente
    """
    if ord(s) < 95:
        return t[int(s) - 1]
    else:
        return tuple(t[row][ord(s) - 97] for row in range(3))


def coloca_peca(t, j, p):
    """
    :param t: Tabuleiro
    :param j: Jogador
    :param p: Posicao
    :return: Tabuleiro novo, com a peca do jogador correspondente na posicao indicada
    """
    t[coordenadas(p)[0] - 1][coordenadas(p)[1] - 1] = j
    return t


def remove_peca(t, p):
    """
    :param t: Tabuleiro
    :param p: Posicao
    :return: Tabuleiro novo, com a peca na posicao correspondente removida
    """
    t[coordenadas(p)[0] - 1][coordenadas(p)[1] - 1] = '[ ]'
    return t


def move_peca(t, p1, p2):
    """
    :param t: Tabuleiro
    :param p1: Posicao de origem
    :param p2: Posicao de chegada
    :return: Tabuleiro novo, em que a peca indicada em p1 foi movida para p2
    """
    t[coordenadas(p2)[0] - 1][coordenadas(p2)[1] - 1] = obter_peca(t, p1)
    t[coordenadas(p1)[0] - 1][coordenadas(p1)[1] - 1] = '[ ]'
    return t


def obter_ganhadores(t):
    """
    :param t: Tabuleiro
    :return: Todos os ganhadores do tabuleiro numa lista
    """
    ganhadores = []
    for peca in ('[X]', '[O]'):
        for c in range(1, 4):
            if peca == obter_vetor(t, str(c))[0] == obter_vetor(t, str(c))[1] == obter_vetor(t, str(c))[2]:
                ganhadores.append(peca)
        for l in range(97, 100):
            if peca == obter_vetor(t, chr(l))[0] == obter_vetor(t, chr(l))[1] == obter_vetor(t, chr(l))[2]:
                ganhadores.append(peca)
    return ganhadores


def eh_tabuleiro(arg):
    """
    :param arg: Argumento qualquer
    :return: Devolve 'True' se o argumneto for um tabuleiro valido e 'False' caso contrario
    """
    val_x = []  # numero de pecas do jogador 'X' no tabuleiro
    val_o = []  # numero de pecas do jogador 'O' no tabuleiro
    if type(arg) == tuple:
        for row in arg:
            for p in row:
                if p == '[X]':
                    val_x.append(p)
                if p == '[O]':
                    val_o.append(p)
    else:
        return False
    return isinstance(arg, tuple) and len(arg) == 3 and \
           all(isinstance(row, list) and len(row) == 3 for row in arg) and \
           all(type(p) == str for row in arg for p in row) and \
           all(p in ('[X]', '[O]', '[ ]') for row in arg for p in row) and \
           len(val_x) <= 3 and len(val_o) <= 3 and not (len(val_x) + 2 == len(val_o) or len(val_o) + 2 == len(val_x)) \
           and len(obter_ganhadores(arg)) <= 1


def eh_posicao_livre(t, p):
    """
    :param t: Tabuleiro
    :param p: Posicao
    :return: 'True' se a posicao for uma posicao livre e 'False' caso contrario
    """
    return bool('[ ]' == obter_peca(t, p))


def tabuleiros_iguais(t1, t2):
    """
    :param t1: Tabuleiro 1
    :param t2: Tabuleiro 2
    :return: 'True' se os tabuleiros forem iguais e 'False' caso contrario
    """
    return t1 == t2


def tabuleiro_para_str(t):
    """
    :param t: Tabuleiro
    :return: Tabuleiro no formato 'visivel' ao jogador
    """
    tabuleiro = [val for row in t for val in row]
    return '   a   b   c\n1 {}-{}-{}\n   | \ | / |\n2 {}-{}-{}\n   | / | \ |\n3 {}-{}-{}'.format(*tabuleiro)


def tuplo_para_tabuleiro(t):
    """
    :param t: Tabuleiro em forma de tuplo
    :return: Tabuleiro de acordo com a forma definida previamente
    """
    dic = {1: '[X]', -1: '[O]', 0: '[ ]'}
    tab = [dic[val] for row in t for val in row]
    a = []
    b = []
    c = []
    res = (a, b, c)
    for cont in range(9):
        if cont <= 2:
            a.append(tab[cont])
        elif 2 < cont <= 5:
            b.append(tab[cont])
        else:
            c.append(tab[cont])
    return res


def obter_ganhador(t):
    """
    :param t: Tabuleiro
    :return: Devolve a peca do jogador ganhador
    """
    for peca in ('[X]', '[O]'):
        for c in range(1, 4):
            if peca == obter_vetor(t, str(c))[0] == obter_vetor(t, str(c))[1] == obter_vetor(t, str(c))[2]:
                return peca
        for l in range(97, 100):
            if peca == obter_vetor(t, chr(l))[0] == obter_vetor(t, chr(l))[1] == obter_vetor(t, chr(l))[2]:
                return peca
    return '[ ]'


def obter_posicoes_livres(t):
    """
    :param t: Tabuleiro
    :return: Todas as posicoes livres do tabuleiro
    """
    n = 0
    res = ()
    while n < 3:
        for a in range(3):
            if t[n][a] == '[ ]':
                res += (coordenadas_de_t_to_pos(n, a),)
        n += 1
    return res


def obter_posicoes_jogador(t, j):
    """
    :param t: Tabuleiro
    :param j: Jogador
    :return: Todas as posicoes do jogador no tabuleiro
    """
    n = 0
    res = ()
    while n < 3:
        for a in range(3):
            if t[n][a] == j:
                res += (coordenadas_de_t_to_pos(n, a),)
        n += 1
    return res


def obter_movimento_manual(t, j):
    """
    Funcao que pede uma posicao ou movimento ao jogador 'humano' de acordo com o estado do tabuleiro, levantando
    erro se a posicao ou movimento for invalida, estiver ocupada ou nao for possivel.

    :param t: Tabuleiro
    :param j: Jogador
    :return: Tuplo com a posicao ou movimento escolhido
    """
    if numero_pecas_tabuleiro(t) < 6:
        m = input('Turno do jogador. Escolha uma posicao: ')
    else:
        m = input('Turno do jogador. Escolha um movimento: ')
    res = ()
    lista_de_pecas = []
    for peca_jogador in obter_posicoes_jogador(t, j):
        for peca_adjacente in obter_posicoes_adjacentes(peca_jogador):
            if peca_adjacente in obter_posicoes_livres(t):
                lista_de_pecas.append(peca_adjacente)

    if len(m) == 4:
        res = (m[0] + m[1], m[2] + m[3])
        if len(lista_de_pecas) == 0:
            return res
        if numero_pecas_tabuleiro(t) < 6 or res[1] not in obter_posicoes_livres(t) or res[
            1] not in obter_posicoes_adjacentes(res[0]) or not \
                eh_posicao(res[0]) or not eh_posicao(res[1]) or res[0] not in obter_posicoes_jogador(t, j) or (
                numero_pecas_tabuleiro(t) >= 6 and len(m) != 4):
            raise ValueError('obter_movimento_manual: escolha invalida')
        else:
            return res
    if (len(m) == 2 and numero_pecas_tabuleiro(t) < 6) and str(m) in obter_posicoes_livres(t) and \
            eh_posicao(str(m)):
        return m,
    if (len(m) != 2 and numero_pecas_tabuleiro(t) < 6) or not eh_posicao(m) or m not in obter_posicoes_livres(t) or (
            len(m) == 2 and numero_pecas_tabuleiro(t) >= 6):
        raise ValueError('obter_movimento_manual: escolha invalida')


def alterna_peca(j):
    """
    :param j: Jogador
    :return: Peca contraria a inserida
    """
    return '[X]' if j == '[O]' else '[O]'


def alterna_jogador(j):
    """
    :param j: Jogador
    :return: Jogador contrario ao inserido
    """
    return 'X' if j == 'O' else 'O'


def atualiza_seq_movimentos(sm, jog):
    """
    Funcao que atualiza uma sequencia de movimentos

    :param sm: Sequencia de movimentos que ira ser atualizada
    :param jog: Posicao/posicoes que atualizarao a sm
    :return: sequencia de movimentos atualizada
    """
    return sm + (jog[0], jog[1],)


def minimax(t, j, profundidade, seq_movimentos):
    if pecas_iguais(obter_ganhador(t), j) or profundidade == 0: #Se existir ganhador ou a profundidade for zero, devolve o valor do tabuleiro e a sequencia de movimentos
        return peca_para_inteiro(obter_ganhador(t)), seq_movimentos
    melhor_resultado = peca_para_inteiro(alterna_peca(j))
    melhor_seq_movimentos = ()
    for peca_jogador in obter_posicoes_jogador(t, j):   #Para cada peca do jogador
        for peca_adjacente in obter_posicoes_adjacentes(peca_jogador):  #Para cada peca adjacente do jogador
            if peca_adjacente in obter_posicoes_livres(t):  #Se for peca livre
                tab = cria_copia_tabuleiro(t)   #cria copia do tabuleiro
                novo_resultado, nova_seq_movimentos = minimax(move_peca(tab, peca_jogador, peca_adjacente),
                                                              alterna_peca(j),
                                                              profundidade - 1, atualiza_seq_movimentos(seq_movimentos,
                                                                                                        (peca_jogador,
                                                                                                         peca_adjacente))) #Chama a funcao minimax recursivamente com novos parametros
                if not melhor_seq_movimentos or (
                        pecas_iguais(cria_peca('X'), j) and novo_resultado > melhor_resultado) or \
                        (pecas_iguais(cria_peca('O'), j) and novo_resultado < melhor_resultado):
                    melhor_resultado, melhor_seq_movimentos = novo_resultado, nova_seq_movimentos

    return melhor_resultado, melhor_seq_movimentos


def numero_pecas_tabuleiro(t):
    """
    :param t: Tabuleiro
    :return: Numero de pecas de um tabuleiro
    """
    res = 0
    for row in t:
        for val in row:
            if val == '[X]' or val == '[O]':
                res += 1
    return res


def obter_movimento_auto(t, j, s):
    """
    Funcao responsavel pelas jogadas do 'computador'

    :param t: Tabuleiro
    :param j: Jogador
    :param s: dificuldade
    :return: Devolve uma certa posicao ou movimento de acordo com o tabuleiro e a dificuldade
    """

    def ganhar_defender(t, j):
        """
        Funcao que devolve a posicao a ocupar para ganhar ou perder,dependendo do jogador introduzido
        """
        res = []
        livres = obter_posicoes_livres(t)
        for pos in livres:
            if obter_ganhador(coloca_peca(t, j, pos)) == j:
                res.append(pos)
            remove_peca(t, pos)
        return res

    def jogar(t):
        """
        Funcao responsavel por escolher uma peca do tuplo pela ordem que aparece, cumprindo assim os requisitos do guiao
        """
        for val in ('b2', 'a1', 'c1', 'a3', 'c3', 'b1', 'a2', 'c2', 'b3'):
            if val in obter_posicoes_livres(t):
                return val,

    def facil(t, j):
        """
        Funcao que percorre as posicoes das pecas do 'computador'

        :return: Primeiro movimento legal do computador
        """
        res = ()
        for val in obter_posicoes_jogador(t, j):
            for x in obter_posicoes_adjacentes(val):
                if x in obter_posicoes_livres(t):
                    res += (val,)
                    res += (x,)
                    return res

    def lista_de_jogadas(t, j):
        """
        Funcao com o objetivo de analisar o numero de jogadas possiveis do computador

        :return: Devolve uma lista com as jogadas possiveis de executar por parte do computador
        """
        lista_de_pecas = []
        for peca_jogador in obter_posicoes_jogador(t, j):
            for peca_adjacente in obter_posicoes_adjacentes(peca_jogador):
                if peca_adjacente in obter_posicoes_livres(t):
                    lista_de_pecas.append(peca_adjacente)
        return lista_de_pecas

    if numero_pecas_tabuleiro(t) < 6:
        if ganhar_defender(t, j):
            return ganhar_defender(t, j)[0],

        elif ganhar_defender(t, alterna_peca(j)):
            return ganhar_defender(t, alterna_peca(j))[0],

        return jogar(t)

    if len(lista_de_jogadas(t, j)) == 0:
        return obter_posicoes_jogador(t, j)[0], obter_posicoes_jogador(t, j)[0]

    else:
        if s == 'facil':
            return facil(t, j)
        if s == 'normal':
            profundidade = 1
        if s=='dificil':
            profundidade = 5
            res=minimax(t, j, profundidade, ())[1]
            return res[0],res[1]

        return minimax(t, j, profundidade, ())[1]


def moinho(s1, s2):
    """
    :param s1: Jogador
    :param s2: Dificuldade

    Funcao principal que permite jogar ao jogo do Moinho contra o computador.
    Recebe duas cadeias de caracteres e devolve o identificador do jogador ganhador ('[X]' ou '[O]').
    Em caso de empate, devolve '[ ]'
    O primeiro argumento corresponde a peca que o jogador ira jogar (’[X]’ ou ’[O]’) q,
    e o segundo argumento selecciona a dificuldade de jogo utilizada pelo computador. Se algum dos argumentos forem
    invalidos, e levantado um erro.
    """
    humano = s1
    if s1 in ('[X]', '[O]') and s2 in ('facil', 'normal', 'dificil'):

        print('Bem-vindo ao JOGO DO MOINHO. Nivel de dificuldade ' + s2 + '.')
        t = cria_tabuleiro()
        print(tabuleiro_para_str(t))
        if s1 == '[X]':
            jogador = 1
        else:
            jogador = -1
        while obter_ganhador(t) == '[ ]':
            if jogador == 1:
                s1 = humano
                pos = obter_movimento_manual(t, s1)
            else:
                print('Turno do computador (' + s2 + '):')
                pos = obter_movimento_auto(t, alterna_peca(s1), s2)
                s1 = alterna_peca(humano)

            if numero_pecas_tabuleiro(t) < 6:
                t = coloca_peca(t, s1, pos[0])
            else:
                t = move_peca(t, pos[0], pos[1])

            print(tabuleiro_para_str(t))
            jogador = -jogador

        return obter_ganhador(t)

    else:
        raise ValueError('moinho: argumentos invalidos')



