	.text
	.globl	asm_main

asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$0,%rdi
	call	mjcalloc
	leaq	TV$$,%rdx
	movq	%rdx,0(%rax)	# Store vtable ptr
	pushq	%rax	# Saving this ptr on stack
	movq	$17,%rax
	pushq	%rax	# Saving arg 0 on stack
	popq	%rsi	# placing arg 0 in %rsi
	popq	%rdi	# object pointer as first arg
	movq	0(%rdi),%rax	# Load vtable into %rax
	call	*0(%rax)
	movq	%rax,%rdi
	call	put

	movq	%rbp,%rsp
	popq	%rbp
	ret

	.data
TV$$:
	.quad TV$Start
TV$Start:
	pushq	%rbp	# prologue
	movq	%rsp,%rbp
	subq	$16,%rsp
	movq	%rdi,-8(%rsp)	# Saving this ptr on stack
	movq	%rsi,-16(%rsp)	# Saving x on stack
	movq	$71,%rax
	movq	%rbp,%rsp	# epilogue
	popq	%rbp
	ret

