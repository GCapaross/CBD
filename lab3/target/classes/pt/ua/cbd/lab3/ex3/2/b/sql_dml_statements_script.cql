-- Use keyspace
USE SistemaPartilhaVideos;

-- Query all records in JSON format for each table and save them into JSON files
SELECT JSON * FROM SistemaPartilhaVideos.User;             -- Redirect output to User.json
SELECT JSON * FROM SistemaPartilhaVideos.Videos;           -- Redirect output to Videos.json
SELECT JSON * FROM SistemaPartilhaVideos.Commments;        -- Redirect output to Comments.json
SELECT JSON * FROM SistemaPartilhaVideos.VideoFollowers;   -- Redirect output to VideoFollowers.json
SELECT JSON * FROM SistemaPartilhaVideos.RegistEvents;     -- Redirect output to RegistEvents.json
SELECT JSON * FROM SistemaPartilhaVideos.VideoRatings;     -- Redirect output to VideoRatings.json
SELECT JSON * FROM SistemaPartilhaVideos.VideoNotifications; -- Redirect output to VideoNotifications.json