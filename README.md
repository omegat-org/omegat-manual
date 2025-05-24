# OmegaT manuals migrate to docbook 5

Here is an experimental repository to build OmegaT manuals with Gradle and migrate to DocBook 5.

## How to build HTML manual

Please execute the Gradle command

```shell
./gradlew manualHtmls
```

You will find HTML files under `build/docs/manual/`

## DocBook 5 data

There are only TWO source files have been migrated into DocBook 5

- [`src_docs/manual/en/sample.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manual/en/sample.xml)
- [`src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml) 

## Build logic Sources

Tasks are defined as a custom local plugin defined in `build-logic`

## GitHub Actions

You can download build artifact from GitHub Actions page with "artifact" link
