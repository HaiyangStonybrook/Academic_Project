#include "utils.h"
#include <errno.h>
#include <string.h>
#include "queue.h"
#include <stdio.h>


/*
hashmap_t *create_map(uint32_t capacity, hash_func_f hash_function, destructor_f destroy_function) {
    return NULL;
}

bool put(hashmap_t *self, map_key_t key, map_val_t val, bool force) {
    return false;
}

map_val_t get(hashmap_t *self, map_key_t key) {
    return MAP_VAL(NULL, 0);
}

map_node_t delete(hashmap_t *self, map_key_t key) {
    return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
}

bool clear_map(hashmap_t *self) {
	return false;
}

bool invalidate_map(hashmap_t *self) {
    return false;
}
*/

#define MAP_KEY(base, len) (map_key_t) {.key_base = base, .key_len = len}
#define MAP_VAL(base, len) (map_val_t) {.val_base = base, .val_len = len}
#define MAP_NODE(key_arg, val_arg, tombstone_arg) (map_node_t) {.key = key_arg, .val = val_arg, .tombstone = tombstone_arg}

int order=1;

map_node_t * lru(hashmap_t *self){
    map_node_t *ptr=self->nodes;
    map_node_t *result=ptr;
    for(int i=1; i<self->capacity; i++){
        if( (ptr+i)->order < result->order ){
            result= (ptr+i);
        }
    }
    return result;
}


hashmap_t *create_map(uint32_t capacity, hash_func_f hash_function, destructor_f destroy_function) {

    if(capacity<=0 || hash_function==NULL || destroy_function==NULL){
        errno=EINVAL;
        return NULL;
    }
    hashmap_t *map=calloc(1,sizeof(hashmap_t));
    if(map==NULL){
        errno=EINVAL;
        return NULL;
    }

    map->capacity=capacity;
    map->size=0;
    map->nodes=calloc(capacity,sizeof(map_node_t));
    map->hash_function=hash_function;
    map->destroy_function=destroy_function;
    map->num_readers=0;
    map->invalid=false;

    int w_lock=pthread_mutex_init(&map->write_lock,NULL);
    int f_lock=pthread_mutex_init(&map->fields_lock,NULL);
    if(w_lock!=0 || f_lock!=0){
        errno=EINVAL;
        return NULL;
    }

    return map;

}

bool put(hashmap_t *self, map_key_t key, map_val_t val, bool force) {

    if(self!=NULL){
         pthread_mutex_lock(&self->write_lock); // lock the write
    }

    if(self==NULL){
        errno=EINVAL;
        return false;
    }

    if( self->invalid==true || self->capacity==0){
         pthread_mutex_unlock(&self->write_lock);
        errno=EINVAL;

        return false;
    }
    if(key.key_base==NULL || key.key_len==0){
        errno=EINVAL;
         pthread_mutex_unlock(&self->write_lock);
        return false;
    }
    if(val.val_base==NULL || val.val_len==0){
        errno=EINVAL;
         pthread_mutex_unlock(&self->write_lock);
        return false;
    }

    if(self->size > self->capacity){
        errno=EINVAL;
         pthread_mutex_unlock(&self->write_lock);
        return false;
    }
    if(self->capacity==self->size && force==false){
        errno=ENOMEM;
         pthread_mutex_unlock(&self->write_lock);
        return false;
    }

    while(1){


    map_node_t *node= self->nodes;
    bool key_exist=false;

    if(self->size < self->capacity){  // not full check exist key
        for(int j=0;j<self->capacity; j++){
            if( (node+j)->key.key_len == key.key_len ){
                if(memcmp((node+j)->key.key_base, key.key_base,key.key_len )==0){

                    self->destroy_function((node+j)->key,(node+j)->val );

                    (node+j)->val.val_len=val.val_len;
                    (node+j)->val.val_base=val.val_base;
                    (node+j)->tombstone=false;
                    (node+j)-> time = clock() ;
                    (node+j) -> order= order++;

                    //return true;
                    key_exist=true;
                    break;
                }
            }
        }
    }

    if(self->size == self->capacity && force==true){  // full and force==true
        //int i=get_index(self,key);

        //self->destroy_function((node+i)->key,(node+i)->val );
        map_node_t* ptr=lru(self);
        self->destroy_function(ptr->key, ptr->val);

        ptr->key.key_len=key.key_len;
        ptr->key.key_base=key.key_base;
        ptr->val.val_len=val.val_len;
        ptr->val.val_base=val.val_base;
        ptr->tombstone=false;
        ptr-> time = time(NULL) ;
        ptr-> order= order++;

        //return true;
    }

    if(self->size<self->capacity && key_exist==false){   //not full, key not exist
        int i=get_index(self,key);
        if((node+i)->key.key_base==NULL && (node+i)->key.key_len==0){  // no collision
            (node+i)->key.key_len=key.key_len;
            (node+i)->key.key_base=key.key_base;
            (node+i)->val.val_len=val.val_len;
            (node+i)->val.val_base=val.val_base;
            self->size=self->size +1;
            (node+i)->tombstone=false;
            (node+i) -> time = time(NULL);

            (node+i) -> order=order++;
            //return true;
        }

        else{       //need linear probing

            int j=(i+1)%self->capacity;
            while(j!=i){
                if( (node+j)->key.key_base==NULL && (node+j)->key.key_len==0 ){
                    *(node+j)= MAP_NODE(MAP_KEY(key.key_base, key.key_len), MAP_VAL(val.val_base, val.val_len), false);
                    self->size++;
                    (node+j)->time=time(NULL);
                    (node+j)->order=order++;
                    break;
                }
                j=(j+1) % self->capacity;
            }
        }
    }
    pthread_mutex_unlock(&self->write_lock);
    return true;
}

}

map_val_t get(hashmap_t *self, map_key_t key) {

    if(self!=NULL){
            pthread_mutex_lock(&self->fields_lock); // lock the write
        }

    if(self==NULL){
        errno=EINVAL;
        map_val_t result;
        result.val_len=0;
        result.val_base=NULL;
        return result;
    }

    if(key.key_len==0 || key.key_base==NULL || self->invalid==true){
        errno=EINVAL;
        map_val_t result;
        result.val_len=0;
        result.val_base=NULL;
        pthread_mutex_unlock(&self->write_lock);
        return result;
    }

    while(1){
        //pthread_mutex_lock(&self->write_lock);
        //pthread_mutex_lock(&self->fields_lock); // P(&mutex)
        self->num_readers++;
        if(self->num_readers==1)
            pthread_mutex_lock(&self->write_lock); // P(&w)

        pthread_mutex_unlock(&self->fields_lock); // V(&mutex)

        map_node_t *node= self->nodes;
        map_val_t result;
        result.val_len=0;
        result.val_base=NULL;

        /*for(int i=0;i<self->capacity; i++){
            if( (node+i)->key.key_len == key.key_len ){
                if(memcmp((node+i)->key.key_base,key.key_base, key.key_len )==0 ){
                    result.val_len= (node+i)->val.val_len;
                    result.val_base = (node+i)->val.val_base;
                    break;
                }
            }
        }*/
        int index=get_index(self, key);
        if((node+index)->key.key_len==key.key_len){
            if(memcmp( (node+index)->key.key_base, key.key_base, key.key_len )==0  ){

                time_t t =time(NULL);
               // printf("clock: %f\n", (double)(t));

                //printf("clock: %f\n", (double)(((node+index) -> time)) );

                //printf("time: %f\n", (double) ( t-(node+index) -> time));

                if(t-(node+index) -> time >5){
                  //  printf("time excced\n");
                   // delete(self, (node+index)->key);
                    *(node+index)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), true);
                    self->size--;
                }

                else{
                    (node+index)->order=order++;

                }

               // printf("return ed\n");
                 result.val_len= (node+index)->val.val_len;
                result.val_base = (node+index)->val.val_base;

            }
        }

        else{
            int i= (index+1) % self->capacity;
            while( (node+i) != (node+index) ){
                if( (node+i)->key.key_len == key.key_len ){
                    if(memcmp((node+i)->key.key_base,key.key_base, key.key_len )==0 ){
                         time_t t =time(NULL);
                   // printf("clock: %f\n", (double)(t));

                   // printf("clock: %f\n", (double)(((node+i) -> time)) );

                   // printf("time: %f\n", (double) ( t-(node+i) -> time));


                        if(t-(node+i) -> time >5){
                            //delete(self, (node+i)->key);
                             *(node+i)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), true);
                            self->size--;
                      //      printf("time excced\n");
                        }

                        else{
                            (node+i)->order=order++;


                        }
                        result.val_len= (node+i)->val.val_len;
                        result.val_base = (node+i)->val.val_base;
                        break;
                    }
                }
                i = (i+1)%self->capacity;
            }
        }


    pthread_mutex_lock(&self->fields_lock); // P(&mutex)
    self->num_readers--;
    if(self->num_readers==0)
        pthread_mutex_unlock(&self->write_lock); // V(&w)

    pthread_mutex_unlock(&self->fields_lock); // V(&mutex)

    return result;

    }

}

map_node_t delete(hashmap_t *self, map_key_t key) {
    //return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
    if(self!=NULL){
         pthread_mutex_lock(&self->write_lock); // lock the write
    }

    if(self==NULL){
         errno=EINVAL;
        return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
    }

    if(key.key_len==0 || key.key_base==NULL || self->invalid==true){
        errno=EINVAL;
        pthread_mutex_unlock(&self->write_lock);
        return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
    }

    while(1){
        //pthread_mutex_lock(&self->write_lock);
        map_node_t *node= self->nodes;
        int index=get_index(self,key);
        map_node_t tem=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);

        if((node+index)->key.key_len==key.key_len){
            if(memcmp( (node+index)->key.key_base, key.key_base, key.key_len )==0  ){
                tem= MAP_NODE(MAP_KEY((node+index)->key.key_base, (node+index)->key.key_len),
                    MAP_VAL((node+index)->val.val_base, (node+index)->val.val_len), (node+index)->tombstone);

                *(node+index)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), true);
                self->size--;

            }
        }

        else{  // search probing
            int i= (index+1) % self->capacity;
            while((node+i)!= (node+index) ){
                if((node+i)->key.key_len==key.key_len){
                    if(memcmp( (node+i)->key.key_base, key.key_base, key.key_len )==0  ){

                        tem= MAP_NODE(MAP_KEY((node+i)->key.key_base, (node+i)->key.key_len),
                        MAP_VAL((node+i)->val.val_base, (node+i)->val.val_len), (node+index)->tombstone);

                        *(node+i)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), true);
                        //return tem;
                        self->size--;
                        break;
                    }
                }
                i=(i+1) % self->capacity;
            }
            if(tem.key.key_len==0 && tem.key.key_base==NULL)
                errno=EINVAL;

        }
        pthread_mutex_unlock(&self->write_lock);
        return tem;
    }
}

bool clear_map(hashmap_t *self) {
    if(self!=NULL){
         pthread_mutex_lock(&self->write_lock); // lock the write
    }

    if(self==NULL){
        errno=EINVAL;
        return false;
    }

    if(self->invalid==true){
        errno=EINVAL;
         pthread_mutex_unlock(&self->write_lock);
        return false;
    }

    while(1){
        //pthread_mutex_lock(&self->write_lock);
        map_node_t * node = self->nodes;
        for(int i=0; i< self->capacity; i++){
            if( (node+i)->key.key_base!=NULL || (node+i)->val.val_base!=NULL){
                //*(node+i)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
                self->destroy_function( (node+i)->key, (node+i)->val );
            }
        }

        pthread_mutex_unlock(&self->write_lock);
        return true;
    }

}

bool invalidate_map(hashmap_t *self) {
    if(self!=NULL){
         pthread_mutex_lock(&self->write_lock); // lock the write
    }

    if(self==NULL){
        errno=EINVAL;
        return false;
    }

    if(self->invalid==true){
        errno=EINVAL;
        pthread_mutex_unlock(&self->write_lock);
        return false;
    }

    while(1){
        //pthread_mutex_lock(&self->write_lock);
        map_node_t * node = self->nodes;
        for(int i=0; i< self->capacity; i++){
            if( (node+i)->key.key_base!=NULL || (node+i)->val.val_base!=NULL){
                //*(node+i)=MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
                self->destroy_function( (node+i)->key, (node+i)->val );
            }
        }

        free(self->nodes);
        self->invalid=true;

        pthread_mutex_unlock(&self->write_lock);
        return true;
    }

}
