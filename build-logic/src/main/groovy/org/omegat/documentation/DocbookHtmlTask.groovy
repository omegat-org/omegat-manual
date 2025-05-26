package org.omegat.documentation

import groovy.transform.CompileStatic
import net.sf.saxon.s9api.QName
import net.sf.saxon.s9api.XdmAtomicValue
import net.sf.saxon.s9api.XsltTransformer

import java.nio.file.Paths

@CompileStatic
class DocbookHtmlTask extends TransformationTask {

    private static String extractRootName(File file) {
        def fileName = file.getName()
        int extensionIndex = fileName.lastIndexOf('.')
        if (extensionIndex <= 0) {
            return fileName
        }
        return fileName.substring(0, extensionIndex)
    }

    @Override
    protected void preTransform(XsltTransformer transformer, File source, File target) {
        def baseDir = Paths.get(outputFile.get().asFile.parent).toUri().toString()
        transformer.setParameter(new QName("chunk"), new XdmAtomicValue("_index.html"))
        transformer.setParameter(new QName("chunk-output-base-uri"), new XdmAtomicValue(baseDir))
        transformer.setParameter(new QName("persistent-toc"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("persistent-toc-search"), new XdmAtomicValue(false))
        transformer.setParameter(new QName("chunk-section-depth"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("section-toc-depth"), new XdmAtomicValue(2))
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
