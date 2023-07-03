### CLI Actions *(Intellij Plugin)*

#### How to use:
* Add `*.cliactions.yaml` files to your project root or/and user home directory
* You may create multiple configs. Configs from user home will be shared across all project/IDEs
* You may force developer to install this plugin when project will be opened

* `.idea/externalDependencies.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<project version="4">
    <component name="ExternalDependencies">
        <plugin id="io.github.vacxe.cliactions" />
    </component>
</project>
```

##### Sample config file:

```yaml
groups:
  - name: First Project Actions
    commands:
      - name: Detekt
        command: ./gradlew detekt
      - name: Detekt (Auto Correct)
        command: ./gradlew detekt -pAutoCorrect
  - name: Second Project Actions
    commands:
      - name: Clean project
        command: ./gradlew apps:second:clean
      - name: Build and install App (Debug)
        command: ./gradlew apps:second:installDebug
  - name: Git
    commands:
      - name: Hard reset
        command: git reset --hard
        prompt: true
```

##### Plugin Tab:

![sample.png](docs%2Fimages%2Fimg.png)

## License
Released under [Apache License v2.0](LICENSE)
