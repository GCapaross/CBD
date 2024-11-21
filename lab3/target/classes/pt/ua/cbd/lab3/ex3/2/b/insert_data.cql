
-- Drop the keyspace if it exists to avoid conflicts
DROP KEYSPACE IF EXISTS SistemaPartilhaVideos;

-- Create keyspace
CREATE KEYSPACE SistemaPartilhaVideos WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

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
    video_id UUID,
    author_id UUID, -- Autor da partilha é o user
    nome_video TEXT,
    descricao TEXT,
    tags LIST<TEXT>,
    upload_date TIMESTAMP,
    PRIMARY KEY (author_id, video_id)
);
CREATE INDEX ON SistemaPartilhaVideos.Videos (tags);


CREATE TABLE SistemaPartilhaVideos.Comments(
    video_id UUID,
    comment_id UUID,
    user_id UUID,
    comment_date TIMESTAMP,
    comment_content TEXT,
    PRIMARY KEY (video_id, comment_date, user_id)
) WITH CLUSTERING ORDER BY (comment_date DESC, user_id DESC);
CREATE INDEX ON SistemaPartilhaVideos.Comments (user_id);


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
CREATE INDEX ON SistemaPartilhaVideos.RegistEvents (user_id);
CREATE INDEX ON SistemaPartilhaVideos.RegistEvents (event_type);



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



CREATE TABLE SistemaPartilhaVideos.VideoRatingsByVideo (
    video_id UUID,
    rating_value INT,
    user_id UUID,
    PRIMARY KEY (video_id, user_id)
);


-- Users
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (20f8114d-191f-40e5-9a80-c113e9638d3e, 'user1', 'User Name 1', 'user1@example.com', '2024-11-08T11:57:32.202182');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (541d1cc5-9130-4974-82f2-423fd86dc14e, 'user2', 'User Name 2', 'user2@example.com', '2024-08-24T11:57:32.202280');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (c16d81c4-e28c-43cb-8601-fb661db99e18, 'user3', 'User Name 3', 'user3@example.com', '2024-09-21T11:57:32.202336');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (2a51d492-b398-4adc-84d5-dabb47187d2c, 'user4', 'User Name 4', 'user4@example.com', '2024-09-11T11:57:32.202376');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (fd2d522d-52f5-411a-a464-9138f0f23e51, 'user5', 'User Name 5', 'user5@example.com', '2024-08-25T11:57:32.202413');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (350321c3-1386-4c9a-8022-9d4d1506c9d6, 'user6', 'User Name 6', 'user6@example.com', '2024-09-25T11:57:32.202449');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (5197366a-f9b8-46df-b3b3-ca10ff1e99ee, 'user7', 'User Name 7', 'user7@example.com', '2024-09-12T11:57:32.202484');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (da2a31b5-990c-4224-b9d3-f0ee2a58fbd5, 'user8', 'User Name 8', 'user8@example.com', '2024-09-06T11:57:32.202519');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (e21b04e1-470c-446d-a893-c7a53dc9c957, 'user9', 'User Name 9', 'user9@example.com', '2024-10-01T11:57:32.202553');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (3a5961e7-b996-499e-a90f-5b1ebb4247a8, 'user10', 'User Name 10', 'user10@example.com', '2024-09-20T11:57:32.202587');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (5f361293-32e4-4451-893d-4bd41e75ca33, 'user11', 'User Name 11', 'user11@example.com', '2024-09-11T11:57:32.202623');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (56249ade-95fb-4067-b0d3-0ad194197ad9, 'user12', 'User Name 12', 'user12@example.com', '2024-09-30T11:57:32.202675');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (6574854f-09dc-420c-b494-80f11605261b, 'user13', 'User Name 13', 'user13@example.com', '2024-11-02T11:57:32.202702');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (25b021ce-571e-4cab-bcf5-a1af3320aa65, 'user14', 'User Name 14', 'user14@example.com', '2024-08-13T11:57:32.202720');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (5e21d680-aed5-4880-8a5c-37707b79f42a, 'user15', 'User Name 15', 'user15@example.com', '2024-09-06T11:57:32.202736');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (f1f2e635-3a3d-4d29-8dd6-2b3f67f3a552, 'user16', 'User Name 16', 'user16@example.com', '2024-10-09T11:57:32.202753');
INSERT INTO SistemaPartilhaVideos.User (id, username, nome, email, resgist_date) VALUES (205eedac-29dc-44fc-82d3-e41e445c64bd, 'user17', 'User Name 17', 'user17@example.com', '2024-08-07T11:57:32.202769');
-- Videos
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (34091a85-e895-4c09-8b3f-34c4e5d42030, 205eedac-29dc-44fc-82d3-e41e445c64bd, 'Video 2', 'amazing great interesting', ['tag11', 'tag12'], '2024-09-10T11:57:32.202872');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (2b46a093-e590-4c45-8fd4-91b7d90b4a04, 205eedac-29dc-44fc-82d3-e41e445c64bd, 'Video 1', 'wonderful awesome cool', ['tag1', 'tag15'], '2024-10-14T11:57:32.202827');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (6bbb9426-9b74-4d95-9a24-6b354130f577, 6d21f0c3-18b1-49aa-b35a-c53bd835b75e, 'Video 3', 'beautiful great beautiful', ['tag4', 'tag7'], '2024-10-15T11:57:32.202910');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (07c2674c-014f-4edd-99de-fd0b68cf84d1, f893cdc2-5c81-4b10-b890-b8fbf2d4353a, 'Video 4', 'interesting wonderful cool', ['tag4', 'tag3', 'tag13'], '2024-10-14T11:57:32.202946');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (6e7dfbf6-96cd-4dbf-b8b4-3b186d93c312, f0d1809b-22a8-4f96-a150-3b8e4684c850, 'Video 5', 'amazing great cool', ['tag6', 'tag1'], '2024-09-13T11:57:32.202981');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (a47a733c-bfa2-4766-8766-808ea6d55556, 23e7b491-b2eb-47a1-a79d-dff80e695eca, 'Video 6', 'interesting cool interesting', ['tag7', 'tag3', 'tag5'], '2024-11-06T11:57:32.203024');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (d1a66d89-7d69-4ddf-8ce2-ce33261d7648, 6d97cf9c-f1bf-4ffa-9ce0-5869537ae733, 'Video 7', 'cool beautiful cool', ['tag1', 'tag8'], '2024-09-07T11:57:32.203100');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (a58efcaa-fe64-4b73-a422-68b69b43f634, dfda25ba-b938-48a9-b404-1921760df6ee, 'Video 8', 'amazing beautiful wonderful', ['tag6', 'tag7'], '2024-08-28T11:57:32.203448');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (71ddd58b-7842-4395-9c56-25888d7c2a65, ccbdd2b7-3757-4c99-835a-32e1f84e9f1c, 'Video 9', 'beautiful beautiful interesting', ['tag7'], '2024-10-01T11:57:32.203501');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (a8768398-dcb0-4172-87df-f040a056b058, c0c9df7c-cf60-4b0d-8852-bffdb19af945, 'Video 10', 'amazing cool awesome', ['tag6', 'tag13'], '2024-10-06T11:57:32.203544');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (08a09e39-46ac-4000-a10a-a51a4af1582b, aa61476e-7940-414b-a623-e1c7507a456e, 'Video 11', 'beautiful beautiful amazing', ['tag11', 'tag15', 'tag3'], '2024-10-17T11:57:32.203589');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (ef29ce46-3fec-4b1c-9f31-f3098ae323ce, 88f289ec-053f-4ce1-b75b-c01e2439ee86, 'Video 12', 'awesome awesome beautiful', ['tag4', 'tag9', 'tag10'], '2024-10-09T11:57:32.203631');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (faf05866-bea3-44f8-ab32-b14777b012a7, 23bc0692-12ff-48b2-862f-a4489efd7d5f, 'Video 13', 'interesting great amazing', ['tag6', 'tag4'], '2024-09-03T11:57:32.203779');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (89ca357d-7e55-4cd2-b3e4-5ff85388a56a, f2fc4f32-14af-4141-95c1-f6e2a217eba9, 'Video 14', 'awesome interesting amazing', ['tag8', 'tag13', 'tag1'], '2024-08-10T11:57:32.203835');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (816d1f9b-b71f-40b9-8b4b-366c27833aef, 39d15694-44e2-4ac9-ba5d-2ec9e0aec4bb, 'Video 15', 'amazing interesting beautiful', ['tag5', 'tag2', 'tag2'], '2024-08-12T11:57:32.203882');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (99a1b515-dde3-4c9b-a2b9-6263cf34c1bc, 27380372-c2fe-4f2d-97c5-b82ad22352be, 'Video 16', 'awesome interesting beautiful', ['tag15', 'tag6', 'tag15'], '2024-08-06T11:57:32.203930');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (53c5b1d5-5567-46f6-9b2d-0b5c9cf25f5c, 176e5efd-7fdf-44ad-ad0b-387ad5064edc, 'Video 17', 'great wonderful awesome', ['tag8', 'tag11'], '2024-09-16T11:57:32.203974');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (a24eff53-4c91-4dad-b667-b1d3578eab85, 24dc7f54-ec4a-4d84-9521-f2a03ac435e8, 'Video 18', 'wonderful amazing beautiful', ['tag3', 'tag2'], '2024-11-06T11:57:32.204024');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (b604d8e2-92e5-4daf-8622-b9727ad5a533, 4420451f-2d0e-46da-ba45-8bf5e782ce2c, 'Video 19', 'interesting wonderful awesome', ['tag6'], '2024-10-31T11:57:32.204059');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (74c1b1b0-60c1-43ad-9ae9-f51d435dba1e, dca19809-eb07-4d20-a993-3a0832052829, 'Video 20', 'great cool beautiful', ['tag14'], '2024-10-08T11:57:32.204096');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (9b925e73-767c-40c6-bf31-cc13ae841713, 83a0d2e2-365a-46d7-9110-012df3495903, 'Video 21', 'amazing great amazing', ['tag10', 'tag10', 'tag14'], '2024-09-23T11:57:32.204135');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (9cf8f6b4-042b-4e62-8308-c2999e65e025, 2bfeb0be-0807-460c-9d89-02f24734b591, 'Video 22', 'great amazing interesting', ['tag1'], '2024-09-25T11:57:32.204176');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (8bcf9dfd-c2f7-40e2-84ec-0f62d62ca91a, 01a96d86-8403-4bda-b956-47a216638741, 'Video 23', 'beautiful amazing awesome', ['tag11', 'tag14'], '2024-08-29T11:57:32.204217');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (8085703b-57f0-4c50-9d0e-596c2d290b57, 3647b16c-5b68-4604-a9f0-70cc0d0e8ceb, 'Video 24', 'interesting great beautiful', ['tag5', 'tag12'], '2024-08-10T11:57:32.204256');
INSERT INTO SistemaPartilhaVideos.Videos (video_id, author_id, nome_video, descricao, tags, upload_date) VALUES (2a53c071-1de8-404b-b125-1caba0164751, 3a0394cb-cb22-4d88-a318-ce73971e4723, 'Video 25', 'amazing wonderful great', ['tag1', 'tag10'], '2024-09-10T11:57:32.204292');
-- Comments
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (7f33a585-0732-42d6-963d-d92017df5c0b, ba48191e-1598-43d6-bae4-0d3e6cceada1, 632d1b65-3f2e-4e0b-90c5-7829bc6dc641, '2024-10-20T11:57:32.204328', 'awesome great beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (a927aa42-2586-4e11-a038-8493d727e9e7, 8338f435-0af1-4905-ad2a-15884984dbbc, 35227f5c-b77b-4f9e-a28d-8d1f56a56d6a, '2024-09-10T11:57:32.204386', 'wonderful great interesting');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (c151ae64-57ca-4207-bdb6-ba945711aec1, fbfbc468-c00c-48be-97e1-64eea794cd86, 70fa27e2-6d88-4711-8561-9e1d5b50e7d5, '2024-10-15T11:57:32.204476', 'awesome awesome cool');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (dc99b96a-954c-4ca4-8213-2e8a566fb7ad, 4ac62eed-8f87-48b9-a690-f54fe349fb08, 45ae1694-2d0f-4238-a215-8a885f07b30b, '2024-08-22T11:57:32.204506', 'awesome amazing amazing');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (e921e1af-67d8-4358-a8a0-2169810b489e, 8bfd5652-96cf-4802-9db6-37c632c1ad8c, 13d1bd0a-8855-4a4d-a537-6a65b1bd0534, '2024-09-30T11:57:32.204528', 'great cool great');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (bafce726-cfe0-4ec6-ab28-292d43a4810d, 51c6651b-0357-4f2d-bd4e-94c7a5630857, 76220e52-3332-401d-93d2-9ef725a93922, '2024-10-28T11:57:32.204549', 'wonderful awesome great');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (bafce726-cfe0-4ec6-ab28-292d43a4810d, 469d90d9-8039-46af-a455-a4eb9199736f, f0dd2277-fef9-47d8-858c-de69be9d952a, '2024-10-13T11:57:32.204569', 'cool great beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (bafce726-cfe0-4ec6-ab28-292d43a4810d, c128bb68-7103-448b-a9d7-b0991d1cff43, 857a8d36-31f0-4b17-8995-dd41d0561eac, '2024-10-01T11:57:32.204599', 'amazing interesting wonderful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (bafce726-cfe0-4ec6-ab28-292d43a4810d, c391a7ec-59f6-4b23-ad4f-ff3ec0deb9bf, 2da58179-a6e4-4b4e-a0e3-390a96e28d1b, '2024-09-21T11:57:32.204621', 'great awesome awesome');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (69fe8089-e13f-45ed-8ce7-1679bb4637e5, 96a5a6a3-862e-491c-9c1a-15be2f6a5f92, 9b135955-6d9e-4da6-9fe8-8ccb1ad581cc, '2024-10-22T11:57:32.204642', 'awesome wonderful great');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (ebef0354-2a4b-4a49-a739-9f9f245d6e59, 1220d846-4632-410f-bf16-e85878847daf, 678a20f3-51e4-46de-976a-2f96a96e7b35, '2024-10-31T11:57:32.204663', 'cool great wonderful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (3abf0b16-4193-4626-9f87-b33296bdc3f3, 66c4670f-df3e-471f-b0d4-0a08c593e4f0, fe9d15b4-cc46-4d0a-8b57-254eccc5a12c, '2024-09-09T11:57:32.204683', 'wonderful awesome cool');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (9770d20f-6c4c-455f-b787-2b28342b18b4, f5c4e62a-6d54-4315-806b-421a0e3f3df9, 6678893d-ed61-4596-910c-ee6488e87439, '2024-09-08T11:57:32.204704', 'cool awesome great');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (6aeea33a-2a5e-42fe-8d92-127bb36fe44d, 76df841b-9f25-4913-b5bb-103441aeeee4, 498113da-71e7-40ac-a4e3-aaec9c74b625, '2024-08-28T11:57:32.204725', 'amazing amazing amazing');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (5861e411-6d71-49ab-9274-aeb0a895b196, 70feeace-4aa1-471c-b7fe-cf723e89aa6c, f632970f-abbc-4cd3-9111-9d4a6b9386ce, '2024-09-03T11:57:32.204748', 'great great amazing');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (0f5e1ebe-f17d-4f92-a34d-8bd6763c089c, 877cdf5f-73d9-45fb-9908-5ab27d527b18, 1678e644-16d5-4063-836c-41eae1ea16f2, '2024-08-31T11:57:32.204771', 'wonderful awesome cool');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (4cb9f827-b49b-4a4a-83a6-79b072317f28, 12143757-3d24-450f-b00e-8d4a144fe7f1, f53e883b-c6c7-4a01-9490-7c394868ae34, '2024-09-30T11:57:32.204796', 'wonderful interesting wonderful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (490aff3d-bfaa-4611-9913-6f9a43831717, ba51a413-94d3-44e0-8fb6-f9bc1fb411dd, 6400cc67-5fb6-41a1-8e50-2069566a771f, '2024-10-20T11:57:32.204969', 'awesome wonderful cool');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (817fa90e-8e62-46f8-9595-735938f64679, 9f160df6-72e1-4fe5-bb11-602d45d9c094, bda25c53-9aee-4376-b163-54689a06fda1, '2024-10-19T11:57:32.205039', 'great cool amazing');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (45061326-11ab-4c1a-af7e-a78cfa3f8d15, d86116aa-61e3-4942-9810-07ff2e303218, 21bc8f07-3588-4e55-9d65-852fb7c05cb9, '2024-10-28T11:57:32.205105', 'awesome wonderful interesting');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (3c879465-80cd-4b93-9e97-8e74d3e5c371, d394d936-4e62-437b-a7c2-794c04ab26db, 23b2a38e-3642-4c64-ad13-8872248f24f0, '2024-09-09T11:57:32.205143', 'wonderful beautiful beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (b14594fd-b1b4-4895-8186-7f1fdbbbd9e4, 4af8f43b-9055-4151-b533-4a3a68ebec94, 7e3eea94-042d-4369-a07c-8f8f1f3daa04, '2024-10-28T11:57:32.205196', 'amazing interesting cool');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (9c777d6e-6019-48f7-a014-63983c0a11c4, 624eb085-8dea-44ef-9e95-68e59213d807, 690a1401-01eb-40b3-998d-9c459d4748d0, '2024-08-08T11:57:32.205235', 'interesting interesting beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (abcd3865-c116-4ba7-88b9-e585876611b7, 93bf6370-a32e-4c88-b5e3-c83c03228848, 440c8163-5ce3-4e2d-8da4-350616a0dcf9, '2024-10-19T11:57:32.205258', 'amazing wonderful wonderful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (87d87cf1-a0a2-4918-b0b7-d57704aefc1b, 9daa77cd-57e3-4b54-9db4-348ebbb0c015, da9a0df9-c6b4-4079-938c-d106b90c0c92, '2024-09-03T11:57:32.205279', 'awesome wonderful wonderful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (3f3dfac7-5d9a-471d-b957-d779755b6724, 0b1d6a42-0425-4e60-87c1-352863ec0159, 5b5e82fe-d6d9-4b80-9cc0-b791634c0a54, '2024-11-03T11:57:32.205302', 'amazing awesome awesome');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (8d7bb188-ab0c-420f-97d6-7e04138f274c, c8f0da62-c7e0-4d74-8ef3-1e8d4bd2088e, a7ca8594-2cda-489f-8a8b-8d184d1bc0cc, '2024-09-18T11:57:32.205325', 'wonderful cool beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (c0f4f3e5-41ed-44b1-82f5-7d19ab31c663, e13d08c5-8462-4b05-869c-f976237d5233, c00c3f1c-0ff9-46e6-bd61-6dad03cc9263, '2024-09-28T11:57:32.205347', 'amazing wonderful beautiful');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (79c3c396-3efa-41a4-83e6-58945597e559, de541856-d61a-4738-bbc1-640cf02349e4, 8c8b1e87-4319-469f-a75d-ea1d1b3a9ef6, '2024-11-13T11:57:32.205368', 'awesome beautiful interesting');
INSERT INTO SistemaPartilhaVideos.Comments (video_id, comment_id, user_id, comment_date, comment_content) VALUES (706f14bc-b53e-4b37-9a05-0649660bac2b, be700378-1ce5-4059-a89a-2474a854a4ee, 1e3a9030-869c-4744-be16-23389b61a53d, '2024-10-01T11:57:32.205389', 'interesting cool beautiful');
-- VideoFollowers
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (3fab1a21-8bd3-4f0d-b8c7-f5b36ae5b5e1, 28f3163c-226c-42aa-9f4f-4a810144d40c, ['2024-11-10T11:57:32.205418']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (b82eed04-99ae-4abd-9b33-05ed0e00614c, 0beee940-a7b2-4601-ba4e-abcd6d3285a0, ['2024-08-25T11:57:32.205436']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (53368ac7-d781-4197-b138-a5fe1fe3b2bf, c5104aa5-e61d-4293-9851-0145ccad7b58, ['2024-10-09T11:57:32.205451']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (0e42c454-e523-43e2-b248-cd56c733a1c8, ce8dbaf0-85a2-453d-9083-73c7b8ac57fa, ['2024-11-11T11:57:32.205465']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (1724f5b7-833f-4590-b541-8135198b6033, 3125840c-fc72-4591-a3f7-fe72c2d428f4, ['2024-09-17T11:57:32.205479']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (b45925bc-479c-4550-a9e9-f0983a80002a, a2a040e1-1903-4412-a6a1-eb4abcde4de8, ['2024-11-03T11:57:32.205492']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (4f18d8c1-d2c4-44d8-80bf-f28429be04c9, c4152dfa-5c4e-4e20-b895-3910537eee6f, ['2024-08-10T11:57:32.205506']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (12664200-de34-4561-b539-1c3b2ef83f83, 68410135-3067-4620-8d4b-7703e2380e8b, ['2024-08-25T11:57:32.205519']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (13a197cb-dc17-41a5-a40b-8dad1b2bc2ef, 30c8f721-b5f7-4604-a95d-87adb23e3633, ['2024-09-24T11:57:32.205533']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (5a64f506-a663-42e1-8c58-a254990a9e00, 533bec61-3f2c-4876-9d4a-8a5f27f0d6a0, ['2024-11-01T11:57:32.205560']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (423ee064-2fc8-4a5d-a644-7d8851ca0b73, 565059db-0c7b-4ac7-8645-7229975ea23f, ['2024-09-07T11:57:32.205575']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (38edc4cd-ce78-4440-b55e-a5ab72f6abf7, 1c4db61c-975a-45c6-8d20-4d2eb85db750, ['2024-10-16T11:57:32.205591']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (a9c9e482-cd70-41a6-b7cc-78516c607c16, e2d9541e-8df8-4df3-9987-5876bac84206, ['2024-08-17T11:57:32.205606']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (7231eff3-a893-4b44-9e88-623dc8c9efe3, 1256c5bf-5710-422e-bc79-80f3e0df6b11, ['2024-08-14T11:57:32.205621']);
INSERT INTO SistemaPartilhaVideos.VideoFollowers (video_id, follower_id, follow_date) VALUES (7399f63c-7eb0-45d5-8529-c57106f1acdb, 1ea60daa-9715-4418-8e25-81533c43e6db, ['2024-10-27T11:57:32.205636']);
-- RegistEvents
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (0bdf27ad-3611-464a-90f8-0fe70a01bcab, 8a450fb4-9bf9-4f3a-9ce4-401fb3dc876f, 'stop', '2024-09-20T11:57:32.205667', 474);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (6d637af8-7108-4a42-8976-9ff5733ae9a7, 7250a601-b14e-4425-8d9e-5cb847aa241b, 'play', '2024-10-19T11:57:32.205739', 182);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (d78ae71a-8221-476a-89b7-89c200772db8, bcc9dddf-bab8-4e9d-a8d7-d8cf08d91054, 'pause', '2024-11-13T11:57:32.205770', 41);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (b04ff207-fcbc-4d52-8d6e-2538c785834f, 0f2c71c1-43be-493c-b38e-ad6582528e7e, 'play', '2024-09-11T11:57:32.205805', 423);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (b92de20b-e9be-4f46-8e26-ccfbfdbad436, 83380339-f610-4764-a6ed-e7644857ba0c, 'pause', '2024-08-13T11:57:32.205828', 104);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (ef5f7ec6-9984-4fb5-8df9-e42e3cfdf4a2, 10f6cb59-a565-4ae9-9ff4-59c3a49fc6dd, 'stop', '2024-08-10T11:57:32.205862', 481);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (8f95ff72-9d59-4bcd-a6e3-b36840f94f14, b856ad87-4fe9-4011-968c-861dcfb6e034, 'pause', '2024-09-18T11:57:32.206008', 373);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (69ab387a-b6fc-4564-9c33-37f9505ea316, 675d9660-15aa-4438-a7dd-43d43ab53fd6, 'stop', '2024-10-01T11:57:32.206061', 39);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (65a31723-b771-40e0-b5af-1084cfa0d736, 37f60fd8-e470-461d-b1d9-37d1d54d3a81, 'pause', '2024-11-12T11:57:32.206091', 432);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (e720ca5a-1d4e-4539-b08a-35b45aae02b9, 9cf04e32-5df8-4732-ae86-7276d14506e4, 'pause', '2024-08-23T11:57:32.206112', 187);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (7c75a743-3095-407e-a4f8-bd7fee890dff, 2749dcb0-01d6-4811-80af-ad9ab65f8609, 'stop', '2024-09-02T11:57:32.206131', 238);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (182d0621-942e-4dfa-988b-5fc07ab4a497, 8686e986-3341-478b-a1e5-2ab0767420f4, 'play', '2024-08-09T11:57:32.206150', 135);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (a24bd377-94fa-46f0-8190-f009bdf9dbb0, adf6b5d6-8be9-4670-a5fe-48941027f42f, 'pause', '2024-08-12T11:57:32.206169', 180);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (de68a574-9a8b-4d9d-8125-f83b1ec61ba0, 3dca3adb-6dc6-44be-aeb9-13a3646fc576, 'pause', '2024-08-06T11:57:32.206186', 414);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (6b1eb367-2c9c-4a3d-b59a-98c0650caa4d, c87f76ea-6f14-434f-85f6-ba0141f7c30b, 'play', '2024-09-21T11:57:32.206214', 201);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (061b6ecb-0f3c-4049-9584-155b630a9106, 8bce6d87-0739-49d7-b3e5-477717db35e3, 'pause', '2024-09-09T11:57:32.206234', 73);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, 2d040dd9-e55d-4622-8964-9019e2706ee3, 'play', '2024-08-16T11:57:32.206250', 196);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (265ebc17-7578-4974-b029-90971ca8284e, 4aa128b3-a6d5-43d8-8233-6cc10e5f661c, 'play', '2024-09-28T11:57:32.206268', 8);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (fa360da8-5668-4e4c-bf8a-6e0370607d43, 3f729a69-5435-44e5-bb66-d62473b62b74, 'pause', '2024-08-16T11:57:32.206286', 33);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (d263e92b-d45b-4be7-bb2c-a1bd8afcefe0, 83496ba2-8ac5-4f3c-8a24-eb33ae92064c, 'pause', '2024-08-27T11:57:32.206305', 150);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (48127d6f-7d78-4ceb-b09e-ef30fd87054a, bb988d4c-d084-4db6-a73b-09d32a282d59, 'stop', '2024-11-10T11:57:32.206323', 73);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (88dfa942-ba71-4827-ad27-d4f993d8ba09, 6fe3dc7d-e63f-42a9-8ce3-9167e9521c7d, 'play', '2024-10-10T11:57:32.206337', 331);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (eeaf8ed3-4499-497c-8c83-d5b1bf5495b1, 1819c5e3-2a42-48cc-848b-12254eb60f56, 'pause', '2024-10-26T11:57:32.206352', 336);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'play', '2024-10-10T11:57:32.206369', 93);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'pause', '2024-10-19T11:57:32.206396', 5);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'stop', '2024-10-11T11:57:32.206436', 312);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'play', '2024-10-31T11:57:32.206468', 226);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'stop', '2024-11-12T11:57:32.206498', 44);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'pause', '2024-10-22T11:57:32.206542', 13);
INSERT INTO SistemaPartilhaVideos.RegistEvents (video_id, user_id, event_type, event_timestamp, video_time_seconds) VALUES (c8f85ebf-2ddc-4c30-947c-c714f08c0f16, b3a2cb05-bddc-4c18-89db-f1f34b4dc03b, 'play', '2024-08-06T11:57:32.206579', 59);
-- VideoRatings
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (f17e561d-8b81-4e2c-b587-5ae6ddf82ef2, 1, 92337b8d-7711-4737-a2b4-bbede5d683c0);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (97b4ec8c-8e7f-45dc-8d72-f647ff37effd, 2, 1ce20dda-950d-4bae-9092-7de843920dc4);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (02bee35e-0863-43d4-b932-72965935296d, 3, 0377f5b7-a034-46cd-a090-1b6f353b82c7);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (b2add022-9793-4f4d-a67c-ed1f820929f4, 5, 3b16c988-3ee0-419a-a71b-76140f672b33);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (45f24c1e-05a4-40a1-b048-80715e091434, 5, 43de9c37-2723-4fee-9697-6c1db90f55a7);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (ce6e0f0d-169b-420c-a093-dc8d52eec75a, 3, 0b45a3bd-f12b-4bb8-8cc5-1368b0ccc965);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (a7dac048-018d-4b6e-a45f-f3bd7f1a9530, 2, d2ae89a8-c445-4942-9fb3-b242acb647c7);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (df096949-1b54-47c1-a2e2-1f9bd44798db, 1, 752e68b8-11c3-45c5-897c-1c18f0990547);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (66017ac7-e8f4-4d2f-8206-b928153b08ea, 3, 76a6d758-6228-4e96-9102-4d12deeba21c);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (f2d589f9-4af2-4fa3-ab0b-5c3db4a41eaa, 1, 67a1890d-6e88-4d2a-ac6a-74d8d96976fc);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (bf4844fd-19f1-47c4-a617-affe5fc6ccb4, 3, 5c12f7fa-9381-4bf0-a94b-eddcbde1785f);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (edb73a19-d66e-4a03-ae0f-18f486116eca, 1, a6c672ad-499e-46fc-9afc-2eb5d42f6072);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (e4706fc2-c0fe-427b-bf2e-96b872c5fc16, 2, c85d32df-856c-4a7a-be61-5a62ea0ba65d);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (76a4b6ec-2ba1-4a94-95c5-131edb88bae6, 2, a6b27c12-de66-492f-bdf3-50c9f1d30c2a);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (6227bd76-1db8-406a-9a3b-3bb4454708e4, 3, bd2a5a1b-405c-43d8-b77f-8457301acafc);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (3d2e980b-c2cd-4e26-97e3-34213bfefe83, 1, b824300f-20bb-4a0c-9849-18802ffb1b99);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (4a8cb7c7-97ca-450a-8e7a-e472b02410b1, 1, 535d1963-fda1-4642-a8a2-02ca1f8e1543);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (9daa9874-dda8-470d-bf88-f42a81724871, 1, 80c1f499-9fad-4c38-8e08-7bce297b1d11);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (26bacc0c-f937-4c52-82d4-93e79bd3143c, 2, d9e29479-a9e2-4878-87bb-3da01cff4c4b);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (d7be9288-6c1d-4826-a2fd-87ddd602a8a8, 3, ba5b507f-eac2-405a-aa09-6876bf263dec);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (f8eb2ef3-bda5-4f15-87ec-0f27d9992a22, 2, f52452ce-f365-4aed-baae-d8573a864a0b);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (c77a749e-c8d6-456c-b8c2-a6a3f49b28ac, 4, fbb1fbb4-ab1a-4661-bc2d-8f9f68147235);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (0fd7adb3-8635-44ce-8dfc-9645e2bfee92, 1, 329ed248-28e8-4049-a858-8284b7d0160e);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (51fcd64b-0baa-4b5d-af09-e53cb9caf465, 1, b6266054-a08a-4562-b78a-981e15119040);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (89fe4145-c9d6-43a0-b2e1-743eeb4ce212, 5, 7e027f6b-9ac6-425b-987f-7fa9e3603996);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (37c18061-9218-48ea-8455-66d5a23a68cd, 5, e7f9e592-1fdf-4658-a743-afdaa78b5e47);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (35d84d73-3307-451f-8880-8144e91c03c9, 2, bf982e39-5631-49d0-9b99-7d43d3da6810);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (5b905802-1f93-45fc-8231-934457f920ac, 2, 77305288-3f2e-4054-9cfd-91b9c6d0b2df);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (21c7a5d9-1e47-423e-81ef-b95537b38be1, 3, ec36e336-711b-44b9-b832-f290f6ab29f2);
INSERT INTO SistemaPartilhaVideos.VideoRatings (video_id, rating_value, user_id) VALUES (3dd4e577-39b0-41bc-8841-c485a0c669c8, 1, 492ca28e-5cbc-484b-8904-42b9f174b0af);
-- VideoNotifications
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 3c15ca0b-0ae8-4170-acc5-b0be6ff59639, 191c4c29-1cfb-4371-bea7-018dbddefbd7, now(), '2024-10-27T11:57:32.207532');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 77cff192-64e4-4804-a82a-e8ac5d16e03d, 94788801-24bc-45e4-91f7-6693d79c987b, now(), '2024-09-17T11:57:32.207603');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 6883d6bc-cea5-42b0-97fb-2a5d7163283d, 98669670-b795-45e0-b50d-1ad07ac3988e, now(), '2024-09-19T11:57:32.207654');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 4f52c3ba-f4a8-443f-b281-84ca1579fef7, 3dae800b-ce1a-41aa-978c-ee6ef2c03987, now(), '2024-08-15T11:57:32.207708');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 0870dcfa-e739-440a-96c2-f7507903fd0f, 63ff9d7c-ae20-43fe-a5f3-b7288f887558, now(), '2024-09-25T11:57:32.207821');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 983bc8f8-d9c2-41f1-add2-0b891f0a759b, 1efed00b-9562-47a9-b2c1-5c8eaf7a4fe5, now(), '2024-11-12T11:57:32.207881');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 20303979-7e0c-4bc0-8c60-ef0de14c4c03, a9397652-17c8-4877-9bc6-0cbc62c06f17, now(), '2024-09-09T11:57:32.207932');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 89bb592d-fe23-4779-b098-e8d6e1821aff, 0e193ae1-64ef-4ecf-aaf5-ce48f6ad4b81, now(), '2024-08-26T11:57:32.207983');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 79039b3b-8273-487c-a195-f6bac274896b, 4c7fc7ff-5198-4fa4-9924-a77cf3195631, now(), '2024-08-27T11:57:32.208031');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 2b60fbbd-a855-46dc-bfb5-86885629c83b, 1752ff88-6456-442d-8644-9e6cc6f3ab21, now(), '2024-10-01T11:57:32.208078');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), d603213f-9d1b-4f62-8cb7-c1c9347b48d2, fab68ccc-bc33-4144-b5a1-1d8d222dd02b, now(), '2024-08-20T11:57:32.208113');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 692ed7bb-b9ff-4cfd-b86d-4042bd3b3a6b, ea8aa9c2-9073-4e44-b447-867902a966e0, now(), '2024-08-07T11:57:32.208148');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 762e51bf-f8f7-4873-9ce6-3ed42854f796, 6d28f665-b0e6-4587-933b-6b8d28c16b21, now(), '2024-11-11T11:57:32.208184');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), b72fc067-7e60-4b0b-adbe-b1641e99d6b7, f4d56b6e-2289-4210-ab07-b7733cb78e5e, now(), '2024-09-29T11:57:32.208219');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 291e7cae-7c9a-470f-8d2b-4ff161ab8b74, a754f743-36e6-4061-9567-403c904c8979, now(), '2024-10-12T11:57:32.208252');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 6125a5b1-e022-414d-bc06-38f109f969f9, 332039f0-3326-42d6-b74b-18d2ff74fbdf, now(), '2024-10-17T11:57:32.208301');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 1b6a72ef-9e65-44ef-90a1-226575ac22c8, d8a9086a-ae96-4e85-8c13-0624cf683523, now(), '2024-10-05T11:57:32.208341');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), e34c226a-1c98-4640-8882-56c72c1c11d0, 788eee9c-cd3d-4999-8fd7-6cc6c9f69836, now(), '2024-11-13T11:57:32.208380');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 5e13195e-90b1-431e-acac-6c7033bdf09d, 405e51fb-a12e-48ab-aaa1-6826fe52784e, now(), '2024-08-31T11:57:32.208420');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 3d3e4984-e500-4974-a95e-24831de68bfb, a88709a4-a9fa-4012-bd32-083f33446e0d, now(), '2024-11-08T11:57:32.208461');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), fdf083d3-04cb-4fde-80da-d24e007f2e7e, 33dfdbad-d447-4107-b273-73e7defc9a01, now(), '2024-08-08T11:57:32.208590');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), dede7ee1-cc54-4d63-a3c6-125a09defb1f, 024de01f-3dc5-4bd5-8d8a-b92289c4742e, now(), '2024-09-14T11:57:32.208637');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 8c02e9fa-d027-482f-b258-170b25568b72, ed12c3bd-98f0-4c06-838d-468fd0b42848, now(), '2024-10-11T11:57:32.208678');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), c12b62e7-1423-4d00-baca-a4b7687819bd, 514f2ea4-43f1-417b-aff8-5d33de379f19, now(), '2024-09-20T11:57:32.208719');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), e91c5cd1-5396-4843-a3bc-6e3da8f14e94, fd2aba5d-4e7b-466b-afa3-3466c5b22649, now(), '2024-11-01T11:57:32.208764');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 7ac45f74-c45f-4c91-b995-34f10af52a53, 4bd90f94-b3cd-467d-9edd-04f7afa9fd6a, now(), '2024-08-06T11:57:32.208806');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 91b94ed7-df48-4cc0-8899-65e556c3237a, cb641a77-edf1-4caa-9618-eea8c10c0b6b, now(), '2024-10-26T11:57:32.208850');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), eb445394-5799-4525-9cf7-e499419beba0, e2f23b9d-0c58-43e0-a486-845d2dd802a3, now(), '2024-09-19T11:57:32.208893');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), afe8a2f9-1af0-4a88-92e8-9c29ed8a71e3, 2e41215e-fd6d-492a-8dad-2a933736fb25, now(), '2024-10-16T11:57:32.208934');
INSERT INTO SistemaPartilhaVideos.VideoNotifications (notification_id, video_id, follower_id, comment_id, notification_date) VALUES (now(), 43cf8037-bd8b-48c1-9d62-ffa9db8dc2ed, 2b8038db-9c9d-4a6f-b186-c95f65feac7b, now(), '2024-09-26T11:57:32.208976');
