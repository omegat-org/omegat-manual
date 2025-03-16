package org.omegat.documentation

import groovy.transform.CompileStatic
import net.sf.saxon.s9api.QName
import net.sf.saxon.s9api.XdmAtomicValue
import net.sf.saxon.s9api.XsltTransformer

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
        String baseDir = outputFile.get().asFile.parent + File.separator
        transformer.setParameter(new QName("root.filename"), new XdmAtomicValue(extractRootName(target)))
        transformer.setParameter(new QName("base.dir"), new XdmAtomicValue(baseDir))
        transformer.setParameter(new QName("use.id.as.filename"), new XdmAtomicValue(1))
        transformer.setParameter(new QName("html.ext"), new XdmAtomicValue(".html"))
        transformer.setParameter(new QName("chunk.section.depth"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("chunk.first.sections"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("chunker.output.encoding"), new XdmAtomicValue("UTF-8"))
        transformer.setParameter(new QName("chunker.output.indent"), new XdmAtomicValue("yes"))
        transformer.setParameter(new QName("use.extensions"), new XdmAtomicValue(1))
        transformer.setParameter(new QName("chapter.autolabel"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("section.autolabel"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("tablecolumns.extension"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("toc.max.depth"), new XdmAtomicValue(2))
        transformer.setParameter(new QName("generate.toc"),
                new XdmAtomicValue("book toc,title,figure,table chapter toc appendix toc"))
        transformer.setParameter(new QName("generate.index"), new XdmAtomicValue(1))
        transformer.setParameter(new QName("html.stylesheet"), new XdmAtomicValue("omegat.css"))
        transformer.setParameter(new QName("docbook.css.link"), new XdmAtomicValue(0))
        transformer.setParameter(new QName("saxon.character.representation"), new XdmAtomicValue("native;decimal"))
    }
}
