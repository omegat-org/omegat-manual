package org.omegat.documentation

import groovy.transform.CompileStatic
import org.apache.xerces.jaxp.SAXParserFactoryImpl
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import org.xml.sax.XMLReader

import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamSource

@CompileStatic
abstract class AbstractTransformationTask extends DefaultTask implements DocConfigurable {

    @InputDirectory
    final DirectoryProperty styleDir = project.objects.directoryProperty()

    /**
     * Sources root for the documentation
     */
    @Internal
    final DirectoryProperty docRoot = project.objects.directoryProperty()

    @Internal
    final DirectoryProperty outputRoot = project.objects.directoryProperty()


    AbstractTransformationTask() {
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        docRoot.convention(extension.docRoot)
        styleDir.convention(extension.styleDir)
        outputRoot.convention(extension.outputRoot)
    }

    @TaskAction
    abstract void transform()


    static final String TRANSFORMER_FACTORY_PROPERTY = "javax.xml.transform.TransformerFactory"
    static final String TRANSFORMER_FACTORY_IMPL = "org.apache.xalan.processor.TransformerFactoryImpl"
    static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities"
    static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities"

    protected static void configureTransformerFactory() {
        System.setProperty(TRANSFORMER_FACTORY_PROPERTY, TRANSFORMER_FACTORY_IMPL)
    }

    protected XMLReader initializeXmlReader(CatalogResolver resolver) {
        def factory = configureSAXParserFactory()
        def xmlReader = factory.newSAXParser().getXMLReader()
        xmlReader.setEntityResolver(resolver)
        return xmlReader
    }

    protected SAXParserFactory configureSAXParserFactory() {
        SAXParserFactory factory = new SAXParserFactoryImpl()
        factory.setValidating(false)
        factory.setNamespaceAware(true)
        factory.setXIncludeAware(true)
        factory.setFeature(EXTERNAL_GENERAL_ENTITIES, true)
        factory.setFeature(EXTERNAL_PARAMETER_ENTITIES, true)
        return factory
    }

    protected static Transformer initializeTransformer(CatalogResolver resolver, File styleSheetFile) {
        def transformerFactory = TransformerFactory.newInstance()
        transformerFactory.setURIResolver(resolver)

        def styleSheetUrl = styleSheetFile.toURI().toURL()
        def styleSource = new StreamSource(styleSheetUrl.openStream(), styleSheetUrl.toExternalForm())
        return transformerFactory.newTransformer(styleSource)
    }

    protected void configureLogging() {
        // Redirect spurious task output to INFO unless explicitly configured for debug
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }
    }
}
