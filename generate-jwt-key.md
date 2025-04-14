### Directory
`src/main/resources/jwt`

### Generate a private key
```shell
openssl genrsa -out private-key.key 2048
```

### Generate a public key
```shell
openssl rsa -in private-key.key -pubout -out public-key.pub
```