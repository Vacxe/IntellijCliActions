### CLI Actions *(Intellij Plugin)*

#### How to use:
Add `*.cliactions.yaml` files to your project root directory with following structure

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

##### Plugin output:

![sample.png](docs%2Fimages%2Fimg.png)

## License
Released under [Apache License v2.0](LICENSE)
