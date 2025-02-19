package org.omegat.documentation

import com.icl.saxon.TransformerFactoryImpl
import groovy.transform.CompileStatic
import org.apache.xerces.jaxp.SAXParserFactoryImpl
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.xml.sax.InputSource
import org.xml.sax.XMLReader

import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@CompileStatic
class DocbookWhc extends DefaultTask implements DocConfigurable {

    /**
     * Language of the documentation (eg 'de', 'ru').
     */
    @Input
    @Optional
    @Option(option = 'language', description = 'The two letter language code for output')
    final Property<String> language = project.objects.property(String)

    @InputDirectory
    final DirectoryProperty styleDir = project.objects.directoryProperty()

    @InputFile
    Provider<RegularFile> headerStyleSheetFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> tocStyleSheetFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> indexStyleSheetFile = project.objects.fileProperty()

    /**
     * Sources root for the documentation
     */
    @Internal
    final DirectoryProperty docRoot = project.objects.directoryProperty()

    @Internal
    final DirectoryProperty outputRoot = project.objects.directoryProperty()

    @Internal
    final Provider<Directory> docsOutput = outputRoot
            .dir(language.map({ value -> "html/${value}" }))

    private TransformerFactory transformerFactory
    private XMLReader reader

    DocbookWhc() {
        def resolver = new CatalogResolver(XsltHelper.createCatalogManager())
        def factory = new SAXParserFactoryImpl()
        factory.setXIncludeAware(true)
        reader = factory.newSAXParser().getXMLReader()
        reader.setEntityResolver(resolver)
        transformerFactory = new TransformerFactoryImpl()
        transformerFactory.setURIResolver(resolver)
    }

    @TaskAction
    void transform() {
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
            case LogLevel.INFO:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }
        docsOutput.get().asFile.mkdirs()

        def srcFile = docRoot.dir(language).get().file("index.xml").asFile
        execute(srcFile, headerStyleSheetFile.get().asFile.toURI().toURL(),
                new File(docsOutput.get().asFile, "header.html"))
        def htmlOutputFile = new File(docsOutput.get().asFile, "index_.html")
        execute(htmlOutputFile, indexStyleSheetFile.get().asFile.toURI().toURL(),
                new File(docsOutput.get().asFile, "index.xhtml"))
        execute(htmlOutputFile, tocStyleSheetFile.get().asFile.toURI().toURL(),
                new File(docsOutput.get().asFile, "toc.xhtml"))
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        docRoot.convention(extension.docRoot)
        outputRoot.convention(extension.outputRoot)
        styleDir.convention(extension.styleDir)
    }

    private void execute(File srcFile, URL url, File outputFile) {
        def inputSource = new InputSource(srcFile.getAbsolutePath())
        def result = new StreamResult(outputFile)
        def source = new StreamSource(url.openStream(), url.toExternalForm())
        def transformer = transformerFactory.newTransformer(source)
        transformer.setParameter("root.filename", srcFile.toPath().toString())
        transformer.setParameter("base.dir", outputFile.getParent() + File.separator)
        transformer.transform(new SAXSource(reader, inputSource), result)
    }
}
