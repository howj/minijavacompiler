
	.text
	.globl	asm_main

asm_main:
	pushq	%rbp
	movq	%rsp,%rbp

	movq	$4,%rax
	pushq	%rax
	movq	$5,%rax
	popq	%rdx
	imulq	%rdx,%rax
	movq	%rax,%rdi
	call	put

	movq	%rbp,%rsp
	popq	%rbp
	ret

