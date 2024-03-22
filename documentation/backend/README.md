# Maven, Java e Quarkus

## Maven

[Maven](https://maven.apache.org/) é uma ferramenta de automação de compilação usada principalmente para projetos Java. Ele gerencia dependências, compilação, testes e distribuição de artefatos em um projeto Java.

### Instalação

Para instalar o Maven, siga as instruções no [site oficial do Maven](https://maven.apache.org/download.cgi).

### Principais Comandos do Maven

Aqui estão alguns dos principais comandos do Maven que você pode achar úteis:

- `mvn clean`: Limpa os arquivos gerados pelo build anterior.
- `mvn compile`: Compila o código-fonte do projeto.
- `mvn test`: Executa os testes do projeto.
- `mvn package`: Empacota o código compilado em um formato distribuível, como um JAR ou WAR.
- `mvn install`: Instala o artefato no repositório local para uso em outros projetos.

Para mais informações sobre o Maven e seus comandos, consulte a [documentação oficial do Maven](https://maven.apache.org/guides/index.html).

## Java

[Java](https://www.java.com/) é uma linguagem de programação de propósito geral, conhecida por sua portabilidade, orientação a objetos e robustez. É amplamente utilizado no desenvolvimento de aplicativos empresariais, aplicativos de desktop, aplicativos web e muito mais.

### Instalação

Para instalar o JDK (Java Development Kit), siga as instruções no [site oficial da Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) ou instale-o usando uma ferramenta de gerenciamento de pacotes, como o apt ou o brew.

### Principais Comandos do Java

Aqui estão alguns dos principais comandos do Java que você pode achar úteis:

- `java -version`: Verifica a versão do Java instalada.
- `javac`: Compila arquivos Java em bytecode.
- `java`: Executa programas Java.
- `jar`: Cria, visualiza e extrai arquivos JAR.

Para mais informações sobre o Java e seus comandos, consulte a [documentação oficial do Java](https://docs.oracle.com/en/java/).

## Quarkus

[Quarkus](https://quarkus.io/) é um framework Java de próxima geração projetado para otimizar a experiência de desenvolvimento de microsserviços e aplicativos em nuvem. Ele oferece inicialização rápida, baixo consumo de memória e suporte a várias extensões para integração com tecnologias modernas.

### Instalação

Para instalar o Quarkus, siga as instruções no [site oficial do Quarkus](https://quarkus.io/get-started/).

### Principais Comandos do Quarkus

Aqui estão alguns dos principais comandos do Quarkus que você pode achar úteis:

- `mvn io.quarkus:quarkus-maven-plugin:create`: Cria um novo projeto Quarkus usando o Maven.
- `./mvnw quarkus:dev`: Inicia o modo de desenvolvimento Quarkus.
- `./mvnw clean package -Dquarkus.package.type=native`: Compila um executável nativo usando GraalVM.

Para mais informações sobre o Quarkus e seus comandos, consulte a [documentação oficial do Quarkus](https://quarkus.io/guides/).

[voltar](../../README.md)