/**
 * All functions you make for the assignment must be implemented in this file.
 * Do not submit your assignment with a main function in this file.
 * If you submit with a main function in this file, you will get a zero.
 */
#include "sfmm.h"
#include <stdio.h>
#include <stdlib.h>

/**
 * You should store the heads of your free lists in these variables.
 * Doing so will make it accessible via the extern statement in sfmm.h
 * which will allow you to pass the address to sf_snapshot in a different file.
 */

free_list seg_free_list[4] = {
    {NULL, LIST_1_MIN, LIST_1_MAX},
    {NULL, LIST_2_MIN, LIST_2_MAX},
    {NULL, LIST_3_MIN, LIST_3_MAX},
    {NULL, LIST_4_MIN, LIST_4_MAX}
};

/*
free_list seg_free_list[4] = {
    {NULL, LIST_4_MIN, LIST_4_MAX},
    {NULL, LIST_3_MIN, LIST_3_MAX},
    {NULL, LIST_2_MIN, LIST_2_MAX},
    {NULL, LIST_1_MIN, LIST_1_MAX}
};
*/
int sf_errno = 0;

static int page=0;

void sort_list(){
    int len=sizeof(seg_free_list)/sizeof(seg_free_list[0]);
    for(int i=1; i<len; i++){
        int key= seg_free_list[i].min;
        free_list tem=seg_free_list[i];
        int j=i-1;

        while(j>=0 && seg_free_list[j].min>key){
            seg_free_list[j+1]=seg_free_list[j];
            j=j-1;
        }
        seg_free_list[j+1]=tem;
    }
}

sf_footer *get_footer(sf_free_header * head){
    sf_footer *footer=(sf_footer*)(head+(head->header.block_size<<4)-8);
    return footer;
}

sf_free_header *find_list(size_t size){
    int len=sizeof(seg_free_list)/sizeof(seg_free_list[0]);
    size_t s=size;
    int i;
    sf_free_header *ptr;

    for(i=0; i<len; i++){
        //if( (s>=seg_free_list[i].min)&& (s<=seg_free_list[i].max)){ // find right list
                ptr= seg_free_list[i].head;
                while(ptr!=NULL){
                    if((ptr->header.block_size <<4) >= s){ // split this block
                    // ----------- find a free block ------
                        return ptr;
                    }
                    else
                        ptr=ptr->next;
                }

           // }
    }
    return NULL;
}

void set_page_HF(sf_free_header* first_header, int size){
    int len=sizeof(seg_free_list)/sizeof(seg_free_list[0]);
    first_header->header.allocated=0;
    first_header->header.padded=0;
    first_header->header.two_zeroes=0;
    first_header->header.block_size= (size>>4);
    first_header->header.unused=0;


    sf_footer* first_footer = get_heap_end();
    first_footer = get_heap_end()-8; //(sf_footer*)(first_header+4096-4);  //get_heap_start()+(4096*16)-8;//(sf_footer*)seg_free_list[3].head+(4096*16)-8;
    first_footer->allocated=0;
    first_footer->padded=0;
    first_footer->two_zeroes=0;
    first_footer->block_size= (size>>4);
    first_footer->requested_size=0;

    //sf_free_header *first_free_header=NULL;
    first_header->next=seg_free_list[len-1].head;
    //seg_free_list[len-1].head->prev=first_header;
    first_header->prev = NULL;

    seg_free_list[len-1].head = first_header;
}

void set_header(sf_free_header* header,int alloc, int size, bool padded){
    header->header.allocated=alloc;
    if(padded==true)
        header->header.padded=1;
    else
        header->header.padded=0;
    header->header.two_zeroes=0;
    header->header.block_size=(size>>4);
    header->header.unused=0;
}

void set_footer(sf_footer *footer, int alloc , int size, int requested_size, bool padded){
    footer->allocated=alloc;
    if(padded==true)
        footer->padded=1;
    else
        footer->padded=0;
    footer->two_zeroes=0;
    footer->block_size= (size>>4);
    footer->requested_size = requested_size;
}

void remove_block(sf_free_header* ptr){
    int len=sizeof(seg_free_list)/sizeof(seg_free_list[0]);
    int i;
    for (i=0; i<len; i++){
        sf_free_header*p =seg_free_list[i].head;
        while(p!=NULL){
            if(p==ptr){
                if(p->prev==NULL && p->next==NULL){
                    seg_free_list[i].head=NULL;
                    return;
                }
                else if(p->prev==NULL && p->next!=NULL){
                    seg_free_list[i].head=p->next;
                    return;
                }
                else if(p->prev!=NULL && p->next==NULL){
                    p->prev->next=NULL;
                    return;
                }
                else{
                    p->prev->next=p->next;
                    p->next->prev=p->prev;
                    return;
                }
            }
            p=p->next;
        }
    }
}

void insert_block(sf_free_header* ptr){
    int len=sizeof(seg_free_list)/sizeof(seg_free_list[0]);
    int i;
    int s = ptr->header.block_size<<4;

    for(i=0; i<len; i++){
        if( (s>=seg_free_list[i].min)&& (s<=seg_free_list[i].max)){ // find right list
               ptr->next=seg_free_list[i].head;
                if(seg_free_list[i].head!=NULL){
                    seg_free_list[i].head->prev=ptr;
                    seg_free_list[i].head=ptr;
                }
                else{
                    seg_free_list[i].head=ptr;
                    ptr->next=NULL;
                    ptr->prev=NULL;
                }


        }
    }

}

void cast_block(sf_free_header* find_head_ptr, int s, int size, bool padding){
    if((find_head_ptr->header.block_size<<4)-s <32 ){
                if(padding==true){
                    find_head_ptr->header.padded=1;
                    sf_footer *find_foot_ptr=get_footer(find_head_ptr);
                    find_foot_ptr->padded=1;
                }
                remove_block(find_head_ptr);

            }
            else{ // spliting block >32
                int block_sz =find_head_ptr->header.block_size<<4;
                int split_size = (find_head_ptr->header.block_size<<4)-s; // split block size
                sf_free_header* split_head = (void*)find_head_ptr+s; // split_head address
                sf_footer *footer = (void*) split_head-8;

                // set allocated block
                set_header(find_head_ptr, 1, s, padding);
                set_footer(footer,1,s,size,padding);
                // remove the whole block
                remove_block(find_head_ptr);

                // set rest free block
                set_header(split_head,0,split_size, false);
                sf_footer *split_footer = (void*) find_head_ptr+block_sz-8;//(sf_footer*)(find_head_ptr+ block_sz -8) ;
                set_footer (split_footer,0,split_size,0,false);
                insert_block(split_head);

            }

}

void check_valid(void *ptr){
    sf_header* header = (sf_header*) ptr;

    int header_b_size = header->block_size<<4;
    int header_pad = header->padded;
    int header_alloc = header->allocated;

    sf_footer* footer = (void*) ptr+header_b_size-8;
    int footer_b_size = footer->block_size<<4;
    int footer_r_size = footer->requested_size;
    int footer_pad = footer->padded;
    int footer_alloc = footer->allocated;

    //----------------------- invalid case ------------------------
    if(ptr==NULL || ptr<get_heap_start() || ptr>get_heap_end())
        abort();

    else if(header_alloc==0 || footer_alloc==0)
        abort();

    else if(header_pad!=footer_pad || header_alloc!=footer_alloc)
        abort();

    else if(header_b_size!=footer_b_size)
        abort();

    else if(footer_r_size+16 != footer_b_size && (footer_pad!=1 || header_pad!=1)){
            abort();
    }
    else if(footer_r_size+16 == footer_b_size && (footer_pad!=0 || header_pad!=0)){
            abort();
    }
}

void sf_colasing(sf_free_header* ptr){
    int header_b_size = ptr->header.block_size<<4;
    //int header_pad = header->padded;
    //int header_alloc = header->allocated;

    sf_footer* footer = (void*) ptr+header_b_size-8;

    sf_free_header* cur_header = ptr;

    sf_free_header* next_header = (void*) cur_header+ (header_b_size);
    int next_alloc = next_header->header.allocated;
    int next_b_size = next_header->header.block_size<<4;
    sf_footer* next_footer = (void*)next_header+next_b_size-8;

    //------------------- check next block allocated ---------------
        //---------------- next is free----------------
    if(next_alloc==0){
        remove_block(next_header);
        int new_b_size=header_b_size+next_b_size;
        set_header(cur_header,0,new_b_size,false);
        set_footer(next_footer,0,new_b_size,0,false);
        insert_block(cur_header);

    }
        //---------------- next is not free------------
    else{
        cur_header->header.allocated=0;
        footer->allocated=0;
        insert_block(cur_header);
    }
}

void *sf_malloc(size_t size) {
    //return NULL;
    sort_list();
    bool padding = false;
     size_t s=size;
    // ------------------------ invalid size ---------------------
    if(s<=0 || s>16384){
        sf_errno=EINVAL;
        return NULL;
    }
    // ------------------------ valid size ----------------------
    // ------------- 1.add the size of header and footer and padding ----------
    // --------------2. start searching list, if not found, give new page -------
    else{
        s=s+16; // add header and footer
        if(s<32){
            s=32;
            padding=true;
        }
        while( (s%16)!=0){
            s++;
            padding=true;
        }

        // -------------------search list-------------------
        sf_free_header *block=find_list(s);
        //--------------------search list----------------------
        // ------------- no free block is found, give page -----------

        while(block==NULL){
            void* sbrk = sf_sbrk();
            page++;

            //---------------page>4----------------------------------
            if(page>4){
                sf_errno=ENOMEM;
                return NULL;
            }

            // ------------------ only 1 page ------------------------
            if(page==1){
                sf_free_header* first_header=sbrk;
                set_page_HF(first_header, 4096);
            }

            // ------------------multi pages--------------------------
            //------------------check colasing with prev page------------
            else if(page>1){
                sf_footer *footer_ptr = sbrk-8;
                int prev_b_size = footer_ptr->block_size<<4;
                int new_b_size = 4096;
                if(footer_ptr->allocated==0){ // prev is free, colasing
                    sf_free_header *prev_block = (void*)sbrk-prev_b_size;
                    remove_block(prev_block);
                    sbrk=sbrk-(footer_ptr->block_size<<4);
                    new_b_size+=prev_b_size;
                }
                sf_free_header* first_header=sbrk;

                //set_page_HF(first_header, (4096+(footer_ptr->block_size<<4)));
                set_header(first_header,0,new_b_size,false);
                sf_footer* new_footer = get_heap_end()-8;
                set_footer(new_footer,0,new_b_size,0,false);
                insert_block(first_header);
            } // else if(page>1)

            // give a new page, start malloc
            //sf_free_header* find_head_ptr = find_list(s);
            block = find_list(s);

            // cast block
            //cast_block(block, s, size, padding);
        } //if(block==NULL)   }

       //else { // block!=NULL
            cast_block(block, s,size,padding);

        //}
        return (void*)block+8;
    } // else }



}

void *sf_realloc(void *ptr, size_t size) {
    //bool padding=false;
    sf_free_header* old_header= ptr-8;
	check_valid(old_header);
    if(size==0){
        sf_free(ptr);
        return NULL;
    }

    int ss=size;
    ss+=16;
    if(ss<32)
        ss=32;

    while(ss%16!=0){
        ss++;
        //padding=true;
    }
    int old_b_size= old_header->header.block_size<<4;
    sf_footer* old_footer=(void*)old_header+old_b_size-8;

    if( (size+16)==old_b_size)
        return ptr;

    //------------ realloc to larger size-----------------
    else if(ss>old_b_size){

        void* new_block = sf_malloc(size);
        if(new_block==NULL){
            return NULL;
        }
        memcpy(new_block, ptr, old_b_size-16);
        sf_free(ptr);
        return new_block;
    }

    //------------ realloc to smaller size----------------
    else if(ss<old_b_size){
        bool padding=false;
        int s=size;
        s=s+16;

        if(s<32){
            s=32;
            padding=true;
        }

        while(s%16!=0){
            s++;
            padding=true;
        }


        int split_size = old_b_size - s;
        // -----------split_size<32-----------
        if(split_size<32){
            old_footer->requested_size=size;
            //return ptr;
        }

        // -----------split_size>=32-----------
        else{
            //----------- set user block------------
            sf_footer *user_footer= ptr+s-16;//ptr+size;
            set_header(old_header,1,s,padding);
            set_footer(user_footer,1,s,size,padding);

            //-----------set free block-------------
            sf_free_header* split_header=(void*) user_footer+8;
            sf_footer *split_footer=(void*) old_header+old_b_size-8;
            set_header(split_header,0,split_size,false);
            set_footer(split_footer,0,split_size,0,false);

            sf_colasing(split_header);

        }

    }
    return ptr;
}

void sf_free(void *ptr) {

    sf_header* header = ptr-8;

    int header_b_size = header->block_size<<4;
    //int header_pad = header->padded;
    //int header_alloc = header->allocated;

    sf_footer* footer = (void*) header+header_b_size-8;
    //int footer_b_size = footer->block_size<<4;
    //int footer_r_size = footer->requested_size;
    //int footer_pad = footer->padded;
    //int footer_alloc = footer->allocated;

    check_valid(header);

    //--------------------- valid case ----------------------------
    //else{
        sf_free_header* cur_header = (sf_free_header*) header;

        sf_free_header* next_header = (void*) cur_header+ (header_b_size);
        int next_alloc = next_header->header.allocated;
        int next_b_size = next_header->header.block_size<<4;
        sf_footer* next_footer = (void*)next_header+next_b_size-8;

        //------------------- check next block allocated ---------------
            //---------------- next is free----------------
        if(next_alloc==0){
            remove_block(next_header);
            int new_b_size=header_b_size+next_b_size;
            set_header(cur_header,0,new_b_size,false);
            set_footer(next_footer,0,new_b_size,0,false);
            insert_block(cur_header);

        }
            //---------------- next is not free------------
        else{
            cur_header->header.allocated=0;
            footer->allocated=0;
            insert_block(cur_header);
        }

    //}

}
