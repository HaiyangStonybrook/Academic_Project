#include <stdio.h>
#include "sfmm.h"


int find_list_index_from_size(int sz) {
    if (sz >= LIST_1_MIN && sz <= LIST_1_MAX) return 0;
    else if (sz >= LIST_2_MIN && sz <= LIST_2_MAX) return 1;
    else if (sz >= LIST_3_MIN && sz <= LIST_3_MAX) return 2;
    else return 3;
}

int main(int argc, char const *argv[]) {

    sf_mem_init();
    /*
    printf("before sort:\n");
    printf("[0]: %d, %d\n", seg_free_list[0].min, seg_free_list[0].max);
    printf("[1]: %d, %d\n", seg_free_list[1].min, seg_free_list[1].max);
    printf("[2]: %d, %d\n", seg_free_list[2].min, seg_free_list[2].max);
    printf("[3]: %d, %d\n", seg_free_list[3].min, seg_free_list[3].max);

    sort_list();
    printf("after sotr:\n");
    printf("[0]: %d, %d\n", seg_free_list[0].min, seg_free_list[0].max);
    printf("[1]: %d, %d\n", seg_free_list[1].min, seg_free_list[1].max);
    printf("[2]: %d, %d\n", seg_free_list[2].min, seg_free_list[2].max);
    printf("[3]: %d, %d\n", seg_free_list[3].min, seg_free_list[3].max);
    */
    void *x = sf_malloc(sizeof(int));
    /* void *y = */ sf_malloc(10);
    x = sf_realloc(x, sizeof(int)*10);
    sf_snapshot();
    sf_blockprint(x-8);

    sf_free(x);
    sf_snapshot();
    sf_blockprint(x-8);
    /*
    void *x = sf_malloc(sizeof(double) * 8);

    sf_snapshot();
    sf_blockprint(x-8);
    void *y = sf_realloc(x, sizeof(int));

    if(y==NULL)
        printf("y is NULL!\n");//cr_assert_not_null(y, "y is NULL!");
    if(x!=y) //cr_assert(x == y, "Payload addresses are different!");
        printf("payload addresses are different!\n");

    sf_header *header = (sf_header*)((char*)y - 8);

    if(header->allocated!=1) //cr_assert(header->allocated == 1, "Allocated bit is not set!");
        printf("Allocated bit is not set!\n");

    //cr_assert(header->block_size << 4 == 48, "Block size not what was expected!");
    if(header->block_size<<4 !=32)
        printf("Block size not what was expected!\n");

    free_list *fl = &seg_free_list[find_list_index_from_size(4064)];

    // There should be only one free block of size 4048 in list 3
    //cr_assert_not_null(fl->head, "No block in expected free list!");
    if(fl->head==NULL)
        printf("No block in expected free list!\n");
    //cr_assert(fl->head->header.allocated == 0, "Allocated bit is set!");
    if(fl->head->header.allocated!=0)
        printf("Allocated bit is set!\n");
    //cr_assert(fl->head->header.block_size << 4 == 4048, "Free block size not what was expected!");

    if(fl->head->header.block_size<<4!=4064)
        printf("Free block size not what was expected!");
    */
    sf_mem_fini();
    return EXIT_SUCCESS;
}
