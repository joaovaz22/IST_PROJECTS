#include <stdio.h>
#include <stdlib.h>

#define MAX_ARRAY_SIZE 100

typedef struct result {
    int t, c;
}Result;


int cmp(const void *a, const void *b)
{
    return ( *(int*)a - *(int*)b );
}



int biggestElement(int elements[], int n){
    /*
     * Iterates through @elements of size @n
     * returning the biggest value @res
     */
    int i;
    int res = elements[0];
    if(n==0) return 0;

    for (i = 1; i < n; i++)
        if (elements[i] > res)
            res = elements[i];

    return res;
}


Result MSC(int *x, int n){
    /* Creates 2 arrays
     * @lengthSC - stores the longest increasing
     *   subsequence in each index of @x
     * @countSubSequences - stores the number of longest
     *   increasing subsequences in each index of @x
     * */
    int i, j;
    Result res;

    int lengthSC[n];
    int countSC[n];
    for(i = 0; i < n; i++){
        lengthSC[i] = 1;
        countSC[i] = 1;
    }

    for(i = 1; i < n; i++) {
        int var1 = countSC[i];
        int var2 = lengthSC[i];
        for (j = 0; j < i; j++) {
            int var3 = lengthSC[j];
            if (x[i] > x[j]) {

                if (var3 + 1 > var2) {
                    var2 = var3 + 1;
                    var1 = countSC[j];
                }
                else if (var3 + 1 == var2)
                    var1 += countSC[j];
            }
        }
        countSC[i] = var1;
        lengthSC[i] = var2;
    }
    int countMSC = 0;
    int lengthMSC;
    lengthMSC = biggestElement(lengthSC, n);


    for(i = 0; i < n; i++){

        if (lengthSC[i] == lengthMSC)
            countMSC += countSC[i];
    }
    res.t = lengthMSC;
    res.c = countMSC;



    return res;
}

/*
 * Create res(array of 0's) of length same as y.
 * For each element of x; iterate through all elements of y.
 * If a common element is found, aux variable is incremented and is stored in res.
 * If element of x is greater than element of y, we update our aux variable with 
 * the maximum of res or the value stored in res at that index.
 * After all elements of x is covered, we return the maximum value from our res as result.
*/
int MSCC(int *x, int *y, int n, int m){

    int i, j;
    int res[m];

    for (i = 0; i < m; i++)
        res[i] = 0;

    for (i = 0; i < n; i++) {
        int aux = 0;
        for (j = 0; j < m; j++) {

            if (x[i] == y[j])
                if (aux + 1 > res[j])
                    res[j] = aux + 1;

            if (x[i] > y[j])
                if (res[j] > aux)
                    aux = res[j];
        }
    }
    return biggestElement(res, m);
}


int main() {

    int i = 0, n, l1 = 0, l2 = 0, count1 = 0, count2 = 0;
    int *x;
    int *y;
    int *ip;
    int r = MAX_ARRAY_SIZE;
    char temp, temp1, temp2;
    int key;
    x = malloc(sizeof(int) * r);
    y = malloc(sizeof(int) * r);

    if (scanf("%d", &n) == 0)
        return 0;

    if (n == 1) {
        while (temp != '\n') {

            if (scanf("%d%c", &x[i], &temp) == 0)
                return 0;
            i++;
            if (i + 1 > r) {
                r *= 2;
                x = realloc(x, sizeof(int) * r);
                
            }
        }
        Result res;
        res = MSC(x, i);
        printf("%d %d\n", res.t, res.c);
        }

    if (n == 2) {
        while (temp1 != '\n') {
            if (scanf("%d%c", &x[l1], &temp1) == EOF)
                return 0;
            l1++;
            if (l1 + 1 > r) {
                r *= 2;
                x = (int *) realloc(x, sizeof(int) * r);
            }
        }
        r = MAX_ARRAY_SIZE;
        while (temp2 != '\n') {
            if (scanf("%d%c", &y[l2], &temp2) == EOF)
                return 0;
            l2++;
            if (l2 + 1 > r) {
                r *= 2;
                y = (int *) realloc(y, sizeof(int) * r);

            }
        }
        int *arr1;
        arr1 = malloc(sizeof(int) * l1);
        for (i = 0; i < l1; i++) {
            arr1[i] = x[i];
        }

        int *arr2;
        arr2 = malloc(sizeof(int) * l2);
        for (i = 0; i < l2; i++) {
            arr2[i] = y[i];
        }

        qsort(arr1, l1, sizeof *arr1, cmp);
        qsort(arr2, l2, sizeof *arr2, cmp);


        for (i = 0; i < l2; i++) {
            key = y[i];
            ip = bsearch(&key, arr1, l1, sizeof *arr1, cmp);
            if (ip != NULL) {
                y[count2++] = key;
            }
        }

        for (i = 0; i < l1; i++) {
            key = x[i];
            ip = bsearch(&key, arr2, l2, sizeof *arr2, cmp);
            if (ip != NULL) {
                x[count1++] = key;
            }
        }
        if (count1==0)
            return 0;
        printf("%d\n", MSCC(x, y, count1, count2));
    }
    return 0;

}



