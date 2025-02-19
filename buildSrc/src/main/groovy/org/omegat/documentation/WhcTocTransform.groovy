package org.omegat.documentation

import groovy.transform.CompileStatic
import org.gradle.api.file.RegularFile
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
class WhcTocTransform extends AbstractStyleSheetTask {

    @InputFile
    Provider<RegularFile> headerStyleSheetFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> tocStyleSheetFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> indexStyleSheetFile = project.objects.fileProperty()

    WhcTocTransform() {
    }

    @TaskAction
    void transform() {
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
                break
            case LogLevel.INFO:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }
        docsOutput.get().asFile.mkdirs()

        def srcFile = docRoot.dir(language).get().file("index.xml").asFile
        execute(srcFile, headerStyleSheetFile.get().asFile,
                new File(docsOutput.get().asFile, "header.xhtml"))
        def htmlOutputFile = new File(docsOutput.get().asFile, "index_.html")
        execute(htmlOutputFile, indexStyleSheetFile.get().asFile,
                new File(docsOutput.get().asFile, "index.html"))
        execute(htmlOutputFile, tocStyleSheetFile.get().asFile,
                new File(docsOutput.get().asFile, "toc.xhtml"))
    }

}
