import random
from datetime import datetime, timedelta
import uuid

# Define the number of records to generate for each table
num_users = 17
num_videos = 25
num_comments = 30
num_followers = 15
num_events = 30
num_ratings = 30

commands = f"""
-- Drop the keyspace if it exists to avoid conflicts
DROP KEYSPACE IF EXISTS SistemaPartilhaVideos;

-- Create keyspace
CREATE KEYSPACE SistemaPartilhaVideos WITH replication = {{'class': 'SimpleStrategy', 'replication_factor': 3}};

-- Use keyspace
USE SistemaPartilhaVideos;

-- Create tables

CREATE TABLE SistemaPartilhaVideos.User(
    id UUID PRIMARY KEY,
    username TEXT,
    nome TEXT,
    email TEXT,
    resgist_date TIMESTAMP
);

CREATE TABLE SistemaPartilhaVideos.Videos(
    video_id UUID PRIMARY KEY,
    user UUID, -- Autor da partilha é o user
    nome_video TEXT,
    descricao TEXT,
    tags LIST<TEXT>,
    upload_date TIMESTAMP
);

CREATE TABLE SistemaPartilhaVideos.Commments(
    video_id UUID,
    comment_id UUID,
    user_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (video_id, comment_id)
);

CREATE TABLE SistemaPartilhaVideos.VideoFollowers(
    video_id UUID,
    follower_id UUID,
    follow_date LIST<TIMESTAMP>,
    PRIMARY KEY (video_id, follower_id)
);

CREATE TABLE SistemaPartilhaVideos.RegistEvents(
    video_id UUID,
    user_id UUID,
    event_type TEXT, 
    event_timestamp TIMESTAMP, 
    video_time_seconds INT,  
    PRIMARY KEY (video_id, user_id, event_timestamp)
);

CREATE TABLE SistemaPartilhaVideos.VideoRatings (
    video_id UUID,
    rating_value INT,
    user_id UUID,
    PRIMARY KEY (video_id, rating_value)
);

CREATE TABLE SistemaPartilhaVideos.VideoNotifications (
    notification_id TIMEUUID,
    video_id UUID,
    follower_id UUID,
    comment_id TIMEUUID,
    notification_date TIMESTAMP,
    PRIMARY KEY (follower_id, notification_id)
) WITH CLUSTERING ORDER BY (notification_id DESC);
"""

# Helper functions to generate random data
def random_username(i):
    return f"user{i}"

def random_uuid():
    return str(uuid.uuid4())

def random_timestamp():
    return (datetime.now() - timedelta(days=random.randint(1, 100))).isoformat()

def random_text():
    words = ["amazing", "beautiful", "great", "wonderful", "cool", "interesting", "awesome"]
    return " ".join(random.choice(words) for _ in range(3))

def random_tags():
    tags = ["tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9", "tag10", "tag11", "tag12", "tag13", "tag14", "tag15"]
    return "['" + "', '".join(random.choice(tags) for _ in range(random.randint(1, 3))) + "']"

# Open the output file
with open("insert_data2.cql", "w") as file:
    
    file.write(commands)
    
    # Insert data into `User`
    for i in range(1, num_users + 1):
        user_id = random_uuid()
        username = random_username(i)
        nome = f"User Name {i}"
        email = f"user{i}@example.com"
        reg_date = random_timestamp()
        file.write(f"INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES ({user_id}, '{username}', '{nome}', '{email}', '{reg_date}');\n")

    # Insert data into `Videos`
    for i in range(1, num_videos + 1):
        video_id = random_uuid()
        user_id = random_uuid()
        nome_video = f"Video {i}"
        descricao = random_text()
        tags = random_tags()
        upload_date = random_timestamp()
        file.write(f"INSERT INTO SistemaPartilhaVideos.Videos (video_id, user, nome_video, descricao, tags, upload_date) VALUES ({video_id}, {user_id}, '{nome_video}', '{descricao}', {tags}, '{upload_date}');\n")

    # Insert data into `Comments`
    for _ in range(num_comments):
        video_id = random_uuid()
        comment_id = random_uuid()
        user_id = random_uuid()
        comment_date = random_timestamp()
        comment_content = random_text()
        file.write(f"INSERT INTO SistemaPartilhaVideos.Commments (video_id, comment_id, user_id, comment_date, comment_content) VALUES ({video_id}, {comment_id}, {user_id}, '{comment_date}', '{comment_content}');\n")

    # Insert data into `VideoFollowers`
    for _ in range(num_followers):
        video_id = random_uuid()
        follower_id = random_uuid()
        follow_date = random_timestamp()
        file.write(f"INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES ({video_id}, {follower_id}, ['{follow_date}']);\n")

    # Insert data into `RegistEvents`
    for _ in range(num_events):
        video_id = random_uuid()
        user_id = random_uuid()
        event_type = random.choice(['play', 'pause', 'stop'])
        event_timestamp = random_timestamp()
        video_time_seconds = random.randint(0, 500)
        file.write(f"INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES ({video_id}, {user_id}, '{event_type}', '{event_timestamp}', {video_time_seconds});\n")

    # Insert data into `VideoRatings`
    for _ in range(num_ratings):
        video_id = random_uuid()
        user_id = random_uuid()
        rating_value = random.randint(1, 5)
        file.write(f"INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES ({video_id}, {rating_value}, {user_id});\n")

    # Insert data into `VideoNotifications`
    for _ in range(num_ratings):  # assuming similar count as ratings for simplicity
        notification_id = "now()"
        video_id = random_uuid()
        follower_id = random_uuid()
        comment_id = "now()"
        notification_date = random_timestamp()
        file.write(f"INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES ({notification_id}, {video_id}, {follower_id}, {comment_id}, '{notification_date}');\n")

print("Data insertion script created: insert_data.cql")
