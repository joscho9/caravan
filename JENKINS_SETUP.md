# Jenkins Setup für Caravan Project

## Credentials Einrichtung

Die Pipeline benötigt folgende Jenkins Credentials. Gehe zu **Manage Jenkins > Manage Credentials > System > Global credentials** und füge sie hinzu:

### 1. Database Credentials

| Credential ID | Type | Beschreibung | Beispiel-Wert |
|---------------|------|--------------|---------------|
| `caravan-postgres-db` | Secret text | PostgreSQL Database Name | `caravan_db` |
| `caravan-postgres-user` | Secret text | PostgreSQL Username | `myuser` |
| `caravan-postgres-password` | Secret text | PostgreSQL Password | `dein_sicheres_passwort` |

### 2. PgAdmin Credentials

| Credential ID | Type | Beschreibung | Beispiel-Wert |
|---------------|------|--------------|---------------|
| `caravan-pgadmin-email` | Secret text | PgAdmin Email | `admin@example.com` |
| `caravan-pgadmin-password` | Secret text | PgAdmin Password | `dein_pgadmin_passwort` |

### 3. API Configuration

| Credential ID | Type | Beschreibung | Beispiel-Wert |
|---------------|------|--------------|---------------|
| `caravan-api-url` | Secret text | Production API URL | `http://193.33.167.253:8086/api` |

## Schritt-für-Schritt Anleitung

### 1. Jenkins Credentials öffnen
1. Gehe zu **Manage Jenkins**
2. Klicke auf **Manage Credentials**
3. Klicke auf **System**
4. Klicke auf **Global credentials (unrestricted)**
5. Klicke auf **Add Credentials**

### 2. Credential hinzufügen
Für jedes Credential:
1. **Kind**: `Secret text`
2. **ID**: Verwende die ID aus der Tabelle oben
3. **Secret**: Gib den entsprechenden Wert ein
4. **Description**: Optionale Beschreibung
5. Klicke **OK**

### 3. Beispiel für `caravan-postgres-db`
```
Kind: Secret text
ID: caravan-postgres-db
Secret: caravan_db
Description: PostgreSQL Database Name for Caravan Project
```

## Fehlerbehebung

### "Missing Jenkins credentials" Fehler
Wenn du diesen Fehler siehst, fehlen noch Credentials:
1. Prüfe ob alle 6 Credentials erstellt wurden
2. Prüfe ob die IDs exakt stimmen (Groß-/Kleinschreibung beachten)
3. Prüfe ob die Credentials im richtigen Scope sind (Global)

### "NullPointerException" Fehler
Dieser Fehler tritt auf, wenn Credentials nicht gefunden werden:
1. Stelle sicher, dass alle Credentials existieren
2. Prüfe die Credential-IDs auf Tippfehler
3. Stelle sicher, dass du Zugriff auf die Credentials hast

## Sicherheitshinweise

- Verwende starke Passwörter für alle Credentials
- Ändere die Passwörter regelmäßig
- Verwende unterschiedliche Passwörter für verschiedene Umgebungen
- Stelle sicher, dass nur autorisierte Benutzer Zugriff auf Jenkins haben

## Pipeline Testen

Nach dem Einrichten aller Credentials:
1. Gehe zu deiner Jenkins Pipeline
2. Klicke auf **Build Now**
3. Die Pipeline sollte ohne Credential-Fehler durchlaufen

## Troubleshooting

### Credentials werden nicht gefunden
```bash
# In Jenkins Script Console prüfen:
Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.GlobalCredentialsProvider')[0].getStore().getCredentials().each { cred ->
    println "${cred.id}: ${cred.class.simpleName}"
}
```

### Pipeline Debugging
Füge temporär Debug-Ausgaben hinzu:
```groovy
echo "Checking credential: caravan-postgres-db"
def cred = credentials('caravan-postgres-db')
echo "Credential found: ${cred != null}"
``` 