<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                  xmlns:saxon="http://icl.com/saxon"
                  xmlns:lxslt="http://xml.apache.org/xslt"
                  xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
                  xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
                  version="1.0"
                  exclude-result-prefixes="doc"
                  extension-element-prefixes="saxon xalanredirect lxslt">

  <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/xhtml/chunk.xsl"/>

  <xsl:output method="html"
              encoding="UTF-8"
              indent="yes"
              saxon:character-representation="native;decimal"/>


</xsl:stylesheet>
