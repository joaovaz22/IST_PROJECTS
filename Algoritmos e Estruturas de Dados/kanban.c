#include <stdio.h>
#include<string.h>
#define MAX_DESC_T 51
#define MAX_DESC_A 21
#define MAX_NUM_T 20000
#define MAX_NUM_A 11


int storage=-1;
char list_of_activities[MAX_NUM_A][MAX_DESC_A]={"TO DO", "IN PROGRESS", "DONE"};
char users[MAX_DESC_T][MAX_DESC_A];
int nr_user=0;
int nr_activ=3;
int time=0;

typedef struct
{
   int id;
   int duracao;
   int inst_inicial;
   int atividade;
   char descricao[MAX_DESC_T];

} Task;

Task tarefa[MAX_NUM_T];


void merge(int arr[], int l, int m, int r)
{
   int n1 = m - l + 1;
   int n2 = r - m;
   int i, j, k;

   int L[MAX_NUM_T], R[MAX_NUM_T];


   for (i = 0; i < n1; i++)
       L[i] = arr[l + i];
   for (j = 0; j < n2; j++)
       R[j] = arr[m + 1 + j];


   i = 0;
   j = 0;
   k = l;

   while (i < n1 && j < n2) {
       if (tarefa[L[i]].inst_inicial <= tarefa[R[j]].inst_inicial) {
           arr[k] = L[i];
           i++;
       }
       else {
           arr[k] = R[j];
           j++;
       }
       k++;
   }

   while (i < n1) {
       arr[k] = L[i];
       i++;
       k++;
   }
   while (j < n2) {
       arr[k] = R[j];
       j++;
       k++;
   }
}
void mergeSort(int arr[],int l,int r){
   int m = l + (r-l) / 2;
   if(l >= r){
       return;
   }
   mergeSort(arr,l,m);
   mergeSort(arr,m+1,r);
   merge(arr,l,m,r);
}


int get_descrition(char a[MAX_DESC_T], int type)
{
   int j,c;
   while ((c = getchar()) == ' ' || c == '\t')
       type++;
   a[0] = c;
   for (j = 1; (c = getchar()) != '\n'; j++){
       a[j] = c;
   }
   a[j++] = '\0';
   return 0;
}

int list_of_index(int size, int destination[MAX_DESC_T])
{
   int i;

   for (i=0; i<=size; i++)
       destination[i]=i;
   return 0;
}
int sort_struct(Task list[MAX_NUM_T],int left, int right)
{
   int i,j, parar;
   Task temp;

   for (i = left; i < right; i++){
       parar = 1;
       for (j = right; j>i; j--){
           if (strcmp(list[j-1].descricao,list[j].descricao)>0){
               temp = list[j-1];
               list[j-1] = list[j];
               list[j] = temp;
               parar = 0;
           }
       }
       if(parar) break;
   }
   return 0;
}


int nr_tasks(int i){

   if (i <= 9999)
       return 1;
   return 0;
}

int add_task(){
   int i;
   int aux;

   storage++;
   scanf("%d", &aux);
   get_descrition(tarefa[storage].descricao, -1);
   if (!nr_tasks(storage))
   {
       printf("too many tasks\n");
       storage--;
       return 0;
   }
   else{
       tarefa[storage].id = storage+1;
       tarefa[storage].inst_inicial=0;
       tarefa[storage].atividade = 0;
       for (i=0; i<storage; i++){
           if(!strcmp(tarefa[i].descricao, tarefa[storage].descricao)){
               storage--;
               printf("duplicate description\n");
               return 0;
           }
       }
       if(aux > 0)
           tarefa[storage].duracao = aux;
       else{
           printf("invalid duration\n");
           storage--;
           return 0;
       }

       if(0 != storage)
           sort_struct(tarefa,0,storage);
       printf("task %d\n", storage+1);
       return 0;
   }
}



int list_task_alfabetical(){




   int index;

   for (index = 0; index <= storage; index++)
       printf("%d %s #%d %s\n",
              tarefa[index].id, list_of_activities[tarefa[index].atividade], tarefa[index].duracao, tarefa[index].descricao);
   return 0;

}

int list_task_ids(){

   int x, j, i = 0;


   while (scanf("%d",&x)){
       if (x <= 0 || x > storage+1)
           printf("%d: no such task\n", x);
       else{
           for (j = 0; j <= storage; j++){
               if (tarefa[j].id == x)
                   printf("%d %s #%d %s\n",
                          tarefa[j].id, list_of_activities[tarefa[j].atividade], tarefa[j].duracao, tarefa[j].descricao);
           }
       }
       i++;
   }
   return 0;
}

int control_time(){

   int leap;
   char temp;

   scanf("%d%c", &leap, &temp);
   if(leap >= 0){
       time += leap;
       printf("%d\n",time);
   }
   else
       printf("invalid time\n");
   return 0;
}

int create_user()
{

   int i;


   char aux[MAX_DESC_A];

   get_descrition(aux,0);

   for (i = 0; i <= nr_user; i++){

       if (!strcmp(aux,users[i]))
       {
           printf("user already exists\n");
           return 0;
       }
   }
   if (nr_user >= 50)/*Nao pode ser 50*/
   {
       printf("too many users\n");
       return 0;
   }
   strcpy(users[nr_user],aux);
   nr_user++;
   return 0;
}
int list_users()
{

   int i;



   for (i = 0; i <= nr_user; i++){
       printf("%s", users[i]);
       if (!(i == nr_user))
           printf("\n");
   }
   return 0;
}

int no_such_activity(char a[MAX_DESC_A])
{


   int i;
   for (i=0; i<nr_activ; i++){
       if (strcmp(a,list_of_activities[i])==0)
           return 1;
   }
   return 0;
}

int no_such_user(char b[MAX_NUM_A])
{



   int i;
   for (i = 0; i < nr_user; i++){
       if (strcmp(b,users[i]) == 0)
           return 1;
   }
   return 0;
}


int ok(char a[MAX_DESC_A], char u[MAX_NUM_A], int index)
{




   if (strcmp(list_of_activities[0], a) ==0 && (strcmp(list_of_activities[tarefa[index].atividade],list_of_activities[0])) !=0 )
   {
       printf("task already started\n");
       return 0;
   }
   if (!no_such_activity(a))
   {
       printf("no such activity\n");
       return 0;
   }

   if (!no_such_user(u))
   {
       printf("no such user\n");
       return 0;
   }
   return 1;
}
int get_number_of_activity(char a[MAX_DESC_A])
{



   int i;

   for (i=0; i<nr_activ; i++)
   {
       if (strcmp(a,list_of_activities[i])==0)
           return i;
   }
   return 0;
}

int get_index(int x){
   int j;
   for (j = 0; j <= storage; j++){
       if (tarefa[j].id == x){
           return j;
       }
   }
   return 0;
}

int move_task()/*CRIAR ERRO DE ID*/
{




   char poss_user[MAX_DESC_T], poss_ativ[MAX_DESC_A];
   int poss_id, x;
   int time_spent;
   int index;

   scanf("%d%s",&poss_id,poss_user);
   get_descrition(poss_ativ,-1);
   index = get_index(poss_id);
   if (poss_id > storage + 1 || poss_id <= 0)
   {
       printf("no such task\n");
   }
   else if (!strcmp(poss_ativ, list_of_activities[0])) {
       if (!strcmp(list_of_activities[tarefa[index].atividade], list_of_activities[0]))
           return 0;

       else {
           if (ok(poss_ativ, poss_user, index)) {

               x = get_number_of_activity(poss_ativ);

               if (!strcmp(list_of_activities[tarefa[index].atividade], list_of_activities[0]) && x != 0)
                   tarefa[index].inst_inicial = time;
               if (strcmp(list_of_activities[tarefa[index].atividade], list_of_activities[2]) != 0) {
                   if (!strcmp(list_of_activities[2], poss_ativ)) {
                       time_spent = time - tarefa[index].inst_inicial;
                       printf("duration=%d slack=%d\n", time_spent, time_spent - tarefa[index].duracao);
                   }
               }
               tarefa[index].atividade = x;
           }

       }
   } else {
       if (ok(poss_ativ, poss_user, index)) {

           x = get_number_of_activity(poss_ativ);

           if (!strcmp(list_of_activities[tarefa[index].atividade], list_of_activities[0]) && x != 0)
               tarefa[index].inst_inicial = time;
           if (strcmp(list_of_activities[tarefa[index].atividade], list_of_activities[2]) != 0) {
               if (!strcmp(list_of_activities[2], poss_ativ)) {
                   time_spent = time - tarefa[index].inst_inicial;
                   printf("duration=%d slack=%d\n", time_spent, time_spent - tarefa[index].duracao);
               }
           }
           tarefa[index].atividade = x;
       }
   }
   return 0;
}


int list_activ_tasks()
{
   char a[MAX_DESC_A];



   int task_of_activities[MAX_NUM_T];
   int i, x, size = 0, j, index[MAX_NUM_T];

   get_descrition(a,0);
   if (!no_such_activity(a))
   {
       printf("no such activity\n");
       return 0;
   }
   x = get_number_of_activity(a);
   list_of_index(storage,index);

   for (i = 0; i <= storage; i++){
       if (tarefa[i].atividade==x)
       {
           task_of_activities[size] = i;
           size++;
       }
   }

   mergeSort(task_of_activities,0,size-1);


   for (j = 0; j < size; j++)
       printf("%d %d %s\n",
              tarefa[task_of_activities[j]].id, tarefa[task_of_activities[j]].inst_inicial, tarefa[task_of_activities[j]].descricao);

   return 0;
}
int allcaps(const char str[MAX_DESC_T]){
   int i;

   for (i = 0; str[i] != '\0'; i++)
       if (str[i] >= 'a' && str[i] <= 'z')
           return 0;
   return 1;
}

int add_activity(){


   int j;
   char a[MAX_DESC_A];

   get_descrition(a,0);
   for (j = 0;j < nr_activ; j++){
       if (!strcmp(a,list_of_activities[j]))
       {
           printf("duplicate activity\n");
           return 0;
       }
   }
   if (!allcaps(a)){
       printf("invalid description\n");
       return 0;
   }

   if (nr_activ >= 10){
       printf("too many activities\n");
       return 0;
   }

   strcpy(list_of_activities[nr_activ], a);
   nr_activ += 1;
   return 0;
}

int list_activities(){


   int i;
   for (i = 0; i < nr_activ; i++)
       printf("%s\n",list_of_activities[i]);
   return 0;
}

int main(){
   int c;


   while (1){
       c=getchar();
       switch(c){

           case 'q':
               return 0;

           case 't':
               add_task();
               break;

           case 'l':(c = getchar()) == '\n' ? list_task_alfabetical() :
               list_task_ids();
               break;

           case 'n':
               control_time();
               break;

           case 'u':
               (c = getchar()) == '\n' ? list_users() :
               create_user();
               break;

           case 'm':
               move_task();
               break;

           case 'd':
               list_activ_tasks();
               break;

           case 'a':
               (c = getchar()) == '\n' ? list_activities() :
               add_activity();
               break;

           default:
               break;
       }
   }
}
