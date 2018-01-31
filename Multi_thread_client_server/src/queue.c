#include "queue.h"
#include <errno.h>

queue_t *create_queue(void) {
    //return NULL;
    queue_t *queue=calloc(1,sizeof(queue_t));
    if(queue==NULL)
        return NULL;
    queue->front=NULL;
    queue->rear=NULL;
    queue->invalid=false;
    sem_init(&queue->items,0,0);
    int lock_init=pthread_mutex_init(&queue->lock,NULL);
    if(lock_init!=0)
        return NULL;

    return queue;
}

bool invalidate_queue(queue_t *self, item_destructor_f destroy_function) {

    if(self==NULL || destroy_function==NULL){
        errno=EINVAL;
        return false;
    }

     //sem_wait(&self->items);
    pthread_mutex_lock(&self->lock);

    if(self->invalid==true){
         errno=EINVAL;
         pthread_mutex_unlock(&self->lock);
        return false;
    }

    queue_node_t *ptr=self->front;

    if(ptr==NULL){
        self->invalid=true;
        pthread_mutex_unlock(&self->lock);
        return true;
    }

    else if(ptr->next==NULL){
        destroy_function(ptr->item);
        free(ptr);

        pthread_mutex_unlock(&self->lock);
        return true;
    }

    else{
        while(self->front->next!=NULL){
            queue_node_t* tem=self->front;
            self->front=self->front->next;
            destroy_function(tem->item);
            free(tem);
        }

        destroy_function(self->front->item);
        free(self->front);

        self->invalid=true;

        pthread_mutex_unlock(&self->lock);
        return true;
    }
}

bool enqueue(queue_t *self, void *item) {
    if(self==NULL||item==NULL){
        errno=EINVAL;
        return false;
    }

    //sem_wait(&self->items);
    pthread_mutex_lock(&self->lock);

    if(self->invalid==true){
        errno=EINVAL;
        pthread_mutex_unlock(&self->lock);
        return false;
    }

    queue_node_t *node=calloc(1,sizeof(queue_node_t));

    node->item=item;
    node->next=NULL;

    if(self->front==NULL){
        self->front=node;
        self->rear=node;
    }

    else{
        self->rear->next=node;
        self->rear=node;
    }

    sem_post(&self->items);
    pthread_mutex_unlock(&self->lock);
    return true;

}

void *dequeue(queue_t *self) {
    if(self==NULL){
        errno=EINVAL;
        return NULL;
    }

    sem_wait(&self->items);
    pthread_mutex_lock(&self->lock);

    if(self->invalid==true){
        errno=EINVAL;
        pthread_mutex_unlock(&self->lock);
        return NULL;
    }


    void* item_ptr=self->front->item;
    queue_node_t* ptr=self->front;
    if(self->front->next!=NULL)
        self->front=self->front->next;
    else{
        self->front=NULL;
        self->front=NULL;
    }

    free(ptr);
    //sem_post(&self->items);
    pthread_mutex_unlock(&self->lock);

    return item_ptr;
}
