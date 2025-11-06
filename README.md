## Startup:

#### Configuration:

In hibernate.cfg.xml change the following values:

If your credentials are `root`-`root` -> no need to change them. (Mine are too:)

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration SYSTEM
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>
        <property name="hibernate.connection.driver_class">
            com.mysql.cj.jdbc.Driver
        </property>

        <property name="hibernate.connection.url">
            jdbc:mysql://localhost:3306/hibernate
        </property>
        <property name="hibernate.connection.username">
            YOUR_USERNAME_HERE (root by default)
        </property>
        <property name="hibernate.connection.password">
            YPUR_PASSWORD_HERE (root by default)
        </property>
        <property name="current_session_context_class">thread</property>
        <property name="show_sql">true</property>
        
    </session-factory>
</hibernate-configuration>

```
---
### If you want to change DB to Postgres

In pom.xml replace this:
```xml
    <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

with this:
```xml
    <dependency>
    <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.7.8</version>
    </dependency>
```

Use the following changed hibernate.cfg.xml:
```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration SYSTEM
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">
            org.hibernate.dialect.PostgreSQLDialect
        </property>
        <property name="hibernate.connection.driver_class">
            org.postgresql.Driver
        </property>

        <property name="hibernate.connection.url">
            jdbc:postgresql://localhost:5432/hibernate
        </property>
        <property name="hibernate.connection.username">
            postgres (or ypur username)
        </property>
        <property name="hibernate.connection.password">
            root (or your password)
        </property>
        <property name="current_session_context_class">thread</property>
        <property name="show_sql">true</property>


    </session-factory>
</hibernate-configuration>
```


---

#### DB initialization:

##### For MySQL:

> [!note]
> Execute it through CMD, not PowerShell

```bash
mysql -u <your_user_name> -p < <path_to_file_on_your_pc>/initDB.sql
```

How the command looks on my pc (i'm in the project's directory):
```bash
mysql -u root -p < src/main/resources/initDB.sql
```

##### For Postgres:

```bash
psql -U <your_user_name> -f <path_to_file_on_your_pc>/initDBForPostgres.sql
```

How the command looks on my pc (i'm in the project's directory):
```bash
psql -U postgres -f src/main/resources/initDBForPostgres.sql
```

---

