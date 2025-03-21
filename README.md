# 宠物领养系统 API 文档

本文档详细描述了宠物领养系统后端 API 接口设计。

## 技术栈

- Spring Boot 3.x
- Spring Security + JWT
- MySQL 8.x + Redis
- MyBatis-Plus

## API 接口设计

### 用户模块 API

#### 认证授权

| 接口                | 方法   | 描述               | 请求参数                                | 响应                           | 权限      |
|--------------------|-------|-------------------|----------------------------------------|--------------------------------|----------|
| `/api/auth/register` | POST  | 用户注册           | `{email, password, nickname}`          | `{code, message, data: {token}}` | 无       |
| `/api/auth/login`    | POST  | 用户登录           | `{email, password}`                     | `{code, message, data: {token}}` | 无       |
| `/api/auth/verify`   | GET   | 邮箱验证           | `?token=xxx`                           | `{code, message}`              | 无       |
| `/api/auth/resend`   | POST  | 重发验证邮件        | `{email}`                              | `{code, message}`              | 无       |
| `/api/auth/forget`   | POST  | 忘记密码           | `{email}`                              | `{code, message}`              | 无       |
| `/api/auth/reset`    | POST  | 重置密码           | `{token, newPassword}`                 | `{code, message}`              | 无       |

#### 用户信息

| 接口                      | 方法   | 描述                | 请求参数                             | 响应                            | 权限      |
|--------------------------|-------|---------------------|-------------------------------------|--------------------------------|----------|
| `/api/user/profile`      | GET   | 获取个人信息          | -                                   | `{code, message, data: {user}}` | 用户      |
| `/api/user/profile`      | PUT   | 更新个人信息          | `{nickname, avatar, ...}`           | `{code, message}`              | 用户      |
| `/api/user/password`     | PUT   | 修改密码             | `{oldPassword, newPassword}`        | `{code, message}`              | 用户      |
| `/api/user/favorites`    | GET   | 获取收藏列表          | `?page=1&size=10`                   | `{code, message, data: {list, total}}` | 用户 |
| `/api/user/likes`        | GET   | 获取点赞列表          | `?page=1&size=10`                   | `{code, message, data: {list, total}}` | 用户 |
| `/api/user/applications` | GET   | 获取领养申请历史       | `?status=xxx&page=1&size=10`        | `{code, message, data: {list, total}}` | 用户 |

### 宠物模块 API

#### 宠物信息

| 接口                     | 方法   | 描述              | 请求参数                             | 响应                              | 权限      |
|-------------------------|-------|-------------------|-------------------------------------|----------------------------------|----------|
| `/api/pets`             | GET   | 获取宠物列表        | `?breed=xx&age=xx&location=xx&page=1&size=10` | `{code, message, data: {list, total}}` | 无 |
| `/api/pets/{id}`        | GET   | 获取宠物详情        | -                                   | `{code, message, data: {pet}}`   | 无       |
| `/api/pets/{id}/like`   | POST  | 点赞宠物           | -                                   | `{code, message}`                | 用户      |
| `/api/pets/{id}/unlike` | POST  | 取消点赞           | -                                   | `{code, message}`                | 用户      |
| `/api/pets/{id}/favorite` | POST | 收藏宠物          | -                                   | `{code, message}`                | 用户      |
| `/api/pets/{id}/unfavorite` | POST | 取消收藏        | -                                   | `{code, message}`                | 用户      |
| `/api/pets/{id}/report` | POST  | 举报宠物信息        | `{reason, description}`             | `{code, message}`                | 用户      |

#### 物流跟踪

| 接口                            | 方法   | 描述              | 请求参数        | 响应                             | 权限      |
|--------------------------------|-------|-------------------|----------------|----------------------------------|----------|
| `/api/logistics/{applicationId}` | GET   | 获取物流信息        | -              | `{code, message, data: {tracks}}` | 用户      |

### 领养流程 API

| 接口                            | 方法   | 描述              | 请求参数                              | 响应                            | 权限      |
|--------------------------------|-------|-------------------|--------------------------------------|--------------------------------|----------|
| `/api/adoption/apply`         | POST  | 提交领养申请        | `{petId, environment, experience, signature}` | `{code, message, data: {id}}` | 用户     |
| `/api/adoption/{id}`          | GET   | 获取申请详情        | -                                    | `{code, message, data: {application}}` | 用户 |
| `/api/adoption/{id}/cancel`   | POST  | 取消申请           | -                                    | `{code, message}`              | 用户      |
| `/api/adoption/{id}/status`   | GET   | 获取申请状态        | -                                    | `{code, message, data: {status}}` | 用户    |

### 社区论坛 API

#### 帖子管理

| 接口                         | 方法   | 描述              | 请求参数                              | 响应                              | 权限      |
|-----------------------------|-------|-------------------|--------------------------------------|----------------------------------|----------|
| `/api/posts`                | GET   | 获取帖子列表        | `?category=xx&page=1&size=10`        | `{code, message, data: {list, total}}` | 无 |
| `/api/posts`                | POST  | 发布帖子           | `{title, content, category, images}` | `{code, message, data: {id}}`   | 用户      |
| `/api/posts/{id}`           | GET   | 获取帖子详情        | -                                    | `{code, message, data: {post}}` | 无       |
| `/api/posts/{id}`           | PUT   | 更新帖子           | `{title, content, images}`           | `{code, message}`              | 用户      |
| `/api/posts/{id}`           | DELETE | 删除帖子          | -                                    | `{code, message}`              | 用户      |
| `/api/posts/{id}/like`      | POST  | 点赞帖子           | -                                    | `{code, message}`              | 用户      |
| `/api/posts/{id}/unlike`    | POST  | 取消点赞           | -                                    | `{code, message}`              | 用户      |
| `/api/posts/{id}/favorite`  | POST  | 收藏帖子           | -                                    | `{code, message}`              | 用户      |
| `/api/posts/{id}/unfavorite`| POST  | 取消收藏           | -                                    | `{code, message}`              | 用户      |
| `/api/posts/{id}/report`    | POST  | 举报帖子           | `{reason, description}`              | `{code, message}`              | 用户      |

#### 评论管理

| 接口                           | 方法   | 描述              | 请求参数                    | 响应                               | 权限      |
|-------------------------------|-------|-------------------|----------------------------|-----------------------------------|----------|
| `/api/posts/{id}/comments`    | GET   | 获取评论列表        | `?page=1&size=10`          | `{code, message, data: {list, total}}` | 无 |
| `/api/posts/{id}/comments`    | POST  | 发表评论           | `{content}`                | `{code, message, data: {id}}`    | 用户      |
| `/api/comments/{id}`          | PUT   | 更新评论           | `{content}`                | `{code, message}`               | 用户      |
| `/api/comments/{id}`          | DELETE | 删除评论          | -                          | `{code, message}`               | 用户      |
| `/api/comments/{id}/like`     | POST  | 点赞评论           | -                          | `{code, message}`               | 用户      |
| `/api/comments/{id}/unlike`   | POST  | 取消点赞           | -                          | `{code, message}`               | 用户      |
| `/api/comments/{id}/report`   | POST  | 举报评论           | `{reason, description}`    | `{code, message}`               | 用户      |

### 在线问诊 API

| 接口                           | 方法   | 描述              | 请求参数                         | 响应                              | 权限      |
|-------------------------------|-------|-------------------|----------------------------------|----------------------------------|----------|
| `/api/consultations`          | GET   | 获取咨询列表        | `?status=xx&page=1&size=10`     | `{code, message, data: {list, total}}` | 用户 |
| `/api/consultations`          | POST  | 创建咨询           | `{title, description, images}`   | `{code, message, data: {id}}`   | 用户      |
| `/api/consultations/{id}`     | GET   | 获取咨询详情        | -                               | `{code, message, data: {consultation}}` | 用户 |
| `/api/consultations/{id}`     | PUT   | 更新咨询           | `{title, description, images}`   | `{code, message}`              | 用户      |
| `/api/consultations/{id}/messages` | GET | 获取咨询消息     | `?page=1&size=20`               | `{code, message, data: {list}}` | 用户/医生 |
| `/api/consultations/{id}/messages` | POST | 发送咨询消息    | `{content, type, attachment}`   | `{code, message, data: {id}}`  | 用户/医生 |

### 管理员 API

#### 用户管理

| 接口                         | 方法   | 描述              | 请求参数                             | 响应                                | 权限      |
|-----------------------------|-------|-------------------|-------------------------------------|-------------------------------------|----------|
| `/api/admin/users`          | GET   | 获取用户列表        | `?keyword=xx&status=xx&page=1&size=10` | `{code, message, data: {list, total}}` | 管理员 |
| `/api/admin/users/{id}`     | GET   | 获取用户详情        | -                                   | `{code, message, data: {user}}`    | 管理员    |
| `/api/admin/users/{id}/ban` | POST  | 封禁用户           | `{reason, days}`                    | `{code, message}`                  | 管理员    |
| `/api/admin/users/{id}/unban` | POST | 解封用户          | -                                   | `{code, message}`                  | 管理员    |
| `/api/admin/users/{id}/stats` | GET  | 获取用户行为统计    | -                                   | `{code, message, data: {stats}}`   | 管理员    |

#### 宠物/帖子管理

| 接口                            | 方法   | 描述              | 请求参数                              | 响应                               | 权限      |
|--------------------------------|-------|-------------------|--------------------------------------|-----------------------------------|----------|
| `/api/admin/pets`              | GET   | 获取宠物列表        | `?status=xx&page=1&size=10`          | `{code, message, data: {list, total}}` | 管理员 |
| `/api/admin/pets`              | POST  | 添加宠物           | `{name, breed, age, ...}`            | `{code, message, data: {id}}`    | 管理员    |
| `/api/admin/pets/{id}`         | GET   | 获取宠物详情        | -                                    | `{code, message, data: {pet}}`   | 管理员    |
| `/api/admin/pets/{id}`         | PUT   | 更新宠物信息        | `{name, breed, age, ...}`            | `{code, message}`               | 管理员    |
| `/api/admin/pets/{id}`         | DELETE | 删除宠物信息       | -                                    | `{code, message}`               | 管理员    |
| `/api/admin/posts/pending`     | GET   | 获取待审核帖子       | `?page=1&size=10`                    | `{code, message, data: {list, total}}` | 管理员 |
| `/api/admin/posts/{id}/approve` | POST | 审核通过帖子        | -                                    | `{code, message}`               | 管理员    |
| `/api/admin/posts/{id}/reject` | POST  | 审核拒绝帖子        | `{reason}`                           | `{code, message}`               | 管理员    |
| `/api/admin/reports`           | GET   | 获取举报列表        | `?type=xx&status=xx&page=1&size=10`  | `{code, message, data: {list, total}}` | 管理员 |
| `/api/admin/reports/{id}/handle` | POST | 处理举报          | `{action, reason}`                   | `{code, message}`               | 管理员    |

#### 领养申请管理

| 接口                                   | 方法   | 描述              | 请求参数                             | 响应                              | 权限      |
|---------------------------------------|-------|-------------------|-------------------------------------|----------------------------------|----------|
| `/api/admin/applications`             | GET   | 获取申请列表        | `?status=xx&page=1&size=10`         | `{code, message, data: {list, total}}` | 管理员 |
| `/api/admin/applications/{id}`        | GET   | 获取申请详情        | -                                   | `{code, message, data: {application}}` | 管理员 |
| `/api/admin/applications/{id}/approve` | POST | 批准申请           | `{comment}`                         | `{code, message}`               | 管理员    |
| `/api/admin/applications/{id}/reject` | POST  | 拒绝申请           | `{reason}`                          | `{code, message}`               | 管理员    |
| `/api/admin/applications/{id}/logistics` | POST | 更新物流信息     | `{trackingNumber, company, status}` | `{code, message}`               | 管理员    |

#### 数据统计

| 接口                         | 方法   | 描述                  | 请求参数               | 响应                                | 权限      |
|-----------------------------|-------|-----------------------|----------------------|-------------------------------------|----------|
| `/api/admin/stats/overview` | GET   | 获取系统概览数据         | `?period=week`       | `{code, message, data: {overview}}` | 管理员    |
| `/api/admin/stats/users`    | GET   | 获取用户统计数据         | `?period=month`      | `{code, message, data: {stats}}`    | 管理员    |
| `/api/admin/stats/posts`    | GET   | 获取帖子统计数据         | `?period=month`      | `{code, message, data: {stats}}`    | 管理员    |
| `/api/admin/stats/adoptions` | GET  | 获取领养统计数据         | `?period=month`      | `{code, message, data: {stats}}`    | 管理员    |

## 通用响应格式

```json
{
  "code": 200,       // 状态码，200成功，非200表示错误
  "message": "成功",  // 响应消息
  "data": {          // 响应数据，可选
    // 具体数据内容
  }
}
```

## 错误码说明

| 错误码 | 描述               |
|-------|-------------------|
| 200   | 成功               |
| 400   | 请求参数错误        |
| 401   | 未登录/token无效    |
| 403   | 权限不足           |
| 404   | 资源不存在          |
| 500   | 服务器内部错误      |

## 权限说明

- **无**: 任何人都可以访问
- **用户**: 需要用户登录
- **医生**: 需要医生角色权限
- **管理员**: 需要管理员角色权限