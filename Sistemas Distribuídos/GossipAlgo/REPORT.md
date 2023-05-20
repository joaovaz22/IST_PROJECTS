# DistLedger Gossip Architecture Implementation

## Vector Clock

Criado um novo módulo [Utils](Utils) que detém esta class.
Esta class tem como atributo `timestamps` com o formato de uma ArrayList.

### Funcionalidades

Esta class é operada com base num `id`, assim os servidores quando necessário sabem sempre qual a sua posição no `timestamps`. Isto tem a vantagem ser possivel operar com mais do que dois servidores(mudar)

### Contrutores

    VectorClock(Integer id)
É criado um VectorClock com timestamps de tamanho `id+1`. Isto é relevante quando sabemos que os timestamps têm pelo menos o tamanho do `id+1`.

    VectorClock(List<Integer> timestamps)
É criado um VectorClock com o timestamps passado como argumento. Será útil quando estamos a criar um VectorClock com base no timestamps de outro.
  
## Implementação do Algoritmo

### Leitura

Durante um processo de leitura, quando um cliente faz um pedido a um servidor, os seguintes passos sao executados:

1. Executada a função de leitura respetiva em [UserServiceImpl](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/UserServiceImpl.java) e feita uma pequena verificação de erros;
2. Em [ServerService](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/grpc/ServerService.java) é executada a respetiva função de leitura e é obtido o valor pedido pelo cliente;
3. Em [UserService](User/src/main/java/pt/tecnico/distledger/userclient/grpc/UserService.java) é feito o merge entre `prevTs` e `new` e é feito um print do valor pedido pelo cliente.

### Escrita

Durante um processo de escita, quando um cliente faz um pedido a um servidor, os seguintes passos são executados:

1. No [UserServiceImpl](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/UserServiceImpl.java) é incrementado o valor de ReplicaTs no respetivo servidor e é enviada uma resposta para [ServerService](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/grpc/ServerService.java).
2. Em [ServerService](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/grpc/ServerService.java) é verificado se valueTS >= prev, se sim executa e classifica a operação como `isStable = True` e é executado um merge entre `valueTS` e `operationTS` senão, esta não é executada e é classificada como `isStable = False`.Por fim a operação é adicionada ao ledger.
3. Em [UserService](User/src/main/java/pt/tecnico/distledger/userclient/grpc/UserService.java) é feito o merge entre `prevTs` e `new`.

### Update/Propagate

O Admin

1. Em [AdminService](Admin/src/main/java/pt/tecnico/distledger/adminclient/grpc/AdminService.java) o admin faz um request ao servidor para dar gossip;
2. Recebe e resposta em [AdminServiceImpl](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/AdminServiceImpl.java);
3. É enviado um propagateStateRequest em [AdminServiceImpl](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/AdminServiceImpl.java);
4. Servidor pede para dar propagate em [DistLedgerCrossServerServiceImpl](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/DistLedgerCrossServerServiceImpl.java) e dá call do ServerService;
5. No [ServerService](DistLedgerServer/src/main/java/pt/tecnico/distledger/server/grpc/ServerService.java) é executado:
   1. Por cada operação no Ledger do server a ser utilizado
      * Verifica a condição `If( not (op.TS <= B.ReplicaTS))`;
   2. Executa as operações;
   3. Dá merge dos replicaTS de ambos os servidores;
   4. É feito um while onde são executadas todas as operações que possam ter mudado de estado para estável.

### Gossip

O gossip foi alterado para receber dois server:
`gossip <serverFrom> <serverTo>`
gossip(A, B)
Quando chamada esta função, o servidor A fará gossip para B.
