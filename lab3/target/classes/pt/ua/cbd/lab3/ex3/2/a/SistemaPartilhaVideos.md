# THIS FILE DOESN'T HAVE THE CHANGES CORRESPONDING TO THE QUERIES OR THE POINTS .7 / .8 / .9 / .10


## Create keyspace

```bash
CREATE KEYSPACE SistemaPartilhaVideos with replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
```

### Usar keyspace

```bash
USE SistemaPartilhaVideos;
```

- Tabela para utilizadores
```bash
CREATE TABLE SistemaPartilhaVideos.User(
    id UUID PRIMARY KEY,
    username TEXT,
    nome TEXT,
    email TEXT,
    resgist_date TIMESTAMP
);
```

- Tabela para videos
```bash
CREATE TABLE SistemaPartilhaVideos.Videos(
    video_id UUID,
    author_id UUID, -- Autor da partilha é o user
    nome_video TEXT,
    descricao TEXT,
    tags LIST<TEXT>,
    upload_date TIMESTAMP
    PRIMARY KEY (author_id, video_id)
);
CREATE INDEX ON SistemaPartilhaVideos.Videos (tags);
```

- Tabela comentários
```bash
CREATE TABLE SistemaPartilhaVideos.Comments(
    video_id UUID,
    comment_id UUID,
    user_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (video_id, comment_date, user_id)
) WITH CLUSTERING ORDER BY (comment_date DESC, user_id DESC);
CREATE INDEX ON SistemaPartilhaVideos.Comments (user_id);
```

- Tabela video followers

Aqui eu apesar de poder fazer isto, não é aconcelhado usar uma collection, ou uma lista dentro duma tabela para os followers, pois como vai escalar para o infinito não é aconcelhado ao contrário de tags
Pode-se fazer mas não é correto
Então passo apenas um seguidor
Para cada video ter entrada (video, follower) em vez de por cada video ter (video Lista[followers])
```bash
CREATE TABLE SistemaPartilhaVideos.VideoFollowers(
    video_id UUID,
    follower_id UUID,
    follow_date LIST<TIMESTAMP>,
    PRIMARY KEY (video_id, follower_id)
);
```

- Tabela registo eventos
```bash
CREATE TABLE SistemaPartilhaVideos.RegistEvents(
    video_id UUID,
    user_id UUID,
    event_type TEXT, 
    event_timestamp TIMESTAMP, 
    video_time_seconds INT,  
    PRIMARY KEY (video_id, user_id, event_timestamp)
);
```

- Tabela para ratings

```No Cassandra, não há suporte direto para constraints como em bancos relacionais (como CHECK para valores em intervalos específicos). No entanto, esse tipo de validação geralmente é implementado na camada de aplicação, garantindo que apenas valores entre 1 e 5 sejam inseridos. Em Cassandra, colunas não obrigatórias (ou opcionais) são definidas implicitamente, pois o Cassandra permite NULL em qualquer coluna que não faça parte da chave primária.```
```bash
CREATE TABLE SistemaPartilhaVideos.VideoRatings (
    video_id UUID,
    rating_value INT,
    user_id UUID,
    PRIMARY KEY (video_id, rating_value)
);
```

- Tabela para notificações
```bash
CREATE TABLE SistemaPartilhaVideos.VideoNotifications (
    notification_id TIMEUUID,
    video_id UUID,
    follower_id UUID,
    comment_id TIMEUUID,
    notification_date TIMESTAMP,
    PRIMARY KEY (follower_id, notification_id)
) WITH CLUSTERING ORDER BY (notification_id DESC);
```

