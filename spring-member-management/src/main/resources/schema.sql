CREATE TABLE USERS(
   USER_ID VARCHAR(8) UNIQUE,
   PASSWORD VARCHAR(64) NOT NULL,
   USER_NAME VARCHAR(8) NOT NULL,
   EMAIL VARCHAR(32) NOT NULL,
   ROLE VARCHAR(8) NOT NULL
);