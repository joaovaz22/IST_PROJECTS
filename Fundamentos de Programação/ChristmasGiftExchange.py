from random import *
pessoas=[]
while True:
    pessoa=input("Escreva as pessoas que participam.(acabou para sair):\n")
    if pessoa=="acabou": break
    pessoas.append(pessoa)


shuffle(pessoas)
lista = [pessoas[-1]] + pessoas[:-1]
for oferecedor, recetor in zip(pessoas, lista):
     print(oferecedor, "oferece a", recetor)
        
