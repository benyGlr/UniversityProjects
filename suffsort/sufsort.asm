%include "asm_io.inc"
global asm_main

section .data
  errl: db "wrong amount of arguments", 0
  errs: db " = you're length, it should be <=30", 0
  errc: db "wrong char, please put in 1, 2 or 0", 0
  done: db "sorted suffix", 0
section .bss
  X:     resd 100
  Z:     resd 100
  LEN:   resd 100
  i:     resd 30
  j:     resd 30
  track: resd 1
  argc:  resd 1 
  argv:  resd 1
  
section .text
  
asm_main:
  enter 0, 0
  pusha
  
  mov eax, dword [ebp+8]   ; adress of argc (amount of arguments)
  mov [argc], eax          ; move from argc from eax to var argc
  mov edi, dword 2         ; move amount of command line arguments required to edi
  cmp dword [argc], edi    ; compare to see if the arguments match
  jne error_length         ; if prev compare was not met jump to error_length
  
  mov ebx, dword [ebp+12]  ; store array of command line arguments into ebx 
  add ebx, 4               ; add 4 to ebx to point to next command line argument      
  push dword [ebx]         ; make ebp point to ebx 
  call string_count        ; call string count function
  add esp, dword 4         ; move stack pointer up to not corrup stack
  ;Begin storing string into array
  mov eax, dword [ebp+12]                   ; store array of command line arguments into eax 
  mov ebx, dword [eax+4]                    ; store first argument of command line into ebx 
  mov ecx, dword 0                          ; start ecx at 0 since it's the counter 
  STORE_LOOP: cmp byte [ebx], byte 0 ; compare the current byte ebx is at to 0 to see if the string is done 
    je STORE_LOOP_END                ; string is done therefor exit loop 
	mov al, byte [ebx]               ; store the current byte of ebx into al  
	mov byte[X+ecx], al              ; move the current char(byte) in al into x[ecx] 
	inc ebx                          ; go to next char 
	inc ecx                          ; counter goes up by 1 
	jmp STORE_LOOP                   ; loop 
  STORE_LOOP_END:                    ; loop done 
  mov eax, X                         ; move into eax THE ADDRESS OF THE ARRAY X 
  ;call print_nl                      ; print new line  
  call print_string                  ; print the VALUE OF THE ADDRESS IN EAX 
  call print_nl 
  ; IMPORTANT SELF NOT: DOING [ ] GIVES THE VALUE AND ASSUMES WHAT INSIDE THE BRACKETS IS THE ADDRESS print_string WANTS AN ADDRESS 
  ;Begin storing suffixes into array (the array stores suffixes byte by byte)
  mov eax, dword [ebp+12]                   ; store array of command line arguments into eax 
  mov ebx, dword [eax+4]                    ; store first argument of command line into ebx 
  mov ecx, dword 0                          ; start ecx at 0 since it's the counter
  mov eax, dword 0                          ; safety sake 
  mov edi, dword 0                          ; counter to go through suffix 
  STORE_SUFF_LOOP: cmp byte [ebx], byte 0   ; go through all the characters in string
    je STORE_SUFF_END                       ; 
	push ebx                                ; store ebx on stack 
	push edi                                ; store edi on stack 
	mov edi, dword [ebp+12]                      ; same as usual  
	mov ebx, dword [edi+4]                       ; ditto
    pop edi 	                                 ; get edi into edi to use for count 
    GET_SUFF_LOOP: cmp byte [ebx+ecx], byte 0    ; go through all chars in suffix 
      je GET_SUFF_END 
	  mov al, byte [ebx+ecx]                     ; put current byte of suffix into al 
	  mov byte[Z+edi], al                        ; put current byte of suffix into Z[edi]
      inc edi                                    ; incease edi (this is to find whats next pos in array)
	  mov eax, edi                               ; check 
      ;call print_int                             ; check 
      ;call print_nl 
      inc ebx 	                                 ; go to next char in suffix 
      jmp GET_SUFF_LOOP
    GET_SUFF_END:
	;call print_nl 
    pop ebx 	                            ; put the ebx from stack into ebx 
	inc ebx                                 ; go to next char in string 
	inc ecx                                 ; go to next suffix in string 
	jmp STORE_SUFF_LOOP                      
  STORE_SUFF_END:                           
  ;mov eax, Z 
  ;call print_string 	 
  ;sorting starts here
  ;Theory 
  ;you have 2 suffixes in the array next to each other that are being sorted 
  ;take last char of 2nd suffix and move it to first spot in first 
    ;to do this store last char then move every other char up 1 spot then place it 
	  ; to move every char up 1 u have to loop through 1st and 2nd suffix starting from the end 
  ;keep doing that till you go through every byte in 2nd suffix 
  ;might also need an array of lengths in order to keep track of how many chars in both args
  ; PS: im so sorry for what you're about to go through   
  mov edi, dword [ebp+12]                      ; same as usual  
  mov ebx, dword [edi+4]                       ; ditto
  mov ecx, dword [LEN]                         ; this is meant to keep count for inner loop (first run is max then max-1 then max-2...)
  push ebx   
  SUFFIX_LOOP: cmp byte [ebx], byte 0        ; this loop goes n times where n = input string length 
    je SUFFIX_LOOP_END
	push ebx
	mov ebx, dword 1                         ; counter for the loop 
	mov dword [track], dword 0               ; tracks where next suffix starts in Z 
	mov edi, LEN                             ; edi stores beggining of array of suffix lengths 
    INNER_LOOP: cmp ecx, ebx                 ; this loop goes n times where n = inpurt string length - amount of sorts already done 
	  je INNER_END
	  ; now you need to find i and j to compare them
	  push ecx                     
	  mov ecx, dword 0
	  mov dword [i], dword 0       ; reset i 
	  I_INSERT cmp [edi], ecx      ; this loop goes through first suffix in comparision and stores it into i 
	    je I_INSERT_END
		push ecx                          ; have to add track to ecx but that will mess it up for the counter of loop sake so put it on stack 
		add ecx, dword[track]             ; need to add track so that the correct suffix from Z is used 
		mov al, byte [Z+ecx]              ; put the correct byte of suffix from z to al 
		pop ecx 
		mov byte[i+ecx], al               
	    inc ecx 
		jmp I_INSERT
	  I_INSERT_END:
      mov byte[i+ecx], byte 0     ; make sure the string i ends in the correct place 	  
	  mov eax, i 
	  ;call print_nl 
	  ;call print_string
      ;call print_nl 	  
	  pop ecx
      mov eax, [edi]               ; move value of suffix length into eax 
	  add dword [track], eax       ; move to next suffix 
      add edi, dword 4	           ; move to next suffix length 
	  push ecx 
	  mov ecx, dword 0
	  mov dword [j], dword 0       ; reset j 
	  J_INSERT cmp [edi], ecx      ; this loop goes through first suffix in comparision and stores it into i 
	    je J_INSERT_END            ; the comments for this loop are the same as the I one 
		push ecx 
		add ecx, dword[track] 
		mov al, byte [Z+ecx]              ; put correct byte from suffix z into al 
		pop ecx 
		mov byte[j+ecx], al 
	    inc ecx 
		jmp J_INSERT
	  J_INSERT_END: 
	  mov byte [j+ecx], byte 0       ; make sure j ends in the correct place 
	  mov eax, j
	  ;call print_nl 
	  ;call print_string
      ;call print_nl 	  
	  pop ecx
      call sufcmp            ; compare suffixes and put result into eax 
	  ;call print_int 
	  cmp eax, dword 1       ; if eax = 1 sorting needs to happen 
	  je sorting 
	  sorting_done:
	  ; last part to do BUBBLE SORT, I CAN SEE THE END 
	  inc ebx                ; counter for 2nd (2nd most outer) loop 
	  jmp INNER_LOOP 
    INNER_END:
	;call print_nl
	mov eax, dword[track]
	;call print_int 
	;call print_nl
    dec ecx    	             ; finished a sort 
	pop ebx 
	inc ebx                  ; counter for 1st (most outer) loop 
    jmp SUFFIX_LOOP 
  SUFFIX_LOOP_END:
  pop ebx 
  mov eax, Z
  ;call print_nl 
  ;call print_string
  ;call print_nl 
  mov esi, dword Z
  mov edi, LEN
  ;call print_nl 
  mov eax, done
  call print_string
  call print_nl
  FINALY_LOOP: cmp byte [ebx], byte 0 
    je FINALY_DONE  
	mov ecx, dword 0 
    PRINT_SUFF: cmp ecx, [edi] 
	  je PRINT_DONE
	  mov al, byte [esi] 
	  call print_char 
	  inc esi 
	  inc ecx 
	  jmp PRINT_SUFF
	PRINT_DONE: 
	call print_nl 
	add edi, 4
    inc ebx 
	jmp FINALY_LOOP
  FINALY_DONE: 
  
  
  popa 
  leave
  ret
  
sorting: 
  pusha 
  ; Explanation: edi held the length of suffix j so we need to roll it back to length of i so that we can also roll back track to the beggining 
  ; so that we can start inserting j where i was and then when we are done inserting j we have to add the len of j to the track so we can start
  ; inserting i into the correct spot 
  sub edi, 4                  ; roll back edi               
  mov ebx, [edi]
  sub dword [track], ebx      ; roll back track 
  mov al, byte [j]
  mov ecx, dword[track]      ; goes through Z
  mov ebx, dword 0           ; goes through j  
  J_SORT cmp al, byte 0      ; this loop goes through j (the second suffix) 
	    je J_SORT_END 
		mov byte [Z+ecx], al              ; put the byte (#ecx) into the position Z+track 
		inc ecx		                      ; next position 
		inc ebx 
		mov al, byte [j+ebx]              ; next byte 
		jmp J_SORT
	  J_SORT_END:
  mov al, byte [i] 
  mov ebx, dword 0           ; goes through i 
  I_SORT cmp al, byte 0      ; this loop goes through j (the second suffix) 
	    je I_SORT_END 
		mov byte [Z+ecx], al              ; put the byte (#ecx) into the position Z+track 
		inc ecx		                      ; next position 
		inc ebx 
		mov al, byte [i+ebx]              ; next byte 
		jmp I_SORT
	  I_SORT_END:
  
  add edi, 4                          ; now that j is where i was edi has to point to len of j (in order to add it to track) 
  mov ebx, [edi]                      
  add dword [track], ebx              ; add len of j to track it now points to the beggining of i which is where the next run through the loop will start 
  ; NOW ARRAY OF LENGTHS HAS TO BE SORTED  
  mov eax, [edi]        ; length of j in eax
  sub edi, dword 4 
  mov ebx, [edi]         ; length of i in ebx 
  mov dword[edi], eax    ; length of j into first pos 
  add edi, 4             ; go to next pos 
  mov dword[edi], ebx    ; length of i into next pos 
  
  popa 
  jmp sorting_done 
  
sufcmp:
  enter 0, 0
  push ebx 
  push ecx 
  push edx
  push esi
  push edi 
  push ebp 
  
  mov ebx, dword i
  mov ecx, dword j 
  COMPARE: cmp byte [ebx], byte 0 ; if it runs out of chars in j that means its bigger and no sorting needs to be done 
  je nosort                       ; hence the no sort jump 
  mov al, byte [ecx]
    cmp byte [ebx], al   ; byte of i compare to byte of j 
    jg sort      ; if i>j need to sort 
    jl nosort    ; if i<j no need to sort 
	inc ebx      ; next char in i 
	inc ecx      ; next char in j 
	jmp COMPARE 
	
  
  CHECK:
  pop ebp 
  pop edi 
  pop esi 
  pop edx 
  pop ecx
  pop ebx   
  
  leave 
  ret
  
sort:
  mov eax, dword 1 
  jmp CHECK
nosort:
  mov eax, dword -1
  jmp CHECK 
string_count:              ; everything associated with counting the string 
  enter 0, 0
  pusha
  
  mov ecx, dword 0         ; counter start at 0
  mov ebx, dword [ebp+8]   ; idk just do this to deref stack pointer
  mov al, byte [ebx]       ; check first byte of ebx
  COUNT: cmp byte [ebx], byte 0  ; compare current char to 0
  je COUNT_END                   ; loop terminates at 0
    COMPA: cmp byte [ebx], '0'          ; compare current char to 0
	  je COMPEND                        ; if its 0 then its right
      COMPB: cmp byte [ebx], '1'        ; compare current char to 1
       je COMPEND                       ; if it's 1 its right
       COMPC: cmp byte [ebx], '2'       ; compare current char to 2
         je COMPEND                     ; if it's 2 its right
         jmp error_char                 ; its not 0, 1 or 2 so its wrong 
    COMPEND:		                    ; its right
	inc ebx 			   ; go to next char
	inc ecx                ; increment eax
	jmp COUNT              
 COUNT_END:
  mov eax, ecx             ; put final count into eax
  mov ecx, dword 30        ; put 30 into ecx
  cmp eax, ecx             ; compare eax and ecx
  jg error_string_count    ; if count > 30 call error 
  ;this loop is meant to store the suffix lengths into array LEN 
  mov ecx, dword 0                  ;ecx keeps track of LEN array position
  SUFF_COUNT_LOOP: cmp eax, dword 0 ;eax is the size of suffix 
    je SUFF_COUNT_END               ;...
	mov dword [LEN+ecx], eax        ;mov current suffix size into array position LEN[ecx] 
    dec eax                         ;the next suffix is going to be 1 less 
    add ecx, 4	                    ;the next array position is 4 bytes away 
	jmp SUFF_COUNT_LOOP             ;...
  SUFF_COUNT_END:                   ;...  
  popa
  leave 
  ret
  
error_char:                ; not a b or c error
  ; nothing here because this is going to end prog anyway so dont need to save regs
 
  mov eax, errc            ; move error msg to eax
  call print_string        ; print eax
  ;DONT KNOW HOW TO STOP PROGRAM,ASK
  popa 
  leave 
  ret
error_string_count:        ; string to long error 

  call print_int           ; print count of string 
  mov eax, errs            ; move error msg to eax
  call print_string        ; print eax
  ;DONT KNOW HOW TO STOP PROGRAM,ASK
  popa 
  leave 
  ret 
error_length:              ; too many command line args error 
  mov eax, errl            ; move error msg to eax
  call print_string        ; print eax
  ;DONT KNOW HOW TO STOP PROGRAM,ASK
  popa 
  leave 
  ret
