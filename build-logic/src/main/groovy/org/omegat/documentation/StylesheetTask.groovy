package org.omegat.documentation

import groovy.transform.CompileStatic
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Templates
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@CompileStatic
class StylesheetTask extends AbstractStyleSheetTask {

    @InputFile
    Provider<RegularFile> inputFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> styleSheetFile = project.objects.fileProperty()

    @OutputFile
    Provider<RegularFile> outputFile = project.objects.fileProperty()

    StylesheetTask() {
    }

    @TaskAction
    void transform() {
        outputFile.get().asFile.parentFile.mkdirs()
        execute(inputFile.get().asFile, styleSheetFile.get().asFile, outputFile.get().asFile)
    }

    void execute(File sourceFile, File sheetFile, File output) {
        def factory = TransformerFactory.newInstance()
        def styleSource = new StreamSource(sheetFile)
        Templates templates = factory.newTemplates(styleSource)
        Transformer transformer = templates.newTransformer()
        Source sourceInput = new StreamSource(sourceFile)
        Result outputTarget = new StreamResult(output)

        preTransform(transformer)
        transformer.transform(sourceInput, outputTarget)
    }

    void preTransform(Transformer transformer) {
        transformer.setParameter("saxon.character.representation", "native;decimal")
    }

}