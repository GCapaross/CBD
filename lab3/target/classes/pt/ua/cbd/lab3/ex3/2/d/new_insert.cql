-- Para novas tabelas ou novos dados adicionados, para nao estar a alterar as tabelas antigas


CREATE TABLE SistemaPartilhaVideos.FollowedVideoComments (
    follower_id UUID,
    video_id UUID,
    comment_id UUID,
    comment_content TEXT,
    comment_date TIMESTAMP,
    PRIMARY KEY (follower_id, video_id, comment_date)
) WITH CLUSTERING ORDER BY (video_id ASC, comment_date DESC);


-- Inserir comentários de vídeos seguidos com UUIDs válidos

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, 4b567adf-5f6b-46c1-bdf0-b6fa1e093073, 7a4eaeed-bbbb-4f10-8a7b-fce382c72f61, 'Great video!', '2024-01-01T10:30:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, 4b567adf-5f6b-46c1-bdf0-b6fa1e093073, a6c424c4-69d1-4cf0-bd65-1f10a9e73075, 'Awesome content, keep it up!', '2024-01-01T12:45:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (e7d4b3cd-0451-43ac-bbd4-bb107b490f27, 9b925e73-767c-40c6-bf31-cc13ae841713, d1ed6b72-03f7-4ab1-b443-43a6251d40fa, 'Very informative!', '2024-02-15T09:00:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (e7d4b3cd-0451-43ac-bbd4-bb107b490f27, 9b925e73-767c-40c6-bf31-cc13ae841713, 8fe290de-fc79-45d6-a35a-338fd037a99b, 'I learned a lot from this video.', '2024-02-16T11:00:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (f8bfe8b1-358d-4b59-86c9-bd44a47e5fdb, c12bc3ff-b01d-4c5f-bb9b-57580e5a1a50, a7a3d9b2-3f3b-4235-a4f9-c110f120ed38, 'Nice explanation, thanks!', '2024-03-01T14:30:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (f8bfe8b1-358d-4b59-86c9-bd44a47e5fdb, c12bc3ff-b01d-4c5f-bb9b-57580e5a1a50, 7d2e3e2a-4c57-4f29-94a4-1842f4bda9c1, 'Looking forward to the next video.', '2024-03-01T16:00:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (a74e244b-d7d5-4667-b728-693c416255ac, ca3b7d7d-d7b2-4e2c-a66e-4a7e8d9b71a3, 9dfe27e9-c3ac-4a97-b9c0-1ca5690a0e11, 'This video is so helpful!', '2024-04-05T08:20:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (a74e244b-d7d5-4667-b728-693c416255ac, ca3b7d7d-d7b2-4e2c-a66e-4a7e8d9b71a3, 27a2f4db-d0c5-44ff-8c9b-4ca5b06373fa, 'I love the tips shared here.', '2024-04-06T09:45:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (bdb7bfa5-282a-420e-9265-6ab20e1d918d, 11fa482b-ec90-4c72-98e3-57929b6165a4, c7e70b9b-26ec-423b-b87e-d8251bb24a1b, 'Amazing tips for beginners!', '2024-05-20T12:15:00');

INSERT INTO SistemaPartilhaVideos.FollowedVideoComments (follower_id, video_id, comment_id, comment_content, comment_date) 
VALUES (bdb7bfa5-282a-420e-9265-6ab20e1d918d, 11fa482b-ec90-4c72-98e3-57929b6165a4, 885d4a93-85de-4ff2-913f-3d6180322c33, 'I really appreciate this content.', '2024-05-21T14:00:00');










CREATE TABLE SistemaPartilhaVideos.VideoRatingsByRank (
    video_id UUID,
    rating_value INT,
    nome_video TEXT,
    PRIMARY KEY (video_id, rating_value)
) WITH CLUSTERING ORDER BY (rating_value DESC);

CREATE TABLE SistemaPartilhaVideos.TopVideosByRating (
    rank INT,
    video_id UUID,
    nome_video TEXT,
    rating_value INT,
    PRIMARY KEY (rank)
);

CREATE TABLE SistemaPartilhaVideos.TagsByVideo (
    tag TEXT,
    video_id UUID,
    PRIMARY KEY (tag, video_id)
);


