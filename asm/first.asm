segment .data
	msg db "Hello World", 0
	len equ $ - msg

segment .text
	global _asm_main
_asm_main:
	mov edx, len
	mov ecx, msg
	mov ebx, 1
	mov eax, 4
	int 0x80

	mov eax, 1
	int 0x80
