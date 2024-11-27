**Nota:** Vão haver queries que são impossiveis de gerar, serão impossíveis por exemplo, se mesmo criando *indexes* ou *tabelas auxiliares*, tenhamos que fazer operações que não são possiveis sobre a partition key.
```Para conseguir pesquisar por ID muitos ID'S foram substituidos à mão, isso pode levar a que posteriormente queries anteriores dêm resultados vazios.```
- É suposto todas as queries funcionarem depois de certas alterações às tabelas?


### Query 1
##### 1 - Os últimos 3 comentários introduzidos para um vídeo;
**Command**
```bash
SELECT comment_content, comment_date
FROM SistemaPartilhaVideos.Comments
WHERE video_id = bafce726-cfe0-4ec6-ab28-292d43a4810d
LIMIT 3;
```
**Result**
```bash
 comment_content               | comment_date
-------------------------------+---------------------------------
       wonderful awesome great | 2024-10-28 11:57:32.204000+0000
          cool great beautiful | 2024-10-13 11:57:32.204000+0000
 amazing interesting wonderful | 2024-10-01 11:57:32.204000+0000
```
I had to get the video id by hand, and the comment date is the same because of the insert_data was done at the same time. So that it would return 3 for testing purposes I altered the video_id in the comments inserts, so that it would have more comments associated to 1 video, since the script to insert produces random uuids, it would never do the same one.
### Query 2
##### 2 - Lista das tags de determinado vídeo;
**Command**
Can't use ALLOW FILTERING -> Criar nova tabela? Indexes?
```bash
SELECT tags
FROM SistemaPartilhaVideos.Videos
WHERE author_id = 205eedac-29dc-44fc-82d3-e41e445c64bd AND video_id = 34091a85-e895-4c09-8b3f-34c4e5d42030;
```
**Result**
```bash
 tags
--------------------
 ['tag11', 'tag12']
```
The author_id and video_id were hand picked.
### Query 3
##### 3 - Todos os vídeos com a tag Aveiro; (tag8 = Aveiro)
(Como eu faço geração de dados através dum script, os nomes das tags foram gerados com números)
**Command**
```bash
SELECT video_id, nome_video, tags
FROM SistemaPartilhaVideos.Videos
WHERE tags CONTAINS 'tag8';
```
**Result**
```bash
 video_id                             | nome_video | tags
--------------------------------------+------------+---------------------------
 d1a66d89-7d69-4ddf-8ce2-ce33261d7648 |    Video 7 |          ['tag1', 'tag8']
 53c5b1d5-5567-46f6-9b2d-0b5c9cf25f5c |   Video 17 |         ['tag8', 'tag11']
 89ca357d-7e55-4cd2-b3e4-5ff85388a56a |   Video 14 | ['tag8', 'tag13', 'tag1']
```
Had to create an index for this one
### Query 4
##### 4.a - Os últimos 5 eventos de determinado vídeo realizados por um utilizador;
**Command**
```bash
SELECT video_id, user_id, event_type, event_timestamp, video_time_seconds
FROM SistemaPartilhaVideos.RegistEvents
WHERE video_id = c8f85ebf-2ddc-4c30-947c-c714f08c0f16 AND user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b
ORDER BY event_timestamp DESC
LIMIT 5;
```
**Result**
```bash
 video_id                             | user_id                              | event_type | event_timestamp                 | video_time_seconds
--------------------------------------+--------------------------------------+------------+---------------------------------+--------------------
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 | b3a2cb05-bddc-4c18-89db-f1f34b4dc03b |       stop | 2024-11-12 11:57:32.206000+0000 |                 44
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 | b3a2cb05-bddc-4c18-89db-f1f34b4dc03b |       play | 2024-10-31 11:57:32.206000+0000 |                226
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 | b3a2cb05-bddc-4c18-89db-f1f34b4dc03b |      pause | 2024-10-22 11:57:32.206000+0000 |                 13
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 | b3a2cb05-bddc-4c18-89db-f1f34b4dc03b |      pause | 2024-10-19 11:57:32.206000+0000 |                  5
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 | b3a2cb05-bddc-4c18-89db-f1f34b4dc03b |       stop | 2024-10-11 11:57:32.206000+0000 |                312
```
##### 4.b - Todos os eventos de determinado utilizador;
**Command**
```bash
SELECT video_id, event_type, event_timestamp, video_time_seconds
FROM SistemaPartilhaVideos.RegistEvents
WHERE user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b;
```
**Result**
```bash
 video_id                             | event_type | event_timestamp                 | video_time_seconds
--------------------------------------+------------+---------------------------------+--------------------
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |       play | 2024-08-06 11:57:32.206000+0000 |                 59
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |       play | 2024-10-10 11:57:32.206000+0000 |                 93
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |       stop | 2024-10-11 11:57:32.206000+0000 |                312
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |      pause | 2024-10-19 11:57:32.206000+0000 |                  5
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |      pause | 2024-10-22 11:57:32.206000+0000 |                 13
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |       play | 2024-10-31 11:57:32.206000+0000 |                226
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |       stop | 2024-11-12 11:57:32.206000+0000 |                 44
```
##### 4.c - Todos os eventos de determinado utilizador do tipo "pause"
Podia criar uma tabela só para os paused events, mas vou criar um índice.
**Command**
```bash
SELECT video_id, event_type, event_timestamp, video_time_seconds
FROM SistemaPartilhaVideos.RegistEvents
WHERE user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b
  AND event_type = 'pause'
  AND video_id = c8f85ebf-2ddc-4c30-947c-c714f08c0f16;
```
**Result**
```bash
 video_id                             | event_type | event_timestamp                 | video_time_seconds
--------------------------------------+------------+---------------------------------+--------------------
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |      pause | 2024-10-19 11:57:32.206000+0000 |                  5
 c8f85ebf-2ddc-4c30-947c-c714f08c0f16 |      pause | 2024-10-22 11:57:32.206000+0000 |                 13
```
Neste query em específico não sei se ficou bem feito, ou se devia estar a usar video_id.
### Query 5 - Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num determinado período de tempo (Agosto de 2017, por exemplo);
**Command**
```bash
SELECT video_id, nome_video, descricao, upload_date
FROM SistemaPartilhaVideos.Videos
WHERE author_id = 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
AND upload_date >= '2016-08-01' AND upload_date <= '2024-08-31';
```

- Para resolver este query alterei a tabela .Videos e coloquei "upload_date" como primary key.
- Teria problema acabar por colocar tudo como primary key?
**Result**
```bash
 video_id                             | nome_video | descricao                   | upload_date
--------------------------------------+------------+-----------------------------+---------------------------------
 a58efcaa-fe64-4b73-a422-68b69b43f634 |    Video 8 | amazing beautiful wonderful | 2024-08-28 11:57:32.203000+0000
```
### Query 6 - Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;
Para resolver esta query adicionei isto "WITH CLUSTERING ORDER BY (upload_date DESC);" à tabela .Videos
**Command**
```bash
SELECT video_id, nome_video, descricao, upload_date
FROM SistemaPartilhaVideos.Videos
WHERE author_id = 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
LIMIT 10;
```
**Result**
```bash
 video_id                             | nome_video | descricao                       | upload_date
--------------------------------------+------------+---------------------------------+---------------------------------
 a24eff53-4c91-4dad-b667-b1d3578eab85 |   Video 18 |     wonderful amazing beautiful | 2024-11-06 11:57:32.204000+0000
 a47a733c-bfa2-4766-8766-808ea6d55556 |    Video 6 |    interesting cool interesting | 2024-11-06 11:57:32.203000+0000
 71ddd58b-7842-4395-9c56-25888d7c2a65 |    Video 9 | beautiful beautiful interesting | 2024-10-01 11:57:32.203000+0000
 d1a66d89-7d69-4ddf-8ce2-ce33261d7648 |    Video 7 |             cool beautiful cool | 2024-09-07 11:57:32.203000+0000
 a58efcaa-fe64-4b73-a422-68b69b43f634 |    Video 8 |     amazing beautiful wonderful | 2024-08-28 11:57:32.203000+0000
```
### Query 7 - Todos os seguidores (followers) de determinado vídeo;
**Command**
```bash
SELECT follower_id
FROM SistemaPartilhaVideos.VideoFollowers
WHERE video_id = 4f18d8c1-d2c4-44d8-80bf-f28429be04c9;
```
**Result**
```bash
 follower_id
--------------------------------------
 3125840c-fc72-4591-a3f7-fe72c2d428f4
 a2a040e1-1903-4412-a6a1-eb4abcde4de8
 c4152dfa-5c4e-4e20-b895-3910537eee6f
```
### Query 8 - Todos os comentários (dos vídeos) que determinado utilizador está a seguir (following);
Esta query não é possivel, pois o cassandra não permite joins, e seriam precisas duas queries para associar duas tabelas para ir buscar os dados que quero.
Teria que obter os videos por user, e depois tinha que obter os comentarios apra cada video obtido.
Teria que secalhar criar outra tabela para esta solução.
Adicionando a nova tabela do ficheiro new_insert.cql poderia fazer a seguinte query.
**Command**
```bash
SELECT comment_content, comment_date
FROM SistemaPartilhaVideos.FollowedVideoComments
WHERE follower_id = c8f85ebf-2ddc-4c30-947c-c714f08c0f16;
```
**Result**
```bash
 comment_content              | comment_date
------------------------------+---------------------------------
 Awesome content, keep it up! | 2024-01-01 12:45:00.000000+0000
                 Great video! | 2024-01-01 10:30:00.000000+0000
```
### Query 9 - Os 5 vídeos com maior rating;
Tenho que criar uma tabela nova, visto que a forma como tinha feito, cada video id ficava associado a mais que um rating, significando que teria que fazer cálculos com os diferentes ratings atribuidos por diferentes users.
```InvalidRequest: Error from server: code=2200 [Invalid query] message="Only clustering key columns can be defined in CLUSTERING ORDER directive: [rating_value] are not clustering columns"```
-> O id do video tinha que estar por cima do ranking, mas para fazer este query iria precisar de 2 tabelas.
Não é possivel.
### Query 10 - Uma query que retorne todos os vídeos e que mostre claramente a forma pela qual estão ordenados;
**Command**
```bash
SELECT video_id, nome_video, descricao, upload_date, author_id
FROM SistemaPartilhaVideos.Videos;
```
**Result**
```bash
 video_id                             | nome_video | descricao                       | upload_date                     | author_id
--------------------------------------+------------+---------------------------------+---------------------------------+--------------------------------------
 6bbb9426-9b74-4d95-9a24-6b354130f577 |    Video 3 |       beautiful great beautiful | 2024-10-15 11:57:32.202000+0000 | 6d21f0c3-18b1-49aa-b35a-c53bd835b75e
 74c1b1b0-60c1-43ad-9ae9-f51d435dba1e |   Video 20 |            great cool beautiful | 2024-10-08 11:57:32.204000+0000 | dca19809-eb07-4d20-a993-3a0832052829
 a24eff53-4c91-4dad-b667-b1d3578eab85 |   Video 18 |     wonderful amazing beautiful | 2024-11-06 11:57:32.204000+0000 | 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
 a47a733c-bfa2-4766-8766-808ea6d55556 |    Video 6 |    interesting cool interesting | 2024-11-06 11:57:32.203000+0000 | 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
 71ddd58b-7842-4395-9c56-25888d7c2a65 |    Video 9 | beautiful beautiful interesting | 2024-10-01 11:57:32.203000+0000 | 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
 d1a66d89-7d69-4ddf-8ce2-ce33261d7648 |    Video 7 |             cool beautiful cool | 2024-09-07 11:57:32.203000+0000 | 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
 a58efcaa-fe64-4b73-a422-68b69b43f634 |    Video 8 |     amazing beautiful wonderful | 2024-08-28 11:57:32.203000+0000 | 24dc7f54-ec4a-4d84-9521-f2a03ac435e8
 6e7dfbf6-96cd-4dbf-b8b4-3b186d93c312 |    Video 5 |              amazing great cool | 2024-09-13 11:57:32.202000+0000 | f0d1809b-22a8-4f96-a150-3b8e4684c850
 9cf8f6b4-042b-4e62-8308-c2999e65e025 |   Video 22 |       great amazing interesting | 2024-09-25 11:57:32.204000+0000 | 2bfeb0be-0807-460c-9d89-02f24734b591
 ef29ce46-3fec-4b1c-9f31-f3098ae323ce |   Video 12 |       awesome awesome beautiful | 2024-10-09 11:57:32.203000+0000 | 88f289ec-053f-4ce1-b75b-c01e2439ee86
 53c5b1d5-5567-46f6-9b2d-0b5c9cf25f5c |   Video 17 |         great wonderful awesome | 2024-09-16 11:57:32.203000+0000 | 176e5efd-7fdf-44ad-ad0b-387ad5064edc
 99a1b515-dde3-4c9b-a2b9-6263cf34c1bc |   Video 16 |   awesome interesting beautiful | 2024-08-06 11:57:32.203000+0000 | 27380372-c2fe-4f2d-97c5-b82ad22352be
 8085703b-57f0-4c50-9d0e-596c2d290b57 |   Video 24 |     interesting great beautiful | 2024-08-10 11:57:32.204000+0000 | 3647b16c-5b68-4604-a9f0-70cc0d0e8ceb
 2a53c071-1de8-404b-b125-1caba0164751 |   Video 25 |         amazing wonderful great | 2024-09-10 11:57:32.204000+0000 | 3a0394cb-cb22-4d88-a318-ce73971e4723
 89ca357d-7e55-4cd2-b3e4-5ff85388a56a |   Video 14 |     awesome interesting amazing | 2024-08-10 11:57:32.203000+0000 | f2fc4f32-14af-4141-95c1-f6e2a217eba9
 2b46a093-e590-4c45-8fd4-91b7d90b4a04 |    Video 1 |          wonderful awesome cool | 2024-10-14 11:57:32.202000+0000 | 205eedac-29dc-44fc-82d3-e41e445c64bd
 34091a85-e895-4c09-8b3f-34c4e5d42030 |    Video 2 |       amazing great interesting | 2024-09-10 11:57:32.202000+0000 | 205eedac-29dc-44fc-82d3-e41e445c64bd
 faf05866-bea3-44f8-ab32-b14777b012a7 |   Video 13 |       interesting great amazing | 2024-09-03 11:57:32.203000+0000 | 23bc0692-12ff-48b2-862f-a4489efd7d5f
 b604d8e2-92e5-4daf-8622-b9727ad5a533 |   Video 19 |   interesting wonderful awesome | 2024-10-31 11:57:32.204000+0000 | 4420451f-2d0e-46da-ba45-8bf5e782ce2c
 816d1f9b-b71f-40b9-8b4b-366c27833aef |   Video 15 |   amazing interesting beautiful | 2024-08-12 11:57:32.203000+0000 | 39d15694-44e2-4ac9-ba5d-2ec9e0aec4bb
 9b925e73-767c-40c6-bf31-cc13ae841713 |   Video 21 |           amazing great amazing | 2024-09-23 11:57:32.204000+0000 | 83a0d2e2-365a-46d7-9110-012df3495903
 07c2674c-014f-4edd-99de-fd0b68cf84d1 |    Video 4 |      interesting wonderful cool | 2024-10-14 11:57:32.202000+0000 | f893cdc2-5c81-4b10-b890-b8fbf2d4353a
 08a09e39-46ac-4000-a10a-a51a4af1582b |   Video 11 |     beautiful beautiful amazing | 2024-10-17 11:57:32.203000+0000 | aa61476e-7940-414b-a623-e1c7507a456e
 a8768398-dcb0-4172-87df-f040a056b058 |   Video 10 |            amazing cool awesome | 2024-10-06 11:57:32.203000+0000 | c0c9df7c-cf60-4b0d-8852-bffdb19af945
 8bcf9dfd-c2f7-40e2-84ec-0f62d62ca91a |   Video 23 |       beautiful amazing awesome | 2024-08-29 11:57:32.204000+0000 | 01a96d86-8403-4bda-b956-47a216638741
```

Os vídeos estão particionados por author_id.
Dentro de cada autor, os vídeos estão ordenados por upload_date em ordem decrescente.
video_id é um clustering key adicional para garantir unicidade.

### Query 11 - Lista com as Tags existentes e o número de vídeos catalogados com cada uma delas;
Cassandra não suporta diretamente COUNT
Teria que criar uma tabela nova
E depois fazer junção de tabelas e talvez um script para fazer a contagem o que torna esta query impossivel.

### 12 - 14. Descreva 3 perguntas adicionais à base dados (alíneas 12 a 14), significativamente distintas das anteriores e que tenham como base nova(s) tabela(s) resultante de novos requisitos (a definir). Apresente igualmente a solução de pesquisa para cada questão.


### PERGUNTA 1 - Quais são os vídeos que receberam um rating maior que 4 por pelo menos um utilizador
Decidi meter apenas um exemplo de allow filtering e explicar o porque de nao se dever usar:
O allow filtering pode gerar problemas graves de desempenho e fiabilidade. O cassandra foi feito para consultas rápidas com partições e índices específicos, e ao usar allow filtering estamos a consultar as tabelas inteiras.

**Command**
```bash
SELECT video_id
FROM SistemaPartilhaVideos.VideoRatings
WHERE rating_value > 4 ALLOW FILTERING;
```
**Result**
```bash
 video_id
--------------------------------------
 37c18061-9218-48ea-8455-66d5a23a68cd
 45f24c1e-05a4-40a1-b048-80715e091434
 b2add022-9793-4f4d-a67c-ed1f820929f4
 89fe4145-c9d6-43a0-b2e1-743eeb4ce212
```
### PERGUNTA 2 - Obter todos os comentários feitos em um vídeo específico, ordenados pela data
**Command**
```bash
SELECT comment_id, user_id, comment_date, comment_content
FROM SistemaPartilhaVideos.Comments
WHERE video_id = 123e4567-e89b-12d3-a456-426614174000;
```
**Result**
```bash
 comment_id                           | user_id                              | comment_date                    | comment_content
--------------------------------------+--------------------------------------+---------------------------------+--------------------------
 4af8f43b-9055-4151-b533-4a3a68ebec94 | 7e3eea94-042d-4369-a07c-8f8f1f3daa04 | 2024-10-28 11:57:32.205000+0000 | amazing interesting cool
 9f160df6-72e1-4fe5-bb11-602d45d9c094 | bda25c53-9aee-4376-b163-54689a06fda1 | 2024-10-19 11:57:32.205000+0000 |       great cool amazing
 877cdf5f-73d9-45fb-9908-5ab27d527b18 | 1678e644-16d5-4063-836c-41eae1ea16f2 | 2024-08-31 11:57:32.204000+0000 |   wonderful awesome cool
```
### PERGUNTA 3 - Obter todos os vídeos enviados por um autor específico
**Command**
```bash
SELECT video_id, nome_video, descricao, upload_date
FROM SistemaPartilhaVideos.Videos
WHERE author_id = 20f8114d-191f-40e5-9a80-c113e9638d3e;
```
**Result**
```bash
 video_id | nome_video | descricao | upload_date
----------+------------+-----------+-------------

```
Neste caso tive um resultado vazio



```Em principio todas as impossiveis davam para fazer com tabelas auxiliares, mas nao seria optimal ter que inserir de novo os dados, para além disso sacrificar consistência```