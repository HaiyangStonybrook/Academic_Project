#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <readline/readline.h>
#include <sys/stat.h>
#include <signal.h>
#include <errno.h>
#include <sys/wait.h>
#include <fcntl.h>


#include "sfish.h"
#include "debug.h"


volatile sig_atomic_t pid;
volatile bool change_Gpid=true;

bool isAdd=false;
int jobCounter=1;

char buffer[128];
char inf[64];
char outf[64];

//struct pipe argument
struct line{
    char *prog[32];
    int size;
};

typedef struct line pipe_args;



// strcut for job node
struct job{
    int pidd;
    int jid;
    //int gpid;
    char name[128];

    //int job_his[128];
    bool active;
    struct job *next;
    //struct job *prev;
};

typedef struct job Job;

// struct for job list
struct job_list{
    Job *header;
    //Job *tail;
    int size;
};

typedef struct job_list jobList;


//init job list
jobList list={NULL,0};


void init_buffer(char str[]){
    int len=strlen(str);
    for(int i=0; i<len; i++){
        str[i]='\0';
    }
}

void sigchld_handler(int s){
    int olderrno = errno;
    int statusp;
    pid = waitpid (-1, &statusp,WUNTRACED);
    if(WIFSTOPPED(statusp)){
        //store the job
        isAdd=true;
    }
    errno = olderrno;

}

void sigint_handler(int s){
    //sigprocmask(SIG_BLOCK, &mask, &prev);
    signal(SIGINT, SIG_IGN);
}

void sigtstp_handler(int s){
    signal(SIGTSTP, SIG_IGN);

}

void addToList(Job* job){

    if(list.header==NULL){
        job->next=NULL;
        //newJob.prev=NULL;
        list.header=job;
        list.size++;
    }

    else{ //list is not null
        job->next=list.header;
        list.header=job;
        list.size++;
    }

}

bool deleteFromList(int jidd){
    //bool find=false;
    Job* cur;

    if(list.header!=NULL){
        Job *ptr=list.header;

        if(ptr->next == NULL){
            if( ptr->jid==jidd){
                cur=ptr;
                list.header=NULL;
                list.size--;
                free(cur);
                return true;
            }
            else
                return false;
        }
        else{
            if(ptr->jid==jidd){
                cur=ptr;
                list.header=ptr->next;
                list.size--;
                free(cur);
                return true;
            }

                while( (ptr->next)!=NULL){
                    if( (ptr->next)->jid==jidd){
                        cur=ptr->next;
                        ptr->next=ptr->next->next;
                        list.size--;
                        free(cur);
                        return true;
                    }
                    else{
                        ptr=ptr->next;
                    }

                }

            /*
            if( ptr->jid==jidd){
                    cur=ptr;
                    ptr=NULL;
                    list.size--;
                    free(cur);
                    return true;
                }
            */
            return false;
        }
    }
    return false;
}


bool is_repeat(char *str, char c){

    char *s1=str;
    char *s2=str+1;
    while(*s2!='\0'){
        if(*s1==*s2 && *s2==c)
            return true; //find
        s1++;
        s2++;
    }
    return false; // not find
}


char* ingore_space(char *str){
    int len=strlen(str);
    char *end=str+len-1;
    while(*end==' '){
        end--;
    }
    *(end+1)='\0';

    char *p=str;
    while(*p==' '){
        p++;
    }
    return p;
}

void get_argument(char str[],  char* arg[]){
     char* ptr=str;
     char* str_1 = strtok(ptr, " "); // first token of user input
        //char* st=strtok(input_ptr, " ");
        int i=0;

        while(str_1!=NULL){
            arg[i]=str_1;
            i++;
            str_1=strtok(NULL," ");
        }

        arg[i]=NULL;
}

/*
void get_pip_argument(char str[],  char* arg, char* arv[]){
    //char* ptr=str;
    strcpy(arg,str);
     char* str_1 = strtok(ptr, " "); // first token of user input
        //char* st=strtok(input_ptr, " ");
        int i=0;

        while(str_1!=NULL){
            //arg[i]=str_1;
            strcpy(arg[i],str_1);
            i++;
            str_1=strtok(NULL," ");
        }
        arg[i]=NULL;
}
*/

int open_infile(char infile[]) {
        int fd = open(infile, O_RDONLY, S_IRUSR|S_IWUSR);
        if (fd < 0) {
                printf(SYNTAX_ERROR,"can't open file");
                return -1;
        }

        dup2(fd, STDIN_FILENO);
        close(fd);
        return fd;
}

int open_outfile(char outfile[]) {
        int fd;
        //if(last == 1)
            fd = open(outfile, O_CREAT | O_WRONLY | O_TRUNC, S_IRUSR|S_IWUSR);
       // else if(last == 2)
        //    fd = open(outfile, O_CREAT | O_WRONLY | O_APPEND, S_IRWXU);

        //if(fd < 0) {
        //        perror(outfile);
        //}
        dup2(fd, STDOUT_FILENO);
        close(fd);
        return fd;
}


void parse_honky(char *input, char *arv[]){
    //printf("input: %s\n",input);
    input=ingore_space(input);
    //printf("input: %s\n",input);
    int left=0;
    int right=0;
    char* lp=0;
    char* rp=0;

    char* ptr=input;
    int len=strlen(input);
    if(*ptr=='<' ||*ptr=='>' )
        printf(SYNTAX_ERROR,"honky at head");
    else if(*(ptr+len-1)=='<' || *(ptr+len-1)=='>')
        printf(SYNTAX_ERROR,"honky at end");
    else if(is_repeat(input,'<')==true)
        printf(SYNTAX_ERROR,"left honky repeat");
    else if(is_repeat(input,'>')==true)
        printf(SYNTAX_ERROR,"right honky repeat");

    else{
       char *p=input;
       while( *p!='\0' ){
        if(*p=='<'){
            left++;
            lp=p;
        }

        if(*p=='>'){
            right++;
            rp=p;
        }

        p++;
       }

            if(left==1 && right==0){  //    <
                char* s=strtok(input,"<");
                strcpy(buffer,s);
                //printf("buffer :%s\n", buffer);
                s=strtok(NULL,"<");
                strcpy(inf,s);
                //char *infil=ingore_space(inf);
                strcpy(inf,ingore_space(inf));

                //printf("infile :%s\n", inf);
                get_argument(buffer,arv);
                //printf("arv[0] :%s\n", arv[0]);
                //printf("arv[1] :%s\n", arv[1]);
                //printf("arv[2] :%s\n", arv[2]);

                //redir_fork(arv,inf,outf);

            }

            else if(left==0 && right==1){  //   >
                 char* s=strtok(input,">");
                 strcpy(buffer,s);
                 //printf("buffer :%s\n", buffer);
                 s=strtok(NULL,">");
                 strcpy(outf,s);
                 strcpy(outf,ingore_space(outf));

                 //printf("outfile :%s\n", outf);


                 get_argument(buffer,arv);
                 //redir_fork(arv,inf,outf);

            }

            else if(left==1 && right==1 && lp >rp){ // >....<
                 char* s=strtok(input,">");
                 strcpy(buffer,s);
                 //printf("buffer :%s\n", buffer);
                 s=strtok(NULL,"<");
                 strcpy(outf,s);
                 strcpy(outf,ingore_space(outf));

                 s=strtok(NULL,"<");
                 strcpy(inf,s);
                 strcpy(inf,ingore_space(inf));

                 //printf("infile :%s\n", inf);
                 //printf("outfile :%s\n", outf);
                 get_argument(buffer,arv);
                 //redir_fork(arv,inf,outf);
            }

            else if(left==1 && right==1 && lp <rp){ // <....>
                char* s=strtok(input,"<");
                 strcpy(buffer,s);
                 //printf("buffer :%s\n", buffer);
                 s=strtok(NULL,">");
                 strcpy(inf,s);
                 strcpy(inf,ingore_space(inf));

                 s=strtok(NULL,">");
                 strcpy(outf,s);
                 strcpy(outf,ingore_space(outf));

                 //printf("infile :%s\n", inf);
                 //printf("outfile :%s\n", outf);
                 //printf("infile len: %d\n", (int)strlen(inf));
                 get_argument(buffer,arv);
                 //redir_fork(arv,inf,outf);
            }

            else{
                printf(SYNTAX_ERROR,"wrong honkies");
            }

    // open file ,dup, execvp
    }
}

void parse_pipe(char *input, pipe_args arv[]){
    input=ingore_space(input);
    char* ptr=input;
    int len=strlen(input);
    if(*ptr=='|')
        printf(SYNTAX_ERROR,"pipe");
    else if(*(ptr+len-1)=='|')
        printf(SYNTAX_ERROR,"pipe");
    else if(is_repeat(input,'|')==true)
        printf(SYNTAX_ERROR,"pipe");

    else{
        // split arguments
        char* ptr=input;
        char* str_1 = strtok(ptr, "|"); // first token of user input
        //char* st=strtok(input_ptr, " ");
        int i=0;
        while(str_1!=NULL){
            arv[0].prog[i]=str_1;
            //arv[i].prog=str_1;
            //strcpy(buffer,str_1);
            //ingore_space(buffer);
            //printf("arv[0].prog[%d]: %s\n",i ,str_1);
            //printf("str_1: %s\n", str_1);
            //char *p = *(arv[i].prog);
            //get_pip_argument(buffer,p);//arv[i].prog);
            i++;
            //arv->size= i;
            //str_1=ptr;
            //printf("str_1: %s\n", str_1);
            str_1=strtok(NULL,"|");
        }
        i=0;
        int j=1;
        char *prog_ptr=arv[0].prog[i];
        prog_ptr=strtok(arv[0].prog[i], " ");
        while(prog_ptr!=NULL){
            arv[j].prog[i]=prog_ptr;
            prog_ptr=strtok(NULL," ");
        }

    }

}



int main(int argc, char *argv[], char* envp[]) {
    //int idd=getpgrp();
    //int id=getpgrp();
    //printf("id: %d\n",id);

    //setpgid(0 ,0 );

    //id=getpgrp();
    //printf("id: %d\n",id);
    //printf("idd: %d\n",idd);
    // signal
    sigset_t mask, prev;

    signal(SIGCHLD, sigchld_handler);
    signal(SIGINT, sigint_handler);
    signal(SIGTSTP,sigtstp_handler);

    sigemptyset(&mask);
    sigaddset(&mask, SIGCHLD);
    //sigaddset(&mask,SIGTSTP);

    char* input;
    bool exited = false;
    char pwd[512];
    getcwd(pwd,sizeof(pwd));
    //printf("current dir: %s\n", pwd);

    char pwd_old[512];
    char pwd_tem[512];

    //pwd_old[0]='\0';
    init_buffer(pwd_old);
    init_buffer(pwd_tem);

    char envHome[512];
    strcpy(envHome,getenv("HOME"));
    //printf("Home: %s\n", envHome);


    char* front=NULL;
    char* tail=NULL;

    //chdir(envHome);
    getcwd(pwd,sizeof(pwd));
    char promp[512];    // = "~/haiyliu/hw4 :: haiyliu >>";
    char promp_old[512];
    char promp_tem[512];

    char color[512];
    init_buffer(color);

    init_buffer(promp);
    init_buffer(promp_old);
    init_buffer(promp_tem);

    strcpy(promp,pwd);
    if(strstr(pwd,envHome)!=NULL){
        //printf("hello\n");
       int lenHome=strlen(envHome);
       int lenPwd=strlen(pwd);
        memcpy(promp,"~",1);
        memcpy(promp+1,pwd+lenHome, lenPwd-lenHome);
        strcpy(promp+1+lenPwd-lenHome," :: haiyliu >>");
    }
    //promp
    //strcpy(promp_color,promp);

    if(!isatty(STDIN_FILENO)) {
        // If your shell is reading from a piped file
        // Don't have readline write anything to that file.
        // Such as the prompt or "user input"
        if((rl_outstream = fopen("/dev/null", "w")) == NULL){
            perror("Failed trying to open DEVNULL");
            exit(EXIT_FAILURE);
        }
    }
    //int count=0;


    do {
       // int l=strlen(promp);
       // printf("promp length: %d\n",l);

        bool isValid=false;

        char *pro = strtok(promp,"[");

        pro = strtok(NULL,"[");

        if(pro!=NULL){
            int l=strlen(pro);
            *(pro+l-1)='\0';
            pro=pro+5;
            strcat(pro,tail);

        //printf("with tail: %s\n",promp );
            init_buffer(color);
            strcpy(color,front);
            strcat(color,pro);
            strcpy(promp,color);


        }
        else if(pro==NULL && front!=NULL && tail != NULL ){
            init_buffer(color);

            strcat(promp,tail);

            strcpy(color,front);

            strcat(color,promp);

            strcpy(promp,color);

        }
    //}
        input = readline( promp );

/*
        write(1, "\e[s", strlen("\e[s"));
        write(1, "\e[20;10H", strlen("\e[20;10H"));
        write(1, "SomeText", strlen("SomeText"));
        write(1, "\e[u", strlen("\e[u"));
*/

        // If EOF is read (aka ^D) readline returns NULL

        char* input_ptr = input;     // user input string
        int length = strlen(input_ptr);

        char *arv[length];      // normal args
        //pipe_args pipeArgs[length]; // pipe args

//------------------------------------------ part 3 --------------------------------------------------
    if(strchr(input_ptr,'<')!=NULL || strchr(input_ptr,'>')!=NULL || strchr(input_ptr,'|')!=NULL){
        if(strchr(input_ptr,'|')==NULL){
            // go to <  >
            parse_honky(input_ptr, arv);

            //int in_fd=open_infile(inf);
            //int out_fd=open_outfile(outf);

            sigprocmask(SIG_BLOCK, &mask, &prev); // block sigchld

            if(fork()==0){
                int save_stdin=dup(0);
                int save_stdout=dup(1);
                int save_stderr=dup(2);

                //int in_fd=open_infile(inf);
                //int out_fd=open_outfile(outf);

                int res=open_infile(inf);
                if(res==-1)
                    exit(1);

                open_outfile(outf);

                //printf("in_fd: %d\n", in_fd);
                //printf("out_fd: %d\n", out_fd);
                signal(SIGINT, SIG_DFL);
                signal(SIGTSTP, SIG_DFL);

                sigprocmask(SIG_SETMASK, &prev, NULL);

                if(strcmp(arv[0],"help")==0){
                    printf("%7s  %7s\n","help", "List of all builtins.");
                    printf("%7s  %7s\n","cd", "Changes the current working directory of the shell.");
                    printf("%7s  %7s\n","pwd", "Prints the absolute path of the current working directory.");
                    printf("%7s  %7s\n","exit", "Exits the shell.");
                }

                else if(strcmp(arv[0],"pwd")){
                    printf("%s\n",pwd);
                }


                else{
                    if(execvp(arv[0], arv)<0){
                        dup2(save_stdin,0);
                        dup2(save_stdout,1);
                        dup2(save_stderr,2);
                        printf(EXEC_ERROR, arv[0]);
                    }
                }

                exit(0);
            }
            pid = 0;

            while (!pid)
                sigsuspend(&prev);

            if(isAdd==true){

                            //else{
                Job *job=malloc(200);
                job->pidd=pid;
                job->jid=jobCounter;
                jobCounter++;
                                //job->gpid=gpid;
                strcpy(job->name,arv[0]);


                addToList(job);
                isAdd=false;
            }

            sigprocmask(SIG_SETMASK, &prev, NULL);

        }

        else {
            // go to pipe
            int count=0;
          //  parse_pipe(input_ptr, pipeArgs);
             input_ptr=ingore_space(input_ptr);
            char* ptr=input;
            int len=strlen(input_ptr);
            char* ptr_arr[len];

            if(*ptr=='|')
                printf(SYNTAX_ERROR,"pipe");
            else if(*(ptr+len-1)=='|')
                printf(SYNTAX_ERROR,"pipe");
            else if(is_repeat(input,'|')==true)
                printf(SYNTAX_ERROR,"pipe");

            else{
                //--------count pipe------------
                int i=0;
                int token;
                while(*(input_ptr+i)!=0){
                    if(*(input_ptr+i)=='|')
                        count++;
                    i++;
                }
                //printf("pipe count: %d\n",count);

                char *str=strtok(input_ptr,"|");
                i=0;
                while(str!=NULL){
                    ptr_arr[i]=str;
                    //ingore_space(ptr_arr[i]);
                //    printf("ptr_arr[%d]: %s\n",i, ptr_arr[i]);
                    i++;
                    str=strtok(NULL,"|");
                }
                ptr_arr[i]=NULL;
                token=i;

                // get_argument(ptr_arr[1],arv);
                // printf("arv[0]: %s\n", arv[0]);
                //  printf("arv[1]: %s\n", arv[1]);
                //   printf("arv[2]: %s\n", arv[2]);
                //    printf("token: %d\n", token);

                if(i-count==1){

                    //int save_stdin=0;
                    //int save_stdout=1;
                    //int save_stderr=2;
                    int in,out;
                    int fd[2]; // fd[0]: read side ;  fd[1]: write side
                   // int pc[2]; //check invalid arugment
                    //bool first=true;
                    int i=0;
                    //int yes;
                    for(i=0; i<token-1;i++){
                      //  yes=1;
                        pipe(fd);
                       // pipe(pc);

                        sigprocmask(SIG_BLOCK, &mask, &prev); // block sigchld

                        out =fd[1];

                         if(fork()==0){

                            sigprocmask(SIG_SETMASK, &prev, NULL);
                            get_argument(ptr_arr[i],arv);
                            // printf("arv[0]: %s\n", arv[0]);
                            //  printf("arv[1]: %s\n", arv[1]);
                            //   printf("arv[2]: %s\n", arv[2]);
                            //    printf("i: %d\n", i);

                            /*
                            if(i==0){
                                dup2(save_stdin,0);
                                //first=false;
                                dup2(out,1);
                            }

                            else if(i==token-1){
                                dup2(in,1);
                                dup2(save_stdout,1);
                            }
                            */
                            //else{
                                if (in!=0){
                                    dup2(in,0); // change std in
                                    close(in);
                                }
                                if (out!=1){
                                    dup2(out,1); //change std out
                                    close(out);
                                }
                            //}


                            if(execvp(arv[0], arv)<0){
                                //dup2(save_stdin,0);
                                //dup2(save_stdout,1);
                                //dup2(save_stderr,2);
                                //
                               // yes=-1;
                               // write(pc[0],&yes,sizeof(yes));
                               // close(pc[0]);
                                printf(EXEC_ERROR, arv[0]);


                            }

                             exit(0);
                        }

                        pid = 0;

                        while (!pid)
                            sigsuspend(&prev);


                        if(isAdd==true){

                            //else{
                            Job *job=malloc(200);
                            job->pidd=pid;
                            job->jid=jobCounter;
                            jobCounter++;
                                //job->gpid=gpid;
                            strcpy(job->name,arv[0]);


                            addToList(job);
                            isAdd=false;
                        }

                        sigprocmask(SIG_SETMASK, &prev, NULL);

                        /*
                        read(pc[1],&yes,sizeof(yes));
                        printf("yes: %d\n", yes);

                        if(yes==-1){
                            //dup2(save_stdout,1);
                            printf(EXEC_ERROR, arv[0]);
                            break;
                        }
                        */
                        close(fd[1]);

                        in=fd[0];


                    } //for(i=0; i<token-1;i++)


                    //if(yes==1){
                     sigprocmask(SIG_BLOCK, &mask, &prev); // block sigchld
                     if(fork()==0){
                         if (in != 0)
                            dup2 (in, 0);
                        sigprocmask(SIG_SETMASK, &prev, NULL);
                        get_argument(ptr_arr[i],arv);
                        if(execvp(arv[0],arv)){
                            printf(EXEC_ERROR, arv[0]);
                        }
                        exit(0);
                    }

                    pid = 0;

                        while (!pid)
                            sigsuspend(&prev);


                        if(isAdd==true){

                            //else{
                            Job *job=malloc(200);
                            job->pidd=pid;
                            job->jid=jobCounter;
                            jobCounter++;
                                //job->gpid=gpid;
                            strcpy(job->name,arv[0]);


                            addToList(job);
                            isAdd=false;
                        }

                        sigprocmask(SIG_SETMASK, &prev, NULL);

                   // } // if(yes==true)

                } //if(i-count==1)

                else{
                    //printf("i: %d\n", i);
                    //printf("count: %d\n", count);
                    printf(SYNTAX_ERROR, "pipe and argument not match");
                }


            }
        }
    }


// -------------------------- check builtins -------------------------------

    else{
        char* str_1 = strtok(input_ptr, " "); // first token of user input
        //char* st=strtok(input_ptr, " ");
        int i=0;

        while(str_1!=NULL){
            arv[i]=str_1;
            i++;
            str_1=strtok(NULL," ");
        }

        arv[i]=NULL;

        if(input == NULL) {
            isValid=true;
            continue;
        }

        else if(arv[0]==NULL){
            isValid=true;
            continue;
        }

        else if(strcmp(arv[0],"help")==0){            // "help"
            isValid=true;
            printf("%7s  %7s\n","help", "List of all builtins.");
            printf("%7s  %7s\n","cd", "Changes the current working directory of the shell.");
            printf("%7s  %7s\n","pwd", "Prints the absolute path of the current working directory.");
            printf("%7s  %7s\n","exit", "Exits the shell.");
        }

        else if(strcmp(arv[0],"cd")==0){         // "cd"
            isValid=true;
            //char* str_2= strtok(NULL, " ");

            if(arv[1]==NULL || strcmp(arv[1],"~")==0){    // cd with no arguments
                strcpy(pwd_old,pwd); // pwd_old
                strcpy(promp_old, promp);

                chdir(envHome);
                getcwd(pwd,sizeof(pwd));
                strcpy(promp, pwd);
                if(strcmp(promp, envHome)==0){
                    strcpy(promp,"~ :: haiyliu >> ");

                }

            }
            else if(strcmp(arv[1], "-")==0){       // "-"
                isValid=true;
                char* pwd_old_ptr=pwd_old;
                if(*pwd_old_ptr=='\0'){
                    printf(BUILTIN_ERROR,"no Oldset path");
                }

                else{
                    strcpy(pwd_tem,pwd);
                    strcpy(promp_tem,promp);

                    strcpy(pwd, pwd_old);
                    strcpy(promp, promp_old);

                    strcpy(pwd_old,pwd_tem);
                    strcpy(promp_old,promp_tem);


                    //strcpy(pwd,pwd_old);
                    chdir(pwd);
                    getcwd(pwd,sizeof(pwd));
                    //strcpy(promp, promp_old);
                    printf("%s\n",pwd);
                }

            }

            else if(strcmp(arv[1], "..")==0){    // "."
                strcpy(pwd_old,pwd);
                strcpy(promp_old, promp);

                chdir("..");
                getcwd(pwd,sizeof(pwd));
                strcpy(promp, pwd);


                if(strcmp(promp, envHome)==0){
                    strcpy(promp,"~ :: haiyliu >> ");

                }

                else if(strstr(promp, envHome)!=NULL){
                    char* ptr=strstr(promp, envHome)+strlen(envHome);
                    char* promp_ptr=promp+1;

                    strcpy(promp_ptr,ptr);
                    strcpy(promp_ptr+strlen(ptr)," :: haiyliu >> ");
                    memcpy(promp,"~",1);
                }

                else{
                    char* promp_ptr= promp+strlen(pwd);
                    strcpy(promp_ptr, " :: haiyliu >> ");

                }


            }
            else if(strcmp(arv[1], ".")==0){   // ".."
                strcpy(pwd_old,pwd);
                strcpy(promp_old, promp);
                continue;

            }

            else{

                if(strstr(arv[1], "~/")!=NULL){
                    arv[1]=strstr(arv[1], "~/")+2;
                }

                if(chdir(arv[1])==0){
                    strcpy(pwd_old,pwd);
                    strcpy(promp_old, promp);

                    getcwd(pwd,sizeof(pwd));
                    strcpy(promp, pwd);

                    if(strstr(promp, envHome)!=NULL){
                        char* ptr=strstr(pwd, envHome)+strlen(envHome);

                        strcpy(promp+1,ptr);
                        strcpy(promp+1+strlen(ptr)," :: haiyliu >> ");
                        memcpy(promp,"~",1);
                    }

                    else{
                        strcpy(promp+strlen(pwd) ," :: haiyliu >> ");
                    }

                }

                else{
                    printf(BUILTIN_ERROR,"No such file or directory");
                }
            }
        }

        else if(strcmp(arv[0], "pwd")==0){       // "pwd"
            isValid=true;
            printf("%s\n", pwd);
        }

        else if(strcmp(arv[0], "exit")==0){      // exit
            if(list.header!=NULL){
                Job *ptr=list.header;
                if(ptr->next==NULL){
                    kill( (ptr->pidd),SIGKILL);
                }
                else{
                    while(ptr->next!=NULL){
                        kill( (ptr->pidd),SIGKILL );
                        ptr=ptr->next;
                    }
                    kill( (ptr->pidd),SIGKILL );
                }
            }
            isValid=true;
            exited=true;
        }


        else if(strcmp(arv[0], "color")==0){
            isValid=true;
            if(arv[1]==NULL){
                printf("Color need to be specified");
            }

            else{
            if(strcmp(arv[1], "RED")==0){
                    front="\033[1;31m";
                    tail="\033[0m";
            }

            else if(strcmp(arv[1], "GRN")==0){
                front="\033[1;32m";
                tail="\033[0m";
            }

            else if(strcmp(arv[1], "YEL")==0){
               tail="\033[0m";
               front="\033[1;33m";
            }

            else if(strcmp(arv[1], "BLU")==0){
                tail="\033[0m";
                front="\033[1;34m";
            }

            else if(strcmp(arv[1], "MAG")==0){
                tail="\033[0m";
                front ="\033[1;35m";
            }

            else if(strcmp(arv[1], "CYN")==0){
                tail="\033[0m";
                front="\033[1;36m";
            }

            else if(strcmp(arv[1], "WHT")==0){
                tail="\033[0m";
                front="\033[1;37m";
            }

            else if(strcmp(arv[1], "BWN")==0){
                tail="\033[0m";
                front="\033[0;33m";
            }

            else{
                printf("color argument is not found\n");
            }
            }
        }

     // -------------------------------- part 4 -------------------------------------
        //--------------------- jobs -----------------------------
     else if(strcmp(arv[0],"jobs")==0){
        isValid=true;
        Job *ptr=list.header;
        while(ptr!=NULL){
            int j=ptr->jid;
            char* n=ptr->name;
            printf(JOBS_LIST_ITEM,j,n);
            ptr=ptr->next;
        }
     }
     //--------------------------- kill PID, kill %JID ------------------------
    else if(strcmp(arv[0],"kill")==0){
        isValid=true;
        if(arv[1]!=NULL){
            if(strchr(arv[1],'%')!=NULL){
                bool valid=true;
                char *ptr=arv[1];
                ptr++;

                char  *check_ptr=ptr;
                while(*check_ptr!='\0'){
                    if(*check_ptr<'0' || *check_ptr>'9'){
                        printf(BUILTIN_ERROR, "invalid JID");
                        valid=false;
                        break;
                    }
                    check_ptr++;
                }

                if(valid==true){
                    int sum=atoi(ptr);
                    //printf("sum: %d\n", sum);
                    int pi=-1;

                    Job *p=list.header;
                    while(p!=NULL){
                        if(p->jid==sum){
                            pi=p->pidd;
                            break;
                        }
                        p=p->next;
                    }
                    //printf("pid: %d\n", pi);
                    //if(kill(pid,SIGKILL));
                    bool remove=deleteFromList(sum);
                    if(remove==false){
                        printf(BUILTIN_ERROR, "JID not exist");
                    }
                    else{
                        kill(pi,SIGKILL);
                    }
                }
            }

            else{       // kill pid
                //printf("pid: %d\n", pid);
                bool valid=true;
                char *ptr=arv[1];

                char  *check_ptr=ptr;
                while(*check_ptr!='\0'){
                    if(*check_ptr<'0' || *check_ptr>'9'){
                        printf(BUILTIN_ERROR, "invalid PID");
                        valid=false;
                        break;
                    }
                    check_ptr++;
                }

                if(valid==true){
                    int sum=atoi(ptr);
                    //printf("sum: %d\n", sum);
                    if(kill(sum,SIGKILL)!=0){
                         printf(BUILTIN_ERROR, "PID not exist");
                    }
                    else{
                        int jidd=-1;
                        Job *p=list.header;
                        while(p!=NULL){
                            if(p->pidd==sum){
                                jidd=p->jid;
                                break;
                            }
                            p=p->next;
                        }
                        //printf("pid: %d\n", pi);
                        //if(kill(pid,SIGKILL));
                        bool remove=deleteFromList(jidd);
                        if(remove){
                            ;
                        }
                    }
                }
            }
        }
        else{
            printf(BUILTIN_ERROR,"kill without argument");
        }
     }

     // ------------------------------fg %JID-------------------------------
     else if(strcmp(arv[0],"fg")==0){
        isValid=true;
        if(arv[1]!=NULL && strchr(arv[1],'%')!=NULL){
            bool valid=true;
            char *ptr=arv[1];
            ptr++;

            char  *check_ptr=ptr;
            while(*check_ptr!='\0'){
                if(*check_ptr<'0' || *check_ptr>'9'){
                    printf(EXEC_ERROR, "invalid JID");
                    valid=false;
                    break;
                }
                check_ptr++;
            }

            if(valid==true){
                int sum=atoi(ptr);
                //printf("sum: %d\n", sum);
                int pi=-1;

                Job *p=list.header;
                while(p!=NULL){
                    if(p->jid==sum){
                        pi=p->pidd;
                        break;
                    }
                    p=p->next;
                }
                    //printf("pid: %d\n", pi);
                    //if(kill(pid,SIGKILL));
                bool remove=deleteFromList(sum);
                if(remove==false){
                    printf(BUILTIN_ERROR, "JID not exist");
                }
                else{
                    //printf("pgid1: %d\n", getpgrp());
                    //int gid=getpgid(pi);
                    //printf("gid: %d\n", getpgrp());
                    //setpgid(getpid(pi),getpid(pi));
                    //int gp=getpgid(pi);
                    //printf("gp_cat: %d\n", gp);
                    //printf("pgid: %d\n", getpgrp());
                    //tcsetpgrp(1,gp);

                    kill(pi,SIGCONT);
                    //waitpid(0,NULL,0);


                    //signal(SIGTSTP,SIG_DFL);
                    //printf("pgid2: %d\n", getpgrp());
                    //printf("kill %d\n",i);
                    //printf("success\n");

                    //
                    //waitpid(0,NULL,WUNTRACED);

                }
            }


        }

        else{   // no argument
            printf(BUILTIN_ERROR, "argument not exist");
        }
     }


    //----------------------------------- part 2 ---------------------------------------
        else{
                                               // not builtins command
            //printf(EXEC_NOT_FOUND, input);
            struct stat sta;
            // --------------- contain / ------------------
            if(strchr(arv[0], '/')!=NULL){
                if(stat(arv[0], &sta)!=0){
                    isValid=true;
                    printf(EXEC_NOT_FOUND,arv[0]);
                }

                else{
                    if(S_IXUSR &sta.st_mode){
                        isValid=true;

                        sigprocmask(SIG_BLOCK, &mask, &prev); // block sigchld
                        if(fork()==0){
                            /*
                            if(change_Gpid==true){

                                if(setpgid(getpid(),getpgrp())==-1){
                                    printf(EXEC_ERROR,"fork");
                                }
                            }
                            change_Gpid=false;
                            */

                            //sigprocmask(SIG_SETMASK, &mask, NULL); // unblock
                            signal(SIGINT, SIG_DFL);
                            signal(SIGTSTP, SIG_DFL);

                            sigprocmask(SIG_SETMASK, &prev, NULL);
                            execvp(arv[0], arv);
                            exit(0);
                        }
                        // wait for sigchld
                        pid = 0;
                        // sigprocmask(SIG_SETMASK, &mask, NULL); // unblock
                        while (!pid)
                            sigsuspend(&prev);

                        if(isAdd==true){

                            //else{
                                Job *job=malloc(200);
                                job->pidd=pid;
                                job->jid=jobCounter;
                                jobCounter++;
                                //job->gpid=gpid;
                                strcpy(job->name,arv[0]);


                                addToList(job);
                                isAdd=false;
                            //}

                        }

                        // optionally unblock sigchld
                        sigprocmask(SIG_SETMASK, &prev, NULL);

                    // printf("receive SIGCHLD\n");
                    }
                    else{
                        printf(EXEC_ERROR,arv[0]);
                    }
                }
            }

            //---------------- not contain / --------------
            else{
                //bool bg=false;
                //int gd=0;
                char envPath[512];
                init_buffer(envPath);
                strcpy(envPath,getenv("PATH"));

                char *path = strtok(envPath, ":");
                char path_buffer[512];

                // create pipe

                /*
                int fd[2];  //fd[0]: parent  ,, fd[1]: child
                pipe(fd);
                */


                while(path!=NULL){

                    init_buffer(path_buffer);
                    strcpy(path_buffer, path);
                    strcat(path_buffer, "/");
                    strcat(path_buffer,arv[0]);

                    if(stat(path_buffer,&sta)==0){

                        if(S_IXUSR &sta.st_mode){

                            isValid=true;
                            sigprocmask(SIG_BLOCK, &mask, &prev); // block sigchld


                            if(fork()==0){
                            //sigprocmask(SIG_SETMASK, &mask, NULL); // unblock

                                //close(fd[0]);
                            /*
                                if(change_Gpid==true){
                                printf("fork pid: %d\n", getpid());  //test
                                //if(setpgid(get() , getpid())==-1){
                                //    printf(EXEC_ERROR,"fork");
                                //}

                                id=getpgrp();
                                //write(fd[1], &id,sizeof(id));
                               // printf("id in fork: %d\n", id);
                                //close(fd[1]);

                                }
                               // printf("fork pgrp: %d\n", getpgrp());  //test

                                change_Gpid=false;

                                //if(! tcsetpgrp(id,0)){
                                //printf("tcsetpgrp fail");
                           //}  //test

                            */
                                signal(SIGINT, SIG_DFL);
                                signal(SIGTSTP, SIG_DFL);
                                sigprocmask(SIG_SETMASK, &prev, NULL);

                                //if(! tcsetpgrp(1,d)){
                                //printf("tcsetpgrp fail");
                           //}  //test

                                execvp(path_buffer, arv);
                                exit(0);
                            }

                            // wait for sigchld

                            pid = 0;
                            //sigprocmask(SIG_SETMASK, &mask, NULL); // unblock
                            while (!pid)
                                sigsuspend(&prev);


                                // close(fd[1]);
                            //read(fd[0], &id,sizeof(id));
                            //close(fd[0]);

                                if(isAdd==true){
                                Job *job=malloc(200);
                                job->pidd=pid;
                                job->jid=jobCounter;
                                jobCounter++;
                                //job->gpid=gpid;
                                strcpy(job->name,arv[0]);

                                addToList(job);
                                isAdd=false;
                        }

                            // optionally unblock sigchld
                            sigprocmask(SIG_SETMASK, &prev, NULL);

                            //tcsetpgrp(1,id);           //test
                            //printf("receive SIGCHLD\n");
                            //break;

                            //printf("cur_grep: %d\n",getpgrp());
                            // printf("id: %d\n", id);
                             //printf("idd: %d\n", idd);


                           //else{
                            //waitpid(0,NULL,WUNTRACED);
                            //kill(id,SIGCONT);
                           //}


                            break;
                        }
                        //else{
                         //   printf(EXEC_ERROR,path_buffer);
                        //}

                    }

                    else{
                        path=strtok(NULL, ":");
                        //if(path==NULL){
                        //    printf(EXEC_NOT_FOUND,path_buffer);
                        //}
                    }
                }
            }

        }// part 2

        if(isValid==false)
            printf(EXEC_NOT_FOUND,arv[0]);

    }//else after part 4

        // You should change exit to a "builtin" for your hw.
        //exited = strcmp(input, "exit") == 0;

        // Readline mallocs the space for input. You must free it.
        rl_free(input);

    } while(!exited);
    debug("%s", "user entered 'exit'");
    exit(EXIT_SUCCESS);
    //return EXIT_SUCCESS;
}
