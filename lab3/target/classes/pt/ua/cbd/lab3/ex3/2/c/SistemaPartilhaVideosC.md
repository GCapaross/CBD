- Igual à alínea a) mas com as correções indicadas da alínea c)

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
    video_id UUID PRIMARY KEY,
    user UUID, -- Autor da partilha é o user
    nome_video TEXT,
    descricao TEXT,
    tags LIST<TEXT>,
    upload_date TIMESTAMP
);
```

- Tabela comentários
```bash
CREATE TABLE SistemaPartilhaVideos.Commments(
    video_id UUID,
    comment_id UUID,
    user_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (video_id, comment_id)
);
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


- Comments by Video
```bash
CREATE TABLE SistemaPartilhaVideos.CommentsByVideo (
    video_id UUID,
    comment_id UUID,
    user_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (video_id, comment_date, comment_id)
) WITH CLUSTERING ORDER BY (comment_date DESC);
```

- Comments by User
```bash
CREATE TABLE SistemaPartilhaVideos.CommentsByUser (
    user_id UUID,
    video_id UUID,
    comment_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (user_id, comment_date, comment_id)
) WITH CLUSTERING ORDER BY (comment_date DESC);
```

- Video ratings by Video
```bash
CREATE TABLE SistemaPartilhaVideos.VideoRatingsByVideo (
    video_id UUID,
    rating_value INT,
    user_id UUID,
    PRIMARY KEY (video_id, user_id)
);
```