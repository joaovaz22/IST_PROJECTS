;Tomáa da Cunha – nº96773
;Ines Cardeira – nº98941
;Joao Vaz – nº98946
;grupo 38

MEMORIA_ECRA   EQU 8000H	  ; endereço base da memória do ecrã
APAGA_ECRAS    EQU 6002H      ; endereço do comando para apagar todos os pixels de todos os ecrãs
APAGA_AVISO    EQU 6040H      ; endereço do comando para apagar o aviso de nenhum cenário selecionado
LINHA          EQU 2          ; linha
POUT1  EQU 0A000H  			  ; endereço do periférico de saída de 16 bits
POUT2  EQU 0C000H  			  ; endereço do periférico de saída de 8 bits
PIN    EQU 0E000H  			  ; endereço do periférico de entrada de 8 bits

;Cores dos pixels
COR_NAVE       EQU 0FFAFH     ; cor do pixel para pintar a nave: rosa em ARGB 
COR_APAGAR     EQU 0H         ; cor para apagar o pixel (verde, vermelho e azul a 0)
COR_MISSIL     EQU 0FF0FH     ; cor do pixel para pintar o missil em ARBG
VERDE          EQU 0F0F0H     ; cor do pixel para pintar o asteróide
VERMELHO       EQU 0FF00H     ; cor do pixel para pintar as naves inimigas
CINZENTO       EQU 0FFFFH     
AZUL_COLISAO   EQU 0F0FFH     ; cor do pixel para o efeito da colisao do missil com os ovnis

;Desenho da nave 
COLUNA_MAXIMA  EQU 60         ; limite para escrever a nave
COLUNA_MINIMA  EQU  0		  ; limite para escrever a nave
POSICAO_X_NAVE_INICIAL EQU 27         ; linha de referencia para desenhar a nave na sua posição inicial
POSICAO_Y_NAVE_INICIAL EQU 30		  ; coluna de referencia para desenhar a nave na sua posição inicial

;Teclas responsáveis pelo controlo do jogo
TECLA_DIREITA  EQU 00002H     ; tecla responsável por mover a nave para a direita (Tecla 2)
TECLA_ESQUERDA EQU 00000H     ; tecla responsável por mover a nave para a esquerda (Tecla 0)
TECLA_MISSIL   EQU 00001H     ; tecla responsável por ativar o missil (Tecla 1)
TECLA_COMECAR  EQU 0000CH     ; tecla responsável por iniciar o jogo (Tecla C)
TECLA_PAUSA    EQU 0000DH     ; tecla responsável por pausar o jogo (Tecla D)
TECLA_TERMINAR EQU 0000EH     ; tecla responsável por terminar o jogo (Tecla E)

;Comandos de escrita do MediaCenter
ECRA      	     EQU 6004H      ;seleciona o ecrã especificado 
DEFINE_LINHA     EQU 600AH      ; endereço do comando para definir a linha
DEFINE_COLUNA    EQU 600CH      ; endereço do comando para definir a coluna
ESCREVE_8_PIXELS EQU 6012H      ; endereço do comando para escrever 8 pixels
IMAGEM_FUNDO     EQU 06042H     ; seleciona o n.º do cenário de fundo a visualizar
ESCOLHER_SOM     EQU 06048H     ; seleciona um som/vídeo para comandos seguintes
REP_SOM		     EQU 0605AH     ; inicia a reprodução do som/vídeo especificado
SOM_MISSIL       EQU 0          ; som ativado quando o missil é disparado
SOM_MORTE_OVNI   EQU 1          ; som ativado quando o missil acerta na nave inimiga
SOM_GAMEOVER     EQU 2          ; som ativado quando se perde o jogo
SOM_ASTEROIDE    EQU 3          ; som atiado quando se minera um asteróide 
;------------------------------------------------------------------------------------------------------
N_LINHAS               EQU 28         ; número de linhas do ecrã
DOIS                   EQU 2
NOVE                   EQU 9
DEZ                    EQU 10
DOZE                   EQU 12
POSICAO_INICIAL        EQU 30         ; 
POSICAO_MAX_MISSIL     EQU 12         ; alcance máximo do missil
DELAY 				   EQU 10000      ; delay para abrandar o movimento da nave

; Transformação de um valor hexadecimal para um valor decimal
FATOR                  EQU 01000      ; utilizado para transformar um número hexadecimal em decimal
DIVISAO                EQU 10         ; utilizado para transformar um número hexadecimal em decimal 

;Display
VALOR_ENERGIA_INICIAL  EQU 0100H      ; valor da energia inicial (100%)
energia_inicial        EQU 100        ; valor da energia inicial (100%)

PLACE     1000H
pilha:    TABLE 100H          ; espaço reservado para a pilha 
                              ; (200H bytes, pois são 100H words)
SP_inicial:                   ; este é o endereço (1200H) com que o SP deve ser 
                              ; inicializado. O 1.º end. de retorno será 
                              ; armazenado em 11FEH (1200H-2)

LINHA1  EQU 00010H  ; primeira linha a ser testada

;--------------------------------------------------------------------------------
; Tabelas das rotinas de interrupção
tab:      WORD rot_int_0      ; rotina de atendimento da interrupção 0
          WORD rot_int_1      ; rotina de atendimento da interrupção 1
          WORD rot_int_2      ; rotina de atendimento da interrupção 2
evento_int:
          WORD 0              ; se 1, indica que a interrupção 0 ocorreu
          WORD 0              ; se 1, indica que a interrupção 1 ocorreu
          WORD 0              ; se 1, indica que a interrupção 2 ocorreu
		  
jogo_comecado: WORD 0         ; se 0, indica que o jogo ainda não começou e se for 1 o jogo está em andamento

cor_ovni: WORD 0              ; representa a cor do ovni 
posicao_x_ovni:
          WORD 0              ; linha em que o ovni está

posicao_y_ovni:
          WORD POSICAO_INICIAL ;posicao localizada a meio do ecra que evolui de acordo com a trajetoria

posicao_coluna_missil:         ; coluna de referencia para o desenho do missil 
		WORD 0           

posicao_linha_missil:          ; linha de referencia para o desenho do missil
		WORD 0
		
posicao_max_missil:            ; utilizado para quando existe a colisao de um missil num ovni, para que o missil então desapareça
		WORD 0
forma:                         ; forma que o objeto ira adquirir
        WORD 0

ENERGIA: WORD 100              ; valor da energia que aparece representada nos displays

gerador_valor:                 ; valor do gerador (random)
          WORD 0  
		  
teclado_valor:                 ; funciona como porta para determinadas funcoes
		WORD 0
		
trajetoria:                    ; valor da trajetoria que o objeto adquire
        WORD 0
		
controlador:                   ; funciona como porta para determinadas funcoes
		WORD 0
		
tipo:                          ; tipo de forma que o objeto adquire(ovni ou asteroide)
        WORD 0
		
variavel_missil:               ; o valor é 1 se o missil estiver ativo e 0 caso contrário
		WORD 0

variavel_game_over:
		WORD 0		
;Lista de figuras do programa, a primeira string representa a altura e comprimento da figura e as
; restantes representam a figura, 1 representa um pixel que tem que ser pintado

nave:STRING 5,5
	 STRING 0,0,1,0,0
	 STRING 0,1,1,1,0
	 STRING 1,1,1,1,1
	 STRING 0,0,1,0,0
	 STRING 0,1,0,1,0

primeira_forma:   
        STRING 1,1
        STRING 1

segunda_forma:   
        STRING 2,2
        STRING 1,1
        STRING 1,1

ovni1:   STRING 3,3
        STRING 1,1,1
        STRING 0,1,0
        STRING 1,0,1 

ovni2:   STRING 4,4
        STRING 0,1,1,0
        STRING 1,0,0,1
        STRING 0,1,1,0
        STRING 1,0,0,1
 

ovni3:  STRING 5,5
        STRING 0,1,1,1,0
        STRING 1,0,0,0,1
        STRING 0,1,1,1,0
        STRING 0,1,1,1,0
        STRING 1,0,1,0,1 

vida1: 
        STRING 5,5
        STRING 0,1,0,0,0
        STRING 1,1,1,0,0
        STRING 0,1,0,0,0
        STRING 0,0,0,0,0
        STRING 0,0,0,0,0   

vida2:  
        STRING 5,5
        STRING 0,1,1,0,0
        STRING 1,1,1,1,0
        STRING 1,1,1,1,0
        STRING 0,1,1,0,0
        STRING 0,0,0,0,0   

vida3:
        STRING 5,5
        STRING 0,1,1,1,0
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1
        STRING 0,1,1,1,0   


colisao:	   
        STRING 5,5
        STRING 0,1,0,1,0
        STRING 1,0,1,0,1
        STRING 0,1,0,1,0
        STRING 1,0,1,0,1
        STRING 0,1,0,1,0   

apagar:
        STRING 5,6
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1
        STRING 1,1,1,1,1   
        STRING 1,1,1,1,1 
		

posicao_nave: STRING POSICAO_X_NAVE_INICIAL,POSICAO_Y_NAVE_INICIAL
	   
posicao_missil: STRING 0,0

PLACE 0H
;inicializações
	MOV  BTE, tab            ; inicializa BTE (registo de Base da Tabela de Exce��es)
	MOV  SP, SP_inicial      ; inicializa SP para a palavra a seguir
                             ; à última da pilha
	MOV  R0, APAGA_ECRAS
    MOV  [R0], R1            ; apaga todos os pixels de todos os ecrãs (o valor de R1 não é relevante)
     
    MOV  R0, APAGA_AVISO
    MOV  [R0], R1            ; apaga o aviso de nenhum cenário selecionado (o valor de R1 não é relevante)
	MOV R0, IMAGEM_FUNDO
	MOV R1, 0
	MOV [R0], R1             ; ativa a imagem de fundo
	MOV R0,0
	MOV R0,posicao_y_ovni
	MOV R1,POSICAO_INICIAL
	MOV [R0], R1
	MOV R4, POUT1                    ;endereço do periférico dos displays
    MOV R5, VALOR_ENERGIA_INICIAL    ;valor inicial dos displays (100H)
    MOV [R4],R5                      ;coloca o valor inicial nos displays
	MOV R0,controlador                 ;
	MOV R1,1
	MOV [R0],R1
	EI0                         ; permite interrupções 0 
	EI1                         ; permite interrupções 1 
	EI2                         ; permite interrupções 2
    EI                          ; permite interrupções (geral)
	CALL escrever_nave          ; escreve a nave na sua posição inicial
; editar mesmo fim para fazer call ao ciclo
ciclo_teclado:
	MOV R0,teclado_valor        ;
	MOV R10,1
	MOV [R0],R10
	CALL gerador     ; escreve o ovni
	CALL teclado                ; chama o teclado para perceber se alguma tecla foi carregada
	MOV R0,teclado_valor
	MOV R10,0
	MOV [R0],R10
	MOV R0,controlador            ;
	MOV R11,[R0]
	CMP R11,0
	JNZ ciclo_teclado
ciclo_ovnis:
	CALL display_inicio         ; reponsável por decrementar o contador dos displays de acordo com a rotina de interrupção
	CALL verificar_display      ; verifica se o display já chegou a zero
	CALL gerador     ; responsável pelo movimento dos ovnis
	CALL ovnis                  ;
	CALL desenhar_figuras    ; respo por desenhar as figuras
	JMP ciclo_teclado

	
;corpo do teclado
; **********************************************************************
; TECLADO - Processo que deteta se uma tecla foi pressionada e reage de acordo com essa tecla 
;
; Devolve: Reage consoante a tecla
; **********************************************************************	
teclado:
	PUSH R0
	PUSH R1
	PUSH R2
	PUSH R3
	PUSH R4
	PUSH R5
    PUSH R6
    PUSH R7
	PUSH R8
	PUSH R9
    PUSH R10
	PUSH R11
    MOV R5,ECRA
	MOV R6,1
	MOV [R5],R6    ;seleciona o ecrã 1
	MOV R2, POUT2  ;endereço do periférico das linhas
	MOV R3, PIN    ;endereço do periférico das colunas
	MOV R1,0
	MOV R1, LINHA1 ;primeira linha a ser testada
	MOV R5, 0
	MOV R7,5
	ADD R7,5       ; R7 fica assim com o valor de 10
	MOV R8,2    ;R8 fica com o valor de 2 para 
	MOV R9, 0 
	MOV R10, 0
	
espera_tecla:
	SHR R1, 1
	ADD R9,1
	MOVB [R2], R1      ; escrever no periférico de saída (linhas)
    MOVB R0, [R3]      ; ler do periférico de entrada (colunas)
	CMP  R9,5
	JZ   sai_teclado
    CMP  R0, 0         ; há tecla premida?
    JZ   espera_tecla  ; se nenhuma tecla premida, repete
	MOV R9,0
	SHL  R1, 4         ; coloca linha no nibble high
    OR   R1, R0        ; junta coluna (nibble low)
	MOV  R5, R0
	MOV  R6, R1
	SUB  R6, R0
	DIV  R6, R7

numero_coluna:         ; encontra o valor da coluna correspondente a um valor entre 0 e 3 (que fica em R9)
	CMP  R5,1          ; quando R5 for igual a 1 passa a numero_linha
	JZ   numero_linha
	DIV  R5,R8         ; divide o valor de R5 por 2
	ADD  R9,1          ; adiciona 1 ao valor do numero da coluna
	JMP  numero_coluna

numero_linha:          ; encontra o valor da linha correspondente a um valor entre 0 e 3 (que fica em R10)
	CMP  R6,1          ; quando R5 for igual a 1 passa a encontra_tecla
	JZ   encontra_tecla
	DIV  R6,R8         ; divide o valor de R6 por 2
	ADD  R10,1         ; adiciona 1 ao valor do numero da linha
	JMP  numero_linha
	
encontra_tecla:
	MUL R10,R8
	MUL  R10, R8       ; 4 * linha
	MOV  R5, 0 
	MOV  R5, R10       
	ADD  R5, R9        ; 4 * linha + coluna
	
fim_encontra_tecla:
	MOV R0,controlador
	MOV R11,[R0]
	CMP R11,0
	JZ reagir_tecla          ; verifica se alguma das teclas de controlo do jogo foi carregada
	MOV R0,teclado_valor
	MOV R10,[R0]
	CMP R10,1
	JZ controlo              ; verifica se alguma das teclas de controlo do jogo foi carregada

	
ha_tecla:
	MOV R2, POUT2      ; endereço do periférico das linhas
	MOV R3, PIN        ; endereço do periférico das colunas
	MOV  R1, LINHA     ; testar a linha 4  (R1 tinha sido alterado)
    MOVB [R2], R1      ; escrever no periférico de saída (linhas)
    MOVB R0, [R3]      ; ler do periférico de entrada (colunas)
    CMP  R0, 0         ; há tecla premida?
    JNZ  ha_tecla      ; se ainda houver uma tecla premida, espera até não haver
    JMP  sai_teclado   ; repete ciclo
	
sai_teclado:
	POP R11
	POP R10
	POP R9
	POP R8
	POP R7
	POP R6
	POP R5
	POP R4
	POP R3
	POP R2
	POP R1
	POP R0
	RET
; **********************************************************************
; ROT_INT_0 - Rotina de atendimento da interrupção 0
;             A animação do movimento do ovni é provocada pela
;             invocação periódica desta rotina
; **********************************************************************
	
rot_int_0:
     PUSH R0
     PUSH R1
     MOV  R0, evento_int
     MOV  R1, 1               ; assinala que houve uma interrupção 0
     MOV  [R0], R1            ; na componente 0 da variável evento_int
     POP  R1
     POP  R0
     RFE
	 
; **********************************************************************
; ROT_INT_1 - Rotina de atendimento da interrupção 1
;             Faz o missil subir uma linha. A animação do movimento do missil é provocada pela
;             invocação periódica desta rotina
; **********************************************************************	
rot_int_1:
     PUSH R0
     PUSH R1
     MOV  R0, evento_int
     MOV  R1, 1               ; assinala que houve uma interrupção 0
     MOV  [R0+2], R1          ; na componente 1 da variável evento_int
                              ; Usa-se 2 porque cada word tem 2 bytes
     POP  R1
     POP  R0
     RFE
	 
; **********************************************************************
; ROT_INT_2 - Rotina de atendimento da interrupção 2
;             A decrementação de 5% de energia é provocada pela
;             invocação periódica desta rotina
; **********************************************************************	
 
rot_int_2:
    PUSH R0
    PUSH R1
    MOV  R0, evento_int
    MOV  R1, 1               ; assinala que houve uma interrupção 0
    MOV  [R0+4], R1          ; na componente 2 da variável evento_int
                              ; Usa-se 4 porque cada word tem 2 bytes
    POP  R1
    POP  R0
    RFE
; **********************************************************************
; CONTROLO - Lê uma variável, correspondente à tecla carregada e decide
;          	 se deve reagir ou não a essa tecla. É responsável pelas 
;            teclas de começar(C), pausa(D) e terminar(E).
;
; Argumentos: R5 - valor da tecla carregada
; **********************************************************************
controlo:
	MOV R4, TECLA_COMECAR 
	CMP R5, R4              
	JZ tecla_comecar              ; verifica se a tecla C  foi carregada(responsável por começar o jogo)
	MOV R4, jogo_comecado
	MOV R10,[R4]
	MOV R4, 0
	CMP R10,R4                    ; verifica se o jogo se encontra em andamento
	JZ sai_teclado                ; caso o jogo ainda não tenha começado sai até que a tecla C seja carregada
	MOV R4,TECLA_PAUSA
	CMP R5,R4
	JZ tecla_pausa                ; verifica se a tecla D foi carregada(responsável por dar pausa ao jogo)
	JMP sai_teclado
	
; **********************************************************************
; REAGIR_TECLA - Lê uma variável, correspondente à tecla carregada e decide
;          		 se deve reagir ou não a essa tecla.
;
; Argumentos: R5 - valor da tecla carregada
; **********************************************************************	
reagir_tecla:
	MOV R4,TECLA_PAUSA
	CMP R5,R4
	JZ tecla_pausa                ; verifica se a tecla D foi carregada (responsável por dar pausa ao jogo)
	MOV R4, TECLA_DIREITA
	CMP  R5,R4
	JZ movimento_direita_nave     ; verifica se a tecla 3 foi carregada (movimenta a nave para a direita)
	MOV R4, TECLA_ESQUERDA
	CMP R5,R4
	JZ movimento_esquerda_nave    ; verifica se a tecla 0 foi carregada (movimenta a nave para a esquerda)
	MOV R4,TECLA_TERMINAR
	CMP R5,R4
	JZ tecla_terminar             ; verifica se a tecla E foi carregada (responsável por terminar o jogo)
	
tecla_missil:
	MOV R1,variavel_missil
	MOV R2,[R1]
	CMP R2,0					  ; verifica se já existe um missil acionado
	JNZ sai_reage_tecla           ; em caso afirmativo sai da rotina
	MOV R4, TECLA_MISSIL
	CMP R5,R4
	JZ escrever_missil1           ; verifica se a tecla 1 foi pressionada (aciona o missil)

tecla_comecar:
	MOV R0, jogo_comecado
	MOV R1,1
	MOV [R0],R1                   ; coloca em jogo_comecado a variável 1 indicando que o jogo está em andamento
	MOV R0,controlador
	MOV R1,0
	MOV [R0],R1                   ; coloca em controlo a variável 0
	MOV R0, IMAGEM_FUNDO
	MOV R2,DOIS
	MOV [R0],R2                   ; atualiza a imagem de fundo
	JMP sai_teclado

tecla_pausa:
	MOV R0,controlador
	MOV R1,[R0]
	CMP R1,0
	JZ pausa                      ; se o controlo for 0 coloca o jogo em pausa
	CALL esfera_tempo
	MOV R0, IMAGEM_FUNDO
	MOV R2,DOIS
	MOV [R0],R2					  ; atualiza a imagem de fundo
	MOV R0,controlador
	MOV R1,[R0]
	MOV R1,0
	MOV [R0],R1                   ; coloca o controlo a 0, significando que o jogo recomeçou 
	JMP ha_tecla
pausa:
	MOV R1,1
	MOV [R0],R1
	MOV R0, IMAGEM_FUNDO          
	MOV R2,3
	MOV [R0],R2                   ; atualiza a imagem de fundo
	CALL esfera_tempo
	JMP ha_tecla

tecla_terminar:
	MOV R0,jogo_comecado
	MOV R1,0
	MOV [R0],R1                  ; coloca em jogo_comecado a variável 0 
	CALL terminar
	JMP sai_teclado

sai_reage_tecla:
	JMP sai_teclado

; **********************************************************************
; ESCREVE_NAVE - Rotina chamada quando se quer desenhar a nave
; Devolve: R0 - cor com que vão ser pintados os pixeis (em formato ARGB de 16 bits)
;          R4 - tabela correspondente à dimensao e posicoes da nave
;          R5 - define que se vai escrver uma figura
;		   R6 - define a linha do pixel de referencia
;		   R7 - define a coluna do pixel de referencia
; **********************************************************************	

escrever_nave:
	PUSH R0
	PUSH R4
	PUSH R5
	PUSH R6
	PUSH R7
	MOV R5,ECRA
	MOV R6,1
	MOV [R5],R6
	MOV R5, 1                 ; 1 para escrever uma figura, 0 é para apagar
	MOV R4, posicao_nave
	MOVB R6, [R4] 			  ;linha do pixel de referencia
	ADD  R4,  1
	MOVB R7, [R4]             ;coluna do pixel de referencia
	MOV  R4, nave
	MOV R0, COR_NAVE          ; coloca em R0 a cor com que vai ser pintada a nave
	CALL escrever_figura      ; vai escrever a nave
	POP R7
	POP R6
	POP R5
	POP R4
	POP R0
	RET
escrever_missil1:
	JMP escrever_missil
	
; **********************************************************************
; APAGAR_FIGURA - Rotina chamada quando se quer apagar alguma figura
; Devolve: R0 - cor com que vão ser pintados os pixeis (em formato ARGB de 16 bits)
;          R4 - tabela correspondente à dimensao e posicoes da figura
;		   R6 - define a linha do pixel de referencia
;		   R7 - define a coluna do pixel de referencia
; **********************************************************************
apagar_figura:
	PUSH R0
	PUSH R4
	PUSH R5
	PUSH R6
	PUSH R7
	MOV R5,0                 ; indica que se vai apagar uma figura
	CALL escrever_figura
	POP R7
	POP R6
	POP R5
	POP R4
	POP R0
	RET

movimento_direita_nave:      ; movimenta a nave para a direita 
	MOV R4,posicao_nave
	ADD R4,1
	MOVB R1, [R4] 
	ADD R1,1                ; adiciona 1 ao valor da coluna do pixel de referencia
	MOV R2, COLUNA_MAXIMA
	CMP R1,R2               ; verifica se já chegou à coluna máxima
	JZ fim_movimento
	MOV R1,0
	MOV R2,0
	MOV R4, posicao_nave
	MOVB R6, [R4] 			; linha do pixel de referencia
	ADD  R4,  1
	MOVB R7, [R4]           ; coluna do pixel de referencia
	MOV  R4, nave
	CALL apagar_figura      ; apaga a nave na sua posicao atual
	MOV R4,posicao_nave
	ADD R4,1
	MOVB R1, [R4]
	ADD R1,1
	MOVB [R4],R1            ; adiciona 1 à coluna da posição da nave
	MOV R1,0
	CALL escrever_nave      ; escreve a nave na sua nova posição (move para a próxima posição à direita)
	JMP fim_movimento
	
movimento_esquerda_nave:    ; movimenta a nave para a esquerda
	MOV R4,posicao_nave
	ADD R4,1
	MOVB R1, [R4]
	SUB R1,1                ; decrementa 1 ao valor da coluna do pixel de referencia
	MOV R2, COLUNA_MINIMA   
	SUB R2,1
	CMP R1,R2               ; verifica se já chegou à coluna mínima
	JZ fim_movimento
	MOV R1,0
	MOV R2,0
	MOV R5, 1               ; 1 para escrever uma figura, 0 é para apagar
	MOV R4, posicao_nave
	MOVB R6, [R4] 			;linha do pixel de referencia
	ADD  R4,  1
	MOVB R7, [R4]           ;coluna do pixel de referencia
	MOV  R4, nave
	CALL apagar_figura      ; apaga a nave na sua posicao atual
	MOV R4,posicao_nave
	ADD R4,1
	MOVB R1, [R4]
	SUB R1,1
	MOVB [R4],R1
	MOV R1,0
	CALL escrever_nave      ; escreve a nave na sua nova posição (move para a próxima posição à esquerda)
	JMP fim_movimento
	
fim_movimento:
	MOV R9,DELAY
	CALL esfera_tempo1      ; abranda o movimento da nave
	JMP sai_teclado

escrever_missil:            ; escreve o missil
	CALL missil             ; decrementa 5% da energia nos displays
	MOV R0, REP_SOM         
	MOV R1, SOM_MISSIL
	MOV [R0], R1 			; ativa o som de efeito do disparo do missil
	MOV R1,variavel_missil
	MOV R2,1
	MOV [R1],R2             ; indica que o missil se encontra ativo (o valor de variavel_missil é 1)
	MOV R5,ECRA
	MOV R6,1
	MOV [R5],R6             ; atualiza o ecrã
	MOV R5, 1               ; 1 para escrever uma figura, 0 é para apagar
	MOV R11,0
	MOV R4, posicao_nave
	MOVB R2, [R4]
	MOV R6,posicao_linha_missil
	SUB R2,1
	MOV [R6], R2
	ADD R4,1
	MOVB R3, [R4]
	ADD R3,2		             ;para obter a posicao inicial das coluna do missil
	MOV R6, posicao_coluna_missil
	MOV [R6],R3
escrever_missil_continuacao:
	CALL teclado
	CALL ovnis
	CALL display_inicio
	CALL gerador
	MOV R11, cor_ovni
	MOV R1,[R11]	        ; coloca no R1 a cor do missil
	CALL desenhar_figuras
    
    MOV  R6, 1             ; cópia de R3 (para não destruir R3)
    SHL  R6, 1               ; multiplica a coluna por 2 porque a posicao_x_ovni e o evento_int são tabelas de words
    MOV  R5, evento_int
    MOV  R4, [R5+R6]         ; valor da variável que diz se houve uma interrupção com o mesmo número da coluna
    CMP  R4, 0
    JZ   escrever_missil_continuacao     ; se não houve interrupção, vai-se embora
    MOV  R4, 0
    MOV  [R5+R6], R4         ; coloca a zero o valor da variável que diz se houve uma interrupção (consome evento)
	
	MOV R1,0
	CALL escreve_byte
	SUB  R2,1
	MOV R1,variavel_missil
	MOV R8,[R1]
	CMP R8,0
	JZ  fim_missil
    MOV R5, POSICAO_MAX_MISSIL
    CMP R2, R5              ; jáestava na linha do fundo?
    JGT  escreve1           ; se ainda não está, escreve o missil na sua nova posição
    MOV R11,1
    JMP fim_missil          ; se já está na sua posição máxima, apaga o missil
escreve1:
	MOV R6, posicao_coluna_missil
	MOV [R6],R3
	MOV R6,posicao_linha_missil
	MOV [R6],R2
	MOV R1, COR_MISSIL
	CALL escreve_byte					; escreve o missil na sua nova posição
	JMP escrever_missil_continuacao
fim_missil:
	MOV R1,variavel_missil
	MOV R2,0
	MOV [R1],R2                         ; passa a variável_missil a 0 pois o missil deixa de estar ativo
	JMP sai_teclado

; **********************************************************************
; ESCREVER_FIGURA - Rotina chamada quando se quer escrever ou apagar alguma figura
; Devolve: R0 - cor com que vão ser pintados os pixeis (em formato ARGB de 16 bits)
;          R4 - tabela correspondente à dimensao e posicoes da figura
;          R5 - R5 é 1 quando se quer escrever uma figura e 0 quando se quer apagar
;		   R6 - define a linha do pixel de referencia
;		   R7 - define a coluna do pixel de referencia
; **********************************************************************
escrever_figura:
	PUSH R1
	PUSH R2
	PUSH R3
	PUSH R8
	PUSH R9
	PUSH R10
	PUSH R11
	MOVB R9,  [R4]      	;comprimento da figura
    ADD  R4,  1
    MOVB R10, [R4]      	;altura da figura
	MOV R8, 0 
	MOV R11,0
	
proximo_elemento0:
    ADD  R4, 1         	; passa ao próximo elemento da tabela
    MOVB  R3, [R4]      ; R3 com o elemento que é 0 ou 1, se for 1 escreve um pixel

    CMP  R3, 1          ; verifica se tem de escrever um pixel
    JNZ  seguinte0    	; se não tiver que escrever um pixel passa ao próximo elemento da tabela

    CMP  R5, 0          ; se tiver que alterar o pixel, verifica se é escrever ou apagar
    JNZ  posicao0

    MOV  R3, 0          ; passa a 0 porque vai apagar um pixel
	
posicao0:
    MOV  R2, R6         ; linha onde se vai escrever
  	MOV  R1, R7         ; coluna onde se vai escrever
    CALL escreve_pixel
	
seguinte0:
    ADD  R7, 1          ; passa para a próxima coluna
    ADD  R8, 1          ; gerador para colunas aumenta

    CMP  R8, R9         ; verifica se chegou ao comprimento maximo
    JNZ  proximo_elemento0

    SUB  R7, R9         ; quando chega ao maximo volta á coluna mais á esquerda
    MOV  R8, 0          ; reinicia gerador para colunas
    ADD  R6, 1          ; passa para a próxima linha
    ADD  R11, 1         ; gerador para linhas aumenta

    CMP  R11, R10       ; verifica se chegou à última linha
    JGE   fim_do_desenho0

    JMP  proximo_elemento0

fim_do_desenho0:
	POP R11
	POP R10
	POP R9
	POP R8
	POP R3
	POP R2
	POP R1
	RET
	
; **********************************************************************
; ESCREVE_PIXEL - Rotina que escreve um pixel na linha e coluna indicadas.
; Argumentos:   R1 - coluna
;               R2 - linha
;               R0 - cor do pixel (em formato ARGB de 16 bits)
;
; **********************************************************************
escreve_pixel:
	PUSH R2
	PUSH R3
	MOV	R3, MEMORIA_ECRA		; endereço de base da memória do ecrã
	SHL	R2, 6				; linha * 64
    ADD  R2, R1                   ; linha * 64 + coluna
    SHL  R2, 1                    ; * 2, para ter o endereço da palavra
	ADD	R3, R2				; MEMORIA_ECRA + 2 * (linha * 64 + coluna)
	MOV	[R3], R0				; escreve cor no pixel
	POP R3
	POP	R2
	RET
	
esfera_tempo1:              ; abranda o movimento da nave
	SUB R9,1
	JNZ esfera_tempo1
	RET
	
esfera_tempo:               ; abranda o efeito da colisão do missil com a nave e a tecla pausa
	ADD R9,1
	JNZ esfera_tempo
	RET
	
; **********************************************************************
; ESCREVE_BYTE - Escreve um byte no ecrã, com a cor da caneta
; Argumentos: R1 - Valor do byte a escrever
;             R2 - Linha onde escrever o byte (entre 0 e N_LINHAS - 1)
;             R3 - Coluna a partir da qual a barra deve ser desenhada 
; **********************************************************************
	
escreve_byte:
    PUSH R0
     
    MOV  R0, DEFINE_LINHA
    MOV  [R0], R2                 ; seleciona a linha
    
    MOV  R0, DEFINE_COLUNA
    MOV  [R0], R3                 ; seleciona a coluna
   
    MOV  R0, ESCREVE_8_PIXELS     ; endereço do comando para escrever 1 pixel
    MOV  [R0], R1                 ; cor do pixel a escrever
    POP  R0
    RET

; **********************************************************************
; OVNIS - Responsavel por desenhar os ovnis e apga-los, chamando a rotina
;		  desenhar_figuras com a cor 0(apagar). Recebe tambem duas variaveis da rotina
; 		  gerador que determinam a trajetoria e a forma ovni ou asteroide
;
; Argumentos: Nenhum
; **********************************************************************
ovnis:
    PUSH R2
    PUSH R3
    PUSH R4
    PUSH R5
    PUSH R6
	PUSH R11
    MOV  R5,ECRA 			;escolhe o ecra para desenhar o objeto(ovni ou asteroide)
    MOV  R6,0
    MOV [R5],R6
	MOV  R6, 0             
    SHL  R6, 1             
    JMP analisa_colisao		; dentro desta rotina e analisada a colisao de um missel com o objeto oou a nave com o objeto
continuacao_ovni:
    MOV  R5, evento_int
    MOV  R2, [R5+R6]         ; valor da variável que diz se houve uma interrupção 
    CMP  R2, 0
    JZ   sai_ovnis           ; se não houve interrupção, vai-se embora
    MOV  R2, 0
    MOV  [R5+R6], R2         ; coloca a zero o valor da variável que diz se houve uma interrupção (consome evento)
    MOV  R6, posicao_y_ovni
    MOV  R3, [R6]            ; coluna em que o objeto
    MOV  R4, posicao_x_ovni
    MOV   R2, [R4]            ; linha em que o objeto
	

    MOV R1,0                 ;para apagar o objeto
    CALL desenhar_figuras    ;apaga o objeto
    ADD  R2, 1               ; passa a linha abaixo
    MOV  R5, trajetoria
    MOV  R8,[R5]			  ;determina a trajetoria do objeto
    MOV  R5, tipo
    MOV  R7,[R5]			  ;determina o tipo(asteroide ou ovni)	
    ADD  R3, R8              ;trajetoria do objeto(-1(diagonal esquerda), 0(vertical), 1(diagonal direita))
    MOV  R5, N_LINHAS
    CMP  R2, R5              ; ja estava na linha do fundo?
    JLT  escreve
	CALL gerador
    MOV  R2, 0
    MOV  R3, POSICAO_INICIAL               ; volta ao topo do ecra
escreve:
    MOV  [R4], R2            ; atualiza na variavel a linha em que o objeto esta
    MOV  [R6], R3            ; atualiza na variavel a coluna em que o objeto esta
    MOV  R5,ECRA
    MOV  R6,0
    MOV [R5],R6
    CMP   R7,0
    JZ vida  				 ; Se em R7(tipo) estiver 0, logo o objeto representa um asteroide
    MOV R1, forma  		 ; variavel que guarda a forma a utilizar
    CMP R2,3 				 ; R2(linha), se estiver na linha 
    JGT mudar_forma0
    MOV R8, primeira_forma
    JMP cinzento
vermelho:     
    MOV [R1], R8
    MOV  R1, VERMELHO             ; cor do objeto
	MOV R11, cor_ovni
	MOV [R11],R1
	JMP sai_ovnis
	
;*************************************
; Reinicia a posicao e forma do objeto
;*************************************
sai_ovnis_colisao:
	MOV R1,variavel_missil
	MOV R2,0
	MOV [R1],R2
	MOV R1, forma
	MOV R8,primeira_forma
	MOV [R1], R8
	MOV  R6, posicao_y_ovni
	MOV  R4, posicao_x_ovni
	MOV  R2, 0
	MOV  R3, POSICAO_INICIAL              
	MOV  [R4], R2           
	MOV  [R6], R3            ; Reinicia a posicao do objeto
	CALL gerador
sai_ovnis: 
	POP R11
    POP  R6
    POP  R5
    POP  R4
    POP  R3
    POP  R2
    RET

analisa_colisao:
	MOV R0,posicao_y_ovni
	MOV R3,[R0]					;coluna ovni

	MOV R0,posicao_x_ovni
	MOV R4,[R0]	                ;linha ovni
	ADD R4,4 					; adiciona 4 para comparar com a parte de baixo do ovni
	
	MOV R6,0
	MOV R2,variavel_missil
    MOV R5,[R2]                 ; analisa se a variavel missil permite um colisao
    CMP R5,0
    JZ continuacao_colisao
	MOV R0,posicao_linha_missil
	MOV R1,[R0]					;linha missil

	MOV R0,posicao_coluna_missil
	MOV R2,[R0]					;coluna missil
	
	CMP R1,R4
	JLE poss_colisao
continuacao_colisao:
	MOV R10, posicao_nave
	MOVB R8, [R10] 			    ;linha do pixel de referencia
	ADD  R10,  1
	MOVB R7, [R10]              ;coluna do pixel de referencia
	CMP R8, R4                  ; compara linha
	JZ poss_colisao_nave0_5
	JMP continuacao_ovni

poss_colisao:
	CMP R3,R2
	JGT continuacao_ovni
	ADD R3,4
	CMP R3,R2
	JGE acionar_colisao
	JMP continuacao_ovni

acionar_colisao:             ; aciona a colisao 
    MOV R1, forma
    MOV R8, apagar
    MOV [R1],R8
    MOV R1,0 
    CALL desenhar_figuras
	JMP colisao_nave_inimiga
    CALL azul_colisao
	JMP sai_ovnis_colisao

azul_colisao:
    MOV R1, forma
    MOV R8, colisao
    MOV [R1],R8
	MOV R1,AZUL_COLISAO
	CALL desenhar_figuras
    CALL esfera_tempo
    MOV R1, forma
    MOV R8, apagar
    MOV [R1],R8
	MOV R1,0
	CALL desenhar_figuras
	RET
;******************************************************
; Rotinas responsaveis pelas diferentes formas/cor que o 
;ovni/asteroide vai tomando.
;******************************************************
mudar_forma0:
    MOV R8,segunda_forma
    MOV R3,6
    CMP R2, R3
    JGT mudar_forma1
    JMP cinzento
cinzento:
    MOV [R1], R8
    MOV  R1, CINZENTO             
    JMP sai_ovnis
mudar_forma1:
    MOV R8,ovni1
    MOV R3,NOVE
    CMP R2, R3
    JGT mudar_forma2
    JMP vermelho

mudar_forma2:
    MOV R8,ovni2
    MOV R3,DOZE
    CMP R2,R3
    JGT mudar_forma3
    JMP vermelho
vida:
    MOV R1, forma
    CMP R2,3
    JGT mudar_forma7
    MOV R8, primeira_forma
    JMP cinzento
mudar_forma3:
    MOV R8,ovni3
    JMP vermelho
;************************************************************
; Rotina responsavel por gerar dois valores, um que determina 
;a trajetoria e outro que determina o tipo do objeto.
;;************************************************************
gerador:
    PUSH R1
    PUSH R2
    PUSH R3
    PUSH R4
    PUSH R5
    
    MOV  R1, gerador_valor
    MOV R3,3
    MOV R2, [R1]
    ADD R2,1
    MOV [R1],R2  ;adciona se um valor ao gerador
    MOV R7, R2
    MOD R2,R3
    MOV R8, R2
    AND R7,R3
    SUB R8,1
    MOV R1, posicao_x_ovni
    MOV R5,[R1]
    MOV R4, 0
    CMP R5,R4
    JNZ sai_gerador
    MOV R1, trajetoria
    MOV [R1], R8
    MOV R1, tipo
    MOV [R1], R7
sai_gerador:
    POP R5
    POP R4
    POP R3
    POP R2
    POP R1
    RET
;********************************************************************************************************
poss_colisao_nave0_5:
	JMP poss_colisao_nave           ; Apenas para nao gerar erro no simulador devido ao limite de linhas

continuacao_ovni0_5:
	JMP continuacao_ovni 			; Apenas para nao gerar erro no simulador devido ao limite de linhas


sai_ovnis_colisao0_5:
	JMP sai_ovnis_colisao 			; Apenas para nao gerar erro no simulador devido ao limite de linhas

;********************************************************************************************************
; Rotinas responsaveis pelas diferentes formas/cor que o 
;ovni/asteroide vai tomando.
;******************************************************
verde:
     MOV [R1], R8
     MOV R1,VERDE
	 MOV R11,cor_ovni
	 MOV [R11],R1
     JMP sai_ovnis
mudar_forma7:
    MOV R8,segunda_forma
    MOV R3,6
    CMP R2, R3
    JGT mudar_forma4
    JMP cinzento
mudar_forma4:
    MOV R8,vida1
    MOV R3,NOVE
    CMP R2, R3
    JGT mudar_forma5
    JMP verde
mudar_forma5:
    MOV R8,vida2
    MOV R3,DOZE
    CMP R2,R3
    JGT mudar_forma6
    JMP verde
mudar_forma6:
    MOV R8,vida3
    JMP verde

desenhar_figuras:
    PUSH R0
    PUSH R1
    PUSH R2
    PUSH R3
    PUSH R4
    PUSH R5
    PUSH R6
    PUSH R7
    PUSH R8
    PUSH R9
    PUSH R10
    PUSH R11

mesmo_o_inicio:
     MOV  R5, 1                ; 1 para escrever uma figura, 0 para apagar 
inicio_desenho:
    MOV  R3,forma
    MOV  R8, [R3]   
    MOV  R3, posicao_y_ovni
    MOV  R7, [R3]            ; coluna em que a barra esta
    MOV  R4, posicao_x_ovni
    MOV  R6, [R4]            ; linha em que a barra esta
    MOVB R9,  [R8]           ;comprimento da figura
    ADD  R8,  1
    MOVB R10, [R8]       ;altura da figura
    MOV  R4, 0 
    MOV  R11,0
     
proximo_elemento:
    ADD  R8, 1           ; passa ao proximo elemento da tabela
    MOVB  R3, [R8]      ; R3 com o elemento que o 0 ou 1, se for 1 escreve um pixel

    CMP  R3, 1          ; verifica se tem de escrever um pixel
    JNZ  seguinte        ; se nao tiver que escrever um pixel passa ao proximo elemento da tabela
     
posicao:
    MOV  R2, R6         ; linha onde se vai escrever
    MOV  R3, R7         ; coluna onde se vai escrever
    CALL escreve_byte
     
seguinte:
    ADD  R7, 1          ; passa para a proxima coluna
    ADD  R4, 1          ; gerador para colunas aumenta

    CMP  R4, R9         ; verifica se chegou ao comprimento maximo
    JNZ  proximo_elemento

    SUB  R7, R9         ; quando chega ao maximo volta a coluna mais a esquerda
    MOV  R4, 0          ; reinicia gerador para colunas
    ADD  R6, 1          ; passa para a proxima linha
    ADD  R11, 1         ; gerador para linhas aumenta

    CMP  R11, R10       ; verifica se chegou a ultima linha
    JGE   fim_do_desenho

    JMP  proximo_elemento

fim_do_desenho:
    POP R11
    POP R10
    POP R9
    POP R8
    POP R7
    POP R6
    POP R5
    POP R4
    POP R3
    POP R2
    POP R1
    POP R0
    RET
	
poss_colisao_nave:
	ADD R7, 4
	CMP R3,R7
	JGT continuacao_ovni0_5
	SUB R7, 4
	ADD R3, 4
	CMP R7, R3
	JGT continuacao_ovni0_5
	JMP acionar_colisao_nave

acionar_colisao_nave:
    MOV R1, tipo
    MOV R2,[R1]
    MOV R1,0 
    CALL desenhar_figuras
    CMP R2,0
    JZ asteriode
 	MOV R1, variavel_game_over
 	MOV R2,4
 	MOV [R1],R2
	CALL game_over
	JMP sai_ovnis_colisao0_5
 
game_over:
	PUSH R0
	PUSH R1
	PUSH R2
	PUSH R3
	MOV R0, REP_SOM
	MOV R1, SOM_GAMEOVER
	MOV [R0], R1 
	MOV R0,ENERGIA
	MOV R1,energia_inicial
	MOV [R0],R1
	MOV R0,jogo_comecado
	MOV R1,0
	MOV [R0],R1
	MOV R0,IMAGEM_FUNDO
	MOV R2,variavel_game_over
	MOV R1,[R2]
	MOV [R0],R1
	MOV  R0, APAGA_ECRAS
    MOV  [R0], R1            ; apaga todos os pixels de todos os ecrãs (o valor de R1 não é relevante)

ciclo_gameover:
	MOV R0,controlador
	MOV R11,1
	MOV [R0],R11
	MOV R0,teclado_valor
	MOV R10,1
	MOV [R0],R10
	CALL teclado
	MOV R0,ENERGIA
	MOV R5,energia_inicial
	MOV [R0],R5
	MOV R0,posicao_y_ovni
	MOV R5,POSICAO_INICIAL
	MOV [R0],R5
	MOV R0,posicao_x_ovni
	MOV R5,0
	MOV [R0],R5
	MOV R0,controlador
	MOV R11,[R0]
	CMP R11,0
	JNZ ciclo_gameover
	CALL escrever_nave
	POP R3
	POP R2
	POP R1
	POP R0
	RET
	
display_inicio:
	PUSH R0
	PUSH R1
	PUSH R2
	PUSH R3
	PUSH R4
	PUSH R5
	PUSH R6
	PUSH R7
	PUSH R8
	PUSH R9
	PUSH R10
    MOV R10, ENERGIA  
	MOV R4,POUT1
	MOV R5,[R10]             ; coloca o valor da energia em R5
    MOV  R6, 2               ;
    SHL  R6, 1               ; multiplica a coluna por 2 porque a posicao_x_ovni e o evento_int são tabelas de words

     
sub5:
    MOV  R0, FATOR            
    MOV  R1, evento_int
    MOV  R2, [R1+R6]         ; valor da variável que diz se houve uma interrupção com o mesmo número da coluna
    CMP  R2, 0
    JZ   mesmo_fim           ; se não houve interrupção, vai-se embora
	SUB R5,5                 ; retira 5 à energia
	MOV [R10],R5             ; atualiz o valor da energia
    MOV  R2, 0
    MOV  [R1+R6], R2         ; coloca a zero o valor da variável que diz se houve uma interrupção (consome evento)  
    JMP hex_dec

hex_dec:
    MOD R5,R0                ; resto da divisao do valor da energia pelo fator
    MOV R2,R5                ; coloca em R2 o resultado da operação anterior
    MOV R1,DIVISAO
    DIV R0,R1                ; dividir o valor de energia por 10
    DIV R2, R0               ; divide outra vez pelo fator
    SHL R3,4                 ; shl 4 resultado
    OR  R3,R2                ; resultado OR digito
    CMP R0,1                 ; repete até o fator ser igual a 1
    JZ fim_display
    JMP hex_dec
     
fim_display:
	MOV [R4],R3
    MOV R0,0
    MOV R1,0
    MOV R2,0
    MOV R3,0
	
mesmo_fim:
	POP R10
	POP R9
	POP R8
	POP R7
	POP R6
	POP R5
	POP R4
	POP R3
	POP R2
	POP R1
	POP R0
	RET
	
asteriode:
	MOV R0, REP_SOM			
	MOV R1, SOM_ASTEROIDE
	MOV [R0], R1             ;som da colisao asteroide
	MOV R0,FATOR
	MOV R4, POUT1  
	MOV R3,ENERGIA
	MOV R5,[R3]              ; coloca em R5 o valor da energia
	ADD R5,5
	ADD R5,5                 ; adiciona 10 a R5 uma vez que a nave bateu num asteroide
	MOV [R3],R5              ; atualiza o valor da energia
	
hex_dec1:
    MOD R5,R0                ; resto da divisao do valor da energia pelo fator
    MOV R2,R5                ; coloca em R2 o resultado da operação anterior
    MOV R1,DIVISAO
    DIV R0,R1                ; dividir o valor de energia por 10
    DIV R2, R0               ; divide outra vez pelo fator
    SHL R3,4                 ; shl 4 resultado
    OR  R3,R2                ; resultado OR digito
    CMP R0,1  
    JNZ hex_dec1             ; repete até chegar a 1

	MOV R4, POUT1
	MOV [R4],R3              ; atualiza o falor da energia nos displays
	JMP sai_ovnis_colisao0_5
; **********************************************************************
; VERIFICAR_DISPLAY - Verifica se o display de energia já chegou a zero
;
; Argumentos: Nenhum
; **********************************************************************	
verificar_display:
	PUSH R7
	PUSH R9
	MOV R7, ENERGIA
	MOV R9,[R7]         ; coloca em R9 o valor da energia
	MOV R7,0
	CMP R7, R9          ; verifica se é igual a 0
	JZ display_zero     ; se for sai para display_zero
	POP R9
	POP R7
	RET
	
display_zero:
 	MOV R1, variavel_game_over
 	MOV R2,3
 	MOV [R1],R2
	CALL game_over  ; como o display chegou a zero acaba o jogo
	POP R9
	POP R7
	RET

	
missil:
	MOV R0,FATOR
	MOV R4, POUT1  
	MOV R3,ENERGIA
	MOV R5,[R3]
	SUB R5,5                 ; retira 5 à energia uma vez que o missil foi acionado
	MOV [R3],R5
	
hex_dec2:
    MOD R5,R0                ; resto da divisao do valor da energia pelo fator
    MOV R2,R5                ; coloca em R2 o resultado da operação anterior
    MOV R1,DIVISAO
    DIV R0,R1                ; dividir o valor de energia por 10
    DIV R2, R0               ; divide outra vez pelo fator
    SHL R3,4                 ; shl 4 resultado
    OR  R3,R2                ; resultado OR digito
    CMP R0,1  
    JNZ hex_dec2             ; repete até o fator ser 1

	MOV R4, POUT1
	MOV [R4],R3              ; atualiza o valor da energia nos displays 
	RET 

nave_destruida:
	MOV R0, REP_SOM
	MOV R1, SOM_MORTE_OVNI
	MOV [R0], R1             ; ativa o som que se ouve quando a nave inimiga é destruída
	MOV R0,FATOR
	MOV R4, POUT1  
	MOV R3,ENERGIA
	MOV R5,[R3]
	ADD R5,5                 ; adiciona 5 à energia dos displays
	MOV [R3],R5

hex_dec3:	
    MOD R5,R0                ; resto da divisao do valor da energia pelo fator
    MOV R2,R5                ; coloca em R2 o resultado da operação anterior
    MOV R1,DIVISAO
    DIV R0,R1                ; dividir o valor de energia por 10
    DIV R2, R0               ; divide outra vez pelo fator
    SHL R3,4                 ; shl 4 resultado
    OR  R3,R2                ; resultado OR digito
    CMP R0,1  
    JNZ hex_dec3             ; repete até o fator ser igual a 1
	MOV R4, POUT1
	MOV [R4],R3              ; atualiza o valor da energia nos displays
	CALL azul_colisao        
	JMP sai_ovnis_colisao
	
colisao_nave_inimiga:
	MOV R1, tipo
    MOV R2,[R1]              ; coloca o tipo de ovni em R2
    MOV R1,0                 
    CALL desenhar_figuras
    CMP R2,0				; analisa o tipo que o objeto tem ao cair, se for 0 e asteroide se for outro valor e ovni
    JNZ nave_destruida
	MOV R0, REP_SOM
	MOV R1, SOM_MORTE_OVNI
	MOV [R0], R1
	CALL azul_colisao
	JMP sai_ovnis_colisao
	
;*********************************************************************************
; Reinicia o jogo por completo funcionando assim como uma "segunda" inicializaçao
;*********************************************************************************
terminar:
	PUSH R0
	PUSH R1
	PUSH R4
	PUSH R5

	MOV  R0, APAGA_ECRAS
    MOV  [R0], R1            ; apaga todos os pixels de todos os ecrãs (o valor de R1 não é relevante)
     
    MOV  R0, APAGA_AVISO
    MOV  [R0], R1            ; apaga o aviso de nenhum cenário selecionado (o valor de R1 não é relevante)
	MOV R0, IMAGEM_FUNDO
	MOV R1, 0
	MOV [R0], R1
	MOV R0,0
	MOV R0,posicao_y_ovni
	MOV R1,POSICAO_INICIAL
	MOV [R0], R1
	MOV R4, POUT1           ;endereço do periférico dos displays
	MOV R0,controlador
	MOV R1,1
	MOV [R0],R1
	MOV R0,ENERGIA
	MOV R5,energia_inicial
	MOV [R0],R5
	MOV R0,posicao_y_ovni
	MOV R5,POSICAO_INICIAL
	MOV [R0],R5
	MOV R0,posicao_x_ovni
	MOV R5,0
	MOV [R0],R5
	CALL escrever_nave
	POP R5
	POP R4
	POP R1
	POP R0
	RET