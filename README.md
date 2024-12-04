# HttpServerJava

Crie um código Java que simule um serviço HTTP, restringindo seu escopo para os requisitos abaixo:

##Requisitos Funcionais

Dado que um cliente realizou uma operação GET /aluno/1, quando existir um aluno com ID = 1, então o servidor deve retornar uma resposta HTTP/1.1 com status 200 OK e contendo os dados do aluno ID = 1 formatado como HTML.

Dado que um cliente realizou uma operação GET /aluno/1, quando não existir um aluno com ID = 1, então o servidor deve retornar uma resposta HTTP/1.1 com status 4040 NOT FOUND formatando uma mensagem de erro apropriada como HTML.

Dado que um cliente realizou uma operação DELETE /aluno/1, quando existir um aluno com ID = 1, então o servidor deve excluir permanentemente o aluno ID = 1 do banco de dados E as requisições a partir de então devem retornar uma mensagem condizente com a condição de não existência do aluno ID = 1 E o servidor deve retornar uma mensagem apropriada formatada como HTML.

Dado que um cliente realizou uma operação POST /aluno, então o servidor deve criar um novo aluno com dados aleatórios e que não repita ou reuse um ID existente ou previamente excluído E retornar uma mensagem formatada como HTML.

##Requisitos Não-Funcionais

O acesso para inclusão e exclusão de alunos deve tratar a concorrência de maneira apropriada.
