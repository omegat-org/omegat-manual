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

- [`src_docs/manual/en/sample.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manuScreenshot from 2025-05-24 15-36-14al/en/sample.xml)
- [`src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml`](https://github.com/omegat-org/omegat-manual/blob/main/src_docs/manual/en/OmegaTUsersManual_xinclude_full.xml)

The `sample.xml` is a file to test the build logic.

## Directory hierarchy

- `.github`: CI/CD configuration to automate build test
- `build-logic`: Gradle build logic to build HTML contents from source files in DocBook 5 format.
- `gradle`: System folder which used by Gradle build system
- `images`: OmegaT resources of the images, that is as same as OmegaT project repository
- `release`: OmegaT release resource `doc-license.txt` which is used by build configuration
- `src_docs/developer`: developer manual of the DocBook 5 transformation
- `src_docs/docbook`: CSS and Javascript resources used by XSLTNG DocBook5 XSLT tookkit.
- `src_docs/greeting`: OmegaT greeting pages for each langauges in DocBook 5 format
- `src_docs/manual/en`: working directory to migrate manual into DocBook 5 compliant format.
- `src_docs/template`: OmegaT manual Web Site index page template as same as the OmegaT project repository.
- `src_docs/xsl`: small glue stylesheet to import XLSTNG stylesheet from internet and configure output.


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

You can download the build artifacts directly from the GitHub Actions page by clicking the "**artifacts**" file download link at the bottom of the Actions page.
Please visit https://github.com/omegat-org/omegat-manual/actions/workflows/manuals-builds-main.yml and click most recent job title.

![image](https://github.com/user-attachments/assets/ce8feb98-d477-46a5-9fef-1777c12615a1)

