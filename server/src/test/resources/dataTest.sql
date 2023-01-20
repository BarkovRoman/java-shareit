MERGE INTO USERS (ID, NAME, EMAIL)
    values ( 1, 'user1', 'user1@email.ru' ),
           ( 2, 'user2', 'user2@email.ru' ),
           ( 3, 'user3', 'user3@email.ru' );

MERGE INTO ITEMS (ID, NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID, REQUEST_ID)
    values ( 1, 'item1', 'descriptionItem1', true, 1, null ),
           ( 2, 'item2', 'descriptionItem2', true, 2, null ),
           ( 3, 'item3', 'descriptionItem3', true, 2, null ),
           ( 4, 'item4', 'descriptionItem4', true, 2, 1 );

MERGE INTO REQUESTS (ID, DESCRIPTION, REQUESTOR_ID, CREATED)
    values ( 3, 'requests3', 1, localtimestamp() );

MERGE INTO COMMENTS (ID, TEXT, ITEM_ID, AUTHOR_ID, CREATED)
    values ( 1, 'commentUser1', 2, 1, localtimestamp() );

MERGE INTO BOOKINGS (ID, START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS)
    values ( 1, localtimestamp(), localtimestamp()+0.001, 2, 1, 'APPROVED' ),
           ( 2, localtimestamp(), localtimestamp()+2, 2, 1, 'APPROVED' ),
           ( 3, localtimestamp(), localtimestamp()+2, 1, 2, 'REJECTED' ),
           ( 4, localtimestamp(), localtimestamp()+2, 3, 1, 'REJECTED' ),
           ( 5, localtimestamp(), localtimestamp()+0.001, 1, 3, 'APPROVED' );