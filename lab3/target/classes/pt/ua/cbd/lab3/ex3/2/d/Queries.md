**Nota:** Vão haver queries que são impossiveis de gerar, serão impossíveis por exemplo, se mesmo criando *indexes* ou *tabelas auxiliares*, tenhamos que fazer operações que não são possiveis sobre a partition key.


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
WHERE user_id = b3a2cb05-bddc-4c18-89db-f1f34b4dc03b  AND event_type = 'pause';
```
