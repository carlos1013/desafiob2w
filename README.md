# Desafio B2W
Construção de uma API Rest para representar planetas vistos nos filmes da série Star Wars.

**Arquivo basico de inicialização do banco: basicinitmongo.txt**

## Endpoints disponíveis:
#### GetPlanet (GET):
Obtém um planeta dado um id ou nome do planeta.

**Paramêtros (Query String):** 
id: Id no planeta desejado
name: Nome do planeta desejado

**Resposta:** 
Objeto do tipo "Planet" ou notFound caso contrario.

**Exemplo de Url:**
localhost:8080/planets?name=Mandalore

#### GetPlanets (GET):
Obtém uma lista com todos os planetas atuais, não utilizando nenhum filtro.

**Resposta:** 
Lista de objetos do tipo "Planet" ou notFound caso nenhum planeta seja encontrado.

**Exemplo de Url:**
localhost:8080/planets/getplanets

#### AddPlanet (POST):
Adiciona um planeta dado seu nome, clima e terreno. 
Faz uma requisição a api SWAPI para obter o número de filmes que esse planeta apareceu.

**Paramêtros (Body do request):** 
name: String representando o nome do planeta
terrain: String representando o terreno do planeta
climate: String representando o clima do planeta

**Resposta:** 
Objeto do tipo "Planet" após sua criação (com o Id de criação e o número de aparições).

#### DeletePlanet (DELETE):
Deleta um planeta dado seu id.

**Paramêtros (URL):** 
id: Id no planeta desejado

**Resposta:** 
Accepted caso não ocorra nenhum erro na exclusão.

**Exemplo de Url:**
localhost:8080/planets/{id}

## Tecnologias utilizadas no projeto (Java):
##### MongoDB
##### Spring Boot
##### JUnit
##### JMockit
##### Mockito
