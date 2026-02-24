# OmegaT Manuals Migration to DocBook 5

This is an experimental repository designed to migrate OmegaT manuals to DocBook 5 and build them with Gradle.

## How to build an HTML manual

To build the HTML manual, run the following Gradle command:

```shell
./gradlew manualHtmls
```

Once the build process is complete, you will find the HTML files under `build/docs/manual/`.

## DocBook 5 Migration tool

The repository has migration stylesheet `src_docs/xsl/db4-upgrade.xsl`

### Greeting page

All greeting pages have been successfully migrated to DocBook 5.

### Manuals

English manuals are migrated into DocBook 5, converted by `src_docs/xsl/db4-upgrade.xsl` tool.
When storing your DocBook 4.5 XML files under `src_docs/manual/4/` and executing `./gradlew upgradeAllManualsEn`, then the contents will be generated into `src_docs/manual/en/`.

## Directory hierarchy

- `.github`: CI/CD configuration to automate build test
- `build-logic`: Gradle build logic to build HTML contents from source files in DocBook 5 format.
- `gradle`: System folder which used by Gradle build system
- `images`: OmegaT resources of the images, that is as same as OmegaT project repository
- `release`: OmegaT release resource `doc-license.txt` which is used by build configuration
- `src_docs/developer`: developer manual of the DocBook 5 transformation
- `src_docs/greeting`: OmegaT greeting pages for each langauges in DocBook 5 format
- `src_docs/manual/en`: working directory to migrate manual into DocBook 5 compliant format.
- `src_docs/template`: OmegaT manual Web Site index page template as same as the OmegaT project repository.
- `src_docs/xsl`: small glue stylesheet to import XLSTNG stylesheet from internet and configure output.


## Build Logic

The tasks for building the project are defined in a custom local plugin located in the `build-logic` directory.

## Developer manual

There is a developer manual in `src_docs/developer` in Markdown markup syntax.
You can build an HTML developer manual with MkDocs, a Python document build toolkit.

```generic
python3 -m venv venv
source venv/bin/activate
mkdocs build -f src_docs/mkdocs.yml
```

Then you can find an HTML document in `build/docs/developer`

## Downloading Artifacts via GitHub Actions

You can download the build artifacts directly from the GitHub Actions page by clicking the "**artifacts**" file download link at the bottom of the Actions page.
Please visit https://github.com/omegat-org/omegat-manual/actions/workflows/manuals-builds-main.yml and click the most recent job.

![image](https://github.com/user-attachments/assets/ce8feb98-d477-46a5-9fef-1777c12615a1)

