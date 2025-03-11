# IBM Security Verify WebAuthn Relying Party SDK pour Android

## Présentation

IBM Security Verify WebAuthn Relying Party SDK for Android est le compagnon côté client d' [IBM Security Verify WebAuthn Relying Party Server](https://github.com/ibm-security-verify/webauthn-relying-party-server-swift) qui expose des API REST hébergées dans une image Docker.

RelyingPartySDK est un cadre léger qui permet aux utilisateurs existants d'enregistrer leur appareil auprès de Passkey et de se connecter ensuite sans mot de passe, et aux nouveaux utilisateurs de s'inscrire et de valider leur compte.

Pour en savoir plus sur le développement d'applications compatibles avec Passkey, rendez-vous sur le [site des développeurs Android](https://passkeys.dev/docs/reference/android/).

### Mise en route
Le RelyingPartySDK est disponible sous forme de fichier APK [ici.](https://github.com/ibm-security-verify/webauthn-relying-party-sdk-android/releases)

## Contenu
### Permettre à un utilisateur de s'inscrire
Dans ce scénario, l'utilisateur n'existe pas dans votre système d'identité et doit posséder une adresse électronique pour être validé.

```java
    // The baseURL is the host of the relying party server.
    val client = RelyingPartyClient(URL("https://www.example.com"))

    // The result is an OTPChallenge to correlate the email sent to the email address.
    val result = client.signup("scott", "scott@mailinator.com")

    // Use the result.transactionId and the OTP value generated in the email to validate. If successful, the returned Token can be used to register for Passkey.
    result.onSuccess {
        val token = client.validate(it.transactionId, "123456")
    }
```

### Authentifier un utilisateur existant
Ce scénario permet d'authentifier un utilisateur existant à l'aide d'un nom d'utilisateur et d'un mot de passe.

```java
    // The baseURL is the host of the relying party server.
    val client = RelyingPartyClient(URL("https://www.example.com"))

    val token = client.authenticate("scott@mailinator.com", "passw0rd")

    token.onSuccess {
        // The result is a Token which can be used to register for Passkey.
    }
```

### S'inscrire à Passkey
Ce scénario exige que l'utilisateur soit authentifié par une adresse `Token` valide. L'enregistrement de Passkey utilise ASAuthorizationControllerDelegate pour gérer le résultat de la demande d'enregistrement de la clé de plate-forme.

<!-- v2.3.7 : caits-prod-app-gp_webui_20241231T140318-2_en_fr -->