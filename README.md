# Agora

## Table of Content
- [1. Pre-requisites](#1-pre-requisites)
- [2. Launch back-end server](#2-launch-back-end-server)
- [3. Endpoints](#3-endpoints)
- [4. Add some data](#4-add-some-data)

## 1. Pre-requisites
- Java 17
- PostGreSQL server with a `agora` database and `backend` user
- Redis server
- Define those environment variables:
```
export PORT="8080"
export DATABASE_URL="postgresql://backend:@localhost:5432/agora"
export DATABASE_MAX_POOL_SIZE="1"
export DATABASE_SHOW_SQL="true"
export REDIS_URL="redis://default:@localhost:6379"
export JWT_SECRET='LZp/cGPJ1mkWst0HEfKVITrnHwYbyak7KWpQysxke8hgrg+CBGGmAqV3RXhOz2+6Fm4aforbrdPDy8Q4VCFr1g=='
export LOGIN_TOKEN_ENCODE_SECRET='E2M9xqpEuqZRkYWNmgIjbw=='
export LOGIN_TOKEN_DECODE_SECRET='E2M9xqpEuqZRkYWNmgIjbw=='
export LOGIN_TOKEN_ENCODE_TRANSFORMATION='AES/ECB/PKCS5Padding'
export LOGIN_TOKEN_DECODE_TRANSFORMATION='AES/ECB/PKCS5Padding'
export LOGIN_TOKEN_ENCODE_ALGORITHM='AES'
export LOGIN_TOKEN_DECODE_ALGORITHM='AES'
export REMOTE_ADDRESS_SECRET_KEY_ALGORITHM='PBKDF2WithHmacSHA512'
export REMOTE_ADDRESS_HASH_ITERATIONS=1000
export REMOTE_ADDRESS_HASH_KEY_LENGTH=256
export REMOTE_ADDRESS_HASH_SALT="a9a4ba4b6130c11686faaf6984bc0be5"
export CONTACT_EMAIL="contact@agora.gouv.fr"
export IS_ASK_QUESTION_ENABLED="true"
export IS_SIGNUP_ENABLED="true"
export IS_LOGIN_ENABLED="true"
export IS_QAG_ARCHIVE_ENABLED="false"
export IS_QAG_SELECT_ENABLED="false"
export IS_FEEDBACK_ON_RESPONSE_QAG_ENABLED="true"
export ERROR_TEXT_QAG_DISABLED="QaG disabled ¯\_(ツ)_/¯"
export REQUIRED_IOS_VERSION="12"
export REQUIRED_ANDROID_VERSION="18"
export REQUIRED_WEB_VERSION="1"
```

## 2. Launch back-end server
- Simply open the project using IntelliJ or your favorite IDE
- Run `fr.gouv.agora.AgoraBackApplicationKt`

## 3. Endpoints
You can use a Postman collection provided in `/docs` directory to have a list of this back-end's endpoints.

To use any endpoints, please make sure beforehand to call `/signup` the first time you use it, otherwise call `/login`. This will generate and automatically store a JWT that will last **24 hours**.

## 4. Add some data

TODO