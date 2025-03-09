package org.omegat.documentation

import groovy.transform.CompileStatic

import javax.xml.transform.Transformer

@CompileStatic
class StylesheetTask extends TransformationTask {

    void preTransform(Transformer transformer, File sourceFile, File outputDir) {
        String rootName = getRootName(outputDir.getName())
        String baseDir = outputDir.parent + File.separator
        final String HTML_EXTENSION = ".html"

        transformer.setParameter("root.filename", rootName)
        transformer.setParameter("base.dir", baseDir)
        transformer.setParameter("html.ext", HTML_EXTENSION)
    }

    private static String getRootName(String filename) {
        int extensionIndex = filename.lastIndexOf('.')
        return extensionIndex > 0 ? filename.substring(0, extensionIndex) : filename
    }
}