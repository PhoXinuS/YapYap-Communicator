

## PUBLIC ENDPOINTS

#### POST /users/register
* Usage - user registration
* Body: 
  * email
  * password
  * name
* Return - user info


#### POST /users/login
* Usage - user login
* Body:
  * email
  * password
* Return - user info

## ROLE_USER ENDPOINTS

#### GET /users
* Usage - get info about the user
* Return 
  * name
  * email
  * id

#### GET /users/friends
* Usage - get all friends of user (via JWT)
* Return - list of
  * name
  * email


#### GET /users/friends/requests/sent
* Usage - get all users to whom you sent requests
* Return - list of
  * name
  * email

#### GET /users/friends/requests/received
* Usage - get all users from whom you received requests
* Return - list of
    * name
    * email

#### POST /users/friends/requests/send
* Usage - send friend invitation
* Body
  * email or id
* Return - user to whom request was sent
  * name
  * email

#### POST /users/friends/requests/accept
* Usage - accept invitation
* Body
  * email or id
* Return - user you accepted invitation from
  * name
  * email

#### POST /users/friends/requests/reject
* Usage - reject invitation
* Body
    * email or id
* Return - user you accepted invitation from
    * name
    * email

#### POST /users/friends/remove
* Usage - remove friend
* Body
  * email or id
* Return - removed friend
  * name
  * email

#### PATCH /users/name/change/{new name}
* Usage - change user name
* Return
  * new name

#### GET /conversations
* Usage - get all user conversations
* Return list of conversations
  * id
  * name
  * list of members
    * name

#### POST /conversations/create/{conversation name}
* Usage - create conversation
* Body - list of conversation members (without creator)
  * id or email
* Return - conversation
  * id
  * name
  * list of members
    * name


#### DELETE /conversations/leave/{conversation id}
* Usage - leave conversation
* Return - true/false


#### POST /conversations/add/{conversation_id}
* Usage - add user to conversation
* Body
  * email or id

To get all messages from conversation
#### GET /messages/{conversation_id}
* Usage - get all messages from conversation
* Return - list of 
  * id
  * content
  * senderName
  * sentAt
  * numberOfLikes
  * isLiked - have you liked this message

#### POST /messages/add/{conversation_id}
* Usage - add message
* Body
* Return
  * content
  * id
  * content
  * senderName
  * sentAt
  * numberOfLikes
  * isLiked - have you liked this message

#### POST /messages/toggle-like/{message_id}
* Usage - like or unlike the message
* Return
  * is message liked

#### POST /posts/add
* Usage - add post
* Body - post content
  * content (String)
* Return
  * id
  * poster
    * name
    * email
  * content
  * postedAt
  * likes
  * liked

#### DELETE /posts/remove/{post_id}
* Usage - remove post

#### POST /posts
* Usage - get posts of a user
* Body
  * user id or email
* Return - list of
  * id
  * poster
    * name
    * email
  * content
  * postedAt
  * likes
  * liked

#### POST /posts/toggle-like/{postId}
* Usage - like or unlike post
* Return - is post liked after the action

#### GET /comments/{post_id}
* Usage - get all comments of a post
* Return - list of
  * comment id
  * post id
  * commenter
    * commenter name
    * commenter email
  * content
  * commented at
  * likes
  * isLiked

#### POST /comments/send/{post_id}
* Usage - send a comment
* Body
  * content (String)
* Return
  * comment id
  * post id
  * commenter
    * commenter name
    * commenter email
  * content
  * commented at
  * likes
  * isLiked

#### DELETE /comments/remove/{comment_id}
* Usage - remove a comment

#### POST /comments/toggle-like/{commentId}
* Usage - liker or unlike
* Return - is comment liked after the action
