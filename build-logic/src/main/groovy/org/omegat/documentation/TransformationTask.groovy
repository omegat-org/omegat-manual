package org.omegat.documentation

import groovy.transform.CompileStatic
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.file.*
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.xml.sax.InputSource
import org.xml.sax.XMLReader

import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult

@CompileStatic
class TransformationTask extends AbstractTransformationTask {

    @InputFile
    Provider<RegularFile> styleSheetFile = project.objects.fileProperty()

    @OutputFile
    Provider<RegularFile> outputFile = project.objects.fileProperty()

    @InputFile
    final Property<RegularFile> inputFile = project.objects.fileProperty()

    TransformationTask() {
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        super.configureWith(extension)
    }

    @TaskAction
    final void transform() {
        configureLogging()
        configureTransformerFactory()

        File outputFile = outputFile.get().asFile

        InputSource inputSource = new InputSource(inputFile.get().asFile.getAbsolutePath())
        StreamResult outputResult = new StreamResult(outputFile)

        CatalogResolver resolver = new CatalogResolver(XsltHelper.createCatalogManager())
        XMLReader xmlReader = initializeXmlReader(resolver)
        Transformer transformer = initializeTransformer(resolver, styleSheetFile.get().asFile)

        preTransform(transformer, inputFile.get().asFile, outputFile)
        transformer.transform(new SAXSource(xmlReader, inputSource), outputResult)
        postTransform(outputFile)
    }

    protected void preTransform(Transformer transformer, File sourceFile, File outputFile) {
    }

    protected void postTransform(File outputFile) {
    }

}
