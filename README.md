# IntegraHub (Case Técnico: Integração com HubSpot)

API REST desenvolvida em Java com Spring Boot para integração com a API da HubSpot utilizando autenticação OAuth 2.0.

> ⚠️ Observação: A aplicação **não persiste dados** em banco permanente. Tokens são armazenados em memória, portanto ao reiniciar a aplicação, será necessário realizar novamente a autenticação.

> ℹ️ A documentação técnica encontra-se no projeto com o nome **doc_tec.pdf**

---

## 🚀 Funcionalidades

- Autenticação OAuth 2.0 com a HubSpot (Authorization Code Flow)
- Criação de contatos no CRM
- Recebimento de webhooks do tipo `contact.creation`
- Tratamento de erros padronizado
- Restrições de **Rate Limit**

---

## ✅ Pré-requisitos

Antes de rodar o projeto, verifique se você possui:

- ✅ Java JDK 21
- ✅ Gradle 8+ instalado (ou utilize o wrapper incluído)
- ✅ Conta de **desenvolvedor HubSpot** ([criar aqui](https://developers.hubspot.com/))
- ✅ [Ngrok](https://ngrok.com/) instalado e configurado para expor seu servidor local na internet
- ✅ Conta HubSpot separada para testes (não a conta de desenvolvedor)
- ✅ App registrado na conta de desenvolvedor HubSpot com:
    - Scopes: `crm.objects.contacts.write`, `crm.objects.contacts.read`
    - Redirect URI configurado (`http://localhost:8080/api/v1/oauth/hubspot/callback`)
    - Target url webhook `https://xxxx.ngrok-free.app/api/v1/contact/webhook/contact-created`
    - Webhook ativo com `subscription` do tipo: `contact.creation`


---

## ⚙️ Executando o projeto localmente

1. Clone o repositório:
```bash
git clone https://github.com/victorcto/integrahub.git
cd integrahub
```

2. Edite o `application.yml`:

```yml
hubspot:
  client-id: SEU_CLIENT_ID
  client-secret: SEU_CLIENT_SECRET
```

3. Inicie o `ngrok` apontando para sua porta local:

```bash
ngrok http 8080
```

4. Rode o projeto:

```bash
./gradlew bootRun
```

---

## 🧪 Fluxo de Teste


1. **Autenticação OAuth2 (manual):**  
   Acesse a URL para autorizar o app:
   ```
   GET http://localhost:8080/api/v1/oauth/hubspot/authorize
   ```
   Isso redirecionará para a tela de autorização da HubSpot.

2. **Callback:**  
   A URL de callback será chamada automaticamente pela HubSpot. O token será salvo na aplicação.

3. **Criação de contato:**
   ```http
   POST http://localhost:8080/api/v1/contact
   Content-Type: application/json

   {
     "email": "teste@teste.com",
     "firstname": "João",
     "lastname": "Silva"
   }
   ```

4. **Recebimento de Webhook (simulação):**  
   A HubSpot enviará um `POST` automático para:
   ```
   POST /api/v1/hubspot/webhook
   ```
   Será exibido no console da aplicação um log informando que um novo contato foi criado.

## 🧑‍💻 Autor

Desenvolvido por Victor Couto.  
Entre em contato: victorcto@outlook.com | [LinkedIn](https://linkedin.com/in/victorcto)

---