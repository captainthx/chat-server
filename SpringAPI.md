# API Spring (0.0.1)

- [Response](#response)
    - [Response Status Code](#response-status-code)
        - [Success Response](#success-response)
        - [Unauthorized Response](#unauthorized-response)
        - [Common Error Response](#common-error-response)
- [API](#api)
    - [Authentication](#authentication)
        - [Sign Up](#sign-up)
        - [Login](#login)
        - [Forgot Password](#forgot-password)
        - [Reset Password](#reset-password)
        - [Refresh Token](#refresh-token)
        - [Verification Email](#verification-email)
        - [Resend Verification Email](#resend-verification-email)
    - [Account](#account)
        - [Get Account](#get-account)
        - [Update Account](#update-account)
        - [Change Password](#change-password)
        - [Delete Avatar](#delete-avatar)
        - [Delete Cover](#delete-cover)
        - [Delete Account](#delete-account)
        - [Logout](#logout)
    - [Files](#files)
        - [Upload File](#upload-file)
        - [Get File](#get-file)

## Response

### Response Status Code

- 200: [Success](#success-response)
- 401: [Unauthorized](#unauthorized-response)
- 417: [Common Error](#common-error-response)

### Success Response

```json
{
  "success": true,
  "data": {
    "accessToken": "accessToken",
    "refreshToken": "refreshToken"
  },
  "error": null,
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Unauthorized Response

```json
{
  "success": false,
  "data": null,
  "error": "unauthorized",
  "message": "You are not authorized to access this resource.",
  "total": null,
  "timestamp": "2024-09-16T09:01:29.255445900Z"
}
```

### Common Error Response

```json
{
  "success": false,
  "data": null,
  "error": "credentials.invalid",
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:06.065237500Z"
}
```

## API

## Authentication

### Sign Up

- request

```http request
POST /v1/auth/signup
Content-Type: application/json
```

- body

```json
{
  "name": "name",
  "email": "example@email.com",
  "password": "password"
}
```

- validations (frontend validate)

| Field    | Type   | Require | Validate                      | Error            |
|----------|--------|---------|-------------------------------|------------------|
| name     | string | true    | length: 1-30                  | name.invalid     |
| email    | string | true    | email                         | email.invalid    |
| password | string | true    | pattern: ([a-zA-Z0-9_]{8,16}) | password.invalid |

- errors (backend validate)

| Error           | Description     |
|-----------------|-----------------|
| email.duplicate | Email duplicate |

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Sign Up successfully, please verify your email.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Login

- request

```http request
POST /v1/auth/login
Content-Type: application/json
```

- body

```json
{
  "email": "example@email.com",
  "password": "password",
  "device": {
    "token": "12312312",
    "platform": "ANDROID"
  }
}
```

- validations (frontend validate)

| Field           | Type   | Require | Validate                      | Error                   |
|-----------------|--------|---------|-------------------------------|-------------------------|
| email           | string | true    | email                         | email.invalid           |
| password        | string | true    | pattern: ([a-zA-Z0-9_]{8,16}) | password.invalid        |
| device          | object | false   |                               |                         |
| device.token    | string | false   | min-max: 1-255                | device.token.invalid    |
| device.platform | string | false   | enum: ANDROID, IOS            | device.platform.invalid |

- errors (backend validate)

| Error               | Description               |
|---------------------|---------------------------|
| credentials.invalid | email or password invalid |

- response

```json
{
  "success": true,
  "data": {
    "accessToken": "accessToken",
    "accessExpire": 1759304171,
    "refreshToken": "refreshToken",
    "refreshExpire": 1759304171,
    "account": {
      "id": 1,
      "name": "john doe",
      "email": "example@email.com",
      "avatar": "https://example.com/path/to/profile_picture.jpg",
      "cover": "https://example.com/path/to/cover_picture.jpg",
      "verified": true,
      "followers": 999,
      "followings": 999,
      "likes": 999
    }
  },
  "error": null,
  "message": "Successfully logged in.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Forgot Password

- request

```http request
POST /v1/auth/forgot-password
Content-Type: application/json
```

- body

```json
{
  "email": "example@email.com"
}
```

- validations (frontend validate)

| Field | Type   | Require | Validate | Error         |
|-------|--------|---------|----------|---------------|
| email | string | true    | email    | email.invalid |

- errors (backend validate)

| Error         | Description    |
|---------------|----------------|
| user.notFound | User not found |

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Successfully sent verification verify code to email.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Reset Password

- request

```http request
POST /v1/auth/reset-password
Content-Type: application/json
```

- body

```json
{
  "email": "example@email.com",
  "verifyCode": "123456",
  "password": "password"
}
```

- validations (frontend validate)

| Field      | Type   | Require | Validate                      | Error              |
|------------|--------|---------|-------------------------------|--------------------|
| email      | string | true    | email                         | email.invalid      |
| verifyCode | string | true    | length: 6                     | verifyCode.invalid |
| password   | string | true    | pattern: ([a-zA-Z0-9_]{8,16}) | password.invalid   |

- errors (backend validate)

| Error              | Description        |
|--------------------|--------------------|
| user.notFound      | User not found     |
| verifyCode.invalid | VerifyCode invalid |

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Password successfully reset.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Refresh Token

- request

```http request
POST /v1/auth/refresh
Content-Type: application/json
```

- body

```json
{
  "refreshToken": "refreshToken"
}
```

- validations (frontend validate)

| Field        | Type   | Require | Validate | Error                |
|--------------|--------|---------|----------|----------------------|
| refreshToken | string | true    |          | refreshToken.invalid |

- response

```json
{
  "success": true,
  "data": {
    "accessToken": "accessToken",
    "accessExpire": 1759304171,
    "refreshToken": "refreshToken",
    "refreshExpire": 1759304171,
    "account": {
      "id": 1,
      "name": "john doe",
      "email": "example@email.com",
      "avatar": "https://example.com/path/to/profile_picture.jpg",
      "cover": "https://example.com/path/to/cover_picture.jpg",
      "verified": true,
      "followers": 999,
      "followings": 999,
      "likes": 999
    }
  },
  "error": null,
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Verification Email

- request

```http request
POST /v1/auth/verify
Content-Type: application/json
```

- body

```json
{
  "email": "example@email.com",
  "verifyCode": "123456",
  "device": {
    "token": "12312312",
    "platform": "ANDROID"
  }
}
```

- validations (frontend validate)

| Field           | Type   | Require | Validate           | Error                   |
|-----------------|--------|---------|--------------------|-------------------------|
| email           | string | true    | email              | email.invalid           |
| verifyCode      | string | true    | length: 6          | verifyCode.invalid      |
| device          | object | false   |                    |                         |
| device.token    | string | false   | min-max: 1-255     | device.token.invalid    |
| device.platform | string | false   | enum: ANDROID, IOS | device.platform.invalid |

- errors (backend validate)

| Error              | Description        |
|--------------------|--------------------|
| user.notFound      | User not found     |
| verifyCode.invalid | VerifyCode invalid |

- response

```json
{
  "success": true,
  "data": {
    "accessToken": "accessToken",
    "accessExpire": 1759304171,
    "refreshToken": "refreshToken",
    "refreshExpire": 1759304171,
    "account": {
      "id": 1,
      "name": "john doe",
      "email": "example@email.com",
      "avatar": "https://example.com/path/to/profile_picture.jpg",
      "cover": "https://example.com/path/to/cover_picture.jpg",
      "verified": true,
      "followers": 999,
      "followings": 999,
      "likes": 999
    }
  },
  "error": null,
  "message": "Email successfully verified.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Resend Verification Email

- request

```http request
POST /v1/auth/resend-verify
Content-Type: application/json
```

- body

```json
{
  "email": "example@email.com"
}
```

- validations (frontend validate)

| Field | Type   | Require | Validate | Error         |
|-------|--------|---------|----------|---------------|
| email | string | true    | email    | email.invalid |

- errors (backend validate)

| Error         | Description    |
|---------------|----------------|
| user.notFound | User not found |

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Successfully sent verification verifyCode to email.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

## Account

### Get Account

- request

```http request
GET /v1/account
Authorization: Bearer {token}
```

- response

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "john doe",
    "email": "example@email.com",
    "avatar": "https://example.com/path/to/profile_picture.jpg",
    "cover": "https://example.com/path/to/cover_picture.jpg",
    "verified": true,
    "followers": 999,
    "followings": 999,
    "likes": 999
  },
  "error": null,
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Update Account

- request

```http request
PUT /v1/account
Content-Type: application/json
Authorization: Bearer {token}
```

- body

```json
{
  "name": "john doe",
  "avatar": "https://example.com/path/to/profile_picture.jpg",
  "cover": "https://example.com/path/to/cover_picture.jpg"
}
```

- validations (frontend validate)

| Field  | Type   | Require | Validate     | Error          |
|--------|--------|---------|--------------|----------------|
| name   | string | false   | length: 1-30 | name.invalid   |
| avatar | string | false   | url          | avatar.invalid |
| cover  | string | false   | url          | cover.invalid  |

- response

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "john doe",
    "email": "example@email.com",
    "avatar": "https://example.com/path/to/profile_picture.jpg",
    "cover": "https://example.com/path/to/cover_picture.jpg",
    "verified": true,
    "followers": 999,
    "followings": 999,
    "likes": 999
  },
  "error": null,
  "message": "Account successfully updated.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

### Change Password

- request

```http request
PATCH /v1/account/change-password
Content-Type: application/json
Authorization: Bearer {token}
```

- body

```json
{
  "currentPassword": "current",
  "latestPassword": "latest"
}
```

- validations (frontend validate)

| Field           | Type   | Require | Validate                      | Error                   |
|-----------------|--------|---------|-------------------------------|-------------------------|
| currentPassword | string | true    | pattern: ([a-zA-Z0-9_]{8,16}) | currentPassword.invalid |
| latestPassword  | string | true    | pattern: ([a-zA-Z0-9_]{8,16}) | latestPassword.invalid  |

- errors (backend validate)

| Error                   | Description              |
|-------------------------|--------------------------|
| currentPassword.invalid | Current Password invalid |

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Password successfully changed.",
  "total": null,
  "timestamp": "2024-09-16T09:41:35.905998300Z"
}
```

### Delete Avatar

- request

```http request
DELETE /v1/account/avatar
Authorization: Bearer {token}
```

- response

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "john doe",
    "email": "example@email.com",
    "avatar": null,
    "cover": "https://example.com/path/to/cover_picture.jpg",
    "verified": true,
    "followers": 999,
    "followings": 999,
    "likes": 999
  },
  "error": null,
  "message": "Avatar successfully deleted.",
  "total": null,
  "timestamp": "2024-09-16T09:41:35.905998300Z"
}
```

### Delete Cover

- request

```http request
DELETE /v1/account/cover
Authorization: Bearer {token}
```

- response

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "john doe",
    "email": "example@email.com",
    "avatar": "https://example.com/path/to/profile_picture.jpg",
    "cover": null,
    "verified": true,
    "followers": 999,
    "followings": 999,
    "likes": 999
  },
  "error": null,
  "message": "Cover successfully deleted.",
  "total": null,
  "timestamp": "2024-09-16T09:41:35.905998300Z"
}
```

### Delete Account

- request

```http request
DELETE /v1/account
Authorization: Bearer {token}
```

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Account successfully deleted.",
  "total": null,
  "timestamp": "2024-09-16T09:41:35.905998300Z"
}
```

### Logout

- request

```http request
DELETE /v1/account/logout
Authorization: Bearer {token}
```

- response

```json
{
  "success": true,
  "data": null,
  "error": null,
  "message": "Logout successfully.",
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

## Files

### Upload File

- request

```http request
POST /v1/files/upload
Content-Type: multipart/form-data
Authorization: Bearer {token}

file: file
public: boolean (default: true)
```

- response

```json
{
  "success": true,
  "data": {
    "name": "file.png",
    "url": "https://example.com/path/to/file.png",
    "event": "image",
    "format": "png",
    "size": 102058
  },
  "error": null,
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:29.213859300Z"
}
```

- rules

    - max-size: `10MB`
    - supported-events: `image`

- errors (backend validate)

| Error             | Description       |
|-------------------|-------------------|
| file.failed       | File failed       |
| file.notSupported | File notSupported |

- error response

```json
{
  "success": false,
  "data": null,
  "error": "file.notSupported",
  "message": null,
  "total": null,
  "timestamp": "2024-09-16T09:02:06.065237500Z"
}
```

### Get File

- request

```http request
GET /v1/files/{fileName}
```