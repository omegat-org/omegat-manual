# OmegaT Manuals Migration to DocBook 5

This is an experimental repository designed to migrate OmegaT manuals to DocBook 5 and build them with Gradle.

## How to build HTML manual

To build the HTML manual, run the following Gradle command:

```shell
./gradlew manualHtmls
```

Once the build process is complete, you will find the HTML files under `build/docs/manual/`.

## DocBook 5 data

### Greeting page

All greeting pages have been successfully migrated to DocBook 5.

### Manuals

Currently, only **two manual source files** have been migrated to DocBook 5:

- [`src_docs/manual/en/sample.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manual/en/sample.xml)
- [`src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml)

The `sample.xml` is a file to test the build logic.


## Build Logic

The tasks for building the project are defined in a custom local plugin located in the `build-logic` directory.

## Developer manual

There is a developer manual in `src_docs/developer` in Markdown mark-up syntax.
You can build HTML developer manual with MkDocs python document build toolkit.
Please install python3 with venv module;

```generic
python3 -m venv venv
source venv/bin/activate
mkdocs build -f src_docs/mkdocs.yml
```

Then you can find a html document in `build/docs/developer`

## Downloading Artifacts via GitHub Actions

You can download the build artifacts directly from the GitHub Actions page by clicking the "**artifact**" link.
