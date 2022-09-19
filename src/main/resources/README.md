## 数据库构造

**User：存储用户所有信息**

| 列名            | 数据类型 | 长度 | 注释             |
| --------------- | -------- | ---- | ---------------- |
| user_id         | long     |      | 用户id，自增     |
| uuid            | varchar  | 35   | 用户的uid，唯一  |
| nickname        | varchar  | 16   | 用户昵称         |
| phoneNumber     | varchar  | 11   | 手机号，唯一     |
| email           | varchar  | 30   | 邮箱，唯一       |
| sex             | int      | 2    | 性别，默认男     |
| address         | varchar  | 50   | 居住地           |
| user_avatar     | varchar  | 70   | 用户头像         |
| password        | varchar  | 200  | 用户密码【加密】 |
| desc            | varchar  | 150  | 用户自己的简介   |
| status          | int      | 5    | 用户状态         |
| last_login_time | date     |      | 上次登录时间     |
| register_time   | date     |      | 注册时间         |

**friend表：保存所有好友信息**

| 列名        | 数据类型 | 长度 | 注释           |
| ----------- | -------- | ---- | -------------- |
| friend_id   | long     |      | 好友表id，自增 |
| uuid        | varchar  | 35   | 用户uid，唯一  |
| friend_uuid | varchar  | 35   | 好友的uuid     |

