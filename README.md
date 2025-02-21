# OmegaT manuals gradle build

Here is an experimental repository to build OmegaT manuals with Gradle.
OmegaT genuine repository has manuals which are built with Docker workflow.

The provision here does not use Docker/Container to generate HTML from docbook XMLs.

## How to build HTML manual

Please execute the Gradle command

```shell
./gradlew manualHtmls
```

You will find HTML files under `build/docs/manual/<lang>/`

```shell
./gradlew assemble
```

You will find Zip files under `build/docs/manuals/<lang>.zip`

## Usage

Project can use manual Zip with Gradle dependencies

```gradle
dependencies {
  implementation 'org.omegat:omegat-manual-en:6.1.0:resources@zip'
  implementation 'org.omegat:omegat-manual-fr:6.1.0:resources@zip'
}
```

## Supported languages

7 languages: CA, DE, EN, FR, IT, JA, NL

## Sources

Tasks are defined as a custom local plugin defined in `buildSrc`

## source difference from original manual source

1. Folder location: `src/docs/<lang>/`
2. Name of the main source file: `OmegaTUsersManual_xincludes_full.xml`
3. DTD of the index file: the first 2 lines of the index file should be like
```text
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd"
```
4. CSS file is located at `src/docs/omegat.css`
5. XSL files are located at `src/docs/xsl`