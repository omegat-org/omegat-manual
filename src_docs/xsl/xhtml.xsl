<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:saxon="http://sourceforge.net/saxon"
                extension-element-prefixes="saxon">

  <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/xhtml5/chunk.xsl"/>

  <xsl:output method="html"
              encoding="UTF-8"
              indent="yes"
              saxon:character-representation="native;decimal"/>

</xsl:stylesheet>
