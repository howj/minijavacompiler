        .text
	        .globl  asm_main

asm_main:
	        pushq   %rbp
	        pushq   %rbp    # Pushing again to keep stack aligned
	        movq    %rsp,%rbp

	        movq    $0,%rdi
	        call    mjcalloc
	        leaq    Three$$,%rdx
	        movq    %rdx,0(%rax)    # Store vtable ptr
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *0(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    %rbp,%rsp
	        popq    %rbp
	        popq    %rbp
	        ret
	        .data


	        .data
Three$$:
	        .quad Three$start
Three$start:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $32,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    $24,%rdi
	        call    mjcalloc
	        leaq    Two$$,%rdx
	        movq    %rdx,0(%rax)    # Store vtable ptr
	        movq    %rax,-16(%rbp)  # putting result in variable location
	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        movq    %rax,-24(%rbp)  # putting result in variable location
	        movq    -24(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *0(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -24(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *8(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -24(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        movq    $17,%rax
	        pushq   %rax    # Saving arg 0 on stack
	        popq    %rsi    # placing arg 0 in %rsi
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *16(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *0(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *24(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *32(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *24(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        pushq   %rax    # Saving this ptr on stack
	        popq    %rdi    # object pointer as first arg
	        movq    0(%rdi),%rax    # Load vtable into %rax
	        call    *32(%rax)
	        movq    %rax,%rdi
	        call    put

	        movq    $1,%rax
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret


	        .data
One$$:
	        .quad One$setTag
	        .quad One$getTag
	        .quad One$setIt
	        .quad One$getIt
One$setTag:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    $1,%rax
	        movq    %rax,%rdi       # Saving result
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    %rdi,8(%rax)    # Assigning to field
	        movq    $0,%rax
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret

One$getTag:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    8(%rax),%rax    # Getting field
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret

One$setIt:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    %rsi,-16(%rbp)  # Saving it on stack
	        movq    -16(%rbp),%rax  # Getting the EXP from stack using offset
	        movq    %rax,-16(%rbp)  # putting result in variable location
	        movq    $0,%rax
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret

One$getIt:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    16(%rax),%rax   # Getting field
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret


	        .data
Two$$:
	        .quad Two$setTag
	        .quad One$getTag
	        .quad One$setIt
	        .quad One$getIt
	        .quad Two$getThat
Two$setTag:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    $2,%rax
	        movq    %rax,%rdi       # Saving result
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    %rdi,8(%rax)    # Assigning to field
	        movq    $3,%rax
	        movq    %rax,%rdi       # Saving result
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    %rdi,24(%rax)   # Assigning to field
	        movq    $0,%rax
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret

Two$getThat:
	        pushq   %rbp    # prologue
	        pushq   %rbp    # pushing again to keep stack aligned
	        movq    %rsp,%rbp
	        subq    $16,%rsp
	        movq    %rdi,-8(%rbp)   # Saving this ptr on stack
	        movq    -8(%rbp),%rax   # Getting this pointer
	        movq    24(%rax),%rax   # Getting field
	        movq    %rbp,%rsp       # epilogue
	        popq    %rbp
	        popq    %rbp
	        ret
	
