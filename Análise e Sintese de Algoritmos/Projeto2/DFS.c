#include <stdio.h>
#include <stdlib.h>
#include <string.h>
enum colours{WHITE, GRAY, BLACK, RED};

typedef struct node{
    int val;
    int parent;
    struct node* next;
    struct node* last;

}*Node;


/*int DFS_Visit(Node nodes[], int pos){
    nodes[pos]->colour = GREY;
    int pos_adj = nodes[pos]->val;
    if(nodes[pos_adj]){
        if(nodes[pos_adj]->colour==WHITE)
            (Tem um node adjacente que ainda nao visitou)
            DFS_Visit(nodes, pos_adj);
        else if(nodes[pos_adj]->colour==GREY)
            printf("ERROUUU\n");
    }


    
    nodes[pos]->colour = BLACK;
    return 0;
}*/


int DFS_Visit(Node *nodes, int pos, enum colours *vertices_colour){
    if(nodes[pos] && nodes[pos]->val!=0){
        vertices_colour[pos] = GRAY;
        Node v;
        for (v = nodes[pos]; v != NULL; v = v->next){
            if (vertices_colour[v->val-1]==WHITE && v->val != 0){

                DFS_Visit(nodes, v->val-1, vertices_colour);
            }
            else if (vertices_colour[v->val-1]==GRAY){
                printf("0");
                exit(0 );   
            }
        }
        vertices_colour[pos] = BLACK;
        return 0;
    }

    vertices_colour[pos] = BLACK;
    return 0;
}

int DFS(Node *nodes, int size){

    int i;

    enum colours *vertices_colour = malloc(sizeof(enum colours)*size);
    for (i = 0; i < size; i++){
        vertices_colour[i] = WHITE; 
    }

    for (i = 0; i < size; i++){

        if(vertices_colour[i]==WHITE)
            DFS_Visit(nodes, i, vertices_colour);
        }
    
    return 0;
}
int LCA_Visit(Node *nodes, int pos, int *LCA_colours, int *number_of_parents, enum colours colour){
    if(LCA_colours[pos] == WHITE)
        LCA_colours[pos] = colour;
    else if(LCA_colours[pos] ==GRAY){
        printf("(%d)->", pos+1);
        LCA_colours[pos] = BLACK;
    }
    else
        return 0;

    if(number_of_parents[pos] == 0){
        return 0;
    }
    if(number_of_parents[pos] == 1){
        LCA_Visit(nodes, nodes[pos]->parent-1, LCA_colours, number_of_parents, colour);
        return 0;
    }
    if(number_of_parents[pos] == 2){
        LCA_Visit(nodes, nodes[pos]->parent-1, LCA_colours, number_of_parents, colour);
        LCA_Visit(nodes, nodes[pos]->next->parent-1, LCA_colours, number_of_parents, colour);
        return 0;
    }
    return 0;
}

int LCA(Node *nodes, int pos1, int pos2, int *number_of_parents, int size){
    int i;

    int *vertices_colour = malloc(sizeof(int)*size);
    for (i = 0; i < size; i++){
        vertices_colour[i] = WHITE; 
    }
    LCA_Visit(nodes, pos1, vertices_colour, number_of_parents, GRAY);
    LCA_Visit(nodes, pos2, vertices_colour, number_of_parents, RED);
    return 0;
}


void showList(Node *nodes, int n){
    int i;
    for (i = 0; i<n; i++){
        if(nodes[i]){
            printf("head(%d)",i+1);
            Node aux = nodes[i];
            while(aux != NULL){
                if(aux->val==0)
                    printf("father ->(%d)",aux->parent);
                else
                    printf("->(%d)",aux->val);
                if(aux->next)
                    aux = aux->next;
                else break;
            }
            printf("\n\n");
        }
    }
}
void showParents(Node *nodes, int pos){
    printf("(%d)->(%d)->(%d)",pos+1 ,nodes[pos]->parent,nodes[pos]->next->parent);
}


int main(){
    int FAILED = 0;

    int *number_of_parents; 

    int v1, v2;
    int i, m, n, x, y;
    if(scanf("%d %d", &v1, &v2) != 2){
        printf("0");
        return 0;
    }
       
    if (scanf("%d %d", &n, &m) != 2){
        printf("0");
        return 0;
    }

    number_of_parents = malloc(sizeof(int)*n);
    for (i = 0; i<n; i++)
        number_of_parents[i] = 0;
    /*nodes = malloc(sizeof(Node) * n);*/
    Node nodes[n];
    for(i = 0; i<n; i++){
        nodes[i] = NULL;
    }

    for(i = 0; i<m; i++){

        if(scanf("%d %d", &x, &y) != 2){
            printf("0");
            return 0;
        }   

        if(nodes[x-1] == NULL)
        {

            nodes[x-1] = (Node) malloc(sizeof(Node));
            nodes[x-1]->val = y;
            nodes[x-1]->next = NULL;
            nodes[x-1]->last = nodes[x-1];
        }
        else if(nodes[x-1]->val==0){
            nodes[x-1]->val = y;
            nodes[x-1]->last = nodes[x-1];
        }

        /*Criacao de adjacentes*/
        else{
        Node aux = nodes[x-1]->last;
        

        /*while(aux->next != NULL)
            aux = aux->next;*/
        if(aux->next)
            aux = aux->next;
        else{
            if(aux->val!=0){
                aux->next = (Node) malloc(sizeof(Node));
            }
            aux = aux->next;
        }

        
        aux->val = y;
        aux->next = NULL; 
        nodes[x-1]->last = aux; 
        }
        if(nodes[y-1]==NULL){
            nodes[y-1] = malloc(sizeof(Node));
            nodes[y-1]->val = 0;
            nodes[y-1]->next = NULL;
        }
        if(number_of_parents[y-1] == 0){
            nodes[y-1]->parent = x;
        }
        else if(number_of_parents[y-1] == 1){
            if(nodes[y-1]->next == NULL){
                nodes[y-1]->next = malloc(sizeof(Node));
                nodes[y-1]->next->val = 0;
            }

            nodes[y-1]->next->parent = x;

       }
        number_of_parents[y-1]++;
        if(number_of_parents[y-1] == 3)
        {

            FAILED = 1;
        }
        
    }
    if (FAILED){
        printf("0QAWAWF");
        return 0;
    }


    DFS(nodes, n);
    LCA(nodes, v1, v2, number_of_parents, n);







    return 0;
}
