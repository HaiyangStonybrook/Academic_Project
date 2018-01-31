
#include "cream.h"
#include "utils.h"
#include "queue.h"
#include <stdio.h>

#include "csapp.h"


queue_t *global_queue;
hashmap_t *global_map;

void *thread(void *vargp);
void handler(int connfd);

void destroy_function(map_key_t key, map_val_t val) {
    free(key.key_base);
    free(val.val_base);
}


int main(int argc, char *argv[]) {
    // queue_t *global_queue = create_queue();
    //printf("%p\n",global_queue);
    //exit(0);


    int i, listenfd, connfd;
    socklen_t clientlen;
    struct sockaddr_storage clientaddr;
    pthread_t tid;

    if(argc<2){
        printf("invalid argument\n");
        exit(EXIT_FAILURE);
    }

    for(int k=0; k<argc; k++){
        if(strcmp(argv[k], "-h")==0){
            printf("-h      Displays this help menu and returns EXIT_SUCCESS.\n");
            printf("NUM_WORKERS      The number of worker threads used to service requests.\n");
            printf("MAX_ENTRIES        The maximum number of entries that can be stored in `cream`'s underlying data store.\n");
            printf("MAX_ENTRIES        The maximum number of entries that can be stored in `cream`'s underlying data store.\n");
            exit(EXIT_SUCCESS);
        }
    }

    int num_thread=atoi(argv[1]);
    char* port_number= argv[2];
    int max_entries= atoi(argv[3]);

    if(num_thread<1){
        printf("invalid number of thread\n");
        exit(EXIT_FAILURE);
    }

    if(max_entries<1){
        printf("invalid number of entries\n");
        exit(EXIT_FAILURE);
    }

    listenfd = Open_listenfd(port_number);
    if(listenfd<0){
        printf("invalid port number");
        exit(EXIT_FAILURE);
    }



    global_queue = create_queue();
    global_map = create_map(max_entries, jenkins_one_at_a_time_hash, destroy_function);

    for(i=0; i<num_thread; i++){        //creat worker thread
        pthread_create(&tid, NULL, thread, NULL);

    }

    while(1){
        clientlen=sizeof(struct sockaddr_storage);
        connfd=Accept(listenfd, (SA*) &clientaddr, &clientlen);
        int *conn = malloc(sizeof(int));
        *conn=connfd;

        enqueue(global_queue, conn);
    }


}

void *thread(void *vargp){
    pthread_detach(pthread_self());
    while(1){
        int* conn=malloc(sizeof(int));
         conn= dequeue(global_queue);
         signal(EPIPE, SIG_IGN);
         signal(SIGPIPE, SIG_IGN);
         signal(EINTR, SIG_IGN);

         int connfd=*conn;
        // ----- customize fun
        handler(connfd);
        //------------------
        free(conn);
        Close(connfd);
    }
}


void handler(int connfd){
    request_header_t request_header;
    char* key_buf;
    char* value_buf;
    Rio_readn(connfd, &request_header, sizeof(request_header));

    if(request_header.request_code == PUT ){

        if(request_header.key_size< MIN_KEY_SIZE || request_header.key_size>MAX_KEY_SIZE
            || request_header.value_size< MIN_VALUE_SIZE ||request_header.value_size> MAX_VALUE_SIZE ){

            response_header_t response_header  = {BAD_REQUEST,0};
            Rio_writen(connfd, &response_header,sizeof(response_header));
            return;
        }

        else{
            key_buf=Calloc(1, request_header.key_size+1 );
            value_buf=Calloc(1, request_header.value_size+1 );
            Rio_readn(connfd, key_buf, request_header.key_size);
            Rio_readn(connfd, value_buf, request_header.value_size);

            if(put(global_map, MAP_KEY(key_buf, request_header.key_size),
                MAP_VAL(value_buf, request_header.value_size), true)==false){
                response_header_t response_header  = {BAD_REQUEST,0};
                Rio_writen(connfd, &response_header,sizeof(response_header));
                return;
            }
            response_header_t response_header = {OK, request_header.value_size};
            Rio_writen(connfd, &response_header,sizeof(response_header));
            return;


        }
    }

    if(request_header.request_code==GET){
         if(request_header.key_size< MIN_KEY_SIZE || request_header.key_size>MAX_KEY_SIZE){
            response_header_t response_header  = {BAD_REQUEST,0};
            Rio_writen(connfd, &response_header,sizeof(response_header));
            return;
         }

         else{
            key_buf=Calloc(1, request_header.key_size+1 );
            Rio_readn(connfd, key_buf, request_header.key_size);
            map_val_t val = get(global_map, MAP_KEY(key_buf, request_header.key_size));
            if(val.val_base==NULL && val.val_len==0){
                response_header_t response_header  = {NOT_FOUND,0};
                Rio_writen(connfd, &response_header,sizeof(response_header));
                return;
            }

            response_header_t response_header  = {OK, val.val_len};
            Rio_writen(connfd, &response_header,sizeof(response_header));
            Rio_writen(connfd, val.val_base, val.val_len);

            return;
         }
    }

    if(request_header.request_code==EVICT){
        if(request_header.key_size< MIN_KEY_SIZE || request_header.key_size>MAX_KEY_SIZE){
            response_header_t response_header  = {OK, 0};
            Rio_writen(connfd, &response_header,sizeof(response_header));
            return;
        }
        else{
            key_buf=Calloc(1, request_header.key_size+1 );
            Rio_readn(connfd, key_buf, request_header.key_size);
            map_node_t val = delete(global_map, MAP_KEY(key_buf, request_header.key_size));

            if(val.val.val_base==NULL && val.val.val_len==0){
                response_header_t response_header  = {OK, 0};
                Rio_writen(connfd, &response_header,sizeof(response_header));
                return;
            }

            else{
                response_header_t response_header  = {OK, 0};
                Rio_writen(connfd, &response_header,sizeof(response_header));
                return;
            }

        }
    }

    if(request_header.request_code==CLEAR){
        clear_map(global_map);
        response_header_t response_header  = {OK, 0};
        Rio_writen(connfd, &response_header,sizeof(response_header));
        return;
    }


}

