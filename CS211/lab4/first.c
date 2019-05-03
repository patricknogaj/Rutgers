#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct block {
	unsigned long int tag;
	int valid;
	unsigned long int time;
} block;

block **cache;
int hit;
int miss;
int read;
int write;
int count;

block** createCache(int numSet, int assoc) {
	int i, j;
	cache = malloc(numSet * sizeof(block*));
	for(i = 0; i < numSet; i++) {
		cache[i] = malloc(assoc * sizeof(block));
	}
	for(i = 0; i < numSet; i++) {
		for(j = 0; j < assoc; j++) {
			cache[i][j].valid = 0;
		}
	}
	return cache;
}

void clear_cache(int numSet, int assoc) {
	int i, j;
	for(i = 0; i < numSet; i++) {
		for(j = 0; j < assoc; j++) {
			cache[i][j].tag = 0;
			cache[i][j].valid = 0;
			cache[i][j].time = 0;
		}
	}
	hit = miss = read = write = count = 0;
}


void cache_read(unsigned long int tagIndex, unsigned long int setIndex, int assoc) {
	int i, j, min;
	for(i = 0; i < assoc; i++) {
		if(cache[setIndex][i].valid == 0) {
			miss++;
			read++;
			count++;
			cache[setIndex][i].valid = 1;
			cache[setIndex][i].tag = tagIndex;
			cache[setIndex][i].time = count;
			return;
		} else {
			if(cache[setIndex][i].tag == tagIndex) {
				hit++;
				count++;
				cache[setIndex][i].time = count;
				return;
			}

			if(i == (assoc - 1)) {
				miss++;
				read++;
				
				min = 0; 
				for(j = 0; j < assoc; j++) {
					if(cache[setIndex][j].time <= cache[setIndex][min].time) {
						min = j;
					}
				}

				cache[setIndex][min].valid = 1;
				cache[setIndex][min].tag = tagIndex;
				count++;
				cache[setIndex][min].time = count;
				return;
			}
		}
	}
	return;
}

void cache_write(unsigned long int tagIndex, unsigned long int setIndex, int assoc) {
	int i, j, min;
	for(i = 0; i < assoc; i++) {
		if(cache[setIndex][i].valid == 0) {
			miss++;
			read++;
			write++;
			count++;
			cache[setIndex][i].valid = 1;
			cache[setIndex][i].tag = tagIndex;
			cache[setIndex][i].time = count;
			return;
		} else {
			if(cache[setIndex][i].tag == tagIndex) {
				hit++;
				write++;
				count++;
				cache[setIndex][i].time = count;
				return;
			}

			if(i == (assoc - 1)) {
				miss++;
				read++;
				write++;
				
				min = 0; 
				for(j = 0; j < assoc; j++) {
					if(cache[setIndex][j].time <= cache[setIndex][min].time) {
						min = j;
					}
				}

				cache[setIndex][min].valid = 1;
				cache[setIndex][min].tag = tagIndex;
				count++;
				cache[setIndex][min].time = count;
				return;
			}
		}
	}
	return;
}

void prefetchedRead(unsigned long int tagIndex, unsigned long int setIndex, int assoc, unsigned long int prefetchedTagIndex ,unsigned long int prefetchedSetIndex) {
	int i, j, min;

	for(i = 0; i < assoc;i++){
		if(cache[setIndex][i].valid == 0) {
			miss++;
			read++;
			count++;

			cache[setIndex][i].valid = 1;
			cache[setIndex][i].tag = tagIndex;
			cache[setIndex][i].time = count;
			
			int k, l, m;

			for(k = 0; k < assoc; k++){
				if(cache[prefetchedSetIndex][k].valid == 0) {
					read++;
					count++;
					cache[prefetchedSetIndex][k].valid = 1;
					cache[prefetchedSetIndex][k].tag = prefetchedTagIndex;
					cache[prefetchedSetIndex][k].time = count;
					return;
				} else {
					if(cache[prefetchedSetIndex][k].tag == prefetchedTagIndex){	
						return;
					}
			
					if(k == (assoc-1)) {
						read++;
						m = 0;
						for(l = 0;l < assoc; l++){
							if(cache[prefetchedSetIndex][l].time <= cache[prefetchedSetIndex][m].time){
							m = l;
						}	
					}
			
			
					cache[prefetchedSetIndex][m].valid = 1;
	        				cache[prefetchedSetIndex][m].tag = prefetchedTagIndex;
	        				count++;
	        				cache[prefetchedSetIndex][m].time = count;
	        				return;
					}
				}
			}
			return;
		} else {
			if(cache[setIndex][i].tag == tagIndex){
				hit++;
				count++;
				cache[setIndex][i].time = count;
				return;
			}
			
			if(i == (assoc-1)){
				miss++;
				read++;
				min = 0;
				for(j = 0; j < assoc; j++){
					if(cache[setIndex][j].time <= cache[setIndex][min].time){
						min = j;
					}	
				}
			
				cache[setIndex][min].valid = 1;
	        			cache[setIndex][min].tag = tagIndex;
	        			count++;
	        			cache[setIndex][min].time = count;
	       			
				int k, l, m;

				for(k = 0; k < assoc; k++) {
					if(cache[prefetchedSetIndex][k].valid == 0) {
						read++;
						count++;
						cache[prefetchedSetIndex][k].valid = 1;
						cache[prefetchedSetIndex][k].tag = prefetchedTagIndex;
						cache[prefetchedSetIndex][k].time = count;
						return;
					} else {
						if(cache[prefetchedSetIndex][k].tag == prefetchedTagIndex) {	
							return;
						}
			
						if(k == (assoc-1)) {
							read++;
							m = 0;
							
							for(l = 0; l < assoc; l++){
								if(cache[prefetchedSetIndex][l].time <= cache[prefetchedSetIndex][m].time){
									m = l;
								}	
							}
			
			
							cache[prefetchedSetIndex][m].valid = 1;
	       					 	cache[prefetchedSetIndex][m].tag = prefetchedTagIndex;
	        						count++;
	        						cache[prefetchedSetIndex][m].time = count;
	        						return;
							}
						}
					}	       			
				return;
			}		
		}	
	}
	return;
}

void prefetchedWrite(unsigned long int tagIndex, unsigned long int setIndex, int assoc, unsigned long int prefetchedTagIndex ,unsigned long int prefetchedSetIndex) {
	int i, j, min;

	for(i = 0; i < assoc;i++){
		if(cache[setIndex][i].valid == 0) {
			miss++;
			read++;
			write++;
			count++;
			cache[setIndex][i].valid = 1;
			cache[setIndex][i].tag = tagIndex;
			cache[setIndex][i].time = count;
			int k, l, m;

			for(k = 0; k < assoc; k++){
				if(cache[prefetchedSetIndex][k].valid == 0) {
					read++;
					count++;
					cache[prefetchedSetIndex][k].valid = 1;
					cache[prefetchedSetIndex][k].tag = prefetchedTagIndex;
					cache[prefetchedSetIndex][k].time = count;
					return;
				} else {
					if(cache[prefetchedSetIndex][k].tag == prefetchedTagIndex){
						return;
					}
			
					if(k == (assoc-1)) {
						read++;
						m = 0;
						for(l = 0; l < assoc; l++){
							if(cache[prefetchedSetIndex][l].time <= cache[prefetchedSetIndex][m].time){
								m = l;
							}	
						}
			
			
						cache[prefetchedSetIndex][m].valid = 1;
	        					cache[prefetchedSetIndex][m].tag = prefetchedTagIndex;
	        					count++;
	        					cache[prefetchedSetIndex][m].time = count;
	        					return;
						}
					}
				}
			return;
		} else {
			if(cache[setIndex][i].tag == tagIndex){
				hit++;
				write++;
				count++;
				cache[setIndex][i].time = count;
				return;
			}
			
			if(i == (assoc-1)){
				miss++;
				read++;
				write++;
				min=0;
				for(j = 0; j < assoc; j++){
					if(cache[setIndex][j].time <= cache[setIndex][min].time){
						min = j;
					}	
				}
			
				cache[setIndex][min].valid=1;
	        			cache[setIndex][min].tag=tagIndex;
	        			count++;
	        			cache[setIndex][min].time=count;
	       			
				int k, l, m;
				for(k = 0; k < assoc; k++){
					if(cache[prefetchedSetIndex][k].valid == 0) {
						read++;
						count++;
						cache[prefetchedSetIndex][k].valid = 1;
						cache[prefetchedSetIndex][k].tag = prefetchedTagIndex;
						cache[prefetchedSetIndex][k].time = count;
						return;
					} else {
						if(cache[prefetchedSetIndex][k].tag == prefetchedTagIndex){
							return;
						}
			
						if(k == (assoc-1)) {
							read++;
							m = 0;
							for(l = 0; l < assoc; l++){
								if(cache[prefetchedSetIndex][l].time <= cache[prefetchedSetIndex][m].time){
									m = l;
								}	
							}
			
			
							cache[prefetchedSetIndex][m].valid = 1;
	        						cache[prefetchedSetIndex][m].tag = prefetchedTagIndex;
	        						count++;
	        						cache[prefetchedSetIndex][m].time = count;
	        						return;
							}
						}
					}
				return;
			}		
		}	
	}
	return;
}



int power_of_two(int number) {
	if(number == 0)
		return 0;
	
	while(number != 1) {
		if(number % 2 != 0)
			return 0;
		number /= 2;
	}
	return 1;
}

int main(int argc, char *argv[]) {

	unsigned long int setIndex;
	unsigned long int tagIndex;

	FILE *file = fopen(argv[5], "r");	

	if(argc == 6) {
		if(power_of_two(atoi(argv[1])) != 1 || power_of_two(atoi(argv[4])) != 1) {
			printf("ERR: invalid cache/block size.");
			return 1;
		} else if(strcmp(argv[3], "lru") != 0) {
			printf("ERR: Invalid replacement policy.");
			return 1;
		} else if(file == NULL) {
			printf("ERR: invalid file.");
			return 1;
		}
	} else {
		printf("ERR: invalid argument count.");
		return 1;
	}

	int cache_size = atoi(argv[1]);
	int block_size = atoi(argv[4]);
	int numSet, assoc, n;

	if(strcmp(argv[2], "direct") == 0) {
		assoc = 1;
		numSet = cache_size / block_size;
	} else if (argv[2][5] != ':') {
		assoc = cache_size / block_size;
		numSet = 1;
	} else {
		sscanf(argv[2],"assoc:%d",&n);
		assoc = n;
		numSet = cache_size / block_size / n;
	}

	int blockBits = log2(block_size);
	int setBits = log2(numSet);
	int setMask = ((1 << setBits) - 1);

	cache = createCache(numSet, assoc);

	unsigned long int address = 0;
	char cmd;

	while(fscanf(file, "%*x: %c %lx", &cmd, &address) == 2) {
		setIndex = (address >> blockBits) & setMask;
		tagIndex = address >> (blockBits + setBits);

		if(cmd == 'R') {
			cache_read(tagIndex, setIndex, assoc);
		} else if (cmd == 'W') {
			cache_write(tagIndex, setIndex, assoc);
		}
	}

	printf("no-prefetch\nMemory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n",read ,write ,hit ,miss);

	fclose(file);
	
	file = fopen(argv[5], "r");
	clear_cache(numSet, assoc);

	unsigned long int prefetchedAddress;
	unsigned long int prefetchedSetIndex;
	unsigned long int prefetchedTagIndex;

	while(fscanf(file, "%*x: %c %lx", &cmd, &address) == 2) {
		setIndex = (address >> blockBits) & setMask;
		tagIndex = address >> (blockBits + setBits);

		prefetchedAddress = address + block_size;
		prefetchedSetIndex = (prefetchedAddress >> blockBits) & setMask;
		prefetchedTagIndex = prefetchedAddress >> (blockBits + setBits);

		if(cmd == 'R') {
			prefetchedRead(tagIndex, setIndex, assoc, prefetchedTagIndex, prefetchedSetIndex);
		} else if(cmd == 'W') {
			prefetchedWrite(tagIndex, setIndex, assoc, prefetchedTagIndex, prefetchedSetIndex);
		}
	}

	fclose(file);

	printf("with-prefetch\nMemory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n", read, write, hit, miss);

	clear_cache(numSet, assoc);

	return 0;
}
