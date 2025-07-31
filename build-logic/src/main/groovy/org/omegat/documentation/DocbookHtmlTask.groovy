package org.omegat.documentation

import groovy.transform.CompileStatic
import net.sf.saxon.s9api.Processor
import net.sf.saxon.s9api.QName
import net.sf.saxon.s9api.XdmAtomicValue
import net.sf.saxon.s9api.XsltTransformer
import org.docbook.xsltng.extensions.Cwd
import org.docbook.xsltng.extensions.ImageMetadata
import org.docbook.xsltng.extensions.ImageProperties
import org.docbook.xsltng.extensions.Register
import org.docbook.xsltng.extensions.XInclude
import org.gradle.api.tasks.CacheableTask

import java.nio.file.Paths

@CompileStatic
@CacheableTask
class DocbookHtmlTask extends TransformationTask {

    @Override
    protected void configProcessor(Processor processor) {
        Register register = new Register()
        register.initialize(processor.getUnderlyingConfiguration())
    }

    @Override
    protected void preTransform(XsltTransformer transformer, File source, File target) {
        def outputBaseDir = Paths.get(outputFile.get().asFile.parent).toUri().toString()
        def inputBaseDir = Paths.get(inputFile.get().asFile.parent).toUri().toString()
        transformer.setParameter(new QName("chunk"), new XdmAtomicValue("index.html"))
        transformer.setParameter(new QName("chunk-output-base-uri"), new XdmAtomicValue(outputBaseDir))
        transformer.setParameter(new QName("mediaobject-input-base-uri"), new XdmAtomicValue(inputBaseDir))
        transformer.setParameter(new QName("resource-base-uri"), new XdmAtomicValue(""))
        transformer.setParameter(new QName("persistent-toc"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("persistent-toc-search"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("chunk-section-depth"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("section-toc-depth"), new XdmAtomicValue(1))
        transformer.setParameter(new QName("html-extension"), new XdmAtomicValue(".html"))
        transformer.setParameter(new QName("output-media"), new XdmAtomicValue("screen"))
        transformer.setParameter(new QName("page-style"), new XdmAtomicValue("book"))
        transformer.setParameter(new QName("pagetoc-dynamic"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("persistent-toc"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("use-id-as-filename"), new XdmAtomicValue("true"))
        transformer.setParameter(new QName("use-docbook-css"), new XdmAtomicValue("false"))
        transformer.setParameter(new QName("user-css-links"), new XdmAtomicValue("css/omegat.css"))
        transformer.setParameter(new QName("lists-of-examples"), new XdmAtomicValue("false"))
        transformer.setParameter(new QName("lists-of-figures"), new XdmAtomicValue("false"))
        transformer.setParameter(new QName("lists-of-tables"), new XdmAtomicValue("false"))
    }
}
